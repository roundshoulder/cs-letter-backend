package com.project.csletter.message.domain;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MessageResponse {
    private Long messageId;
    private String body;
    private String nickname;
    private String toMemberToken;
    private Long color;
    private LocalDateTime time;
    private Boolean isCorrect;

    @Builder
    public MessageResponse(Message message) {
        this.messageId = message.getId();
        this.body = message.getBody();
        this.nickname = message.getNickname();
        this.toMemberToken = message.getToMemberToken();
        this.color = message.getColor();
        this.time = message.getCreateDate();
    }

}
