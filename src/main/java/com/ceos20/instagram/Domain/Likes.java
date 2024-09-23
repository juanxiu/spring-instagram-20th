package com.ceos20.instagram.Domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
/*
고민해볼 것 ..
하나의 좋아요는 하나의 post( , comment)에 적용될 수 있다.
하나의 post(, comment)는 여러 개의 좋아요를 받을 수 있다.
ikes가 적용되는 대상을 처리하기 위해 likes 테이블에 target_id field와 target_type field를 추가해서 target_type (0: post, 1: comment) 에 따라 target_id를 참조해 데이터를 얻어올 수 있게 구성했다.

likes는 주체인 user와 N:1 관계를 갖는다. 이 부분은 likes 테이블에 user_id라는 참조키를 주어서 구성했다.
*/


@Entity
@Table(name = "Likes") // Likes 는 sql 예약어와 동일해서 사용 불가.
@Getter
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    private Long targetId;
    private Integer targetType;

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
    public Likes(Long targetId, Integer targetType, User userLike, Post postLike, Comment commentLike){
        this.targetId = targetId;
        this.targetType = targetType;
        this.userLike = userLike;
        this.postLike = postLike;
        this.commentLike = commentLike;
        this.createdAt = new Date(); // 생성 시점에 자동으로 객체 생성 설정.

    }


}
