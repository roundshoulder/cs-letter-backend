package com.project.csletter.message.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageListResponse {
    private Long messageId;
    private String body;
    private String nickname;
    private String toMemberToken;
    private Long color;
    private Boolean haveNextMessage;

    @Builder
    public MessageListResponse(Message message) {
        this.messageId = message.getId();
        this.body = message.getBody();
        this.nickname = message.getNickname();
        this.toMemberToken = message.getToMemberToken();
        this.color = message.getColor();
    }

}
