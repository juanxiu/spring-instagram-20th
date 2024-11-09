package com.ceos20.instagram.user.service;

import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.dto.UserSaveReqDto;
import com.ceos20.instagram.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    //DTO는 일반적으로 빈으로 등록하지 않으며, 컨트롤러에서 파라미터로 받아서 사용한다.
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    // 회원가입.
    public void saveUser(UserSaveReqDto request){
        if (userRepository.existsUserByemail(request.email())){
            throw new IllegalArgumentException("이미 존재하는 이메일입니다. "+request.email());
        }
        if (userRepository.existsByusername(request.username())){
            throw new IllegalArgumentException("이미 존재하는 사용자명입니다."+request.username());
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        // DTO를 Entity로 변환 (비밀번호는 이미 인코딩된 상태)
        User user = request.toEntity(encodedPassword);
        userRepository.save(user);
    }

}
