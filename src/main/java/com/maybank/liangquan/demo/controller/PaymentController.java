package com.maybank.liangquan.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.maybank.liangquan.demo.exception.ConcurrentUpdateException;
import com.maybank.liangquan.demo.object.PaymentHistoryDto;
import com.maybank.liangquan.demo.object.UpdatePaymentHistoryRequest;
import com.maybank.liangquan.demo.object.UserCredential;
import com.maybank.liangquan.demo.repository.UserCredentialRepository;
import com.maybank.liangquan.demo.security.JwtTokenProvider;
import com.maybank.liangquan.demo.service.PaymentService;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

	private final PaymentService paymentService;

	private final JwtTokenProvider jwtTokenProvider;

	private UserCredentialRepository ucRepository;

	@Autowired
	PaymentController(PaymentService paymentService, JwtTokenProvider jwtTokenProvider,
			UserCredentialRepository ucRepository) {
		this.paymentService = paymentService;
		this.jwtTokenProvider = jwtTokenProvider;
		this.ucRepository = ucRepository;
	}

	@ResponseBody
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	private String entry() {
		return "Payment Page";
	}

	@RequestMapping(value = "/history", method = RequestMethod.GET)
	public ResponseEntity<Page<PaymentHistoryDto>> getPaymentHistory(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestHeader("Authorization") String authHeader) {

		String jwt = authHeader.substring(7);
		String username = jwtTokenProvider.getUsername(jwt);
		UserCredential uc = ucRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		return ResponseEntity.ok(paymentService.getPaymentHistory(uc.getUsername(),
				PageRequest.of(page, size, Sort.by("trxDate").descending())));
	}

	@RequestMapping(value = "/{id}/description", method = RequestMethod.PATCH)
	public ResponseEntity<PaymentHistoryDto> updateDescription(@PathVariable String id,
			@RequestBody UpdatePaymentHistoryRequest request, @RequestHeader("Authorization") String authHeader) {

		String jwt = authHeader.substring(7);
		String username = jwtTokenProvider.getUsername(jwt);
		UserCredential uc = ucRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		try {
			return ResponseEntity.ok(paymentService.updateDescription(id, request.getDescription()));
		} catch (ObjectOptimisticLockingFailureException e) {
			throw new ConcurrentUpdateException(
					"This record was updated by another user. Please refresh and try again.");
		}
	}

}
