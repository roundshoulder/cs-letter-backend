package com.project.csletter.message.domain;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MessageUpdate {

    private Boolean isRead;

    public MessageUpdate(Boolean isRead) {
        this.isRead = isRead;
    }

}
