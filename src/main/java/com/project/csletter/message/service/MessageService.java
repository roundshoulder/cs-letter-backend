package com.project.csletter.message.service;

import com.project.csletter.global.utils.SecurityUtil;
import com.project.csletter.marking.domain.Marking;
import com.project.csletter.marking.repository.MarkingRepository;
import com.project.csletter.marking.service.MarkingService;
import com.project.csletter.member.domain.Member;
import com.project.csletter.member.exception.MemberException;
import com.project.csletter.member.exception.MemberExceptionType;
import com.project.csletter.member.repository.MemberRepository;
import com.project.csletter.message.domain.Message;
import com.project.csletter.message.domain.MessageCreate;
import com.project.csletter.message.domain.MessageListResponse;
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
    private final MarkingRepository markingRepository;
    private final MarkingService markingService;

    public void write(MessageCreate messageCreate) {
        Message message = Message.builder()
                .body(messageCreate.getBody())
                .nickname(messageCreate.getNickname())
                .toMemberToken(messageCreate.getToMemberToken())
                .color(messageCreate.getColor())
                .build();

        messageRepository.save(message);
    }

    public String initialList(String body) {
        char[] result = body.toCharArray();

        char[] chs = {
                'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ',
                'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ',
                'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ',
                'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
        };

        for(int i = 0; i < result.length; i++) {
            if(result[i] >= '\uAC00' && result[i] <= '\uD7A3') {
                int tmp = result[i] - 0xAC00;
                int c = ((tmp - (tmp%28))/28)/21;
                result[i] = chs[c];
            }
        }

        return String.valueOf(result);
    }

    public List<MessageListResponse> getMessageFeed(Long cursor, Pageable pageable, String memberToken) {
        List<MessageListResponse> mainList = getMessageList(cursor, pageable, memberToken)
                .stream()
                .map(MessageListResponse::new)
                .collect(Collectors.toList());

        mainList.forEach( f -> {
            if(markingRepository.findByMessageId(f.getMessageId()).isEmpty()){
                f.setIsCorrect(false);
            }else {
                f.setIsCorrect(f.getBody().equals(markingRepository.findByMessageId(f.getMessageId()).orElseThrow().getBody()));
            }

            f.setBody(initialList(f.getBody()));
            f.setHaveNextMessage(!messageRepository.mainFeedLess(memberToken, f.getMessageId(), pageable).isEmpty());
        });

        return mainList;
    }

    private List<Message> getMessageList(Long id, Pageable page, String memberToken) {
        return id.equals(0L)
                ? messageRepository.mainFeed(memberToken, page)
                : messageRepository.mainFeedLess(memberToken, id, page);
    }


    public MessageResponse getMessage(Long messageId) {

        Member member = memberRepository.findByKakaoNickname((SecurityUtil.getLoginUsername()))
                .orElseThrow();

        Message message = messageRepository.findById(messageId).orElseThrow();


        if(message.getToMemberToken().equals(member.getMemberToken())) {
            MessageResponse result = new MessageResponse(message);

            if(markingRepository.findByMessageId(result.getMessageId()).isEmpty()) {
                result.setIsCorrect(false);
                result.getMarkingResult().setBody(null);
                result.getMarkingResult().setCount(0L);
                result.getMarkingResult().setTotalCount(0L);
                result.getMarkingResult().setResult(null);
            } else {
                Boolean[] markingResult = new Boolean[result.getBody().length()];

                Marking marking = markingRepository.findByMessageId(result.getMessageId()).orElseThrow();
                result.setIsCorrect(result.getBody().equals(marking.getBody()));
                result.getMarkingResult().setBody(marking.getBody());
                result.getMarkingResult().setCount(marking.getCount());
                result.getMarkingResult().setTotalCount(marking.getTotalCount());
                result.getMarkingResult().setResult(getMarkingResult(marking.getBody(), result.getBody(), markingResult));
            }
            result.setBody(initialList(result.getBody()));


            return result;
        } else {
            throw new MemberException(MemberExceptionType.TOKEN_INVALID);
        }
    }

    public Boolean[] getMarkingResult(String markingBody, String messageBody, Boolean[] result) {
        for(int i = 0; i < messageBody.length(); i++) {
            if(messageBody.charAt(i) >= '\uAC00' && messageBody.charAt(i) <= '\uD7A3') {
                result[i] = markingBody.charAt(i) == messageBody.charAt(i);
            }
        }

        return result;
    }
}
