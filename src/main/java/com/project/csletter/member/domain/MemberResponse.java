package com.project.csletter.member.domain;


import lombok.*;

@Data
@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class MemberResponse {

    private Long userCode;

    private Long kakaoId;

    private String kakaoProfileImg;

    private String kakaoNickname;

    private String kakaoEmail;

    private String userRole;

    private String refreshToken;

    private String memberToken;
}
