package com.ceos20.instagram;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ceos20.instagram.Domain.Follow;
import com.ceos20.instagram.Domain.User;
import com.ceos20.instagram.Repository.FollowRepository;
import com.ceos20.instagram.Repository.UserRepository;
import com.ceos20.instagram.Service.FollowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

class FollowServiceTest {

    @InjectMocks
    private FollowService followService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FollowRepository followRepository;

    private User fromUser;
    private User toUser;
    private Long fromUserId;
    private Long toUserId;
    private Follow follow;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // 테스트용 사용자 설정
        fromUserId = 1L;
        toUserId = 2L;

        fromUser = User.builder().userId(fromUserId).username("fromUser").build();
        toUser = User.builder().userId(toUserId).username("toUser").build();
        follow = Follow.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .build();
    }

    @Test
    void createFollow_Success() {
        // given
        when(userRepository.findById(fromUserId)).thenReturn(Optional.of(fromUser));
        when(userRepository.findById(toUserId)).thenReturn(Optional.of(toUser));
        when(followRepository.existsByFromUserAndToUser(fromUser, toUser)).thenReturn(false);

        // when
        followService.createFollow(fromUserId, toUserId);

        // then
        verify(followRepository).save(any(Follow.class));
    }

    @Test
    void createFollow_UserNotFound() {
        // given
        when(userRepository.findById(fromUserId)).thenReturn(Optional.empty());

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> followService.createFollow(fromUserId, toUserId));

        assertEquals("존재하지 않는 사용자입니다. :" + fromUserId, exception.getMessage());
    }

    @Test
    void createFollow_ToUserNotFound() {
        // given
        when(userRepository.findById(fromUserId)).thenReturn(Optional.of(fromUser));
        when(userRepository.findById(toUserId)).thenReturn(Optional.empty());

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> followService.createFollow(fromUserId, toUserId));

        assertEquals("존재하지 않는 사용자입니다. :" + toUserId, exception.getMessage());
    }

    @Test
    void createFollow_AlreadyFollowing() {
        // given
        when(userRepository.findById(fromUserId)).thenReturn(Optional.of(fromUser));
        when(userRepository.findById(toUserId)).thenReturn(Optional.of(toUser));
        when(followRepository.existsByFromUserAndToUser(fromUser, toUser)).thenReturn(true);

        // when & then
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> followService.createFollow(fromUserId, toUserId));

        assertEquals("이미 팔로우 하고 있는 사용자입니다.", exception.getMessage());
    }
    @Test
    void unfollow_Success() {
        // given
        when(userRepository.findById(fromUserId)).thenReturn(Optional.of(fromUser));
        when(userRepository.findById(toUserId)).thenReturn(Optional.of(toUser));
        when(followRepository.findByFromUserAndToUser(fromUser, toUser)).thenReturn(Optional.of(follow));

        // when
        assertDoesNotThrow(() -> followService.unfollow(fromUserId, toUserId));

        // then
        verify(followRepository).delete(follow);
    }
    @Test
    void unfollow_UserNotFound_FromUser() {
        // given
        when(userRepository.findById(fromUserId)).thenReturn(Optional.empty());

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            followService.unfollow(fromUserId, toUserId);
        });
        assertEquals("존재하지 않는 사용자입니다: " + fromUserId, exception.getMessage());
    }

    @Test
    void unfollow_UserNotFound_ToUser() {
        // given
        when(userRepository.findById(fromUserId)).thenReturn(Optional.of(fromUser));
        when(userRepository.findById(toUserId)).thenReturn(Optional.empty());

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            followService.unfollow(fromUserId, toUserId);
        });
        assertEquals("존재하지 않는 사용자입니다: " + toUserId, exception.getMessage());
    }
    @Test
    void unfollow_FollowRelationshipNotFound() {
        // given
        when(userRepository.findById(fromUserId)).thenReturn(Optional.of(fromUser));
        when(userRepository.findById(toUserId)).thenReturn(Optional.of(toUser));
        when(followRepository.findByFromUserAndToUser(fromUser, toUser)).thenReturn(Optional.empty());

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            followService.unfollow(fromUserId, toUserId);
        });
        assertEquals("팔로우 관계를 찾을 수 없습니다.", exception.getMessage());
    }
}
