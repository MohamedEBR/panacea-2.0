package com.example.panacea;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.example.panacea.models")
public class PanaceaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PanaceaApplication.class, args);
	}

}
