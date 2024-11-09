package com.ceos20.instagram.user.dto;

import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.domain.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
public record UserSaveReqDto(String username, String email, String password, UserRole userRole) {
    public User toEntity(final String encodedPassword){
        log.info("Encoded password: " + encodedPassword);  // 인코딩된 비밀번호 확인

        return User. builder()
                .username(username)
                .email(email)
                .password(encodedPassword) // 인코딩된 비밀번호 사용
                .role(userRole)
                .build();
    }
}
