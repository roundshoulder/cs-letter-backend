package com.project.csletter.member.service;

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

        Member member = memberRepository.findByKakaoEmail(profile.getKakao_account().getEmail());
        if(member == null) {
            member = Member.builder()
                    .kakaoId(profile.getId())
                    .kakaoProfileImg(profile.getKakao_account().getProfile().getProfile_image_url())
                    .kakaoNickname(profile.getKakao_account().getProfile().getNickname())
                    .kakaoEmail(profile.getKakao_account().getEmail())
                    .userRole("USER").build();

            memberRepository.save(member);
        }

        member.updateRefreshToken(jwtService.createRefreshToken());
        return jwtService.createAccessToken(member);
    }

    public Long getUserCodeByToken(String token) {
        KakaoProfile profile = findProfile(token);
        Member member = memberRepository.findByKakaoEmail(profile.getKakao_account().getEmail());

        return member.getUserCode();
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
                .build();
        return memberResponse;
    }

    public MemberProfile getMemberInfo(Long memberId) {

        Member member = memberRepository.findByUserCode(memberId)
                .orElseThrow();

        MemberProfile memberProfile = MemberProfile.builder()
                .userCode(member.getUserCode())
                .isMe(member.getKakaoNickname() == SecurityUtil.getLoginUsername())
                .kakaoProfileImg(member.getKakaoProfileImg())
                .kakaoNickname(member.getKakaoNickname())
                .kakaoEmail(member.getKakaoEmail())
                .userRole(member.getUserRole())
                .build();
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
}
