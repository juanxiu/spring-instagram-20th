package com.ceos20.instagram.Domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Date;

@Entity
@Table(name = "Reply")
@Getter
public class Reply { // 대댓글
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id") //
    private User userId;// 하나의 유저는 여러 개의 대댓글을 달 수 있음.

    @ManyToOne
    @JoinColumn(name = "commnet_id") // 하나의 댓글에 여러 개의 대댓글을 달 수 있음.
    private Comment ParentCommentId; //부모 댓글 아이디

    public Reply(){

    }
    public Reply(String content,User userId, Comment parentCommentId){
        this.content = content;
        this.userId = userId;
        this.ParentCommentId = parentCommentId;
        this.createdAt = new Date(); // 생성 시점에 자동으로 객체 생성 설정.

    }


}
