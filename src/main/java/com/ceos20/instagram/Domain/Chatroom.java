package com.ceos20.instagram.Domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "Chatroom")
@Getter
public class Chatroom { // 채팅 서버는 웹소켓 프로토콜로?
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "user_id") //
    private User userId;// 채팅방을 만든 유저

    @OneToOne
    @JoinColumn(name = "member_id") // 채팅방에 참여한 유저.
    private User memberId;

    public Chatroom(){

    }
    public Chatroom(User userId, User memberId){
        this.userId = userId;
        this.memberId = memberId;
        this.createdAt = LocalDateTime.now(); // 생성 시점에 자동으로 객체 생성 설정.

    }


}

