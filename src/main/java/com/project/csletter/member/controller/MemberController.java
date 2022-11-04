package com.project.csletter.member.controller;

import com.project.csletter.jwt.JwtProperties;
import com.project.csletter.member.domain.Member;
import com.project.csletter.member.domain.OAuthToken;
import com.project.csletter.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class MemberController {

    private MemberService memberService;

    @GetMapping("/token")
    public ResponseEntity getLogin(@RequestParam("code") String code) {

        OAuthToken oAuthToken = memberService.getAccessToken(code);

        String jwtToken = memberService.saveMemberAndGetToken(oAuthToken.getAccess_token());

        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);

        return ResponseEntity.ok().headers(headers).body("success");
    }

    @GetMapping("/me")
    public ResponseEntity<Object> getCurrentUser(HttpServletRequest request) {

        Member member = memberService.getMember(request);

        return ResponseEntity.ok().body(member);
    }
}
