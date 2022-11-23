package com.project.csletter.message.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class MessageListResponse {
    private Long messageId;
    private List<String> body;
    private String nickname;
    private String toMemberToken;
    private Long color;
    private Boolean haveNextMessage;
    private LocalDateTime time;
    private Boolean isCorrect;
    private Boolean isRead;
    private Long prevCursor;
    private Long nextCursor;

    @Builder
    public MessageListResponse(Message message) {
        this.messageId = message.getId();
        this.body = message.getBody();
        this.nickname = message.getNickname();
        this.toMemberToken = message.getToMemberToken();
        this.color = message.getColor();
        this.time = message.getCreateDate();
        this.isRead = message.getIsRead();
    }

}
