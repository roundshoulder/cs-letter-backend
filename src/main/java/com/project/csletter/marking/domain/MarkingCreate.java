package com.project.csletter.marking.domain;

import lombok.*;

@Data
@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class MarkingCreate {

    private String body;
    private Long messageId;
}
