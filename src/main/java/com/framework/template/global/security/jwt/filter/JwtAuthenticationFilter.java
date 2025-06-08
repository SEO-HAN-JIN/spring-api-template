package com.framework.template.global.security.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.template.global.security.context.CustomUser;
import com.framework.template.global.security.jwt.dto.JwtTokenDto;
import com.framework.template.global.security.dto.LoginDto;
import com.framework.template.global.security.jwt.service.JwtProcess;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import com.framework.template.global.util.CustomResponseUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtProcess jwtProcess;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtProcess jwtProcess) {
        setFilterProcessesUrl("/api/login");
        this.authenticationManager = authenticationManager;
        this.jwtProcess = jwtProcess;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.debug("debug : attemptAuthentication 호출");

        try {
            ObjectMapper om = new ObjectMapper();
            LoginDto.Request loginReqDto = om.readValue(request.getInputStream(), LoginDto.Request.class);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginReqDto.getLoginId(), loginReqDto.getPassword());
            return authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            // unsuccessfulAuthentication 호출
            throw new InternalAuthenticationServiceException(e.getMessage());
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        CustomResponseUtil.fail(response, "로그인 실패", HttpStatus.UNAUTHORIZED);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.debug("디버그 : successfulAuthentication 호출");
        CustomUser loginUser = (CustomUser) authResult.getPrincipal();
        JwtTokenDto jwtTokenDto = jwtProcess.createJwtTokenDto(loginUser);

        jwtProcess.updateRefreshToken(loginUser.getAuthenticationDto().getLoginId(), jwtTokenDto);

        response.setHeader("Authorization", "Bearer " + jwtTokenDto.getAccessToken());
        response.setHeader("Refresh-Token", jwtTokenDto.getRefreshToken());

        LoginDto.Response loginResDto = new LoginDto.Response(loginUser.getAuthenticationDto());
        CustomResponseUtil.success(response, "로그인 성공", loginResDto);
    }
}
