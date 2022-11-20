package com.project.csletter.marking.domain;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class MarkingLastResponse {

    private List<String> body;
    private Boolean[][] result;
    private Long count;
    private Long totalCount;
    private Boolean isCorrect;
}
