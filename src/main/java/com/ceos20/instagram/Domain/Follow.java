package com.ceos20.instagram.Domain;

import jakarta.persistence.*;
import lombok.Builder;

@Entity
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_user")
    private User fromUser;

    @ManyToOne  // ManyToMany 안 쓰려고 유저 엔티티 안에 onetomany 리스트로 받음.
    @JoinColumn(name = "to_user")
    private User toUser;


    @Builder
    public Follow(User toUser, User fromUser){
        this.toUser = toUser;
        this.fromUser = fromUser;
    }
}
