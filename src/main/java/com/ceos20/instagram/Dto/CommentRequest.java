package com.ceos20.instagram.Dto;

import com.ceos20.instagram.Domain.Comment;
import com.ceos20.instagram.Domain.Post;
import com.ceos20.instagram.Domain.User;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
public class CommentRequest {
    private final String comment;
    private final Date createdAt;
    private final User user;
    private final Post post;

    @Builder
    public CommentRequest(String comment, Date createdAt, User user, Post post) {
        this.comment = comment;
        this.createdAt = createdAt;
        this.user = user;
        this.post = post;
    }

    public Comment toEntity() {
        return Comment.builder()
                .createdAt(createdAt)
                .comment(comment)
                .user(user)
                .post(post)
                .build();
    }

}

