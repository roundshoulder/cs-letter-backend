package com.project.csletter.message.service;

import com.project.csletter.global.utils.SecurityUtil;
import com.project.csletter.member.domain.Member;
import com.project.csletter.member.repository.MemberRepository;
import com.project.csletter.message.domain.Message;
import com.project.csletter.message.domain.MessageCreate;
import com.project.csletter.message.domain.MessageResponse;
import com.project.csletter.message.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;

    public void write(MessageCreate messageCreate) {
        Message message = Message.builder()
                .body(messageCreate.getBody())
                .nickname(messageCreate.getNickname())
                .toUserCode(messageCreate.getToUserCode())
                .build();

        messageRepository.save(message);
    }

    public String initialList(String body) {
        char[] result = body.toCharArray();

        for(int i = 0; i < result.length; i++) {
            if(result[i] >= '\uAC00' && result[i] <= '\uD7A3') {
                result[i] = (char)(((result[i] - 0xAC00)/28)/21);
            }
        }

        return String.valueOf(result);
    }

    public List<MessageResponse> getMessageFeed(Long cursor, Pageable pageable) {
        Member member = memberRepository.findByKakaoNickname(SecurityUtil.getLoginUsername()).orElseThrow();
        List<MessageResponse> mainList = getMessageList(cursor, pageable)
                .stream()
                .map(MessageResponse::new)
                .collect(Collectors.toList());

        return mainList;
    }

    private List<Message> getMessageList(Long id, Pageable page) {
        Member member = memberRepository.findByKakaoNickname(SecurityUtil.getLoginUsername()).orElseThrow();
        return id.equals(0L)
                ? messageRepository.mainFeed(member.getUserCode(), page)
                : messageRepository.mainFeedLess(member.getUserCode(), id, page);
    }


    public MessageResponse getMessage(Long messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow();

        MessageResponse result = new MessageResponse(message);

        result.setBody(initialList(result.getBody()));

        return result;
    }
}
