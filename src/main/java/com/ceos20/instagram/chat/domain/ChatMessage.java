package com.ceos20.instagram.chat.domain;

import com.ceos20.instagram.chatRoom.domain.Chatroom;
import com.ceos20.instagram.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Entity
@Table(name = "ChatMessage")
@Getter
@Builder
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date sentAt;

    private String message;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;// 하나의 유저는 여러 개의 메시지를 보낼 수 있다.

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Chatroom room;




}
