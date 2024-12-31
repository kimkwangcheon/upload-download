package com.example.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UploadMapper {
    // upload xssf
    void insertAddress(@Param("data") List<Map<String, Object>> data);

    // upload workbook
    void insertBooks(@Param("data") List<Map<String, Object>> data);
}
