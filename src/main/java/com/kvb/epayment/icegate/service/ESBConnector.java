package com.kvb.epayment.icegate.service;

import com.kvb.epayment.icegate.model.ESBRequest;
import com.kvb.epayment.icegate.model.ESBResponse;
import com.kvb.epayment.icegate.model.Verification;

public interface ESBConnector {

	public String processEPaymentRequest(String encdata);

	public String processEPaymentResponse(String fldencdata);

	public Verification processEPaymentVerification(Verification verification);

}
