package com.ceos20.instagram.Dto;

import com.ceos20.instagram.Domain.ChatMessage;
import com.ceos20.instagram.Domain.Chatroom;
import com.ceos20.instagram.Domain.User;
import lombok.Builder;

import java.util.Date;

@Builder
public record ChatDto() {

    Long messageId;
    User sender;
    Date sentAt;

    public static ChatMessage of(Chatroom chatroom) {
        return ChatMessage.builder()
                .messageId(chatroom.getRoomId())
                .sender(chatroom.getSender())
                .sentAt(chatroom.getCreatedAt())
                .build();
    }


    public String getMessage() {
    }
}
