package com.project.csletter.message.repository;

import com.project.csletter.message.domain.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query(value = "SELECT * FROM message WHERE to_user_code = :sessionId ORDER BY message_id DESC", nativeQuery = true)
    List<Message> mainFeed(long sessionId, Pageable pageable);

    @Query(value = "SELECT * FROM message WHERE to_user_code = :sessionId AND message_id < :id ORDER BY message_id DESC", nativeQuery = true)
    List<Message> mainFeedLess(long sessionId, Long id, Pageable pageable);

}
