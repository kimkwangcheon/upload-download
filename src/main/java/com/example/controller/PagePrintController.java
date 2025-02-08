package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PagePrintController {
    
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/upload_test", method = RequestMethod.GET)
    public String uploadTest() {
        return "upload_test";
    }

    @RequestMapping(value = "/naver_maps_test", method = RequestMethod.GET)
    public String naverMapsTest() {
        return "naver_maps_test";
    }
}
