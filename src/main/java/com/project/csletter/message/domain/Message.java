package com.project.csletter.message.domain;

import com.project.csletter.global.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
public class Message extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @Column(nullable = false)
    private String body;

    private String nickname;

    private String toMemberToken;

    private Long color;

    @Builder
    public Message(String body, String nickname, String toMemberToken, Long color) {
        this.body = body;
        this.nickname = nickname;
        this.toMemberToken = toMemberToken;
        this.color = color;
    }


}
