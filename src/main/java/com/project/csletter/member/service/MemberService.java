package com.project.csletter.member.service;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.csletter.global.utils.SecurityUtil;
import com.project.csletter.jwt.JwtService;
import com.project.csletter.jwt.TokenRequestDto;
import com.project.csletter.jwt.TokenResponseDto;
import com.project.csletter.member.domain.*;
import com.project.csletter.member.exception.MemberException;
import com.project.csletter.member.exception.MemberExceptionType;
import com.project.csletter.member.repository.MemberRepository;
import jdk.jshell.spi.ExecutionControl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletException;
import java.rmi.ServerError;
import java.util.Random;


@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtService jwtService;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    String client_id;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    String client_secret;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    String redirect_uri;

    public OAuthToken getAccessToken(String code) {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", client_id);
        params.add("redirect_uri", redirect_uri);
        params.add("code", code);
        params.add("client_secret", client_secret);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers);

        ResponseEntity<String> accessTokenResponse = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oauthToken = null;
        try {
            oauthToken = objectMapper.readValue(accessTokenResponse.getBody(), OAuthToken.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return oauthToken;
    }

    public KakaoProfile findProfile(String token) {

        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest =
                new HttpEntity<>(headers);

        ResponseEntity<String> kakaoProfileResponse = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper.readValue(kakaoProfileResponse.getBody(), KakaoProfile.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return kakaoProfile;
    }

    public String saveMemberAndGetToken(String token) {
        KakaoProfile profile = findProfile(token);

        Member member = memberRepository.findByKakaoNickname(profile.getProperties().getNickname()).orElseThrow();
        if(member == null) {
            member = Member.builder()
                    .kakaoId(profile.getId())
                    .kakaoProfileImg(profile.getKakao_account().getProfile().getProfile_image_url())
                    .kakaoNickname(profile.getKakao_account().getProfile().getNickname())
                    .kakaoEmail(profile.getKakao_account().getEmail())
                    .memberToken(createMemberToken())
                    .userRole("USER").build();

            memberRepository.save(member);
        }

        member.updateRefreshToken(jwtService.createRefreshToken());
        return jwtService.createAccessToken(member);
    }

    public String getMemberTokenByToken(String token) {
        KakaoProfile profile = findProfile(token);
        Member member = memberRepository.findByKakaoEmail(profile.getKakao_account().getEmail());

        return member.getMemberToken();
    }

    public MemberResponse getMyInfo() {

        Member member = memberRepository.findByKakaoNickname((SecurityUtil.getLoginUsername()))
                .orElseThrow();

        MemberResponse memberResponse = MemberResponse.builder()
                .userCode(member.getUserCode())
                .kakaoId(member.getKakaoId())
                .kakaoProfileImg(member.getKakaoProfileImg())
                .kakaoNickname(member.getKakaoNickname())
                .kakaoEmail(member.getKakaoEmail())
                .userRole(member.getUserRole())
                .refreshToken(member.getRefreshToken())
                .memberToken(member.getMemberToken())
                .build();
        return memberResponse;
    }

    public MemberProfile getMemberInfo(String memberToken) {

        Member member = memberRepository.findByMemberToken(memberToken)
                .orElseThrow();

        MemberProfile memberProfile = MemberProfile.builder()
                .userCode(member.getUserCode())
                .kakaoProfileImg(member.getKakaoProfileImg())
                .kakaoNickname(member.getKakaoNickname())
                .kakaoEmail(member.getKakaoEmail())
                .userRole(member.getUserRole())
                .memberToken(member.getMemberToken())
                .build();

        try {
            String name = SecurityUtil.getLoginUsername();
            memberProfile.setIsMe(name.equals(member.getKakaoNickname()));
        }catch (ClassCastException e) {
            memberProfile.setIsMe(false);
        }

        return memberProfile;
    }

    public TokenResponseDto reIssue(TokenRequestDto requestDto) {
        if (!jwtService.isTokenValid(requestDto.getRefreshToken())) {
            throw new MemberException(MemberExceptionType.TOKEN_INVALID);
        }

        Member member = memberRepository.findByRefreshToken(requestDto.getRefreshToken()).orElseThrow();

        String accessToken = jwtService.createAccessToken(member);
        String refreshToken = jwtService.createRefreshToken();
        member.updateRefreshToken(refreshToken);
        return new TokenResponseDto(accessToken, refreshToken);
    }

    public String createMemberToken() {
        Random random = new Random();
        int length = random.nextInt(5)+5;

        StringBuffer newWord = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int choice = random.nextInt(3);
            switch(choice) {
                case 0:
                    newWord.append((char)((int)random.nextInt(25)+97));
                    break;
                case 1:
                    newWord.append((char)((int)random.nextInt(25)+65));
                    break;
                case 2:
                    newWord.append((char)((int)random.nextInt(10)+48));
                    break;
                default:
                    break;
            }
        }
        return newWord.toString();
    }
}
