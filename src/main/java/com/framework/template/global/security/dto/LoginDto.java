package com.framework.template.global.security.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class LoginDto {

    @Getter @Setter @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Request {
        private String loginId;
        private String password;

        public Request(String loginId, String password) {
            this.loginId = loginId;
            this.password = password;

        }
    }

    @Getter @Setter
    public static class Response {
        private String loginId;
        private String name;
        private String email;

        public Response(AuthenticationDto authenticationDto) {
            this.loginId = authenticationDto.getLoginId();
            this.name = authenticationDto.getName();
            this.email = authenticationDto.getEmail();
        }
    }
}
