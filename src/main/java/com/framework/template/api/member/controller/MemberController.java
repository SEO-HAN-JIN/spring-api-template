package com.framework.template.api.member.controller;

import com.framework.template.api.member.dto.MemberJoinDto;
import com.framework.template.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity join(@RequestBody @Valid MemberJoinDto.Request memberJoinRequestDto) {
        return ResponseEntity.ok(memberService.join(memberJoinRequestDto));
    }
}
