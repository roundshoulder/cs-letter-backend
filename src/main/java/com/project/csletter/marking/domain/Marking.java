package com.project.csletter.marking.domain;

import com.project.csletter.global.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@AllArgsConstructor
@Builder
public class Marking extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "marking_id")
    private Long id;

    @Column(name = "body")
    @ElementCollection(targetClass = String.class)
    @Builder.Default
    private List<String> body = new ArrayList<>();

    private Long count;

    private Long totalCount;

    private Long messageId;

    public MarkingUpdater.MarkingUpdaterBuilder toUpdater() {
        return MarkingUpdater.builder()
                .body(body)
                .count(count)
                .totalCount(totalCount);

    }

    public void update(MarkingUpdater markingUpdater) {
        body = markingUpdater.getBody();
        count = markingUpdater.getCount();
        totalCount = markingUpdater.getTotalCount();
    }
}
