package com.maybank.liangquan.demo.batch.paymentHistory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.maybank.liangquan.demo.object.PaymentHistory;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableBatchProcessing
public class PaymentHistoryBatchConfig {

	@Value("${batch.paymentHistory.location}")
	private String paymentHistorySource;

	@Bean
	public FlatFileItemReader<PaymentHistory> reader() {
		return new FlatFileItemReaderBuilder<PaymentHistory>().name("paymentItemReader")
				.resource(new ClassPathResource(paymentHistorySource)).delimited().delimiter("|")
				.names("ACCOUNT_NUMBER", "TRX_AMOUNT", "DESCRIPTION", "TRX_DATE", "TRX_TIME", "CUSTOMER_ID")
				.linesToSkip(1) // Skip
				// Header
				// Row
				.fieldSetMapper(new PaymentHistoryFieldFormatter()).strict(false).build();
	}

	@Bean
	public PaymentHistoryItemProcessor processor() {
		return new PaymentHistoryItemProcessor();
	}

	@Bean
	public JpaItemWriter<PaymentHistory> writer(EntityManagerFactory entityManagerFactory) {
		return new JpaItemWriterBuilder<PaymentHistory>().entityManagerFactory(entityManagerFactory).build();
	}

	@Bean
	public Job importPaymentJob(JobRepository jobRepository, Step step1) {
		return new JobBuilder("importPaymentJob", jobRepository).flow(step1).end().build();
	}

	@Bean
	public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager,
			JpaItemWriter<PaymentHistory> writer, FlatFileItemReader<PaymentHistory> reader) {
		return new StepBuilder("step1", jobRepository).<PaymentHistory, PaymentHistory>chunk(10, transactionManager)
				.reader(reader).processor(processor()).writer(writer).build();
	}

}