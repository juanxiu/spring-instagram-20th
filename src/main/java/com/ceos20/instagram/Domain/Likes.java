package com.ceos20.instagram.Domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Entity
@Table(name = "Likes")
@Getter
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id") //
    private User userLike;// 하나의 유저는 여러 개의 좋아요 누를 수 있음.

    @ManyToOne
    @JoinColumn(name = "post_id") // 하나의 게시물은 여러 개의 좋아요 받을 수 있음.
    private Post postLike;

    @ManyToOne
    @JoinColumn(name = "comment_id") // 하나의 댓글은 여러 개의 좋아요 받을 수 있음.
    private Comment commentLike;

    @Builder
    public Likes(User userLike, Post postLike){
        this.userLike = userLike;
        this.postLike = postLike;
//        this.commentLike = commentLike;
        this.createdAt = new Date(); // 생성 시점에 자동으로 객체 생성 설정.

    }


}
