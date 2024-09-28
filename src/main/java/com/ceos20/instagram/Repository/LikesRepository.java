package com.ceos20.instagram.Repository;

import com.ceos20.instagram.Domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    Likes findByPostIdAndUserId(Long postId, Long userId);
}
