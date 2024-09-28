package com.ceos20.instagram.Domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Entity
@Table(name = "Comment")
@Getter
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    private String comment;

    @ManyToOne
    @JoinColumn(name = "user_id") //
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    //댓글 수정
    public void update(String comment) {
        this.comment = comment;
    }


}
