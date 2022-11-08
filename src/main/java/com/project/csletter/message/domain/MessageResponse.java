package com.project.csletter.message.domain;

import lombok.*;

@Data
@NoArgsConstructor
public class MessageResponse {
    private Long messageId;
    private String body;
    private String nickname;

    @Builder
    public MessageResponse(Message message) {
        this.messageId = message.getId();
        this.body = message.getBody();
        this.nickname = message.getNickname();
    }

}