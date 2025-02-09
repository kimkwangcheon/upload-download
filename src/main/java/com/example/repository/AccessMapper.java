package com.example.repository;

import com.example.entity.AccessLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccessMapper {
    void saveAccessLog(AccessLog accessLog);
}
