package com.example.service;

import com.example.entity.AccessLog;
import com.example.repository.AccessMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@Service
public class AccessLogService {
    private final AccessMapper accessMapper;

    public AccessLogService(AccessMapper accessMapper) {
        this.accessMapper = accessMapper;
    }

    public void saveAccessLog(String ipAddress, String ipAgentDevice, String clientOS, String clientBrowser, String userAgent,
                              String reqMethod, String reqUrl, String reqUrlFull, Boolean pageExists,
                              String fileExtension, Boolean errorOccurred, String errorMessage
    ) throws Exception {
        AccessLog accessLog = new AccessLog();
        accessLog.setIpAddress(ipAddress);
        accessLog.setIpAgentDevice(ipAgentDevice);
        accessLog.setIpAgentOs(clientOS);
        accessLog.setIpAgentBrowser(clientBrowser);
        accessLog.setIpAgent(userAgent);
        accessLog.setReqMethod(reqMethod);
        accessLog.setReqUrl(reqUrl);
        accessLog.setReqUrlFull(reqUrlFull);
        accessLog.setPageExists(pageExists);
        accessLog.setFileExtension(fileExtension);
        accessLog.setErrorOccurred(errorOccurred);
        accessLog.setErrorMessage(errorMessage);
        accessLog.setCreatedAtWithTimeZone();
        accessLog.setCreatedAtLdt(LocalDateTime.now());
        log.debug("[[[[[ZonedDateTime(Asia/Seoul)]]]]] {}, [[[[[LocalDateTime.now()]]]]] {}"
                , accessLog.getCreatedAtKst(), accessLog.getCreatedAtLdt());

        try {
            accessLog.setErrorOccurred(false);
            accessLog.setErrorMessage(null);
            accessMapper.saveAccessLog(accessLog);
            log.info("Access log saved");
        } catch (Exception e) {
            accessLog.setErrorMessage("Error saving access log: " + e.getMessage());
            log.error("{}", accessLog.getErrorMessage(), e);
            accessMapper.saveAccessLog(accessLog);
            throw new Exception(accessLog.getErrorMessage());
        }
    }

    public List<AccessLog> selectAccessLog() {
        return accessMapper.selectAccessLog();
    }
}