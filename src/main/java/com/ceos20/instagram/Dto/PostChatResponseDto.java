package com.ceos20.instagram.Dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class PostChatResponseDto{
    private List<PostDto> postChatDtoList;
    
    @Builder
    public PostChatResponseDto(List<PostDto> postChatDtoList){
        this.postChatDtoList=postChatDtoList;
    }
}
