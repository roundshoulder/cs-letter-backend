package com.project.csletter.marking.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class MarkingUpdate {

    private List<String> body;
    private Long count;
    private Long totalCount;

    public MarkingUpdate(List<String> body, Long count, Long totalCount) {
        this.body = body;
        this.count = count;
        this.totalCount = totalCount;
    }
}
