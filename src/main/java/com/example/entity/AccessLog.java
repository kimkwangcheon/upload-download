package com.example.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AccessLog {
    private Long id;
    private String ipAddress;
    private LocalDateTime createdAt;
    private String pagePath;
    private String restapiMethod;
    private String restapiUrl;
    private boolean pageExists;
    private String fileExtension;
    private boolean errorOccurred;
    private String errorMessage;
}