package com.ceos20.instagram.Domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Date;
/*
고민사항: 대댓글을 어떻게 구현????? 아 그리고 댓글 좋아요는?
대댓글 = 답글: reply? 로 할까? comment: reply = 1: n
 */
@Entity
@Table(name = "Comment")
@Getter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id") //
    private User userId;// 하나의 유저는 여러 개의 댓글을 달 수 있음.

    @ManyToOne
    @JoinColumn(name = "post_id") // 하나의 게시물은 여러 개의 좋아요 받을 수 있음.
    private Post postId;

    public Comment(){

    }
    public Comment(String content,User userId, Post postId){
        this.content = content;
        this.userId = userId;
        this.postId = postId;
        this.createdAt = new Date(); // 생성 시점에 자동으로 객체 생성 설정.

    }


}
