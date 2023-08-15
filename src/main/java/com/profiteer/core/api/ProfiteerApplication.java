package com.profiteer.core.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAutoConfiguration
@EnableTransactionManagement
@ComponentScan(basePackages = "com.profiteer")
@EntityScan("com.profiteer")
@EnableJpaRepositories(basePackages = "com.profiteer")
@EnableScheduling
public class ProfiteerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProfiteerApplication.class, args);
	}

}
