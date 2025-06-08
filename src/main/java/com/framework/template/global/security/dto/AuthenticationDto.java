package com.framework.template.global.security.dto;

import com.framework.template.domain.member.constant.Role;
import com.framework.template.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

@Getter @Builder
public class AuthenticationDto {

    private String loginId;
    private String password;
    private String name;
    private String email;
    private Role role;

    public static AuthenticationDto of(Member member) {

        return AuthenticationDto.builder()
                .loginId(member.getLoginId())
                .password(member.getPassword())
                .name(member.getName())
                .email(member.getEmail())
                .role(member.getRole())
                .build()
                ;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> this.role.toAuthority());
        return authorities;
    }
}
