package com.project.csletter.message.domain;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@ToString
public class MessageCreate {

    private List<String> body;
    private String nickname;
    private String toMemberToken;
    private Long color;


    @Builder
    public MessageCreate(List<String> body, String nickname, String toMemberToken, Long color) {
        this.body = body;
        this.nickname = nickname;
        this.toMemberToken = toMemberToken;
        this.color = color;
    }

}
