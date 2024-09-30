package com.ceos20.instagram.Service;

import com.ceos20.instagram.Domain.User;
import com.ceos20.instagram.Dto.UserDto;
import com.ceos20.instagram.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 회원가입.
    public Long vaildateAndJoinUser(UserDto userDto){
        if (userRepository.existsUserByemail(userDto.getEmail())){
            throw new IllegalArgumentException("이미 존재하는 이메일입니다. "+userDto.getEmail());
        }
        if (userRepository.existsByuserName(userDto.getUsername())){
            throw new IllegalArgumentException("이미 존재하는 사용자명입니다."+userDto.getUsername());
        }

        User user = userDto.toEntity();
        userRepository.save(user);
        return user.getUserId();
    }

}
