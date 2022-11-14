package com.project.csletter.marking.repository;

import com.project.csletter.marking.domain.Marking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MarkingRepository extends JpaRepository<Marking, Long> {

    Optional<Marking> findByMessageId(long messageId);

    @Query(value = "SELECT * FROM marking", nativeQuery = true)
    List<Marking> getList();
}
