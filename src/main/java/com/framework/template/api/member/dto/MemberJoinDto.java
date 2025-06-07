package com.framework.template.api.member.dto;

import com.framework.template.domain.member.entity.Member;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class MemberJoinDto {

    @Getter @Setter
    public static class Request {

        @NotEmpty
        @Pattern(regexp = "^[a-zA-Z]{2,20}$", message = "영문/숫자 2~20자 이내로 작성해주세요")
        private String loginId;

        @NotEmpty
        @Size(min = 4, max = 20)
        private String password;

        @NotEmpty
        @Pattern(regexp = "^[a-zA-Z0-9]{2,10}@[a-zA-Z0-9]{2,6}\\.[a-zA-Z]{2,3}$", message = "이메일 형식으로 작성해주세요")
        private String email;

        @NotEmpty
        private String name;

        public Member toEntity(BCryptPasswordEncoder passwordEncoder) {
            return Member.builder()
                    .loginId(loginId)
                    .password(passwordEncoder.encode(password))
                    .name(name)
                    .email(email)
                    .build();
        }
    }

    @Getter @Setter
    public static class Response {
        private Long id;
        private String loginId;
        private String name;
        private String email;

        public Response(Member member) {
            this.id = member.getId();
            this.loginId = member.getLoginId();
            this.name = member.getName();
            this.email = member.getEmail();
        }
    }

}
