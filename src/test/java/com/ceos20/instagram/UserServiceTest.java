package com.ceos20.instagram;

import com.ceos20.instagram.Domain.User;
import com.ceos20.instagram.Dto.UserDto;
import com.ceos20.instagram.Repository.UserRepository;
import com.ceos20.instagram.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService; // 테스트 대상 클래스

    @Mock
    private UserRepository userRepository; // 의존성을 모킹

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 준비
        MockitoAnnotations.openMocks(this); // Mockito 초기화

        userDto = UserDto.builder()
                .username("testUser")
                .email("testUser@egmail.com")
                .password("password1")
                .build();
    }

    @Test
    void validateAndJoinUser_Success() {
        // given - 이메일과 사용자명이 모두 중복되지 않을 때.
        when(userRepository.existsUserByemail(userDto.getEmail())).thenReturn(false);
        when(userRepository.existsByuserName(userDto.getUsername())).thenReturn(false);

        // when
        userService.vaildateAndJoinUser(userDto);

        // then
        verify(userRepository).save(any(User.class));
    }

    @Test
    void joinUser_EmailAlreadyExists() {
        // given - 이미 존재하는 이메일인 경우
        when(userRepository.existsUserByemail(userDto.getEmail())).thenReturn(true);

        // when - 이메일 중복으로 인해 예외 발생 검증
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.vaildateAndJoinUser(userDto);
        });

        // then - 예외 메시지 검증
        assertEquals("이미 존재하는 이메일입니다. " + userDto.getEmail(), exception.getMessage());
        verify(userRepository, never()).save(any(User.class)); // 이메일 중복 시 save 메서드는 호출되지 않음
    }

    @Test
    void joinUser_UsernameAlreadyExists() {
        // given - 이미 존재하는 사용자명인 경우
        when(userRepository.existsUserByemail(userDto.getEmail())).thenReturn(false);
        when(userRepository.existsByuserName(userDto.getUsername())).thenReturn(true);

        // when - 사용자명 중복으로 인해 예외 발생 검증
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.vaildateAndJoinUser(userDto);
        });

        // then - 예외 메시지 검증
        assertEquals("이미 존재하는 사용자명입니다." + userDto.getUsername(), exception.getMessage());
        verify(userRepository, never()).save(any(User.class)); // 사용자명 중복 시 save 메서드는 호출되지 않음
    }
}
