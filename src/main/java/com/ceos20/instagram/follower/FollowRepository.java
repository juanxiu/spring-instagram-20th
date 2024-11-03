package com.ceos20.instagram.follower;

import com.ceos20.instagram.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Boolean existsByFromUserAndToUser(User fromUser, User toUser);
    Optional<Follow> findByFromUserAndToUser(User fromUser, User toUser);

}
