package com.project.csletter.marking.service;

import com.project.csletter.marking.domain.Marking;
import com.project.csletter.marking.domain.MarkingCreate;
import com.project.csletter.marking.domain.MarkingResponse;
import com.project.csletter.marking.domain.MarkingUpdater;
import com.project.csletter.marking.repository.MarkingRepository;
import com.project.csletter.message.domain.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.Math.min;

@Service
@RequiredArgsConstructor
public class MarkingService {

    private final MarkingRepository markingRepository;

    @Transactional
    public void marking(MarkingCreate markingCreate) {
        if(markingRepository.findByMessageId(markingCreate.getMessageId()).isEmpty()) {
            Marking marking = Marking.builder()
                    .body(markingCreate.getBody())
                    .messageId(markingCreate.getMessageId())
                    .count(1L)
                    .totalCount(1L)
                    .build();

            markingRepository.save(marking);
        } else {
            Marking marking = markingRepository.findByMessageId(markingCreate.getMessageId()).orElseThrow();
            MarkingUpdater.MarkingUpdaterBuilder updaterBuilder = marking.toUpdater();

            if(marking.getCount() >= 5) {
                updaterBuilder.count(marking.getCount() + 1L);
                marking.update(updaterBuilder.build());
                return;
            }

            updaterBuilder.body(markingCreate.getBody());
            updaterBuilder.count(marking.getCount() + 1L);
            updaterBuilder.totalCount(marking.getTotalCount() + 1L);
            marking.update(updaterBuilder.build());
        }

    }

    public MarkingResponse getResult(MarkingCreate markingCreate, Message message, Boolean[][] result) {

        Marking marking = markingRepository.findByMessageId(markingCreate.getMessageId()).orElseThrow();
        for(int j = 0; j < result.length; j++) {
            result[j] = new Boolean[message.getBody().get(j).length()];
            for (int i = 0; i < min(message.getBody().get(j).length(), marking.getBody().get(j).length()); i++) {
                result[j][i] = markingCreate.getBody().get(j).charAt(i) == message.getBody().get(j).charAt(i);
            }
        }

        MarkingResponse response = MarkingResponse.builder()
                .result(result)
                .count(marking.getCount())
                .totalCount(marking.getTotalCount())
                .build();

        response.setBody(markingCreate.getBody());
        response.setIsCorrect(message.getBody().containsAll(markingCreate.getBody()));

        return response;
    }
}
