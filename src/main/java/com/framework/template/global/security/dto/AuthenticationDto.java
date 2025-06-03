package com.framework.template.global.security.dto;

import com.framework.template.domain.member.constant.Role;
import com.framework.template.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Getter @Builder
public class AuthenticationDto {

    private String loginId;
    private String password;
    private String name;
    private String email;
    private List<SimpleGrantedAuthority> authorities;

    public static AuthenticationDto of(Member member) {
        List<SimpleGrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority(member.getRole().toAuthority()));

        return AuthenticationDto.builder()
                .loginId(member.getLoginId())
                .password(member.getPassword())
                .name(member.getName())
                .email(member.getEmail())
                .authorities(authorities)
                .build()
                ;
    }
}
