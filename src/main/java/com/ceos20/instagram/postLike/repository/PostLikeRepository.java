package com.ceos20.instagram.postLike.repository;

import com.ceos20.instagram.post.domain.Post;
import com.ceos20.instagram.Domain.User;
import com.ceos20.instagram.postLike.domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByPostAndUser(final Post post, final User user);

    Optional<PostLike> findByPostAndUser(final Post post, final User user);
}
