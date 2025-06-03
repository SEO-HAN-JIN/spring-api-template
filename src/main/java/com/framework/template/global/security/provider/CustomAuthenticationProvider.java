package com.framework.template.global.security.provider;

import com.framework.template.global.error.ErrorCode;
import com.framework.template.global.error.exception.BadCredentialsException;
import com.framework.template.global.security.context.CustomUser;
import com.framework.template.global.security.jwt.filter.JwtAuthenticationFilter;
import com.framework.template.global.security.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailService customUserDetailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication){

        String userId = authentication.getName();
        String userPw = authentication.getCredentials().toString();

        // 회원 조회
        CustomUser customUser = (CustomUser) customUserDetailService.loadUserByUsername(userId);

        // 패스워드 검증
        if (!passwordEncoder.matches(userPw, customUser.getPassword())){
            throw new BadCredentialsException(ErrorCode.INVALID_PASSWORD);
        }

        return new UsernamePasswordAuthenticationToken(customUser, null, customUser.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
