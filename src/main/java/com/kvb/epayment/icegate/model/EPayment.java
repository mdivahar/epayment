package com.kvb.epayment.icegate.model;

import lombok.Data;

@Data
public class EPayment {
	
	private String clientId;
	private String signature;
	private String secretKey;
	private String challanData;

}
