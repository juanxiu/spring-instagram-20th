package com.ceos20.instagram.postLike.service;

import com.ceos20.instagram.user.repository.UserRepository;
import com.ceos20.instagram.exception.BadRequestException;
import com.ceos20.instagram.exception.ExceptionCode;
import com.ceos20.instagram.exception.NotFoundException;
import com.ceos20.instagram.post.domain.Post;
import com.ceos20.instagram.post.repository.PostRepository;
import com.ceos20.instagram.postLike.domain.PostLike;
import com.ceos20.instagram.postLike.dto.PostLikeReqDto;
import com.ceos20.instagram.postLike.repository.PostLikeRepository;
import com.ceos20.instagram.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostLikeService {

    @Autowired
    private PostLikeRepository postLikeRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;


    @Transactional
    public void like(final Long postId, final PostLikeReqDto request){
        final Post post = getPost(postId);
        final User user = getUser(request);
        if (postLikeRepository.existsByPostAndUser(post, user)) {
            throw new BadRequestException(ExceptionCode.ALREADY_EXIST_POST_LIKE);
        }

        final PostLike postLike = new PostLike(user, post);
        post.increaseLikeNumber();

        postLikeRepository.save(postLike);
    }

    @Transactional
    public void cancelLike(final Long postId, final PostLikeReqDto request) throws BadRequestException {
        final Post post = getPost(postId);
        final User user = getUser(request);
        final PostLike postLike = postLikeRepository.findByPostAndUser(post, user)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_POST_LIKE));
        post.decreaseLikeNumber();

        postLikeRepository.delete(postLike);
    }

    private Post getPost(final Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_POST));
    }

    private User getUser(final PostLikeReqDto request) {
        return userRepository.findById(request.userId())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_USER));
    }
}