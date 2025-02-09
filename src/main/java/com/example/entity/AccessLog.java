package com.example.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Data
public class AccessLog {
    private Long id;
    private String ipAddress;
    private ZonedDateTime createdAt;
    private String pagePath;
    private String restapiMethod;
    private String restapiUrl;
    private boolean pageExists;
    private String fileExtension;
    private boolean errorOccurred;
    private String errorMessage;

    public void setCreatedAtWithTimeZone() {
        this.createdAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    }
}