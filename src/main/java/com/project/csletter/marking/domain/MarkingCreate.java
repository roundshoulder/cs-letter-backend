package com.project.csletter.marking.domain;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class MarkingCreate {

    private List<String> body;
    private Long messageId;
}
