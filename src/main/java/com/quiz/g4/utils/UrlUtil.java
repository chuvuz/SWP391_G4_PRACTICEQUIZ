package com.quiz.g4.utils;

import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class UrlUtil {
    public static String getBaseUrl(HttpServletRequest request) {
        String serverHost = request.getHeader("X-Forwarded-Host");
        if (!StringUtils.isEmpty(serverHost)) return serverHost;
        return request.getRequestURL().toString().replace(request.getRequestURI(), "");
    }
}