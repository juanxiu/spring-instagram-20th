package com.ceos20.instagram.post.controller;


import com.ceos20.instagram.comment.domain.Comment;
import com.ceos20.instagram.post.domain.Post;
import com.ceos20.instagram.post.dto.PostCreateReqDto;
import com.ceos20.instagram.post.dto.PostResDto;
import com.ceos20.instagram.post.service.PostService;
import com.ceos20.instagram.postLike.dto.PostLikeReqDto;
import com.ceos20.instagram.postLike.service.PostLikeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
public class PostControllerTest {

    @InjectMocks
    private PostController postController;

    @Mock
    private PostService postService;

    @Mock
    private PostLikeService postLikeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreatePost() {

        String username = "testUser";
        String caption = "샘플 캡션";
        String imageUrl = "1L";

        PostCreateReqDto request = PostCreateReqDto.builder()
                .caption(caption)
                .imageUrl(imageUrl)
                .build();


        ResponseEntity<Void> response = postController.createPost(request, username);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        verify(postService, times(1)).createPost(request, username);
    }

    @Test
    public void testGetPost() {
        Long postId = 1L;
        Post post = new Post();
        List<Comment> comments = Collections.emptyList();

        PostResDto postResDto = PostResDto.of(post, comments);
        when(postService.getPost(postId)).thenReturn(postResDto);

        ResponseEntity<PostResDto> response = postController.getPost(postId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(postResDto);
    }

    @Test
    public void testGetUserPosts() {
        Long userId = 1L;
        Post post = new Post();
        List<Comment> comments = Collections.emptyList();
        PostResDto postResDto = PostResDto.of(post, comments);

        when(postService.getPostByUser(any())).thenReturn(Collections.singletonList(postResDto));

        ResponseEntity<List<PostResDto>> response = postController.getUserPosts(userId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactly(postResDto);
    }

    @Test
    public void testDeletePost() {
        Long postId = 1L;

        ResponseEntity<Void> response = postController.deletePost(postId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(postService, times(1)).deletePost(postId);
    }

    @Test
    public void testLikePost() {
        Long userId = 1L;
        PostLikeReqDto request = PostLikeReqDto.builder()
                .userId(userId)
                .build();

        ResponseEntity<Void> response = postController.likePost(userId, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(postLikeService, times(1)).like(userId, request);
    }

    @Test
    public void testCancelLike() {
        Long userId = 1L;
        PostLikeReqDto request = PostLikeReqDto.builder()
                .userId(userId)
                .build();

        ResponseEntity<Void> response = postController.cancelLike(userId, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(postLikeService, times(1)).cancelLike(userId, request);
    }
}

