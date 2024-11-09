package com.ceos20.instagram.jwt.filler;

import com.ceos20.instagram.jwt.LoginDto;
import com.ceos20.instagram.jwt.TokenProvider;
import com.ceos20.instagram.user.domain.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private TokenProvider tokenProvider;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");
        try{
            ObjectMapper objectMapper = new ObjectMapper(); //HTTP 요청이나 응답의 JSON 데이터를 ava 객체로 변환(역직렬화)하거나, Java 객체를 JSON 문자열로 변환(직렬화).
//            String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
//            LoginDto loginDto = objectMapper.readValue(messageBody, LoginDto.class); // messageBody 를 LoginDto 클래스의 객체로 변환

            LoginDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);


            // 토큰 생성(JWT 토큰 검증 후 Security Context에 인증된 상태로 설정하는 토큰)
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDto.username(), loginDto.password());

            // 인증절차. 성공 시 Authentication 객체가 반환
            return getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response,
                                         final FilterChain chain, final Authentication authResult){
        log.info("로그인 성공");
        final String username = authResult.getName();
        log.info("Authenticated user: " + username);


        String role = authResult.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        UserRole userRole = UserRole.fromString(role);

        String accessToken = tokenProvider.createAccessToken(username, authResult, userRole);


        // Access Token을 Response 헤더에 추가 (또는 Response body에 포함)
        response.addHeader("Authorization", "Bearer " + accessToken);

        // SecurityContextHolder에 인증 정보 설정
        SecurityContextHolder.getContext().setAuthentication(authResult);

        authResult.getAuthorities().forEach(authority -> log.info("Authority: " + authority.getAuthority()));



    }

    @Override
    public void setFilterProcessesUrl(String loginUrl) {
        super.setFilterProcessesUrl("api/users/login"); // /login 경로로 설정
    }




}
