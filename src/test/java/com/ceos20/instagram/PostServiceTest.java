package com.ceos20.instagram;

import com.ceos20.instagram.Domain.Post;
import com.ceos20.instagram.Domain.User;
import com.ceos20.instagram.Dto.PostDto;
import com.ceos20.instagram.Repository.PostRepository;
import com.ceos20.instagram.Service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PostServiceTest {

    @InjectMocks
    private PostService postService;

    // @Mock 어노테이션을 통해 Mock 클래스로 생성해야하는 가짜 객체임을 지정
    @Mock
    private PostRepository postRepository;

    private Post post;
    private PostDto postDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        User user = new User(); // User 객체를 생성해야 할 수 있습니다.
        post = Post.builder()
                .caption("Original Caption")
                .imageUrl("http://example.com/image.jpg")
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();
        postDto = PostDto.builder()
                .caption("Updated Caption")
                .imageUrl("http://example.com/new_image.jpg")
                .build();
    }

    @Test
    void createPost() {
        // Arrange
        when(postRepository.save(any(Post.class))).thenReturn(post);

        // Act
        postService.createPost(postDto);

        // Assert
        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);
        verify(postRepository).save(postCaptor.capture());
        assertEquals("Updated Caption", postCaptor.getValue().getCaption());
        assertEquals("http://example.com/new_image.jpg", postCaptor.getValue().getImageUrl());
    }

    @Test
    void getPostByUser() {
        // Arrange
        User user = new User();
        when(postRepository.findAllByUser(user)).thenReturn(List.of(post));

        // Act
        List<Post> result = postService.getPostByUser(user);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Original Caption", result.get(0).getCaption());
    }

    @Test
    void modifyPost() {
        // Arrange
        when(postRepository.findPostById(1L)).thenReturn(Optional.of(post));

        // Act
        postService.modifyPost(1L, postDto);

        // Assert
        assertEquals("Updated Caption", post.getCaption());
        assertEquals("http://example.com/new_image.jpg", post.getImageUrl());
        verify(postRepository).save(post);
    }

    @Test
    void deletePost() {
        // Arrange
        when(postRepository.findPostById(1L)).thenReturn(Optional.of(post));

        // Act
        postService.deletePost(1L);

        // Assert
        verify(postRepository).delete(post);
    }

    @Test
    void deletePost_NotFound() {
        // Arrange
        when(postRepository.findPostById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> postService.deletePost(1L));
    }
}

