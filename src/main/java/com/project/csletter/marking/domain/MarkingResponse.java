package com.project.csletter.marking.domain;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class MarkingResponse {
    private List<String> body;
    private Boolean[][] result;
    private Boolean isCorrect;
    private Long count;
    private Long totalCount;

}
