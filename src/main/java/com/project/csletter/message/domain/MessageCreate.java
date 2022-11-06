package com.project.csletter.message.domain;

import lombok.*;

@Data
@Getter
@Setter
@ToString
public class MessageCreate {

    private String body;

    @Builder
    public MessageCreate(String body) {
        this.body = body;
    }

}
