package com.project.csletter.message.repository;

import com.project.csletter.message.domain.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query(value = "SELECT * FROM message WHERE to_member_token = :sessionId ORDER BY message_id DESC", nativeQuery = true)
    List<Message> mainFeed(String sessionId, Pageable pageable);

    @Query(value = "SELECT * FROM message WHERE to_member_token = :sessionId AND message_id < :id ORDER BY message_id DESC", nativeQuery = true)
    List<Message> mainFeedLess(String sessionId, Long id, Pageable pageable);

    List<Message> findAllByToMemberToken(String memberToken);

    Long countAllByToMemberToken(String memberToken);

    Long countAllByToMemberTokenAndIsReadFalse(String memberToken);

}
