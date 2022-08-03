package com.kvb.epayment.icegate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.kvb.epayment.icegate.config.EPaymentConfiguration;

import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
public class IcegateV2Application {

	public static void main(String[] args) {
		SpringApplication.run(IcegateV2Application.class, args);
		
	}

}
