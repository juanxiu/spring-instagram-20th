package com.ceos20.instagram.post.dto;

import lombok.Builder;


@Builder
public record PostCreateReqDto(String caption, String imageUrl) {

}
