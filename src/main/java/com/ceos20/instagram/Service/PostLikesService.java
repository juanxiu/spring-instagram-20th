package com.ceos20.instagram.Service;

import com.ceos20.instagram.Domain.Likes;
import com.ceos20.instagram.Domain.Post;
import com.ceos20.instagram.Domain.User;
import com.ceos20.instagram.Repository.LikesRepository;
import com.ceos20.instagram.Repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class PostLikesService {

    @Autowired
    private LikesRepository likesRepository;
    private PostRepository postRepository;

    private void checkUser(Post post, User user) {
        // Post의 작성자와 현재 Member 동일한지 확인
        if (post.getUser().equals(user)) {
            throw new IllegalArgumentException("자신의 게시물에 좋아요를 누를 수 없습니다.");
        }
    }

    //좋아요 완료
    public Integer likePost(Long postId, User user) {
        Post post = postRepository.findPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다. postId=" + postId));

        checkUser(post, user);
        likesRepository.save(new Likes(user,post));
        return post.getLikesCount();
    }

    //좋아요 취소
    public Integer unlikePost(Long postId, User user) {
        Post post = postRepository.findPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다. postId=" + postId));

        Likes like = likesRepository.findByPostIdAndUserId(postId, user.getId());
        likesRepository.delete(like);
        // 다시 db에서 읽어 올 것.
        return post.getLikesCount();
    }
}
