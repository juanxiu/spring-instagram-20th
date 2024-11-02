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


        /*
        여기도 dto toEntity 로 .
        dto -> entity
        entity -> dto 둘 다 하고 싶으면 ? mapper 어노테이션
        근데 엔티티에서 dto 변환은 왜 하냐?
         */


        Follow follow = Follow.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .build();

        followRepository.save(follow);
    }

    /*
    예외 처리 - custom을 templete이 상속받도록? 그리고  exceptionhandler 에서 하나를 관리.
     */
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
