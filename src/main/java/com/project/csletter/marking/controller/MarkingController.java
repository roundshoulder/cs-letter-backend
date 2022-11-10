package com.project.csletter.marking.controller;

import com.project.csletter.marking.domain.MarkingCreate;
import com.project.csletter.marking.domain.MarkingResponse;
import com.project.csletter.marking.service.MarkingService;
import com.project.csletter.message.domain.Message;
import com.project.csletter.message.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MarkingController {

    private final MarkingService markingService;
    private final MessageRepository messageRepository;

    @PostMapping("/marking")
    @ResponseStatus(HttpStatus.CREATED)
    public MarkingResponse marking(@RequestBody MarkingCreate markingCreate) {

        markingService.marking(markingCreate);

        Message message = messageRepository.findById(markingCreate.getMessageId()).orElseThrow();

        Boolean[] result = new Boolean[message.getBody().length()];;

        MarkingResponse markingResponse = markingService.getResult(markingCreate, message, result);

        return markingResponse;
    }
}
