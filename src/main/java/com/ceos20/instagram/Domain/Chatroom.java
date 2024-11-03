package com.ceos20.instagram.Domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Chatroom")
@Getter
@Builder
public class Chatroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    private String roomName;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "sender_id") //
    private User sender;// 채팅방을 만든 유저

    @ManyToOne
    @JoinColumn(name = "receiver_id") // 채팅방에 참여한 유저.
    private User receiver;


}

