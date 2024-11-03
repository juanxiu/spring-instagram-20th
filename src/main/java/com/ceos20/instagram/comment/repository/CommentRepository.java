package com.ceos20.instagram.comment.repository;

import com.ceos20.instagram.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByPostIdAndUserId(Long postId, Long userId);
}

