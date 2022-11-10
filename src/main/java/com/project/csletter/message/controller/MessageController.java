package com.project.csletter.message.controller;

import com.project.csletter.message.domain.MessageCreate;
import com.project.csletter.message.domain.MessageListResponse;
import com.project.csletter.message.domain.MessageResponse;
import com.project.csletter.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private static final int PAGE_DEFAULT_SIZE = 6;
    private final MessageService messageService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/message")
    public void message(@RequestBody MessageCreate messageCreate) {
        messageService.write(messageCreate);
    }

    @GetMapping("/messages/{memberToken}")
    public List<MessageListResponse> getMessageList(@RequestParam("cursor") Long cursor, @PathVariable String memberToken) {
        return messageService.getMessageFeed(cursor, PageRequest.of(0, PAGE_DEFAULT_SIZE), memberToken);
    }

    @GetMapping("/message/{messageId}")
    public MessageResponse getMessage(@PathVariable Long messageId) {
        return messageService.getMessage(messageId);
    }
}
