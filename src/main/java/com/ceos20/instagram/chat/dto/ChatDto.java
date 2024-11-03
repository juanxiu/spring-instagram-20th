package com.ceos20.instagram.chat.dto;

import com.ceos20.instagram.chat.domain.ChatMessage;
import com.ceos20.instagram.chatRoom.domain.Chatroom;
import com.ceos20.instagram.user.domain.User;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import java.util.Date;

@Data
@Builder
@Getter
public class ChatDto {

    private String message;
    private User sender;
    private Date sentAt;

    public ChatMessage toEntity(Chatroom chatroom) {
        return ChatMessage.builder()
                .sender(this.sender)
                .message(this.message)
                .sentAt(new Date()) // 메시지 전송 시간
                .room(chatroom)
                .build();
    }


}
