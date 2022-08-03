package com.kvb.epayment.icegate.service;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kvb.epayment.icegate.model.ChallanData;
import com.kvb.epayment.icegate.model.EPayment;

import in.gov.icegate.crypto.EncryptionUtil;

@Service
public class CryptoServiceImpl implements CryptoService {

	@Autowired
	private Environment env;
	@Autowired
	private ApplicationContext ctx;
	private static final Logger logger = LoggerFactory.getLogger(CryptoServiceImpl.class);

	@Override
	public String generateInputPayload(String inputData) {

		String encodedText = Base64.getEncoder().encodeToString(inputData.getBytes());

		try {
			String cerpath = env.getProperty("cer.certPath");
			byte[] secretKey = EncryptionUtil.getSecretKey();
			String encryptedText = EncryptionUtil.encryptDataWithAES(encodedText, secretKey);
			String encryptedKey = EncryptionUtil.encryptSecretKeyWithRSA(cerpath, secretKey);
			EPayment ePay = ctx.getBean(EPayment.class);
			ePay.setChallanData(encryptedText);
			ePay.setClientId("4354345-543454556-6568687-65465");
			ePay.setSecretKey(encryptedKey);
			ePay.setSignature("<signature can generate from icegate portal>");
			ObjectMapper mapper = ctx.getBean(ObjectMapper.class);
			mapper.setSerializationInclusion(Include.NON_NULL);
			return mapper.writeValueAsString(ePay);
		} catch (JsonProcessingException | NoSuchAlgorithmException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} 

		return null;
	}

	@Override
	public ChallanData decrypt(EPayment request) {
		String secretKey = request.getSecretKey();
		String challanDataEncrypted = request.getChallanData();
		logger.info("Encrypted challan data"+challanDataEncrypted);
		String cerpath = env.getProperty("pfx.certPath");
		logger.info("pfx certificate path"+cerpath);
		String password = env.getProperty("certPassword");
		byte[] secretKeyBuffer = EncryptionUtil.decryptSecretKeyWithRSA(secretKey, cerpath, password);
		String data = EncryptionUtil.decryptDataWithAES(challanDataEncrypted, secretKeyBuffer);
		byte[] buffer = Base64.getDecoder().decode(data.getBytes());
		data = new String(buffer);
		logger.info("challanData "+data);
		ObjectMapper mapper = ctx.getBean(ObjectMapper.class);
		mapper.setSerializationInclusion(Include.NON_NULL);
		ChallanData challanData = ctx.getBean(ChallanData.class);
		try {
			challanData = mapper.readValue(data, ChallanData.class);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return challanData;
	}

	@Override
	public EPayment encrypt(ChallanData request) {
		ObjectMapper mapper = ctx.getBean(ObjectMapper.class);
		mapper.setSerializationInclusion(Include.NON_NULL);
		EPayment ePay = ctx.getBean(EPayment.class);
		try {
			String challanData = mapper.writeValueAsString(request);
			String encodedText = Base64.getEncoder().encodeToString(challanData.getBytes());

			String cerpath = env.getProperty("cer.certPath");
			byte[] secretKey = EncryptionUtil.getSecretKey();
			String encryptedText = EncryptionUtil.encryptDataWithAES(encodedText, secretKey);
			String encryptedKey = EncryptionUtil.encryptSecretKeyWithRSA(cerpath, secretKey);

			ePay.setChallanData(encryptedText);
			ePay.setClientId("4354345-543454556-6568687-65465");
			ePay.setSecretKey(encryptedKey);
			cerpath = env.getProperty("pfx.certPath");
			String password = env.getProperty("certPassword");
			ePay.setSignature(CryptoService.getSign(encodedText, cerpath, password));
		} catch (JsonProcessingException | NoSuchAlgorithmException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return ePay;
	}

}
