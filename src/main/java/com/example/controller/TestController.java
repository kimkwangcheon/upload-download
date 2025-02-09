package com.example.controller;

import com.example.entity.AccessLog;
import com.example.service.AccessLogService;
import com.example.service.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class TestController {

    private final TestService testService;
    private final AccessLogService accessLogService;

    public TestController(TestService testService, AccessLogService accessLogService) {
        this.testService = testService;
        this.accessLogService = accessLogService;
    }

    @GetMapping("/mybatis/test")
    public String mybatisTest() {
        return testService.mybatisTest();
    }

    @GetMapping("/test/accessLog")
    public List<AccessLog> accessLog() {
        return accessLogService.selectAccessLog();
    }
}
