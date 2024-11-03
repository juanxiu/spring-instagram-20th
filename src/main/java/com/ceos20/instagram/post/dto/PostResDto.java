package com.ceos20.instagram.post.dto;

import com.ceos20.instagram.comment.domain.Comment;
import com.ceos20.instagram.post.domain.Post;
import com.ceos20.instagram.user.domain.User;
import lombok.Builder;

import java.util.List;

@Builder
public record PostResDto(User user, String caption, List<Comment> comments) {

    public static PostResDto of(final Post post, List<Comment> comments) {
        return new PostResDto(post.getUser(), post.getCaption(), comments);
    }
}


