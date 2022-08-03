package com.kvb.epayment.icegate.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationConfig {

	public static ApplicationContext getApplicationContextInstance() {
		ApplicationContext ctx = new AnnotationConfigApplicationContext();
		return ctx;
	}
}
