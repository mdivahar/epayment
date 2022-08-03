package com.kvb.epayment.icegate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kvb.epayment.icegate.model.ChallanData;
import com.kvb.epayment.icegate.model.EPayment;
import com.kvb.epayment.icegate.service.CryptoServiceImpl;

@RestController
@RequestMapping(value = "crypto", method = RequestMethod.POST)
public class CryptoController {

	@Autowired
	CryptoServiceImpl crypto;

	@PostMapping(value="payloadEncryption", produces = MediaType.TEXT_HTML_VALUE)
	public @ResponseBody String generateEncryptPayload(@RequestParam(name = "data") String data) {
		return crypto.generateInputPayload(data);
	}
	
	@PostMapping(path = "/decrypt", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ChallanData decryptPayload(@RequestBody EPayment request) {
		return crypto.decrypt(request);
	}
	
	@PostMapping(path = "/encrypt", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody EPayment Payload(@RequestBody ChallanData request) {
		return crypto.encrypt(request);
	}

}
