package com.example.controller;

import com.example.service.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class TestController {

    private final TestService testService;

    public TestController(TestService testService){
        this.testService = testService;
    }

    @GetMapping("/mybatis/test")
    public String mybatisTest() {
        return testService.mybatisTest();
    }
}
