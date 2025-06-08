package com.framework.template.global.security.jwt.service;

import com.framework.template.domain.member.entity.Member;
import com.framework.template.domain.member.repository.MemberRepository;
import com.framework.template.global.error.ErrorCode;
import com.framework.template.global.error.exception.EntityNotFoundException;
import com.framework.template.global.security.jwt.dto.JwtTokenDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final MemberRepository memberRepository;

    public void updateRefreshToken(String loginId, JwtTokenDto jwtTokenDto) {
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_EXISTS));
        member.updateRefreshToken(jwtTokenDto);
    }
}
