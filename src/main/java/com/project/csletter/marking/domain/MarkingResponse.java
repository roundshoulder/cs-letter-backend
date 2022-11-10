package com.project.csletter.marking.domain;

import lombok.*;

@Data
@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class MarkingResponse {
    private Boolean[] result;
    private Long count;
    private Long totalCount;
}
