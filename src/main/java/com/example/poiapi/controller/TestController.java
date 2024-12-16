package com.example.poiapi.controller;

import com.example.poiapi.service.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
