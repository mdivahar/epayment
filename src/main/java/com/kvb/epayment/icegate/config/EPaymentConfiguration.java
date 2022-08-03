package com.kvb.epayment.icegate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kvb.epayment.icegate.model.ChallanData;
import com.kvb.epayment.icegate.model.EPayment;
import com.kvb.epayment.icegate.model.ESBRequest;
import com.kvb.epayment.icegate.model.InputVariables;
import com.kvb.epayment.icegate.model.PaymentParameter;

@Configuration
public class EPaymentConfiguration {

	@Bean
	@Scope("prototype")
	public InputVariables getInputVariablesInstance() {
		return new InputVariables();
	}

	@Bean
	@Scope("prototype")
	public ESBRequest getESBRequestInstance() {
		return new ESBRequest();
	}

	@Bean
	@Scope("prototype")
	public ObjectMapper getObjectMapperInstance() {
		return new ObjectMapper();
	}

	@Bean
	@Scope("prototype")
	public PaymentParameter getIcegatePaymentRequestInstance() {
		return new PaymentParameter();
	}

	@Bean
	@Scope("prototype")
	public EPayment getEPaymentInstance() {
		return new EPayment();
	}

	@Bean
	@Scope("prototype")
	public ChallanData getChallanDataInstance() {
		return new ChallanData();
	}

}
