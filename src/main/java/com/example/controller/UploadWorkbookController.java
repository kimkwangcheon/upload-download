package com.example.controller;

import com.example.service.UploadWorkbookService;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

@Log4j2
@RestController
@RequestMapping("/api/v1")
public class UploadWorkbookController {
    private final UploadWorkbookService uploadWorkbookService;

    public UploadWorkbookController(UploadWorkbookService uploadWorkbookService) {
        this.uploadWorkbookService = uploadWorkbookService;
    }

    @PostMapping("/uploadExcelWorkbook")
    public ResponseEntity<Map<String, Object>> uploadWorkbook(MultipartHttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        // stopwatch로 시작 시간 측정
        // ---------------- 시작 ----------------
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // ---------------- 끝 ----------------

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

            response = uploadWorkbookService.uploadWorkbook(Objects.requireNonNull(file));

            // stopwatch로 종료 시간 측정
            // ---------------- 시작 ----------------
            stopWatch.stop();
            log.debug(">>>>>>>>>> [poi-uploadWorkbook][controller] 엑셀 업로드 총 실행 시간: {} milliseconds", stopWatch.getTotalTimeMillis());
            response.put("[controller] 엑셀 업로드 총 실행 시간 (ms)", stopWatch.getTotalTimeMillis());
            response.put("message", "엑셀파일 업로드 성공: " + file.getOriginalFilename());
            // ---------------- 끝 ----------------

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
