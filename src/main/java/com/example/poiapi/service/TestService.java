package com.example.poiapi.service;

import com.example.poiapi.repository.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
