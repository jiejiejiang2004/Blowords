package com.example.blowords;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// MyBatis-Plus会自动扫描@Mapper注解的接口，不需要@MapperScan注解
public class BlowordsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlowordsApplication.class, args);
	}

}