package com.maybank.liangquan.demo.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maybank.liangquan.demo.object.PaymentHistory;
import com.maybank.liangquan.demo.object.PaymentHistoryDto;
import com.maybank.liangquan.demo.repository.PaymentHistoryRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PaymentService {

	private final PaymentHistoryRepository paymentHistRepo;

	@Autowired
	public PaymentService(PaymentHistoryRepository paymentHistRepo) {
		this.paymentHistRepo = paymentHistRepo;
	}

	@Transactional
	public Page<PaymentHistoryDto> getPaymentHistory(String userId, Pageable pageable) {
		return paymentHistRepo.findByCustomerId(userId, pageable).map(this::toPaymentHistoryDto);
	}

	@Transactional
	public PaymentHistoryDto updateDescription(String uuid, String description) {
		PaymentHistory paymentHistory = paymentHistRepo.findById(uuid)
				.orElseThrow(() -> new EntityNotFoundException("Payment History not found"));

		paymentHistory.setDesc(description);
		paymentHistory = paymentHistRepo.save(paymentHistory);

		return toPaymentHistoryDto(paymentHistory);
	}

	private PaymentHistoryDto toPaymentHistoryDto(PaymentHistory history) {
		PaymentHistoryDto dto = new PaymentHistoryDto();
		dto.setAccountNo(history.getAccountNo());
		dto.setDesc(history.getDesc());
		dto.setTrxAmount(history.getTrxAmount());
		dto.setTrxDate(history.getTrxDate());
		dto.setTrxTime(history.getTrxTime());
		dto.setUuid(history.getUuid());
		return dto;
	}

}
