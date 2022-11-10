package com.project.csletter.member.controller;

import com.project.csletter.jwt.JwtProperties;
import com.project.csletter.jwt.TokenRequestDto;
import com.project.csletter.jwt.TokenResponseDto;
import com.project.csletter.member.domain.*;
import com.project.csletter.member.repository.MemberRepository;
import com.project.csletter.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @GetMapping("/oauth/token")
    public ResponseEntity getLogin(@RequestParam("code") String code) {

        OAuthToken oAuthToken = memberService.getAccessToken(code);

        String jwtToken = memberService.saveMemberAndGetToken(oAuthToken.getAccess_token());
        
        String memberToken = memberService.getMemberTokenByToken(oAuthToken.getAccess_token());

        Member member = memberRepository.findByMemberToken(memberToken).orElseThrow();

        MemberLoginResponse memberLoginResponse = MemberLoginResponse.builder()
                .memberToken(memberToken)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add("AccessToken", jwtToken);
        headers.add("RefreshToken", member.getRefreshToken());

        return ResponseEntity.ok().headers(headers).body(memberLoginResponse);
    }

    @GetMapping("/hi")
    public String hi() {
        return "dohagod";
    }

    @GetMapping("/oauth/me")
    public ResponseEntity getMyInfo(HttpServletResponse response) throws Exception {
        MemberResponse memberResponse = memberService.getMyInfo();
        return new ResponseEntity(memberResponse, HttpStatus.OK);
    }

    @GetMapping("/member/{memberToken}")
    public ResponseEntity getMember(@PathVariable String memberToken) {
        MemberProfile memberProfile = memberService.getMemberInfo(memberToken);
        return new ResponseEntity(memberProfile, HttpStatus.OK);
    }

    @PostMapping("/reIssue")
    public TokenResponseDto reIssue(@RequestBody TokenRequestDto tokenRequestDto){
        TokenResponseDto tokenResponseDto = memberService.reIssue(tokenRequestDto);
        return tokenResponseDto;
    }


}
