package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResTargetTableSchemaInfo {
    private String COLUMN_NAME;
    private String DATA_TYPE;
    private Integer CHARACTER_MAXIMUM_LENGTH;
    private String IS_NULLABLE;
    private String COLUMN_KEY;
}
