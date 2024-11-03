package com.ceos20.instagram.post.controller;

import com.ceos20.instagram.post.dto.PostCreateReqDto;
import com.ceos20.instagram.post.dto.PostResDto;
import com.ceos20.instagram.post.service.PostService;
import com.ceos20.instagram.postLike.dto.PostLikeReqDto;
import com.ceos20.instagram.postLike.service.PostLikeService;
import com.ceos20.instagram.user.dto.UserPostsReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private final PostService postService;
    private final PostLikeService postLikeService;

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody final PostCreateReqDto request, final String username) {
        postService.createPost(request, username);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResDto> getPost(@PathVariable final Long postId) {
        final PostResDto response = postService.getPost(postId);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostResDto>> getUserPosts(@PathVariable Long userId) {
        UserPostsReqDto request = new UserPostsReqDto(userId);
        List<PostResDto> userPosts = postService.getPostByUser(request);

        return ResponseEntity.ok(userPosts);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/likes/{postId}")
    public ResponseEntity<Void> likePost(@PathVariable Long postId, @RequestBody PostLikeReqDto request) {
        postLikeService.like(postId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/likes/{postId}")
    public ResponseEntity<Void> cancelLike(@PathVariable Long postId, @RequestBody PostLikeReqDto request) {
        postLikeService.cancelLike(postId, request);
        return ResponseEntity.ok().build();
    }
}
