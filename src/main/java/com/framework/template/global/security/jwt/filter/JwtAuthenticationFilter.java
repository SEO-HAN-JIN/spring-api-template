package com.framework.template.global.security.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.template.domain.member.service.MemberService;
import com.framework.template.global.error.ErrorCode;
import com.framework.template.global.security.context.CustomUser;
import com.framework.template.global.security.jwt.dto.JwtTokenDto;
import com.framework.template.global.security.jwt.service.JwtProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import com.framework.template.global.security.dto.UserResDto;
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
    private final MemberService memberService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtProcess jwtProcess, MemberService memberService) {
        setFilterProcessesUrl("/api/login");
        this.authenticationManager = authenticationManager;
        this.jwtProcess = jwtProcess;
        this.memberService = memberService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.debug("debug : attemptAuthentication 호출");

        try {
            ObjectMapper om = new ObjectMapper();
            UserResDto.LoginReqDto loginReqDto = om.readValue(request.getInputStream(), UserResDto.LoginReqDto.class);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginReqDto.getUsername(), loginReqDto.getPassword());
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

        memberService.updateRefreshToken(loginUser.getAuthenticationDto().getLoginId(), jwtTokenDto);

        response.setHeader("Authorization", "Bearer " + jwtTokenDto.getAccessToken());
        response.setHeader("Refresh-Token", jwtTokenDto.getRefreshToken());
    }
}
