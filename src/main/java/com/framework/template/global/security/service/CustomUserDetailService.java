package com.framework.template.global.security.service;

import com.framework.template.domain.member.entity.Member;
import com.framework.template.domain.member.repository.MemberRepository;
import com.framework.template.global.error.ErrorCode;
import com.framework.template.global.error.exception.EntityNotFoundException;
import com.framework.template.global.security.context.CustomUser;
import com.framework.template.global.security.dto.AuthenticationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws EntityNotFoundException {
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_EXISTS));

        AuthenticationDto authenticationDto = AuthenticationDto.of(member);

        return new CustomUser(authenticationDto, authenticationDto.getAuthorities());
    }
}
