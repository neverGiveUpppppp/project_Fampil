package com.fampill.api.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.fampill.api")
@MapperScan("com.fampill.api.mapper")
public class JavaWeb06SpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaWeb06SpringBootApplication.class, args);
	}

}
