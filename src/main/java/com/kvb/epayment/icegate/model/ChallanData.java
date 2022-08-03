package com.kvb.epayment.icegate.model;

import java.util.List;

import lombok.Data;

@Data
public class ChallanData {

	private int totalChallan;
	private String totalDutyToPay;
	private String paymentChannel;
	private String igReferenceNumber;
	private String icegateReturnUrl;
	private int entityType;
	private String idNumber;
	private String idName;
	private String documentType;
	private long transactionExpiryDate;
	private List<MajorHeadsVerticalSum> majorHeadsVerticalSum;
	private List<ChallanDetails> challanDetails;
	private String challanIgReferenceNumber;
	private String amountPaidAtBank;
	private String bankBranchCode;
	private String bankTransactionId;
	private long dateOfPayment;
	private String transactionStatus;
	private String errorReason;
}
