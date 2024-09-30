package com.ceos20.instagram.Service;

import com.ceos20.instagram.Domain.Follow;
import com.ceos20.instagram.Domain.User;
import com.ceos20.instagram.Repository.FollowRepository;
import com.ceos20.instagram.Repository.UserRepository;

public class FollowService {

    private FollowRepository followRepository;
    private UserRepository userRepository;

    public void createFollow(Long fromUserId, Long toUserId) {
        User fromUser = userRepository.findById(fromUserId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. :" + fromUserId));
        User toUser = userRepository.findById(toUserId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. :" + toUserId));

        if (followRepository.existsByFromUserAndToUser(fromUser, toUser)) {
            throw new IllegalStateException("이미 팔로우 하고 있는 사용자입니다.");
        }

        Follow follow = Follow.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .build();

        followRepository.save(follow);
    }

    public void unfollow(Long fromUserId, Long toUserId) {
        User fromUser = userRepository.findById(fromUserId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다: " + fromUserId));
        User toUser = userRepository.findById(toUserId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다: " + toUserId));

        // 팔로우 관계를 찾음
        Follow follow = followRepository.findByFromUserAndToUser(fromUser, toUser)
                .orElseThrow(() -> new IllegalArgumentException("팔로우 관계를 찾을 수 없습니다."));

        followRepository.delete(follow);
    }

}
