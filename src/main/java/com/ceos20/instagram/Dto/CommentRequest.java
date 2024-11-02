package com.ceos20.instagram.Dto;

import com.ceos20.instagram.Domain.Comment;
import com.ceos20.instagram.post.domain.Post;
import com.ceos20.instagram.Domain.User;
import lombok.Builder;
import lombok.Data;

// class 가 아니라 record 로.
@Data
public class CommentRequest {
    private final String comment;

    @Builder
    public CommentRequest(String comment) {
        this.comment = comment;
    }

    public Comment toEntity(User writer, Post post, Comment parentComment) {
        return Comment.builder()
                .comment(comment) // DTO의 댓글 내용을 엔티티에 설정
                .user(writer)
                .parentComment(parentComment)
                .post(post)
                .build();
    }

}

