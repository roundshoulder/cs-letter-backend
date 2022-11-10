package com.project.csletter.marking.repository;

import com.project.csletter.marking.domain.Marking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MarkingRepository extends JpaRepository<Marking, Long> {

    Optional<Marking> findByMessageId(long messageId);
}
