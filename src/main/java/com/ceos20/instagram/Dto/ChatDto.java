package com.ceos20.instagram.Dto;

import com.ceos20.instagram.Domain.ChatMessage;
import com.ceos20.instagram.Domain.Chatroom;
import com.ceos20.instagram.Domain.User;
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
                .roomId(chatroom)
                .build();
    }


}
