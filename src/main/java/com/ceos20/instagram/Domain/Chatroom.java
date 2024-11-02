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
    /*
    @TeMPOraL Date 타입에 대해 이 필드가 날짜인지, 시간인지, 둘다 포함하는건지 명시적으로 알려주기 위해 사용하는 어노테이션이라고 하네요!
    근데 LocalDateTime은 JPA가 자동으로 처리해주기 때문에 해당 어노테이션이 필요없다고 합니다!
     */
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "sender_id") // 이 이름 카멜레이스? 자동으로 컬럼 설정되는 거라 안 해도 된다 함.
    private User sender;// 채팅방을 만든 유저

    @ManyToOne
    @JoinColumn(name = "receiver_id") // 채팅방에 참여한 유저.
    private User receiver;


}

