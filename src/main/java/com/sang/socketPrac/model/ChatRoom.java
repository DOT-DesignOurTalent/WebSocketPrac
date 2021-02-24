package com.sang.socketPrac.model;

import com.sang.socketPrac.service.ChatService;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Getter
public class ChatRoom {
    private String roomId;
    private String name;
    //DB없이 임시로 Set으로 세션 관리
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoom(String roomId, String name){
        this.roomId=roomId;
        this.name= name;
    }
    public void handleActions(WebSocketSession session, ChatMessage chatMessage, ChatService chatService){
        if (chatMessage.getType().equals(ChatMessage.MessageType.ENTER)) {
            sessions.add(session);
            chatMessage.setMessage(chatMessage.getSender() + "님이 입장했습니다.");
        }
        else if (chatMessage.getType().equals(ChatMessage.MessageType.TALK)) {
            sendMessage(chatMessage, chatService);
        }
        else if (chatMessage.getType().equals(ChatMessage.MessageType.EXIT)) {
            sessions.remove(session);
            chatMessage.setMessage(chatMessage.getSender() + "님이 입장했습니다.");
        }

    }
    public <T> void sendMessage(T message, ChatService chatService){
        sessions.parallelStream().forEach(session -> chatService.sendMessage(session, message));
    }
}
