package com.project.csletter.message.service;

import com.project.csletter.global.utils.SecurityUtil;
import com.project.csletter.marking.domain.Marking;
import com.project.csletter.marking.repository.MarkingRepository;
import com.project.csletter.member.domain.Member;
import com.project.csletter.member.exception.MemberException;
import com.project.csletter.member.exception.MemberExceptionType;
import com.project.csletter.member.repository.MemberRepository;
import com.project.csletter.message.domain.*;
import com.project.csletter.message.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.lang.Math.min;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;
    private final MarkingRepository markingRepository;

    public void write(MessageCreate messageCreate) {

        List<String> bodyList = new ArrayList<>();

        int startIndex = 0;

        if(messageCreate.getBody().length() - startIndex < 24) {
            bodyList.add(messageCreate.getBody().substring(startIndex));
        }else {
            for(int i = 1; i <= messageCreate.getBody().length()/24+1; i++) {
                String tmpString;
                if(messageCreate.getBody().length() - startIndex < 24) {
                    bodyList.add(messageCreate.getBody().substring(startIndex));
                    break;
                }
                tmpString = messageCreate.getBody().substring(startIndex, startIndex+23);
                int lastIndex = tmpString.length()-1;
                if(tmpString.contains(" ")) {
                    while(tmpString.charAt(lastIndex) != ' ') {
                        lastIndex--;
                    }
                }

                bodyList.add(tmpString.substring(0, lastIndex));
                startIndex = startIndex + lastIndex;
            }
        }



        for(int i = 0; i < bodyList.size(); i++) {
            bodyList.set(i, bodyList.get(i).strip());
        }

        for(int i = 0; i < bodyList.size(); i++) {
            if(bodyList.get(i).isBlank()) {
                bodyList.remove(i);
                i = 0;
            }
        }

        Message message = Message.builder()
                .body(bodyList)
                .nickname(messageCreate.getNickname())
                .toMemberToken(messageCreate.getToMemberToken())
                .color(messageCreate.getColor())
                .isRead(false)
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
                f.setIsCorrect(f.getBody().containsAll(markingRepository.findByMessageId(f.getMessageId()).orElseThrow().getBody()));
            }

            f.setBody(Collections.singletonList(f.getBody().get(0).length() > 20 ? initialList(f.getBody().get(0)).substring(0, 20) : initialList(f.getBody().get(0))));
            f.setHaveNextMessage(!messageRepository.mainFeedLess(memberToken, f.getMessageId(), pageable).isEmpty());

            if(cursor == 0) {
                f.setPrevCursor(null);
            }else {
                List<Message> messages = messageRepository.prevCursor(memberToken, mainList.get(0).getMessageId(), Pageable.ofSize(7));
                if (Objects.equals(messages.get(messages.size() - 1).getId(), messageRepository.mainFeed(memberToken, pageable).get(0).getId())) {
                    f.setPrevCursor(0L);
                } else {
                    f.setPrevCursor(messages.get(messages.size() - 1).getId());
                }
            }
            f.setNextCursor(mainList.get(mainList.size()-1).getMessageId());
        });

        if (mainList.size() > 0) {
            if(!mainList.get(mainList.size() - 1).getHaveNextMessage()) {
                mainList.forEach(f -> f.setNextCursor(null));
            }
        }

        return mainList;
    }

    private List<Message> getMessageList(Long id, Pageable page, String memberToken) {
        return id.equals(0L)
                ? messageRepository.mainFeed(memberToken, page)
                : messageRepository.mainFeedLess(memberToken, id, page);
    }


    @Transactional
    public MessageResponse getMessage(Long messageId) {

        Member member = memberRepository.findByKakaoNickname((SecurityUtil.getLoginUsername()))
                .orElseThrow();

        Message message = messageRepository.findById(messageId).orElseThrow();


        if(message.getToMemberToken().equals(member.getMemberToken())) {
            MessageResponse result = new MessageResponse(message);

            MessageUpdater.MessageUpdaterBuilder messageUpdaterBuilder = message.toUpdater();
            messageUpdaterBuilder.isRead(true);
            message.update(messageUpdaterBuilder.build());

            if(markingRepository.findByMessageId(result.getMessageId()).isEmpty()) {
                result.getMarkingResult().setIsCorrect(false);
                result.getMarkingResult().setBody(null);
                result.getMarkingResult().setCount(0L);
                result.getMarkingResult().setTotalCount(0L);
                result.getMarkingResult().setResult(null);
            } else {
                Boolean[][] markingResult = new Boolean[result.getBody().size()][];

                Marking marking = markingRepository.findByMessageId(result.getMessageId()).orElseThrow();
                result.getMarkingResult().setIsCorrect(result.getBody().containsAll(marking.getBody()));
                result.getMarkingResult().setBody(marking.getBody());
                result.getMarkingResult().setCount(marking.getCount());
                result.getMarkingResult().setTotalCount(marking.getTotalCount());
                result.getMarkingResult().setResult(getMarkingResult(marking.getBody(), result.getBody(), markingResult));
            }

            List<String> initBody = new ArrayList<>();

            for(int i = 0; i < result.getBody().size(); i++) {
                initBody.add(initialList(result.getBody().get(i)));
            }

            result.setBody(initBody);
            return result;
        } else {
            throw new MemberException(MemberExceptionType.TOKEN_INVALID);
        }
    }

    public Boolean[][] getMarkingResult(List<String> markingBody, List<String> messageBody, Boolean[][] result) {

        for (int j = 0; j < result.length; j++) {
            result[j] = new Boolean[messageBody.get(j).length()];
            for (int i = 0; i < min(markingBody.get(j).length(), messageBody.get(j).length()); i++) {
                result[j][i] = markingBody.get(j).charAt(i) == messageBody.get(j).charAt(i);
            }
        }

        return result;
    }
}
