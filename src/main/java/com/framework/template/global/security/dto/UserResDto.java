package com.framework.template.global.security.dto;

import lombok.Builder;
import lombok.Getter;

public class UserResDto {

    @Getter @Builder
    public static class LoginReqDto {
        private String username;
        private String password;
    }
}
