package com.project.csletter.marking.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class MarkingUpdater {

    private List<String> body;
    private Long count;
    private Long totalCount;

    @Builder
    public MarkingUpdater(List<String> body, Long count, Long totalCount) {
        this.body = body;
        this.count = count;
        this.totalCount = totalCount;
    }

}
