package com.ceos20.instagram.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 사용자에게 받는 정보와 실제 저장되어야 할 정보가 다르기 때문에 DTO가 필요

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    private String caption;
    private String imageUrl;
    private LocalDateTime createdAt;
    

}
