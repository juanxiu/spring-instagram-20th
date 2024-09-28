package com.ceos20.instagram.Dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class PostChatRequestDto{ // 댓글 입력 시 받는 요청에 대한 dto. 포스트아이디와 내용을 전달받음.
    private Long postId;
    private String caption;
    
    @Builder
    public PostChatRequestDto(Long postId, String caption){
        this.postId=postId;
        this.caption = caption;
    }
}

