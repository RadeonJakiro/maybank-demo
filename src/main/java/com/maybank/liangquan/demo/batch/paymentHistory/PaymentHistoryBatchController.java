package com.maybank.liangquan.demo.batch.paymentHistory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/batch")
public class PaymentHistoryBatchController {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job importPaymentJob;

	@RequestMapping(value = "/import-payment-history", method = RequestMethod.GET)
	public ResponseEntity<String> launchJob(@RequestParam String filePath) {
		try {
			JobParameters jobParameters = new JobParametersBuilder().addString("inputFile", filePath)
					.addLong("startAt", System.currentTimeMillis()).toJobParameters();

			jobLauncher.run(importPaymentJob, jobParameters);

			return ResponseEntity.accepted().body("Batch job started");
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Error starting job: " + e.getMessage());
		}
	}
}