package com.ceos20.instagram.auth.service;

import com.ceos20.instagram.auth.domain.CustomUserDetails;
import com.ceos20.instagram.jwt.TokenProvider;
import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    // DB 에 저장된 사용자 정보와 일치하는지 여부를 판단
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Not Found: "+ username));

        // UserDetails 에 담아서 return 하면 Authentication Manager 가 검증 함 (인증 처리를 담당)
        return new CustomUserDetails(user);
    }

}
