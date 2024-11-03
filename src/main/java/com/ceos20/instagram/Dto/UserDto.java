package com.ceos20.instagram.Dto;

import com.ceos20.instagram.Domain.User;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Builder
public class UserDto {
    private String username;
    private String email;

    /*
    유효성 검사 어노테이션
    @NotBlank
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
     */
    private String password;

    public User toEntity() {
        return User.builder()
                .username(username)
                .email(email)
                .password(password)
                .build();

    }
}
