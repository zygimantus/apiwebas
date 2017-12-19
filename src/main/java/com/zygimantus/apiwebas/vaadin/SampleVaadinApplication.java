package com.zygimantus.apiwebas.vaadin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.zygimantus.apiwebas.vaadin")
public class SampleVaadinApplication {

        private static final Logger LOGGER = LoggerFactory.getLogger(SampleVaadinApplication.class);

        public static void main(String[] args) {
                SpringApplication.run(SampleVaadinApplication.class, args);
        }
}
