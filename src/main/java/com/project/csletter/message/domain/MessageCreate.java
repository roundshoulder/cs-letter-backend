package com.project.csletter.message.domain;

import lombok.*;

@Data
@Getter
@Setter
@ToString
public class MessageCreate {

    private String body;
    private String nickname;
    private String toMemberToken;
    private Long color;


    @Builder
    public MessageCreate(String body, String nickname, String toMemberToken, Long color) {
        this.body = body;
        this.nickname = nickname;
        this.toMemberToken = toMemberToken;
        this.color = color;
    }

}
