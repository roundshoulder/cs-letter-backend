package com.project.csletter.member.controller;

import com.project.csletter.global.utils.SecurityUtil;
import com.project.csletter.jwt.JwtProperties;
import com.project.csletter.jwt.TokenRequestDto;
import com.project.csletter.jwt.TokenResponseDto;
import com.project.csletter.member.domain.Member;
import com.project.csletter.member.domain.MemberProfile;
import com.project.csletter.member.domain.MemberResponse;
import com.project.csletter.member.domain.OAuthToken;
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

        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtProperties.HEADER_STRING, jwtToken);

        Member member = memberRepository.findByKakaoNickname(SecurityUtil.getLoginUsername()).orElseThrow();

        return ResponseEntity.ok().headers(headers).body("success" + member.getUserCode());
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

    @GetMapping("/member/{memberId}")
    public ResponseEntity getMember(@PathVariable Long memberId) {
        MemberProfile memberProfile = memberService.getMemberInfo(memberId);
        return new ResponseEntity(memberProfile, HttpStatus.OK);
    }

    @PostMapping("/reIssue")
    public TokenResponseDto reIssue(@RequestBody TokenRequestDto tokenRequestDto){
        TokenResponseDto tokenResponseDto = memberService.reIssue(tokenRequestDto);
        return tokenResponseDto;
    }


}
