package com.project.csletter.member.domain;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@Builder
public class MemberProfile {

    private Long userCode;

    private Long kakaoId;

    private String kakaoProfileImg;

    private String kakaoNickname;

    private String kakaoEmail;

    private String userRole;

    private Boolean isMe;

    private String memberToken;

    private Long countMessage;

    private Long correctMessage;

    private Long solving;

    private Long notRead;

}
