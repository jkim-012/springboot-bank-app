package com.cos.bank.util;

import com.cos.bank.handler.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomResponseUtil {

    private static final Logger log = LoggerFactory.getLogger(CustomResponseUtil.class);

    // exception handling will use this when there is no authentication
    public static void noAuthentication(HttpServletResponse response, String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ResponseDto<?> responseDto = new ResponseDto<>(-1, message, null);
            // user objectMapper to change it into Json
            String responseBody = objectMapper.writeValueAsString(responseDto);

            response.setContentType("application/json; charset=utf-8");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getWriter().print(responseBody);
        } catch (IOException e) {
            log.error("Error: parsing error.");
        }
    }

    // exception handling will use this when there is no authorization
    public static void noAuthorization(HttpServletResponse response, String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ResponseDto<?> responseDto = new ResponseDto<>(-1, message, null);
            // user objectMapper to change it into Json
            String responseBody = objectMapper.writeValueAsString(responseDto);

            response.setContentType("application/json; charset=utf-8");
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().print(responseBody);
        } catch (IOException e) {
            log.error("Error: parsing error.");
        }
    }

    // when authentication succeed, this response will be return
    public static void authenticationSuccess(HttpServletResponse response, String message, Object dto) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ResponseDto<?> responseDto = new ResponseDto<>(1, message, dto);
            // user objectMapper to change it into Json
            String responseBody = objectMapper.writeValueAsString(responseDto);

            response.setContentType("application/json; charset=utf-8");
            response.setStatus(HttpStatus.OK.value());
            response.getWriter().print(responseBody);
        } catch (IOException e) {
            log.error("Error: parsing error.");
        }
    }





}
