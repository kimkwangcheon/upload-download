package com.example.repository;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestMapper {
    String selectTest();
}
