package com.ceos20.instagram.auth.controller;

import com.ceos20.instagram.auth.domain.CustomUserDetails;
import com.ceos20.instagram.auth.service.CustomUserDetailService;
import com.ceos20.instagram.jwt.LoginDto;
import com.ceos20.instagram.jwt.TokenProvider;
import com.ceos20.instagram.user.domain.UserRole;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/users")
@RequiredArgsConstructor
public class AuthController {

    private final CustomUserDetailService customUserDetailService;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;


    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<String> userLogin(@RequestBody LoginDto loginDto) {
        // 1. UsernamePasswordAuthenticationToken을 사용하여 인증 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.username(), loginDto.password());

        // 2. AuthenticationManager를 이용하여 인증 수행
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // 3. 인증 성공 후, role 추출 (첫 번째 권한을 role로 사용)
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        // String 값을 UserRole로 변환
        UserRole userRole = UserRole.fromString(role);

        // 3. 인증 성공 후, 토큰 생성
        String token = tokenProvider.createAccessToken(authentication.getName(), authentication, userRole);

        // 4. 토큰을 ResponseEntity로 반환
        return ResponseEntity.ok(token);
    }

    // 인증된 사용자 정보를 가져오기
    // loadUserByUsername()에서 반환된 CustomUserDetails 객체는 Authentication 객체에 포함되어 Spring Security의 인증 정보로 사용
    @GetMapping("/current-user")
    public ResponseEntity<String> getCurrentUser(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String username = customUserDetails.getUsername();  // 로그인한 사용자명
        String authorities = customUserDetails.getAuthorities().toString();  // 권한 정보

        return ResponseEntity.ok("Username: " + username + ", Authorities: " + authorities);
    }

}