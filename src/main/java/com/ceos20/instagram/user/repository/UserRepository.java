package com.ceos20.instagram.user.repository;

import com.ceos20.instagram.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Boolean existsUserByemail(String email);
    Boolean existsByusername(String username);


}