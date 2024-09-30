package com.ceos20.instagram.Repository;

import com.ceos20.instagram.Domain.Follow;
import com.ceos20.instagram.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository {
    Boolean existsByFromUserAndToUser(User fromUser, User toUser);
    Optional<Follow> findByFromUserAndToUser(User fromUser, User toUser);

}
