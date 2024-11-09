package com.ceos20.instagram.jwt;



import com.ceos20.instagram.user.domain.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import org.springframework.security.core.GrantedAuthority;

import io.jsonwebtoken.io.Decoders;


import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TokenProvider implements InitializingBean {

    private static final long ACCESS_TOKEN_VALIDITY_SECONDS = 60 * 60; // access token은 24시간

    private Key key;

    @Value("${jwt.secretKey}")
    private String secretKey; // 할당되지 않음?

    @Override
    public void afterPropertiesSet() {

        byte[] keyBytes = Decoders.BASE64.decode(secretKey); // base64로 인코딩된 비밀 키를 디코딩할 때
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // Access Token 추출
    public String getAccessToken(HttpServletRequest request) {
        // Authorization 헤더에 Bearer <토큰> 형식으로 전달되는지 확인.

        String bearerToken = request.getHeader("Authorization"); // 요청 헤더
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // Access Token 생성
    public String createAccessToken(String username, Authentication authentication, UserRole role) {
        String authorities = // authentication의 GrantedAuthority 정보를
                authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(","));

        Claims claims = Jwts.claims().setSubject(username); // set
        claims.put("auth", authorities); // 위의 authorities .
        claims.put("role", role.name());  // UserRole을 "role" 클레임에 추가


        Date now = new Date();
        Date validity = new Date(now.getTime() + ACCESS_TOKEN_VALIDITY_SECONDS);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    // 토큰에서 사용자 ID 추출
    public String getTokenUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject(); // get
    }


    // Access Token 유효성 검사
    public boolean validateAccessToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }
        catch (JwtException | IllegalArgumentException e){
            return false;
        }
    }

    public String getRole(final String token) {
        return getPayload(token).get("role", String.class);
    }

    public String getUsername(final String token) {
        return getPayload(token).get("username", String.class);
    }

    private Claims getPayload(final String token) {
        return Jwts.parser() // 권장되지 않음.
                .setSigningKey(secretKey) // 권장되지 않음.
                .parseClaimsJws(token)
                .getBody();
    }



}