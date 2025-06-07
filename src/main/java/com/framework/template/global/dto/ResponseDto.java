package com.framework.template.global.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ResponseDto<T> {

    private final Integer code; // 1 성공, -1 실패
    private final String msg;
    private final T data;

    // 성공 응답
    public static <T> ResponseDto<T> success(T data) {
        return new ResponseDto<>(1, "요청 성공", data);
    }

    // 실패 응답
    public static <T> ResponseDto<T> fail(String message) {
        return new ResponseDto<>(-1, message, null);
    }

    // 실패 응답 (데이터 포함)
    public static <T> ResponseDto<T> fail(String message, T data) {
        return new ResponseDto<>(-1, message, data);
    }

    private static String createErrorMessage(BindingResult bindingResult) {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            if (!isFirst) {
                sb.append(", ");
            } else {
                isFirst = false;
            }
            sb.append("[");
            sb.append(fieldError.getField());
            sb.append("]");
            sb.append(fieldError.getDefaultMessage());
        }

        return sb.toString();
    }
}
