package com.project.csletter.member.domain;

import lombok.*;

@Data
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class MemberLoginResponse {

    private String memberToken;
    private String refreshToken;
}
