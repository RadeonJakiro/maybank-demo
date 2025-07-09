package com.maybank.liangquan.demo.batch.paymentHistory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PaymentHistoryBatchScheduler {

	private final JobLauncher paymentHistoryLauncher;

	private final Job importPaymentJob;

	@Autowired
	PaymentHistoryBatchScheduler(JobLauncher launcher, Job job) {
		this.importPaymentJob = job;
		this.paymentHistoryLauncher = launcher;
	}

	@Scheduled(fixedRate = 600000) // Every 600 seconds for Demo (10 Minutes)
	public void runPaymentBatchJob() throws Exception {
		JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
				.toJobParameters();
		paymentHistoryLauncher.run(importPaymentJob, jobParameters);
	}
}
