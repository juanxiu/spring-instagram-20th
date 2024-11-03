package com.ceos20.instagram.user.dto;

import com.ceos20.instagram.user.domain.User;

public record UserSaveReqDto(String username, String email) {
    public User toEntity(){
        return User. builder()
                .username(username)
                .email(email)
                .build();
    }
}
