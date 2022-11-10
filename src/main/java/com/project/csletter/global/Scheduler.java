package com.project.csletter.global;

import com.project.csletter.marking.domain.Marking;
import com.project.csletter.marking.domain.MarkingUpdater;
import com.project.csletter.marking.repository.MarkingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Scheduler {

    private final MarkingRepository markingRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void resetCount() {
        List<Marking> list = markingRepository.getList();
        list.forEach( f -> {
                    MarkingUpdater.MarkingUpdaterBuilder updaterBuilder = f.toUpdater();
                    updaterBuilder.count(0L);
                    f.update(updaterBuilder.build());
                }
        );

    }
}
