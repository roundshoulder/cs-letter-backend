package com.project.csletter.message.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@RequiredArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @Column(nullable = false)
    private String body;

    private String nickname;

    @Builder
    public Message(String body, String nickname) {
        this.body = body;
        this.nickname = nickname;
    }


}
