package com.test.schemaTest;

import com.test.schemaTest.service.InitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SchemaTestApplication implements CommandLineRunner {

	@Autowired
	private InitService initService;

	public static void main(String[] args) {
		SpringApplication.run(SchemaTestApplication.class, args);
	}

	@Override
	public void run(final String... args) throws Exception {
		initService.initialise();
	}
}
