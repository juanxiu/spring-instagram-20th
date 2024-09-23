package com.ceos20.instagram.Domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "ChatMessage")
@Getter
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id") //
    private User userId;// 하나의 유저는 여러 개의 메시지를 보낼 수 있다.

    @ManyToOne
    @JoinColumn(name = "room_id") // 하나의 채팅방에서 여러 개의 메시지를 보낼 수 있다.
    private Chatroom roomId;

    public ChatMessage(){

    }
    public ChatMessage(String content,User userId, Chatroom roomId){
        this.content = content;
        this.userId = userId;
        this.roomId = roomId;
        this.createdAt = LocalDateTime.now(); // 생성 시점에 자동으로 객체 생성 설정.

    }


}
