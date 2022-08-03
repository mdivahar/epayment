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
@RequestMapping(value = "/ibResponse", method = RequestMethod.POST)
public class IBPaymentController {

	private static final Logger logger = LoggerFactory.getLogger(IBPaymentController.class);

	@Autowired
	private ESBConnectorImpl esbConnector;

	@PostMapping(produces = MediaType.TEXT_HTML_VALUE)
	public @ResponseBody String redirectToMerchant(@RequestParam(name = "epifldencdata") String epifldencdata) {
		logger.info("Data received from IB " + epifldencdata);
		String ePayRequest = esbConnector.processEPaymentRequest(epifldencdata);
		logger.info("Data send to Merchant " + ePayRequest);
		return ePayRequest;
	}
}
