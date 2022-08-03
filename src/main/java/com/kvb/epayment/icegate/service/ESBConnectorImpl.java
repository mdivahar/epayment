package com.kvb.epayment.icegate.service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kvb.epayment.icegate.config.ApplicationParameter;
import com.kvb.epayment.icegate.controller.MerchantPaymentController;
import com.kvb.epayment.icegate.model.ChallanData;
import com.kvb.epayment.icegate.model.EPayment;
import com.kvb.epayment.icegate.model.ESBRequest;
import com.kvb.epayment.icegate.model.ESBResponse;
import com.kvb.epayment.icegate.model.PaymentParameter;
import com.kvb.epayment.icegate.model.InputVariables;
import com.kvb.epayment.icegate.model.Verification;

import in.gov.icegate.crypto.EncryptionUtil;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Service
public class ESBConnectorImpl implements ESBConnector {

	@Autowired
	private Environment env;
	@Autowired
	private ApplicationContext ctx;

	@Autowired
	CryptoServiceImpl crypto;
	private WebClient webclient;

	private static final Logger logger = LoggerFactory.getLogger(ESBConnectorImpl.class);

	@Override
	public String processEPaymentRequest(String encdata) {
		try {
			ObjectMapper mapper = ctx.getBean(ObjectMapper.class);
			EPayment epayment = mapper.readValue(encdata, EPayment.class);
			String certPath = env.getProperty("pfx.certPath");
			logger.info("Certificate Path - " + certPath);
			String certPassword = env.getProperty("certPassword");
			logger.info("Certificate Password - " + certPassword);
			String encryptedKey = epayment.getSecretKey();
			logger.info("Encrypted Key - " + encryptedKey);
			String challanDataEncrypted = epayment.getChallanData();
			logger.info("Challan Data - " + challanDataEncrypted);
			byte[] buffer = EncryptionUtil.decryptSecretKeyWithRSA(encryptedKey, certPath, certPassword);
			logger.info("Secret Key - " + new String(Base64.getEncoder().encode(buffer)));
			String data = EncryptionUtil.decryptDataWithAES(challanDataEncrypted, buffer);
			buffer=Base64.getDecoder().decode(data.getBytes());
			data=new String(buffer);
			logger.info("Data - " + data);
			ChallanData challanData = mapper.readValue(data, ChallanData.class);
			PaymentParameter icegateRequest = ctx.getBean(PaymentParameter.class);
			icegateRequest.setClientId(epayment.getClientId());
			icegateRequest.setSecretKey(epayment.getSecretKey());
			icegateRequest.setSignature(epayment.getSignature());
			icegateRequest.setChallanData(challanData);

			InputVariables inputVariables = ctx.getBean(InputVariables.class);
			inputVariables.setIn_msg(icegateRequest);
			ESBRequest esbRequest = ctx.getBean(ESBRequest.class);
			esbRequest.setInputVariables(inputVariables);

			String esbInputStr = mapper.writeValueAsString(esbRequest);
			logger.info(esbInputStr);

			ESBResponse esbResponse = webclient.post().uri(ApplicationParameter.MERCHANT_PAYMENT)
					.body(Mono.just(esbRequest), ESBRequest.class).retrieve().bodyToMono(ESBResponse.class).block();
			String responseStr = esbResponse.getOut_msg();
			logger.info("Response from "+responseStr);
			if (responseStr == null) {
				responseStr = ApplicationParameter.ERROR_PAGE;
			}

			if (responseStr.startsWith("<html>")) {
				return responseStr;
			} else {
					
			}

			return responseStr;
		} catch (JsonMappingException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return ApplicationParameter.ERROR_PAGE;

	}

	@Override
	public String processEPaymentResponse(String fldencdata) {
		logger.info("Data from IB - " + fldencdata);
		Map inputVariables=new HashMap();
		Map in_msg=new HashMap();
		in_msg.put("in_msg", fldencdata);
		inputVariables.put("inputVariables", in_msg);
		ESBResponse esbResponse = webclient.post().uri(ApplicationParameter.IB_PAYMENT)
				.body(Mono.just(inputVariables), Map.class).retrieve().bodyToMono(ESBResponse.class).block();
		logger.info("Data from ESB - " + esbResponse.getOut_msg());
		return esbResponse.getOut_msg();
		
		 
	}

	@Override
	public Verification processEPaymentVerification(Verification verification) {
		// TODO Auto-generated method stub
		return null;
	}

	@PostConstruct
	public void init() {

		try {

			String paymentURL = env.getProperty("esb.payment.url");
			logger.info(paymentURL);
			SslContext sslContext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE)
					.build();

			HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));

			webclient = WebClient.builder().baseUrl(paymentURL)
					.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
					.clientConnector(new ReactorClientHttpConnector(httpClient)).build();

		} catch (SSLException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}

	}

}
