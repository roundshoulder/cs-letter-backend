package com.project.csletter.message.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MessageUpdater {

    private Boolean isRead;

    @Builder
    public MessageUpdater(Boolean isRead) {
        this.isRead = isRead;
    }
}
