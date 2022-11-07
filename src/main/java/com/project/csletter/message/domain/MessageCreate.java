package com.project.csletter.message.domain;

import lombok.*;

@Data
@Getter
@Setter
@ToString
public class MessageCreate {

    private String body;

    private String nickname;

    @Builder
    public MessageCreate(String body, String nickname) {
        this.body = body;
        this.nickname = nickname;
    }

}
