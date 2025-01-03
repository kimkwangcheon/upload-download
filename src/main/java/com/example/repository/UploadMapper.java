package com.example.repository;

import com.example.model.ResTargetTableSchemaInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface UploadMapper {
    // 공통
    List<ResTargetTableSchemaInfo> getTargetTableSchemaInfo(@Param("tableSchema") String tableSchema, @Param("tableName") String tableName);

    // upload xssf
    void insertAddress(@Param("data") List<Map<String, Object>> data);

    // upload workbook
    void insertBooks(@Param("data") List<Map<String, Object>> data);
}
