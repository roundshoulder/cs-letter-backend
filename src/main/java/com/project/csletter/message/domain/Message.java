package com.project.csletter.message.domain;

import com.project.csletter.global.BaseTimeEntity;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
@Builder
public class Message extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @Column(name = "body")
    @ElementCollection(targetClass = String.class)
    @Builder.Default
    private List<String> body = new ArrayList<>();

    private String nickname;

    private String toMemberToken;

    private Long color;


}
