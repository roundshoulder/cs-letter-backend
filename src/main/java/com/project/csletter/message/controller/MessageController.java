package com.project.csletter.message.controller;

import com.project.csletter.message.domain.MessageCreate;
import com.project.csletter.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/message")
    public void message(@RequestBody MessageCreate messageCreate) {

    }
}
