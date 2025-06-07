package com.framework.template.domain.member.entity;

import com.framework.template.domain.member.constant.Role;
import com.framework.template.global.security.jwt.dto.JwtTokenDto;
import com.framework.template.global.util.DateTimeUtils;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String refreshToken;

    private LocalDateTime tokenExpireTime;

    @Builder
    public Member(String loginId, String password, String name, String email, Role role) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public void updateRefreshToken(JwtTokenDto jwtTokenDto) {
        this.refreshToken = jwtTokenDto.getRefreshToken();
        this.tokenExpireTime = DateTimeUtils.converToLocalDateTime(jwtTokenDto.getRefreshTokenExpireTime());
    }
}
