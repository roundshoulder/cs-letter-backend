package com.project.csletter.message.domain;

import lombok.*;

@Data
@Getter
@Setter
@ToString
public class MessageCreate {

    private String body;
    private String nickname;
    private Long toUserCode;


    @Builder
    public MessageCreate(String body, String nickname, Long toUserCode) {
        this.body = body;
        this.nickname = nickname;
        this.toUserCode = toUserCode;
    }

}
