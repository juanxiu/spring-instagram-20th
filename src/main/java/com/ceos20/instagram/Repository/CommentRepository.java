package com.ceos20.instagram.Repository;

import com.ceos20.instagram.Domain.Comment;
import com.ceos20.instagram.Domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByPostIdAndUserId (Long postId, Long userId);
}
