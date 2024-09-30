package com.ceos20.instagram.Repository;

import com.ceos20.instagram.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByuserName(String userName);
    Boolean existsUserByemail(String email);
    Boolean existsByuserName(String userName);


}