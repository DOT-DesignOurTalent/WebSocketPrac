package com.sang.socketPrac.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sang.socketPrac.model.ChatMessage;
import com.sang.socketPrac.model.ChatRoom;
import com.sang.socketPrac.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

//log 라이브러리
@Slf4j
//의존성 주입(초기화되지 않은 final, @NonNull이 붙은 필드 등에 생성자 생성)
@RequiredArgsConstructor
//직접 작성한 class 를 bean으로 등록하기 위한 annotation
@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final ChatService chatService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
        String payload = message.getPayload();
        log.info("payload {}", payload);
        //payload(String)을 ChatMessage object로 변경
        ChatMessage chatMessage= objectMapper.readValue(payload, ChatMessage.class);
        ChatRoom room = chatService.findRoomById(chatMessage.getRoomId());
        room.handleActions(session,chatMessage, chatService);
    }
}
