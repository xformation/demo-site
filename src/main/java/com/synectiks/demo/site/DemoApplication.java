package com.synectiks.demo.site;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
@SpringBootApplication
@ComponentScan(basePackages = { "com.synectiks" })
public class DemoApplication {

	private static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

	private static ConfigurableApplicationContext ctx;

	/*@Autowired
	private Environment env;
	@Autowired
	private RestTemplate rest;*/

	public static void main(String[] args) {
		ctx = SpringApplication.run(DemoApplication.class, args);
		for (String bean : ctx.getBeanDefinitionNames()) {
			logger.info("Beans: " + bean);
		}
	}

	public static <T> T getBean(Class<T> clz) {
		return ctx.getBean(clz);
	}
}
