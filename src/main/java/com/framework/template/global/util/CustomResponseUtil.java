package com.framework.template.global.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.template.global.dto.ResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class CustomResponseUtil {

    public static void success(HttpServletResponse response, String msg, Object dto) {

        try {
            ObjectMapper om = new ObjectMapper();
            ResponseDto<?> responseDto = new ResponseDto<>(1, msg, dto);
            String responseBody = om.writeValueAsString(responseDto);

            response.setContentType("application/json;charset=utf-8");
            response.setStatus(200);
            response.getWriter().println(responseBody);
        } catch (Exception e) {
            log.error("response success 파싱 에러 : {0}", e);
        }

    }

    public static void fail(HttpServletResponse response, String msg, HttpStatus httpStatus) {

        try {
            ObjectMapper om = new ObjectMapper();
            ResponseDto<?> responseDto = new ResponseDto<>(-1, msg, null);
            String responseBody = om.writeValueAsString(responseDto);

            response.setContentType("application/json;charset=utf-8");
            response.setStatus(httpStatus.value());
            response.getWriter().println(responseBody);
        } catch (Exception e) {
            log.error("response error 서버 파싱 에러");
        }
    }
}
