package com.example.util;

import java.util.regex.Pattern;

public class CommonUtil {

    public static String getClientDevice(String userAgent) {
        String device = "";
        if (userAgent != null) {
            userAgent = userAgent.toLowerCase();
        } else {
            return null;
        }

        // 정규식으로 desktop, mobile, tablet을 구분
        // 정규식 자바 코드
        Pattern patternM = Pattern.compile("mozilla/5.0\\s\\((android|iphone|ipod|blackberry);"); // MOBILE
        Pattern patternM2 = Pattern.compile("android(?!.*tablet)");                   // MOBILE
        Pattern patternT = Pattern.compile("mozilla/5.0\\s\\((ipad|macintosh);");   // TABLET
        Pattern patternT1 = Pattern.compile("ipad");                                // TABLET
        Pattern patternT2 = Pattern.compile("(windows.*tablet)");                    // TABLET
        Pattern patternT3 = Pattern.compile("(android.*tablet)");                    // TABLET
        Pattern patternD = Pattern.compile("mozilla/5.0\\s\\((windows).*;");        // DESKTOP
        Pattern patternD1 = Pattern.compile("windows(?!.*tablet)");                   // DESKTOP
        Pattern patternD2 = Pattern.compile("(windows|macintosh|linux)");           // DESKTOP

        if (patternM.matcher(userAgent).find()) {
            device = "MOBILE";
        } else if (patternM2.matcher(userAgent).find()) {
            device = "MOBILE";
        } else if (patternT.matcher(userAgent).find()) {
            device = "TABLET";
        } else if (patternT1.matcher(userAgent).find()) {
            device = "TABLET";
        } else if (patternT2.matcher(userAgent).find()) {
            device = "TABLET";
        } else if (patternT3.matcher(userAgent).find()) {
            device = "TABLET";
        } else if (patternD.matcher(userAgent).find()) {
            device = "DESKTOP";
        } else if (patternD1.matcher(userAgent).find()) {
            device = "DESKTOP";
        } else if (patternD2.matcher(userAgent).find()) {
            device = "DESKTOP";
        } else {
            return "OTHERS";
        }

        return device;
    }

    public static String getClientOS(String userAgent) {
        String os = "";
        if (userAgent != null) {
            userAgent = userAgent.toLowerCase();
        } else {
            return null;
        }

        if (userAgent.contains("windows nt 10.0")) {
            os = "WINDOWS10";
        }else if (userAgent.contains("windows nt 6.1")) {
            os = "WINDOWS7";
        }else if (userAgent.contains("windows nt 6.2") || userAgent.contains("windows nt 6.3")) {
            os = "WINDOWS8";
        }else if (userAgent.contains("windows nt 6.0")) {
            os = "WINDOWSVISTA";
        }else if (userAgent.contains("windows nt 5.1")) {
            os = "WINDOWSXP";
        }else if (userAgent.contains("windows nt 5.0")) {
            os = "WINDOWS2000";
        }else if (userAgent.contains("windows nt 4.0")) {
            os = "WINDOWSNT";
        }else if (userAgent.contains("windows 98")) {
            os = "WINDOWS98";
        }else if (userAgent.contains("windows 95")) {
            os = "WINDOWS95";
        }else if (userAgent.contains("iphone")) {
            os = "IPHONE";
        }else if (userAgent.contains("ipad")) {
            os = "IPAD";
        }else if (userAgent.contains("android")) {
            os = "ANDROID";
        }else if (userAgent.contains("mac")) {
            os = "MAC";
        }else if (userAgent.contains("linux")) {
            os = "LINUX";
        }else{
            os = "OTHER";
        }
        return os;
    }

    public static String getClientBrowser(String userAgent) {
        String browser = "";
        if (userAgent != null) {
            userAgent = userAgent.toLowerCase();
        } else {
            return null;
        }

        if (userAgent.contains("trident/7.0")) {
            browser = "IE 11";
        }
        else if (userAgent.contains("msie 10")) {
            browser = "IE 10";
        }
        else if (userAgent.contains("msie 9")) {
            browser = "IE 9";
        }
        else if (userAgent.contains("msie 8")) {
            browser = "IE 8";
        }
        else if (userAgent.contains("edg/")) {
            browser = "EDGE";
        }
        else if (userAgent.contains("chrome/")) {
            browser = "CHROME";
        }
        else if (!userAgent.contains("chrome/") && userAgent.contains("safari/")) {
            browser = "SAFARI";
        }
        else if (userAgent.contains("firefox/")) {
            browser = "FIREFOX";
        }
        else {
            browser ="OTHER";
        }
        return browser;
    }
}
