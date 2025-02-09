package com.example;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class UploadDownloadApplication {

	public static void main(String[] args) {
		SpringApplication.run(UploadDownloadApplication.class, args);
	}

	@PostConstruct
	public void init() {
		// 타임존 설정
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}
}
