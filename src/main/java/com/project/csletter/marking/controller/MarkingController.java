package com.project.csletter.marking.controller;

import com.project.csletter.marking.domain.Marking;
import com.project.csletter.marking.domain.MarkingCreate;
import com.project.csletter.marking.domain.MarkingLastResponse;
import com.project.csletter.marking.domain.MarkingResponse;
import com.project.csletter.marking.repository.MarkingRepository;
import com.project.csletter.marking.service.MarkingService;
import com.project.csletter.message.domain.Message;
import com.project.csletter.message.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MarkingController {

    private final MarkingService markingService;
    private final MarkingRepository markingRepository;
    private final MessageRepository messageRepository;

    @PostMapping("/marking")
    @ResponseStatus(HttpStatus.CREATED)
    public MarkingResponse marking(@RequestBody MarkingCreate markingCreate) {

        markingService.marking(markingCreate);

        Message message = messageRepository.findById(markingCreate.getMessageId()).orElseThrow();

        Boolean[] result = new Boolean[message.getBody().length()];;

        return markingService.getResult(markingCreate, message, result);
    }

    @GetMapping("/marking/{messageId}")
    public MarkingLastResponse getMarking(@PathVariable Long messageId) {
        Marking marking = markingRepository.findByMessageId(messageId).orElseThrow();
        return MarkingLastResponse.builder()
                .body(marking.getBody()).build();
    }
}
