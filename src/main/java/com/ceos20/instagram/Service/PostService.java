package com.ceos20.instagram.Service;

import com.ceos20.instagram.Domain.Post;
import com.ceos20.instagram.Domain.User;
import com.ceos20.instagram.Dto.PostDto;
import com.ceos20.instagram.Repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


/*
1. 게시물 작성 서비스.
2. 게시물 조회 서비스.
3. 게시물 수정 서비스
4. 게시물 삭제 서비스.
5. 피드 조회 서비스.
6. 게시물 좋아요 서비스.
7. 게시물 댓글 서비스.
 */

@Service
@RequiredArgsConstructor
public class PostService {

    @Autowired
    private PostRepository postRepository;

    // 게시물 작성.
    public void createPost(PostDto postDto){

        Post newPost = Post.builder()
                .caption(postDto.getCaption())
                .imageUrl(postDto.getImageUrl())
                .createdAt(LocalDateTime.now())
                .build();

        postRepository.save(newPost);
    }

    //특정 사용자 게시물 조회.
    public List<Post> getPostByUser(User user) {
        return postRepository.findAllByUser(user);
    }

    // 게시물 내용 수정.
    @Transactional
    public void modifyPost(Long postId, PostDto postDto) {
        // ID로 게시물 조회
        Post post = postRepository.findPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다. ID: " + postId));

        post.changeContent(postDto.getCaption(), postDto.getImageUrl());

        postRepository.save(post);
    }

    // 게시물 삭제
    @Transactional
    public void deletePost(Long postId) {
        // ID로 게시물 조회
        Post findPost = postRepository.findPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다. ID: " + postId));

        postRepository.delete(findPost); // 엔티티를 직접 삭제
    }

}
