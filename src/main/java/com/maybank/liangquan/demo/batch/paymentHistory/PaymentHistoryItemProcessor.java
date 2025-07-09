package com.maybank.liangquan.demo.batch.paymentHistory;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.maybank.liangquan.demo.object.PaymentHistory;

public class PaymentHistoryItemProcessor implements ItemProcessor<PaymentHistory, PaymentHistory> {

	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentHistoryItemProcessor.class);

	@Override
	public PaymentHistory process(PaymentHistory item) throws Exception {

		LOGGER.debug("Display AccNo : {}", item.getAccountNo());
		LOGGER.debug("Display Desc : {}", item.getDesc());
		LOGGER.debug("Display TrxAmount : {}", item.getTrxAmount());
		LOGGER.debug("Display Customer : {}", item.getCustomerId());
		// Validate and transform data
		if (item.getTrxAmount() == null || item.getTrxAmount().compareTo(BigDecimal.ZERO) <= 0) {
			LOGGER.debug("Invalid transaction amount for : {}", item.getAccountNo());
			return null; // Skip this record
		}

		// Additional business logic can be added here
		return item;
	}
}
