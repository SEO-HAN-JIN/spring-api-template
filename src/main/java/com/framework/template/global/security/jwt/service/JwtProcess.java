package com.framework.template.global.security.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.framework.template.domain.member.constant.Role;
import com.framework.template.domain.member.entity.Member;
import com.framework.template.domain.member.repository.MemberRepository;
import com.framework.template.global.security.context.CustomUser;
import com.framework.template.global.security.dto.AuthenticationDto;
import com.framework.template.global.security.jwt.constant.GrantType;
import com.framework.template.global.security.jwt.constant.TokenType;
import com.framework.template.global.security.jwt.dto.JwtTokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
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
        String accessToken = createAccessToken(authenticationDto.getLoginId(), authenticationDto.getRole(), accessTokenExpireTime);
        String refreshToken = createRefreshToken(authenticationDto.getLoginId(), authenticationDto.getRole(), refreshTokenExpireTime);

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

    public String createAccessToken(String loginId, Role role, Date expirationTime) {
        String accessToken = JWT.create()
                .withSubject(TokenType.ACCESS.name())
                .withIssuedAt(new Date())
                .withExpiresAt(expirationTime)
                .withClaim("loginId", loginId)
                .withClaim("role", role.name())
                .sign(Algorithm.HMAC512(tokenSecret.getBytes(StandardCharsets.UTF_8)));
        return accessToken;
    }

    public String createRefreshToken(String loginId, Role role, Date expirationTime) {
        String refreshToken = JWT.create()
                .withSubject(TokenType.REFRESH.name())
                .withIssuedAt(new Date())
                .withExpiresAt(expirationTime)
                .withClaim("loginId", loginId)
                .withClaim("role", role.name())
                .sign(Algorithm.HMAC512(tokenSecret.getBytes(StandardCharsets.UTF_8)));
        return refreshToken;
    }


    // 토큰 검증 (return 되는 LoginUser 객체를 강제로s 시큐리티 세션에 직접 주입할 예정)
    public static AuthenticationDto verify(String token) {
        try {
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512("Authentication")).build().verify(token);

            String loginId = decodedJWT.getClaim("loginId").asString();
            String role = decodedJWT.getClaim("role").asString();

            Member member = Member.builder().loginId(loginId).role(Role.valueOf(role)).build();

            return AuthenticationDto.of(member);
        } catch (TokenExpiredException e) {
            throw new TokenExpiredException("AccessToken이 만료되었습니다.", e.getExpiredOn());
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException("유효하지 않은 토큰입니다.");
        }
    }
}
