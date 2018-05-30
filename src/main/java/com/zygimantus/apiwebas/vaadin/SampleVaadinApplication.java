package com.zygimantus.apiwebas.vaadin;

import java.sql.SQLException;

import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@ComponentScan(basePackages = "com.zygimantus.apiwebas.vaadin")
public class SampleVaadinApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(SampleVaadinApplication.class);

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(SampleVaadinApplication.class, args);
		LOGGER.info("Application started, active profiles are:");

		for (String profile : context.getEnvironment().getActiveProfiles()) {
			LOGGER.info(">>> " + profile);
		}

	}

	@Bean(initMethod = "start", destroyMethod = "stop")
	public Server h2Server() throws SQLException {
		return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
	}
}
