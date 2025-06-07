package com.framework.template.domain.member.service;

import com.framework.template.api.member.dto.MemberJoinDto;
import com.framework.template.domain.member.entity.Member;
import com.framework.template.domain.member.repository.MemberRepository;
import com.framework.template.global.error.ErrorCode;
import com.framework.template.global.error.exception.BusinessException;
import com.framework.template.global.error.exception.EntityNotFoundException;
import com.framework.template.global.security.jwt.dto.JwtTokenDto;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public MemberJoinDto.Response join(MemberJoinDto.Request memberJoinRequestDto) {
        memberRepository.findByLoginId(memberJoinRequestDto.getLoginId()).ifPresent(m -> { throw new BusinessException(ErrorCode.MEMBER_EXIST); });

        Member member = memberRepository.save(memberJoinRequestDto.toEntity(passwordEncoder));
        return new MemberJoinDto.Response(member);
    }

    public void updateRefreshToken(String loginId, JwtTokenDto jwtTokenDto) {

    }
}
