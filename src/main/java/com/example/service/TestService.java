package com.example.service;

import com.example.repository.TestMapper;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    private final TestMapper testMapper;

    public TestService(TestMapper testMapper){
        this.testMapper = testMapper;
    }

    public String mybatisTest() {
        return testMapper.selectTest();
    }
}
