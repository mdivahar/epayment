package com.kvb.epayment.icegate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentParameter {
	private String clientId;
	private String signature;
	private String secretKey;
	private ChallanData challanData;

}
