package com.project.csletter.marking.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MarkingUpdate {

    private String body;
    private Long count;
    private Long totalCount;

    public MarkingUpdate(String body, Long count, Long totalCount) {
        this.body = body;
        this.count = count;
        this.totalCount = totalCount;
    }
}