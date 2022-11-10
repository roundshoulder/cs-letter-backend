package com.project.csletter.marking.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MarkingUpdater {

    private String body;
    private Long count;
    private Long totalCount;

    @Builder
    public MarkingUpdater(String body, Long count, Long totalCount) {
        this.body = body;
        this.count = count;
        this.totalCount = totalCount;
    }

}
