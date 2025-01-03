package com.example.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface UploadMapper {
    // 공통
    List<Map<String, Object>> getTargetTableSchemaInfo(String tableSchema, String tableName);

    // upload xssf
    void insertAddress(@Param("data") List<Map<String, Object>> data);

    // upload workbook
    void insertBooks(@Param("data") ArrayList<HashMap<String, Object>> data);
}
