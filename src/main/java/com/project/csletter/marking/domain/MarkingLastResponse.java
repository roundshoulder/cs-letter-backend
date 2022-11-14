package com.project.csletter.marking.domain;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class MarkingLastResponse {

    private String body;
    private Long count;
    private Long totalCount;
}
