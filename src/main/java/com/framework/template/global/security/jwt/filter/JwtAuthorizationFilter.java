package com.framework.template.global.security.jwt.filter;

import com.framework.template.global.security.context.CustomUser;
import com.framework.template.global.security.dto.AuthenticationDto;
import com.framework.template.global.security.jwt.service.JwtProcess;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtProcess jwtProcess;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtProcess jwtProcess) {
        super(authenticationManager);
        this.jwtProcess = jwtProcess;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (isHeaderVerify(request, response)) {
            // 토큰 검증
            String authorizationHeader = request.getHeader("Authorization");
            String token = authorizationHeader.split(" ")[1];
            AuthenticationDto authenticationDto = JwtProcess.verify(token);

            // 임시 세션 (UserDetails 타입 or username)
            Authentication authentication = new UsernamePasswordAuthenticationToken(authenticationDto.getLoginId(), null, authenticationDto.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

        }
        chain.doFilter(request, response);
    }

    private boolean isHeaderVerify(HttpServletRequest request, HttpServletResponse response) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            return false;
        } else {
            return true;
        }
    }
}
