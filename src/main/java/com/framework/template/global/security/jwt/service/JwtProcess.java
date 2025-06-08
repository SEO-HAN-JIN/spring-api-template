package com.framework.template.global.security.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.framework.template.domain.member.repository.MemberRepository;
import com.framework.template.global.security.context.CustomUser;
import com.framework.template.global.security.dto.AuthenticationDto;
import com.framework.template.global.security.jwt.constant.GrantType;
import com.framework.template.global.security.jwt.constant.TokenType;
import com.framework.template.global.security.jwt.dto.JwtTokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtProcess {

    @Value("${token.access-token-expiration-time}")
    private String accessTokenExpirationTime;

    @Value("${token.refresh-token-expiration-time}")
    private String refreshTokenExpirationTime;

    @Value("${token.secret}")
    private String tokenSecret;

    private final MemberRepository memberRepository;

    public JwtTokenDto createJwtTokenDto(CustomUser customUser) {
        Date accessTokenExpireTime = createAccessTokenExpireTime();
        Date refreshTokenExpireTime = createRefreshTokenExpireTime();

        AuthenticationDto authenticationDto = customUser.getAuthenticationDto();
        String accessToken = createAccessToken(authenticationDto.getLoginId(), authenticationDto.getAuthorities(), accessTokenExpireTime);
        String refreshToken = createRefreshToken(authenticationDto.getLoginId(), authenticationDto.getAuthorities(), refreshTokenExpireTime);

        return JwtTokenDto.builder()
                .grantType(GrantType.BEARER.getType())
                .accessToken(accessToken)
                .accessTokenExpireTime(accessTokenExpireTime)
                .refreshToken(refreshToken)
                .refreshTokenExpireTime(refreshTokenExpireTime)
                .build();
    }

    public Date createAccessTokenExpireTime() {
        return new Date(System.currentTimeMillis() + Long.parseLong(accessTokenExpirationTime));
    }

    public Date createRefreshTokenExpireTime() {
        return new Date(System.currentTimeMillis() + Long.parseLong(refreshTokenExpirationTime));
    }

    public String createAccessToken(String loginId, List<SimpleGrantedAuthority> authorities, Date expirationTime) {
        List<String> roles = authorities.stream().map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toList());
        String accessToken = JWT.create()
                .withSubject(TokenType.ACCESS.name())
                .withIssuedAt(new Date())
                .withExpiresAt(expirationTime)
                .withClaim("loginId", loginId)
                .withClaim("role", roles)
                .sign(Algorithm.HMAC512(tokenSecret.getBytes(StandardCharsets.UTF_8)));
        return accessToken;
    }

    public String createRefreshToken(String loginId, List<SimpleGrantedAuthority> authorities, Date expirationTime) {
        List<String> roles = authorities.stream().map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toList());
        String refreshToken = JWT.create()
                .withSubject(TokenType.REFRESH.name())
                .withIssuedAt(new Date())
                .withExpiresAt(expirationTime)
                .withClaim("loginId", loginId)
                .withClaim("role", roles)
                .sign(Algorithm.HMAC512(tokenSecret.getBytes(StandardCharsets.UTF_8)));
        return refreshToken;
    }
}
