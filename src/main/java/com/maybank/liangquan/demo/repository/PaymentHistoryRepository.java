package com.maybank.liangquan.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maybank.liangquan.demo.object.PaymentHistory;

@Repository
public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, String> {

	// Customer Id refer to User UUID
	Page<PaymentHistory> findByCustomerId(String customerId, Pageable pageable);

}
