package com.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
@Setter
public class AccessLog {
    private String ipAddress;
    private String ipAgentDevice;
    private String ipAgentOs;
    private String ipAgentBrowser;
    private String ipAgent;
    private String reqMethod;
    private String reqUrl;
    private String reqUrlFull;
    private Boolean pageExists;
    private String fileExtension;
    private Boolean errorOccurred;
    private String errorMessage;
    private ZonedDateTime createdAtKst;
    private LocalDateTime createdAtLdt;

    public void setCreatedAtWithTimeZone() {
        this.createdAtKst = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    }
}