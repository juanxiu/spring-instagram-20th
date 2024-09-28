package com.ceos20.instagram.Domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
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
    @JoinColumn(name = "sender_id") //
    private User sender;// 하나의 유저는 여러 개의 메시지를 보낼 수 있다.

    @ManyToOne
    @JoinColumn(name = "room_id") // 하나의 채팅방에서 여러 개의 메시지를 보낼 수 있다.
    private Chatroom roomId;



}
