package com.maybank.liangquan.demo;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableBatchProcessing
@SpringBootApplication
@EnableJpaRepositories("com.maybank.liangquan.demo.repository") // Ensure package is scanned
@EntityScan("com.maybank.liangquan.demo.object") // If your entities are in a different package
@ComponentScan("com.maybank.liangquan.demo") // Scan all components
@EnableScheduling
public class MaybankDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaybankDemoApplication.class, args);
	}

}
