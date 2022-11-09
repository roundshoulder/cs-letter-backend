package com.project.csletter.message.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @Column(nullable = false)
    private String body;

    private String nickname;

    private Long toUserCode;

    @Builder
    public Message(String body, String nickname, Long toUserCode) {
        this.body = body;
        this.nickname = nickname;
        this.toUserCode = toUserCode;
    }


}
