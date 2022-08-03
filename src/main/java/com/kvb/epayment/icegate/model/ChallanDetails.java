package com.kvb.epayment.icegate.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChallanDetails {

	private long challanNumber;
	private long documentNumber;
	private String dutyToPay;
	private long challanDate;
	private String challanStatus;
	private List<MajorHeads> majorHeads;
	
}
