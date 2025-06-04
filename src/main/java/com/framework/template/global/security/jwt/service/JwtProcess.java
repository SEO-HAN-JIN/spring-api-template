package com.framework.template.global.security.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.framework.template.domain.member.constant.Role;
import com.framework.template.global.security.context.CustomUser;
import com.framework.template.global.security.jwt.constant.TokenType;
import com.framework.template.global.security.jwt.dto.JwtTokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class JwtProcess {

    @Value("${token.access-token-expiration-time}")
    private String accessTokenExpirationTime;

    @Value("${token.refresh-token-expiration-time}")
    private String refreshTokenExpirationTime;

    @Value("${token.secret}")
    private String tokenSecret;

    public JwtTokenDto createJwtTokenDto(Long loginId, Role role) {

    }

    public Date createAccessTokenExpireTime() {
        return new Date(System.currentTimeMillis() + Long.parseLong(accessTokenExpirationTime));
    }

    public Date createRefreshTokenExpireTime() {
        return new Date(System.currentTimeMillis() + Long.parseLong(refreshTokenExpirationTime));
    }

    public String createAccessToken(CustomUser customUser) {
        String accessToken = JWT.create()
                .withSubject(TokenType.ACCESS.name())
                .withIssuedAt(new Date())
                .withExpiresAt(createAccessTokenExpireTime())
                .withClaim("loginId", customUser.getAuthenticationDto().getLoginId())
//                .withClaim("role", customUser.getAuthenticationDto().get)
                .sign(Algorithm.HMAC512(tokenSecret.getBytes(StandardCharsets.UTF_8)));
        return accessToken;
    }

    public String createRefreshToken(CustomUser customUser) {
        String refreshToken = JWT.create()
                .withSubject(TokenType.REFRESH.name())
                .withIssuedAt(new Date())
                .withExpiresAt(createRefreshTokenExpireTime())
                .withClaim("loginId", customUser.getAuthenticationDto().getLoginId())
                .sign(Algorithm.HMAC512(tokenSecret.getBytes(StandardCharsets.UTF_8)));
        return refreshToken;
    }


}
