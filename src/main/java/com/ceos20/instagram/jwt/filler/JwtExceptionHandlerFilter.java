package com.ceos20.instagram.jwt.filler;

import com.ceos20.instagram.jwt.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtExceptionHandlerFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        String requestURI = request.getRequestURI();
        log.info("Request URI: {}", requestURI); // URI 확인용 로그

        // JWT 토큰이 Authorization 헤더에 있는지 확인하고 로그 출력
        String authorizationHeader = request.getHeader("Authorization");
        log.info("Authorization Header: {}", authorizationHeader);

        // Swagger와 관련된 요청을 필터링하지 않도록 설정
        if (requestURI.contains("/swagger-ui") || requestURI.contains("/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }
        // 회원가입 관련된 요청을 필터링하지 않도록 설정
        if (requestURI.contains("/api/users")) {
            filterChain.doFilter(request, response);
            return;
        }

        // accessToken을 TokenProvider의 getAccessToken 메서드를 사용하여 추출
        String accessToken = tokenProvider.getAccessToken(request);

        // 토큰이 만료되지 않았는지 확인
        if (accessToken != null && tokenProvider.validateAccessToken(accessToken)) {
            setAuthentication(accessToken);
        }
        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String accessToken) {
        // 토큰에서 Authentication 객체를 생성
        Authentication authentication = getAuthentication(accessToken);

        // SecurityContext에 Authentication 설정
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    // 인증 객체 생성 - tokenprovider 에 있던 거 순환 빈 문제 때문에 가져옴.
    public Authentication getAuthentication(String token) {
        UserDetails userDetails =
                userDetailsService.loadUserByUsername(tokenProvider.getTokenUserId(token));
        return new UsernamePasswordAuthenticationToken(
                userDetails, token, userDetails.getAuthorities());
    }
}

