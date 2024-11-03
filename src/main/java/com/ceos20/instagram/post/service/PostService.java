package com.ceos20.instagram.post.service;

import com.ceos20.instagram.comment.domain.Comment;
import com.ceos20.instagram.exception.ExceptionCode;
import com.ceos20.instagram.exception.NotFoundException;
import com.ceos20.instagram.post.domain.Post;
import com.ceos20.instagram.post.dto.PostCreateReqDto;
import com.ceos20.instagram.post.dto.PostResDto;
import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.post.repository.PostRepository;
import com.ceos20.instagram.user.dto.UserPostsReqDto;
import com.ceos20.instagram.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PostService {

    @Autowired
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createPost(PostCreateReqDto request, String username){

        final User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new NotFoundException(ExceptionCode.NOT_FOUND_USER));

        final Post newPost = Post.builder()
                .caption(request.caption())
                .imageUrl(request.imageUrl())
                .createdAt(LocalDateTime.now())
                .build();

        postRepository.save(newPost);
    }

    @Transactional
    public PostResDto getPost(Long postId) {
        Post post = postRepository.findPostById(postId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_POST));
        List<Comment> comments = post.getComments();
        return PostResDto.of(post, comments);
    }

    @Transactional
    public List<PostResDto> getPostByUser(final UserPostsReqDto request) {
        final List<Post> posts = postRepository.findAllByUserId(request.userId());

        return posts.stream()
                .map(post -> PostResDto.of(post, post.getComments()))
                .toList();
    }


    @Transactional
    public void deletePost(Long postId) {

        final Post post = postRepository.findPostById(postId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_POST));

        postRepository.delete(post);
    }

}

