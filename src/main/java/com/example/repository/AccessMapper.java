package com.example.repository;

import com.example.entity.AccessLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccessMapper {
    void saveAccessLog(AccessLog accessLog);
    List<AccessLog> selectAccessLog();
}
