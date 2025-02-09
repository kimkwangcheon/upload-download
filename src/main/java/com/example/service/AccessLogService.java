package com.example.service;

import com.example.entity.AccessLog;
import com.example.repository.AccessMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Log4j2
@Service
public class AccessLogService {
    private final AccessMapper accessMapper;

    public AccessLogService(AccessMapper accessMapper) {
        this.accessMapper = accessMapper;
    }

    public void saveAccessLog(String ipAddress, String pagePath, String restapiMethod, String restapiUrl,
                              Boolean pageExists, String fileExtension, Boolean errorOccurred, String errorMessage
    ) {
        AccessLog accessLog = new AccessLog();
        accessLog.setIpAddress(ipAddress);
        accessLog.setCreatedAt(LocalDateTime.now());
        accessLog.setPagePath(pagePath);
        accessLog.setRestapiMethod(restapiMethod);
        accessLog.setRestapiUrl(restapiUrl);
        accessLog.setPageExists(pageExists);
        accessLog.setFileExtension(fileExtension);
        accessLog.setErrorOccurred(errorOccurred);
        accessLog.setErrorMessage(errorMessage);

        try {
            accessLog.setErrorOccurred(false);
            accessLog.setErrorMessage(null);
            accessMapper.saveAccessLog(accessLog);
            log.info("Access log saved");
        } catch (Exception e) {
            accessLog.setErrorOccurred(true);
            accessLog.setErrorMessage("Error saving access log: " + e.getMessage());
            log.error("{}", accessLog.getErrorMessage(), e);
            accessMapper.saveAccessLog(accessLog);
        }
    }
}