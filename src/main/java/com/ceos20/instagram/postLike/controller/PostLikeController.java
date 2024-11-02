package com.ceos20.instagram.postLike.controller;

import com.ceos20.instagram.postLike.dto.PostLikeReqDto;
import com.ceos20.instagram.postLike.service.PostLikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}/likes")
public class PostLikeController {

    private final PostLikeService postLikeService;

    public PostLikeController(PostLikeService postLikeService) {
        this.postLikeService = postLikeService;
    }

    @PostMapping
    public ResponseEntity<Void> likePost(@PathVariable Long postId, @RequestBody PostLikeReqDto request) {
        postLikeService.like(postId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> cancelLike(@PathVariable Long postId, @RequestBody PostLikeReqDto request) {
        postLikeService.cancelLike(postId, request);
        return ResponseEntity.ok().build();
    }
}
