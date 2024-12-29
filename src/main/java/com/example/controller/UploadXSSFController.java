package com.example.controller;

import com.example.service.UploadXSSFService;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.*;

@Log4j2
@RestController
public class UploadXSSFController {

    private final UploadXSSFService uploadXSSFService;

    public UploadXSSFController(UploadXSSFService uploadXSSFService) {
        this.uploadXSSFService = uploadXSSFService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> handleFileUpload(MultipartHttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        long allStartTime = System.currentTimeMillis();

        if (!request.getFileNames().hasNext()) {
            response.put("message", "업로드 할 엑셀파일이 없습니다. 선택 후 다시 시도해주세요.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            MultipartFile file = null;
            Iterator<String> myIterator = request.getFileNames();

            if (myIterator.hasNext()) {
                file = request.getFile(myIterator.next());
            }

            response = uploadXSSFService.processExcelFile(Objects.requireNonNull(file));

            // [poi 엑셀 업로드] 총 종료 시간 측정
            long allEndTime = System.currentTimeMillis();
            // [poi 엑셀 업로드] 총 실행 시간 계산
            long allDuration = (allEndTime - allStartTime);
            log.debug(">>>>>>>>>> [poi] 엑셀 업로드 총 실행 시간: {} milliseconds", allDuration);
            response.put("allDuration", allDuration);
            response.put("message", "엑셀파일 업로드 성공: " + file.getOriginalFilename());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            response.put("message", "엑셀파일 업로드 실패: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (InvalidFormatException e) {
            e.printStackTrace();
            response.put("message", "엑셀파일 형식이 잘못되었습니다: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
