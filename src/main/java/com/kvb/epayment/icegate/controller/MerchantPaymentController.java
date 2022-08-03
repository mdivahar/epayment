package com.kvb.epayment.icegate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kvb.epayment.icegate.service.ESBConnectorImpl;

@RestController
@RequestMapping(value = "/epayment", method = RequestMethod.POST)
public class MerchantPaymentController {

	private static final Logger logger = LoggerFactory.getLogger(MerchantPaymentController.class);
	
	@Autowired
	private ESBConnectorImpl esbConnector;

	@PostMapping(produces = MediaType.TEXT_HTML_VALUE)
	public @ResponseBody String redirectToPayment(@RequestParam(name = "encdata") String encdata) {
		logger.info(encdata);
		String ePayRequest = esbConnector.processEPaymentRequest(encdata);
		logger.info(ePayRequest);
		return ePayRequest;
	}

}
