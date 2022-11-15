package com.project.csletter.marking.domain;

import lombok.*;

@Data
@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class MarkingResponse {
    private String body;
    private Boolean[] result;
    private Boolean isCorrect;
    private Long count;
    private Long totalCount;

}
