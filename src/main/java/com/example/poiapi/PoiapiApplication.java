package com.example.poiapi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@MapperScan("com.example.poiapi.repository")
@SpringBootApplication
public class PoiapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PoiapiApplication.class, args);
	}

}
