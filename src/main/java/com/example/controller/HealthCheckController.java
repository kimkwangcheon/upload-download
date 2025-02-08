package com.example.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class HealthCheckController {

    @GetMapping("/health")
    public String healthCheck() {
        log.info("애플리케이션이 실행 중입니다");
        return "애플리케이션이 실행 중입니다";
    }
}
