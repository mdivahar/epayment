package com.kvb.epayment.icegate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Verification {

	private String signature;
	private String secretKey;
	private String challanData;

}
