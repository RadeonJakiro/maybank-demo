package com.maybank.liangquan.demo.batch.paymentHistory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.maybank.liangquan.demo.object.PaymentHistory;

public class PaymentHistoryFieldFormatter implements FieldSetMapper<PaymentHistory> {

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

	@Override
	public PaymentHistory mapFieldSet(FieldSet fieldSet) throws BindException {
		PaymentHistory payment = new PaymentHistory();

		payment.setAccountNo(fieldSet.readString("ACCOUNT_NUMBER"));
		payment.setTrxAmount(fieldSet.readBigDecimal("TRX_AMOUNT"));
		payment.setDesc(fieldSet.readString("DESCRIPTION"));

		// Parse dates with custom formatters
		payment.setTrxDate(LocalDate.parse(fieldSet.readString("TRX_DATE"), DATE_FORMATTER));

		payment.setTrxTime(LocalTime.parse(fieldSet.readString("TRX_TIME"), TIME_FORMATTER));

		payment.setCustomerId(fieldSet.readString("CUSTOMER_ID"));

		return payment;
	}

}
