package com.framework.template.domain.member.service;

import com.framework.template.api.member.dto.MemberJoinDto;
import com.framework.template.domain.member.entity.Member;
import com.framework.template.domain.member.repository.MemberRepository;
import com.framework.template.global.dto.ResponseDto;
import com.framework.template.global.error.ErrorCode;
import com.framework.template.global.error.exception.BusinessException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public ResponseDto<MemberJoinDto.Response> join(MemberJoinDto.Request memberJoinRequestDto) {
        memberRepository.findByLoginId(memberJoinRequestDto.getLoginId()).ifPresent(m -> { throw new BusinessException(ErrorCode.MEMBER_EXIST); });

        Member member = memberRepository.save(memberJoinRequestDto.toEntity(passwordEncoder));
        return ResponseDto.success(new MemberJoinDto.Response(member));
    }
}
