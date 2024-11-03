package com.ceos20.instagram.user.service;

import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.dto.UserSaveReqDto;
import com.ceos20.instagram.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class UserService {

    @Autowired
    private UserRepository userRepository;
    private UserSaveReqDto userSaveReqDto;

    // 회원가입.
    public void saveUser(UserSaveReqDto request){
        if (userRepository.existsUserByemail(request.email())){
            throw new IllegalArgumentException("이미 존재하는 이메일입니다. "+request.email());
        }
        if (userRepository.existsByusername(request.username())){
            throw new IllegalArgumentException("이미 존재하는 사용자명입니다."+request.username());
        }

        User user = request.toEntity();
        userRepository.save(user);
    }

}
