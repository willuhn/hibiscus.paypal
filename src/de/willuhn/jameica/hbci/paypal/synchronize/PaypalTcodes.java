/**********************************************************************
 *
 * Copyright (c) 2023 Olaf Willuhn, Mathias Behrle
 * All rights reserved.
 * 
 * This software is copyrighted work licensed under the terms of the
 * Jameica License.  Please consult the file "LICENSE" for details. 
 *
 **********************************************************************/

package de.willuhn.jameica.hbci.paypal.synchronize;

import java.util.HashMap;
import java.util.Map;

/**
 * Enthält eine Liste von Paypal-Codes mit Beschreibungen.
 */
public class PaypalTcodes
{
  private final static HashMap<String, String> creditTCodes = new HashMap<>();
  private final static HashMap<String, String> debitTCodes = new HashMap<>();

  // structure:
  // "T-Code", "Description|SubCategory|Additional information"
  // https://developer.paypal.com/docs/reports/reference/tcodes/
  static
  {
    creditTCodes.put("T1300", "General Authorizations|Authorizations|");
    creditTCodes.put("T1302", "Void of Authorizations|Authorizations|");
    creditTCodes.put("T1301", "ReAuthorization|ReAuthorization|");
    creditTCodes.put("T1201", "Chargeback|Chargeback Activity|");
    creditTCodes.put("T1202", "Chargeback Reversal|Chargeback Activity|Reversals (Chargebacks won, where the merchant receives a reimbursement for the Chargeback amount).");
    creditTCodes.put("T1208", "PACMAN Buyer cancellation|Chargeback Activity|Adjustment to PayPal Account for Reversal of chargeback reversal based on cancellation of a chargeback in PayPal.");
    creditTCodes.put("T1207", "PACMAN Represenment Rejected|Chargeback Activity|Adjustment to PayPal Account for Reversal of payment based on rejection of the representment for a chargeback in PayPal.");
    creditTCodes.put("T1106", "Payment Reversal|Chargeback Activity|Initiated by PayPal.");
    creditTCodes.put("T1205", "Reimbursement of Chargeback|Chargeback Activity|");
    creditTCodes.put("T1111", "Cancellation of Hold for Dispute Resolution|Chargeback Releases|Cancellation of temporary hold to cover possible chargeback.");
    creditTCodes.put("T1110", "Hold on Balance for Dispute Investigation|Chargeback Releases|Hold to cover possible chargeback.");
    creditTCodes.put("T1106", "Payment Reversal|Chargeback Reversals|Initiated by PayPal.");
    creditTCodes.put("T2000", "General intraaccount transfer|Account Transfer|");
    creditTCodes.put("T2003", "Transfer To External GL Entity|Account Transfer|");
    creditTCodes.put("T2004", "Receivables financing|Account Transfer|Applicable only in Brazil.");
    creditTCodes.put("T1203", "Charge-off Adjustment|Adjustments|");
    creditTCodes.put("T1200", "General Account Adjustment|Adjustments|");
    creditTCodes.put("T1204", "Incentive Adjustment|Adjustments|");
    creditTCodes.put("T0803", "Balance Manager Account Bonus|Balance Manager Account Bonus|");
    creditTCodes.put("T1000", "BillPay transaction|BillPay transaction|");
    creditTCodes.put("T1601", "BML Credit - Transfer from BML|BML Credit -Transfer from BML|");
    creditTCodes.put("T1801", "BML Withdrawal - Transfer to BML|BML WithdrawalTransfer to BML|");
    creditTCodes.put("T1603", "Buyer Credit Payment Withdrawal - Transfer To BML|BML WithdrawalTransfer to BML|");
    creditTCodes.put("T0806", "Bonus for First ACH Use|Bonus for Bank Funding Program|");
    creditTCodes.put("T1900", "General Account Correction|Correction Adjustment|");
    creditTCodes.put("T0903", "Coupon Redemption|Coupon applied|");
    creditTCodes.put("T0808", "Credit Card Cash Back Bonus|Credit Card Cash Back Bonus|The monthly cash back accrued based on PayPal Extras Card signatures transactions for cash back program enrollees.");
    creditTCodes.put("T0701", "Credit Card Deposit for Negative PayPal Account Balance|Credit Card Deposit|");
    creditTCodes.put("T0807", "CC Security Charge Refund|Credit Card Security Credit|");
    creditTCodes.put("T0801", "Debit Card Cash Back Bonus|Debit Card Cash Back Bonus|Requires a PayPal debit card associated with the PayPal account. The monthly cash back accrued based on Merchant Debit Card signatures transactions for cash back program enrollees.");
    creditTCodes.put("T0503", "Hidden Virtual PayPal Debit Card Transaction|Debit card purchase|");
    creditTCodes.put("T1102", "Reversal of Debit Card Transaction|Debit Card Reversal|Reversal of a debit card payment. Requires a PayPal debit card.");
    creditTCodes.put("T0303", "Electronic Funds Transfer Funding|Electronic Funds Transfer|");
    creditTCodes.put("T0800", "General Bonus|General Bonus|");
    creditTCodes.put("T0700", "General Credit Card Deposit|General Credit Card Deposit|Purchase with a credit card.");
    creditTCodes.put("T0600", "General Credit Card Withdrawal|General Credit Card Withdrawal|Reversal of purchase with a credit card. Seen only in PayPal account of the credit card owner.");
    creditTCodes.put("T1400", "General Dividend|General Dividend|");
    creditTCodes.put("T0900", "General Incentive/Certificate Redemption|General Incentive/Certificate Redemption|");
    creditTCodes.put("T1100", "General Reversal|General Reversal|");
    creditTCodes.put("T1700", "General withdrawal to not bank entity|General Withdrawal to Non-Bank Institution|");
    creditTCodes.put("T3000", "General GI/Open wallet Transaction|Generic instrument(PLCC)|");
    creditTCodes.put("T0901", "Gift Certificate Redemption|Gift Certificate applied|");
    creditTCodes.put("T1116", "Instant Payment Review (IPR)reversal|Instant Payment Review|");
    creditTCodes.put("T0802", "Merchant Referral Account Bonus|Merchant Referral Account Bonus|Must have created a Merchant Referral Bonus link.");
    creditTCodes.put("T0905", "MSB Redemption|MSB Redemption|");
    creditTCodes.put("T0500", "General PayPal Debit Card Transaction|Other debit card activity|Requires a PayPal debit card associated with the PayPal account.");
    creditTCodes.put("T0505", "PayPal Debit Authorization|Other debit card activity|");
    creditTCodes.put("T0504", "PayPal Debit Card Cash Advance|Other debit card activity|Initiated by PayPal.");
    creditTCodes.put("T0501", "Virtual PayPal Debit Card Transaction|Other debit card activity|");
    creditTCodes.put("T1115", "Mass Pay Refund|Payment Refund|");
    creditTCodes.put("T1107", "Payment Refund|Payment Refund|");
    creditTCodes.put("T1119", "Generic Instrument/Open wallet Reversals (Buyer side)|Payment Reversals|");
    creditTCodes.put("T1114", "Mass Pay Reversal|Payment Reversals|");
    creditTCodes.put("T0804", "PayPal Buyer Warranty Bonus|PayPal Buyer Warranty Bonus|");
    creditTCodes.put("T0502", "PayPal Debit Card Withdrawal to ATM|PayPal Debit Card Withdrawal to ATM|");
    creditTCodes.put("T0805", "PayPal Protection Bonus, Payout for PayPal Buyer Protection, Payout for Full Protection with PayPal Buyer Credit|PayPal Protection Credit|");
    creditTCodes.put("T0902", "Points Incentive Redemption|Points Incentive applied|");
    creditTCodes.put("T1104", "Reversal of ACH Deposit|Reversal of ACH Deposit|");
    creditTCodes.put("T1101", "Reversal of ACH Withdrawal Transaction|Reversal of ACH Withdrawal Transaction|Reversal of a withdrawal from PayPal balance to a bank account.");
    creditTCodes.put("T1103", "Reversal of Points Usage|Reversal of Points Usage|");
    creditTCodes.put("T1118", "Generic Instrument/Open wallet Reversals (Seller side)|Reversals|");
    creditTCodes.put("T0904", "Reward Voucher Redemption|Reward Voucher applied|");
    creditTCodes.put("T2001", "Consolidation Transfer|Settlement consolidation|");
    creditTCodes.put("T0302", "ACH funding for Funds Recovery from Account balance|Transfer Deposit|");
    creditTCodes.put("T0300", "Bank Deposit to PP Account|Transfer Deposit|");
    creditTCodes.put("T0301", "PayPal Balance Manager Funding of PayPal Account|Transfer Deposit|PayPal-system generated.");
    creditTCodes.put("T0401", "Auto-sweep|Transfer Withdrawal|");
    creditTCodes.put("T0400", "General Withdrawal - Bank Account|Transfer Withdrawal|Settlement Withdrawal.");
    creditTCodes.put("T1701", "World Link withdrawal|World Link Withdrawal|");
    creditTCodes.put("T0106", "Chargeback Fee|Chargeback fee|Fees charged to a merchant for a credit card chargeback received.");
    creditTCodes.put("T0100", "General NonPayment Related Fee|Fee|");
    creditTCodes.put("T1109", "Fee Refund|Fee Credits|Fees refunded when refunds are issued.");
    creditTCodes.put("T1108", "Fee Reversal|Fee Credits|");
    creditTCodes.put("T0104", "Fee for Mass Pay request|Payment Fee|");
    creditTCodes.put("T0113", "Partner Fee|Payment Fee|");
    creditTCodes.put("T0107", "Payment Fee|Payment Fee|Fees charged for payments received, plus fees charged for sending Mass Payments.");
    creditTCodes.put("T0101", "Web Site Payment Pro Account Monthly Fee|Product Fee|");
    creditTCodes.put("T0102", "Fee for Foreign ACH withdrawal|Product Fee|");
    creditTCodes.put("T0103", "Fee for World Link Check withdrawal|Product Fee|");
    creditTCodes.put("T1109", "Fee Refund|Service Fee|Fees refunded when refunds are issued.");
    creditTCodes.put("T0112", "Gift Certificate Expiration Fee|Service Fee|");
    creditTCodes.put("T0110", "International CC withdrawal|Service Fee|");
    creditTCodes.put("T0111", "Warranty Fee for warranty purchase|Service Fee|");
    creditTCodes.put("T0108", "ATM withdrawal|Service Fee|");
    creditTCodes.put("T0109", "Auto-sweep from account|Service Fee|");
    creditTCodes.put("T0105", "Check withdrawal|Service Fee|");
    creditTCodes.put("T0114", "Dispute Fee|Service Fee|");
    creditTCodes.put("T0115", "Custody Fee|Service Fee|Service fee charged as a custody fee if user holds balance beyond allowed threshold during that period.");
    creditTCodes.put("T0116", "Bank Return Fee|Service Fee|");
    creditTCodes.put("T9900", "Other|Other|");
    creditTCodes.put("T9800", "Display only transaction row|Other - Non- balance Impacting|");
    creditTCodes.put("T9701", "Funds Payable|Funds payable|Funds provided by PayPal that need to be paid back.");
    creditTCodes.put("T9702", "Funds Receivable|Funds payable|Funds provided by PayPal that are being paid back.");
    creditTCodes.put("T9700", "Payables and Receivables|Payables and Receivables|");
    creditTCodes.put("T0010", "3rd Party Auction Payment|3rd Party Auction Payment|");
    creditTCodes.put("T0017", "Chained Payment/Refund|Chained Payment/Refund|Store to store transfer.");
    creditTCodes.put("T0005", "Direct Credit Card Payment|Direct Credit Card Payment|");
    creditTCodes.put("T0013", "Donation Payment|Donation Payment|");
    creditTCodes.put("T0004", "eBay Auction Payment|eBay Auction Payment|");
    creditTCodes.put("T0006", "Express Checkout Payment|Express Checkout Payment|");
    creditTCodes.put("T1602", "Buyer Credit Payment|General Buyer Credit Payment|");
    creditTCodes.put("T1800", "General Buyer Credit Payment|General Buyer Credit Payment|Must have signed up for Buyer Credit.");
    creditTCodes.put("T0000", "General Payment|General payment|");
    creditTCodes.put("T0019", "Generic Instrument funded Payment|Generic Instrument funded Payment|");
    creditTCodes.put("T0009", "Gift Certificate Payment (Purchase)|Gift Certificate Purchase|");
    creditTCodes.put("T1112", "Cancelled Transfer|MAM Reversal|");
    creditTCodes.put("T0001", "Mass Pay Payment|Mass Pay payment|");
    creditTCodes.put("T0011", "Mobile Payment|Mobile Payment|Payment made via a mobile phone.");
    creditTCodes.put("T0003", "PreApproved Payment Bill User Payment|Payment Bill User Payment|");
    creditTCodes.put("T2201", "Digital goods transaction|Payments received|Transfer from a credit card funded restricted balance.");
    creditTCodes.put("T1113", "Non Reference Credit Payment|Payments received|");
    creditTCodes.put("T1600", "PayPal Buyer Credit Payment Funding|PayPal Buyer Credit Payment Funding|Must have signed up for Buyer Credit.");
    creditTCodes.put("T0018", "PayPal Here Payment|PayPal Here Payment|");
    creditTCodes.put("T0008", "Postage Payment|Postage Payment|Payment to postage carrier.");
    creditTCodes.put("T0014", "Rebate Payment/Cash Back|Rebate Payment/Cash Back|");
    creditTCodes.put("T1117", "Rebate/Cashback reversal|Rebate/Cash back reversal|");
    creditTCodes.put("T0002", "Subscription Payment|Subscription Payment|");
    creditTCodes.put("T0020", "Tax collected by Partner|Tax collected by Partner|Refund of tax amount collected by Partners or Marketplaces in the event of a refund, reversal, or chargeback of a sale transaction.");
    creditTCodes.put("T0015", "Third Party Payout|Third Party Payout|");
    creditTCodes.put("T0016", "Third Party Recoupment|Third Party Recoupment|");
    creditTCodes.put("T0012", "Virtual Terminal Payment|Virtual Terminal Payment|");
    creditTCodes.put("T0007", "Website Payment|Website Payment|");
    creditTCodes.put("T0021", "Cryptocurrency|Cryptocurrency|");
    creditTCodes.put("T0022", "PayPal Seller Profiles|PayPal Seller Profiles|");
    creditTCodes.put("T1502", "Account Hold for ACH deposit|Account Release|");
    creditTCodes.put("T2113", "Blocked Payments|Account Release|");
    creditTCodes.put("T2111", "External Hold|Account Release|");
    creditTCodes.put("T1500", "General Account Hold|Account Release|");
    creditTCodes.put("T2101", "General Hold|Account Release|");
    creditTCodes.put("T2102", "General Hold Release|Account Release|");
    creditTCodes.put("T1503", "Hold on Available Balance|Account Release|");
    creditTCodes.put("T2109", "Gift Certificate Purchase|Gift Card redemption|When a gift certificate is purchased by your buyer, then that amount is placed on hold.");
    creditTCodes.put("T2110", "Gift Certificate Redemption|Gift Certificate Redemption|A gift certificate, when redeemed, is available for withdrawal.");
    creditTCodes.put("T1501", "Account Hold for Open Authorization|Open PayPal Authorizations|");
    creditTCodes.put("T2107", "Payment Hold|Payment review release|Payment holds are funds that belong to you that we set aside, such as security deposit. This amount is part of your total balance and will not be available for withdrawal.");
    creditTCodes.put("T2105", "Payment Review Hold|Payment review release|A payment review hold is used to protect you against unauthorized fraud loss and for seller protection. While a transaction is on payment review hold, we recommend that you do not ship the item. We are reviewing this transaction for up to 24 hours.");
    creditTCodes.put("T1105", "Reversal of General Account Hold|Release|");
    creditTCodes.put("T2108", "Payment Release|Release on payment review release|A payment hold, when released, is now available for withdrawal.");
    creditTCodes.put("T2106", "Payment Review Release|Release on payment review release|A payment review hold, when released, is now available for withdrawal.");
    creditTCodes.put("T2112", "External Release|Release on risk release|");
    creditTCodes.put("T2115", "Release on tax hold|Tax release|A tax hold, when released, is now available for withdrawal.");
    creditTCodes.put("T2103", "Reserve Hold|Reserve Release|PayPal is holding x% of funds for y days as a condition for processing for you. This amount is part of your total balance and will not be available for withdrawal.");
    creditTCodes.put("T2104", "Reserve Release|Reserve Release|A reserve, when released, is now available for withdrawal.");
    creditTCodes.put("T0202", "Conversion to Cover Negative Balance|Currency Conversion|PayPal-system generated.");
    creditTCodes.put("T0200", "General Currency Conversion|Currency Conversion|Transfer of funds from one currency balance to a different currency balance.");
    creditTCodes.put("T0201", "User Initiated Currency Conversion|Currency Conversion|");
    creditTCodes.put("T0117", "Campaign Fee|Campaign Fee|");
    
    debitTCodes.put("T0117", "Campaign Fee|Campaign Fee|");
    debitTCodes.put("T1300", "General Authorizations|Authorizations|");
    debitTCodes.put("T1302", "Void of Authorizations|Authorizations|");
    debitTCodes.put("T1301", "ReAuthorization|ReAuthorization|");
    debitTCodes.put("T1201", "Chargeback|Chargeback Activity|");
    debitTCodes.put("T1202", "Chargeback Reversal|Chargeback Activity|Reversals (Chargebacks won, where the merchant receives a reimbursement for the Chargeback amount).");
    debitTCodes.put("T1208", "PACMAN Buyer cancellation|Chargeback Activity|Adjustment to PayPal Account for Reversal of chargeback reversal based on cancellation of a chargeback in PayPal.");
    debitTCodes.put("T1207", "PACMAN Represenment Rejected|Chargeback Activity|Adjustment to PayPal Account for Reversal of payment based on rejection of the representment for a chargeback in PayPal.");
    debitTCodes.put("T1106", "Payment Reversal|Chargeback Activity|Initiated by PayPal.");
    debitTCodes.put("T1205", "Reimbursement of Chargeback|Chargeback Activity|");
    debitTCodes.put("T1201", "Chargeback|Chargeback Adjustments|");
    debitTCodes.put("T1202", "Chargeback Reversal|Chargeback Adjustments|");
    debitTCodes.put("T1208", "PACMAN Buyer cancellation|Chargeback Adjustments|Adjustment to PayPal Account for Reversal of chargeback reversal based on cancellation of a chargeback in the PayPal system.");
    debitTCodes.put("T1207", "PACMAN Represenment Rejected|Chargeback Adjustments|Adjustment to PayPal Account for Reversal of payment based on rejection of the representment for a chargeback in PayPal.");
    debitTCodes.put("T1205", "Reimbursement of Chargeback|Chargeback Adjustments|");
    debitTCodes.put("T1111", "Cancellation of Hold for Dispute Resolution|Chargeback Hold|Cancellation of temporary hold to cover possible chargeback.");
    debitTCodes.put("T1110", "Hold on Balance for Dispute Investigation|Chargeback Hold|Hold to cover possible chargeback.");
    debitTCodes.put("T2002", "Withdraw funds to Partner Account|Account Transfer|");
    debitTCodes.put("T0106", "Chargeback Fee|Chargeback fee|Fees charged to a merchant for a credit card chargeback received.");
    debitTCodes.put("T0100", "General NonPayment Related Fee|Fee|");
    debitTCodes.put("T1109", "Fee Refund|Fee Credits|Fees refunded when refunds are issued.");
    debitTCodes.put("T1108", "Fee Reversal|Fee Credits|");
    debitTCodes.put("T0104", "Fee for Mass Pay request|Payment Fee|");
    debitTCodes.put("T0113", "Partner Fee|Payment Fee|");
    debitTCodes.put("T0107", "Payment Fee|Payment Fee|Fees charged for payments received, plus fees charged for sending Mass Payments.");
    debitTCodes.put("T0101", "Web Site Payment Pro Account Monthly Fee|Product Fee|");
    debitTCodes.put("T0102", "Fee for Foreign ACH withdrawal|Product Fee|");
    debitTCodes.put("T0103", "Fee for World Link Check withdrawal|Product Fee|");
    debitTCodes.put("T1109", "Fee Refund|Service Fee|Fees refunded when refunds are issued.");
    debitTCodes.put("T0112", "Gift Certificate Expiration Fee|Service Fee|");
    debitTCodes.put("T0110", "International CC withdrawal|Service Fee|");
    debitTCodes.put("T0111", "Warranty Fee for warranty purchase|Service Fee|");
    debitTCodes.put("T0108", "ATM withdrawal|Service Fee|");
    debitTCodes.put("T0109", "Auto-sweep from account|Service Fee|");
    debitTCodes.put("T0105", "Check withdrawal|Service Fee|");
    debitTCodes.put("T0114", "Dispute Fee|Service Fee|");
    debitTCodes.put("T0115", "Custody Fee|Service Fee|Service fee charged as a custody fee if user holds balance beyond allowed threshold during that period.");
    debitTCodes.put("T0116", "Bank Return Fee|Service Fee|");
    debitTCodes.put("T9900", "Other|Other|");
    debitTCodes.put("T9800", "Display only transaction row|Other - Non- balance Impacting|");
    debitTCodes.put("T0010", "3rd Party Auction Payment|3rd Party Auction Payment|");
    debitTCodes.put("T0017", "Chained Payment/Refund|Chained Payment/Refund|Store to store transfer.");
    debitTCodes.put("T0005", "Direct Credit Card Payment|Direct Credit Card Payment|");
    debitTCodes.put("T0013", "Donation Payment|Donation Payment|");
    debitTCodes.put("T0004", "eBay Auction Payment|eBay Auction Payment|");
    debitTCodes.put("T0006", "Express Checkout Payment|Express Checkout Payment|");
    debitTCodes.put("T1603", "Buyer Credit Payment Withdrawal - Transfer To BML|General Buyer Credit Payment|");
    debitTCodes.put("T1800", "General Buyer Credit Payment|General Buyer Credit Payment|Must have signed up for Buyer Credit");
    debitTCodes.put("T0000", "General Payment|General payment|");
    debitTCodes.put("T0019", "Generic Instrument funded Payment|Generic Instrument funded Payment|");
    debitTCodes.put("T0009", "Gift Certificate Payment(Purchase)|Gift Certificate Purchase|");
    debitTCodes.put("T1112", "Cancelled Transfer|MAM Reversal|");
    debitTCodes.put("T0001", "Mass Pay Payment|Mass Pay payment|");
    debitTCodes.put("T0011", "Mobile Payment|Mobile Payment|Payment made via a mobile phone.");
    debitTCodes.put("T0003", "PreApproved Payment Bill User Payment|Payment Bill User Payment|");
    debitTCodes.put("T2201", "Digital goods transaction|Payment Sent|Transfer from a credit card funded restricted balance.");
    debitTCodes.put("T1113", "Non Reference Credit Payment|Payment Sent|");
    debitTCodes.put("T1600", "PayPal Buyer Credit Payment Funding|PayPal Buyer Credit Payment Funding|Must have signed up for Buyer Credit.");
    debitTCodes.put("T0018", "PayPal Here Payment|PayPal Here Payment|");
    debitTCodes.put("T0008", "Postage Payment|Postage Payment|");
    debitTCodes.put("T0014", "Rebate Payment/Cash Back|Rebate Payment/Cash Back|");
    debitTCodes.put("T1117", "Rebate/Cashback reversal|Rebate/Cashback reversal|");
    debitTCodes.put("T0002", "Subscription Payment|Subscription Payment|");
    debitTCodes.put("T0020", "Tax collected by Partner|Tax collected by Partner|Tax collected by Partners or Marketplaces on your sale transactions.");
    debitTCodes.put("T0015", "Third Party Payout|Third Party Payout|");
    debitTCodes.put("T0016", "Third Party Recoupment|Third Party Recoupment|");
    debitTCodes.put("T0012", "Virtual Terminal Payment|Virtual Terminal Payment|");
    debitTCodes.put("T0007", "Website Payment|Website Payment|");
    debitTCodes.put("T0021", "Cryptocurrency|Cryptocurrency|");
    debitTCodes.put("T0022", "PayPal Seller Profiles|PayPal Seller Profiles|");
    debitTCodes.put("T9701", "Funds Payable|Funds receivable|Funds provided by PayPal that need to be paid back.");
    debitTCodes.put("T9702", "Funds Receivable|Funds receivable|Funds provided by PayPal that are being paid back.");
    debitTCodes.put("T9700", "Payables and Receivables|Payables and Receivables|");
    debitTCodes.put("T2103", "Reserve Hold|Reserve Hold|PayPal is holding x% of funds for y days as a condition for processing for you. This amount is part of your total balance and will not be available for withdrawal.");
    debitTCodes.put("T2104", "Reserve Release|Reserve Hold|A reserve, when released, is now available for withdrawal.");
    debitTCodes.put("T0202", "Conversion to Cover Negative Balance|Currency Conversion|PayPal-system generated.");
    debitTCodes.put("T0200", "General Currency Conversion|Currency Conversion|Withdrawal of funds from one currency balance that is covered by funds from a different currency balance.");
    debitTCodes.put("T0201", "User Initiated Currency Conversion|Currency Conversion|");
    debitTCodes.put("T2000", "General intraaccount transfer|Account Transfer|");
    debitTCodes.put("T2003", "Transfer To External GL Entity|Account Transfer|");
    debitTCodes.put("T2004", "Receivables financing|Account Transfer|Applicable only in Brazil.");
    debitTCodes.put("T1200", "General Account Adjustment|Adjustments|");
    debitTCodes.put("T1204", "Incentive Adjustment|Adjustments|");
    debitTCodes.put("T0803", "Balance Manager Account Bonus|Balance Manager Account Bonus Reversal|");
    debitTCodes.put("T1000", "BillPay transaction|BillPay transaction|");
    debitTCodes.put("T1601", "BML Credit - Transfer from BML|BML Credit - Transfer from BML|");
    debitTCodes.put("T1801", "BML Withdrawal - Transfer to BML|BML WithdrawalTransfer to BML|");
    debitTCodes.put("T1602", "Buyer Credit Payment|BML WithdrawalTransfer to BML|");
    debitTCodes.put("T0806", "Bonus for First ACH Use|Bonus for First Bank Activity Reversal|");
    debitTCodes.put("T0800", "General Bonus|Bonus Reversal|");
    debitTCodes.put("T0807", "CC Security Charge Refund|CC Security Charge Refund|");
    debitTCodes.put("T1203", "Charge-off Adjustment|Charge-off Adjustment|");
    debitTCodes.put("T1900", "General Account Correction|Correction Adjustment|");
    debitTCodes.put("T0903", "Coupon Redemption|Coupon applied|");
    debitTCodes.put("T0808", "Credit Card Cash Back Bonus|Credit Card Cash Back Bonus|The monthly cash back accrued based on PayPal Extras Card signatures transactions for cash back program enrollees.");
    debitTCodes.put("T0701", "Credit Card Deposit for Negative PayPal Account Balance|Credit Card Deposit|");
    debitTCodes.put("T0505", "PayPal Debit Authorization|Debit Card Authorization|");
    debitTCodes.put("T0801", "Debit Card Cash Back Bonus|Debit Card Bonus Reversal|Requires a PayPal debit card associated with the PayPal account. The monthly cash back accrued based on Merchant Debit Card signatures transactions for cash back program enrollees.");
    debitTCodes.put("T0500", "General PayPal Debit Card Transaction|Debit card purchase|Requires a PayPal debit card associated with the PayPal account. The monthly cash back accrued based on Merchant Debit Card signatures transactions for cash back program enrollees.");
    debitTCodes.put("T0501", "Virtual PayPal Debit Card Transaction|Debit card purchase|");
    debitTCodes.put("T0303", "Electronic Funds Transfer Funding|Electronic Funds Transfer|");
    debitTCodes.put("T0700", "General Credit Card Deposit|General Credit Card Deposit|Purchase with a credit card.");
    debitTCodes.put("T0600", "General Credit Card Withdrawal|General Credit Card Withdrawal|Reversal of purchase with a credit card. Seen only in PayPal account of the credit card owner.");
    debitTCodes.put("T1400", "General Dividend|General Dividend|");
    debitTCodes.put("T0900", "General Incentive/ Certificate Redemption|General Incentive/Certificate Redemption|");
    debitTCodes.put("T1100", "General Reversal|General Reversal|");
    debitTCodes.put("T1700", "General withdrawal to not bank entity|General Withdrawal to Non-Bank Institution|");
    debitTCodes.put("T3000", "General GI/Open wallet Transaction|Generic instrument (PLCC)|");
    debitTCodes.put("T0901", "Gift Certificate Redemption|Gift Certificate applied|");
    debitTCodes.put("T1116", "Instant Payment Review (IPR)reversal|Instant Payment Review|");
    debitTCodes.put("T0802", "Merchant Referral Account Bonus|Merchant Referral Account Bonus Reversal|Must have created a Merchant Referral Bonus link.");
    debitTCodes.put("T0905", "MSB Redemption|MSB Redemption|");
    debitTCodes.put("T0503", "Hidden Virtual PayPal Debit Card Transaction|Other debit card activity|");
    debitTCodes.put("T0502", "PayPal Debit Card Withdrawal to ATM|Other debit card activity|");
    debitTCodes.put("T1102", "Reversal of Debit Card Transaction|Other debit card activity|Reversal of a debit card payment. Requires a PayPal debit card.");
    debitTCodes.put("T1115", "Mass Pay Refund|Payment Refund|");
    debitTCodes.put("T1107", "Payment Refund|Payment Refund|");
    debitTCodes.put("T1119", "Generic Instrument/Open wallet Reversals (Buyer side)|Payment Reversals|");
    debitTCodes.put("T1114", "Mass Pay Reversal|Payment Reversals|");
    debitTCodes.put("T0805", "PayPal Protection Bonus, Payout for PayPal Buyer Protection, Payout for Full Protection with PayPal Buyer Credit|PayPal Bonus Reversal|");
    debitTCodes.put("T0804", "PayPal Buyer Warranty Bonus|PayPal Buyer Warranty Bonus Reversal|");
    debitTCodes.put("T0504", "PayPal Debit Card Cash Advance|PayPal Debit Card Cash Advance|");
    debitTCodes.put("T0902", "Points Incentive Redemption|Points Incentive applied|");
    debitTCodes.put("T1104", "Reversal of ACH Deposit|Reversal of ACH Deposit|");
    debitTCodes.put("T1101", "Reversal of ACH Withdrawal Transaction|Reversal of ACH Withdrawal Transaction|Reversal of a withdrawal from PayPal balance to a bank account.");
    debitTCodes.put("T1103", "Reversal of Points Usage|Reversal of Points Usage|");
    debitTCodes.put("T1118", "Generic Instrument/Open wallet Reversals (Seller side)|Reversals|");
    debitTCodes.put("T0904", "Reward Voucher Redemption|Reward Voucher applied|");
    debitTCodes.put("T2001", "Consolidation Transfer|Settlement consolidation|");
    debitTCodes.put("T0302", "ACH funding for Funds Recovery from Account balance|Transfer Deposit|");
    debitTCodes.put("T0300", "Bank Deposit to PP Account|Transfer Deposit|");
    debitTCodes.put("T0301", "PayPal Balance Manager Funding of PayPal Account|Transfer Deposit|PayPal-system generated.");
    debitTCodes.put("T0401", "Auto-sweep|Transfer Withdrawal|");
    debitTCodes.put("T0400", "General Withdrawal - Bank Account|Transfer Withdrawal|Settlement Withdrawal.");
    debitTCodes.put("T0402", "Withdrawal to Hyperwallet|Transfer Withdrawal|");
    debitTCodes.put("T0403", "Withdrawals initiated by user manually. Not related to automated scheduled withdrawals|Transfer Withdrawal|");
    debitTCodes.put("T1701", "World Link withdrawal|World Link Withdrawal|");
    debitTCodes.put("T1502", "Account Hold for ACH deposit|Account Holds|");
    debitTCodes.put("T2111", "External Hold|Account Holds|");
    debitTCodes.put("T1500", "General Account Hold|Account Holds|");
    debitTCodes.put("T2101", "General Hold|Account Holds|");
    debitTCodes.put("T2102", "General Hold Release|Account Holds|");
    debitTCodes.put("T1503", "Hold on Available Balance|Account Holds|");
    debitTCodes.put("T2109", "Gift Certificate Purchase|Gift Card Purchase|When a gift certificate is purchased by your buyer, then that amount is placed on hold.");
    debitTCodes.put("T2110", "Gift Certificate Redemption|Gift Certificate Redemption|A gift certificate, when redeemed, is available for withdrawal.");
    debitTCodes.put("T1105", "Reversal of General Account Hold|Hold|");
    debitTCodes.put("T1501", "Account Hold for Open Authorization|Open PayPal Authorizations|");
    debitTCodes.put("T2107", "Payment Hold|Payment review hold|Payment holds are funds that belong to you that we set aside, such as security deposit. This amount is part of your total balance and will not be available for withdrawal.");
    debitTCodes.put("T2105", "Payment Review Hold|Payment review hold|A payment review hold is used to protect you against unauthorized fraud loss and for seller protection. While a transaction is on payment review hold, we recommend that you do not ship the item. We are reviewing this transaction for up to 24 hours.");
    debitTCodes.put("T2113", "Blocked Payments|Release on compliance hold|");
    debitTCodes.put("T2108", "Payment Release|Release on payment review hold|A payment hold, when released, is now available for withdrawal.");
    debitTCodes.put("T2106", "Payment Review Release|Release on payment review hold|A payment review hold, when released, is now available for withdrawal.");
    debitTCodes.put("T2112", "External Release|Release on risk hold|");
    debitTCodes.put("T2114", "Tax hold|Tax hold|When a merchant account does not have a TIN and TPV crosses $600, then the gross amount is placed on hold.");
    debitTCodes.put("T2301", "Tax withholding to IRS|Tax hold|");
  }
  
  /**
   * Liefert den Wert für den angegebenen Code bei Credit-Buchungen.
   * @param code der Code.
   * @return der Wert für den Code oder NULL, wenn er nicht gefunden wurde.
   */
  public static String getCreditDescription(String code)
  {
    return getDescription(code,creditTCodes);
  }
      
  /**
   * Liefert den Wert für den angegebenen Code bei Debit-Buchungen.
   * @param code der Code.
   * @return der Wert für den Code oder NULL, wenn er nicht gefunden wurde.
   */
  public static String getDebitDescription(String code)
  {
    return getDescription(code,debitTCodes);
  }
  
  /**
   * Liefert den Wert für den angegebenen Code aus der Map.
   * @param tcode der TCode.
   * @param map die zu verwendene Map
   * @return die Beschreibung oder NULL, wenn keine gefunden wurde.
   */
  private static String getDescription(String tcode, Map<String,String> map)
  {
    if (tcode == null || map == null)
      return null;
    
    final String desc = map.get(tcode.toUpperCase());
    if (desc == null)
      return null;
    
    final String[] splitted = desc.split("\\|");
    return splitted != null && splitted.length > 0 ? splitted[0] : null;
  }
}
