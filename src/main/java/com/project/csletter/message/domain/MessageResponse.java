package com.project.csletter.message.domain;

import com.project.csletter.marking.domain.MarkingLastResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class MessageResponse {
    private Long messageId;
    private List<String> body;
    private String nickname;
    private String toMemberToken;
    private Long color;
    private LocalDateTime time;
    private MarkingLastResponse markingResult;

    @Builder
    public MessageResponse(Message message) {
        this.messageId = message.getId();
        this.body = message.getBody();
        this.nickname = message.getNickname();
        this.toMemberToken = message.getToMemberToken();
        this.color = message.getColor();
        this.time = message.getCreateDate();
        this.markingResult = MarkingLastResponse.builder().build();
    }

}
