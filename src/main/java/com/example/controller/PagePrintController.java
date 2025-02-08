package com.example.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Log4j2
@Controller
public class PagePrintController {
    
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        log.info("메인 페이지를 출력합니다");
        return "index";
    }

    @RequestMapping(value = "/upload_test", method = RequestMethod.GET)
    public String uploadTest() {
        log.info("엑셀 업로드 테스트 페이지를 출력합니다");
        return "upload_test";
    }

    @RequestMapping(value = "/naver_maps_test", method = RequestMethod.GET)
    public String naverMapsTest() {
        log.info("네이버 지도 테스트 페이지를 출력합니다");
        return "naver_maps_test";
    }
}
