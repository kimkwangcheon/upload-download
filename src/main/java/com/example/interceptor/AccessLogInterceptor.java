package com.example.interceptor;

import com.example.service.AccessLogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

@Log4j2
@Component
public class AccessLogInterceptor implements HandlerInterceptor {

    private final AccessLogService accessLogService;

    public AccessLogInterceptor(AccessLogService accessLogService) {
        this.accessLogService = accessLogService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean errorOccurred = false;
        String errorMessage = null;

        try {
            String ipAddress = request.getRemoteAddr();
            try {
                if (ipAddress.equals("0:0:0:0:0:0:0:1")) {
                    ipAddress = "127.0.0.1";
                } else {
                    InetAddress inetAddress = InetAddress.getByName(ipAddress);
                    if (inetAddress instanceof java.net.Inet6Address) {
                        ipAddress = InetAddress.getByName(inetAddress.getHostAddress()).getHostAddress();
                    }
                }
            } catch (UnknownHostException e) {
                errorOccurred = true;
                errorMessage = "Error converting IP address: ";
                errorMessage = (e.getMessage() == null) ? errorMessage : errorMessage.concat(e.getMessage());
                log.error("{}{}", errorMessage, e.getMessage(), e);
                accessLogService.saveAccessLog(ipAddress, null, null, null,
                            null, null, errorOccurred, errorMessage);
            }

            String reqMethod = request.getMethod();
            String reqUrl = request.getRequestURI();
            String reqUrlFull = request.getRequestURL().toString();

            // 매핑된 컨트롤러명 가져오기
            String controllerName = null;
            if (handler instanceof HandlerMethod handlerMethod) {
                controllerName = handlerMethod.getBeanType().getSimpleName();
            }

            // 페이지 존재 여부를 확인하는 로직 추가
            boolean pageExists = false;
            String fileExtension = null;
            if (Objects.equals(controllerName, "PagePrintController")) // 클래스명이 PagePrintController 인지 체크
            {
                pageExists = true;
                fileExtension = "jsp";
                accessLogService.saveAccessLog(ipAddress, reqMethod, reqUrl, reqUrlFull,
                        pageExists, fileExtension, errorOccurred, errorMessage);
            }

            // 정적 데이터는 'html' 파일만 로그 저장
            if (reqUrl.contains(".html")) {
                String realPath = new File("src/main/resources/static" + reqUrl).getAbsolutePath();
                File htmlFile = new File(realPath);
                // 실제로 파일이 존재할 때만 로그 저장
                if (htmlFile.exists()) {
                    pageExists = true;
                    fileExtension = "html";
                    accessLogService.saveAccessLog(ipAddress, reqMethod, reqUrl, reqUrlFull,
                            pageExists, fileExtension, errorOccurred, errorMessage);
                }
            }

            // restapi 호출 시 로그 저장
            if (reqUrl.contains("/api/v1")) {
                pageExists = false;
                fileExtension = null;
                accessLogService.saveAccessLog(ipAddress, reqMethod, reqUrl, reqUrlFull,
                        pageExists, fileExtension, errorOccurred, errorMessage);
            }

            return true;

        } catch (Exception e) {
            errorMessage = "Error in AccessLogInterceptor preHandle: " + e.getMessage();
            log.error("{}", errorMessage);
            accessLogService.saveAccessLog(null, null, null, null,
                    null, null, true, errorMessage);
            return false;
        }
    }
}