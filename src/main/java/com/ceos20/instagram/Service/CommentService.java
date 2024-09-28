package com.ceos20.instagram.Service;

import com.ceos20.instagram.Domain.Comment;
import com.ceos20.instagram.Domain.Post;
import com.ceos20.instagram.Domain.User;
import com.ceos20.instagram.Dto.CommentRequest;
import com.ceos20.instagram.Repository.CommentRepository;
import com.ceos20.instagram.Repository.PostRepository;
import com.ceos20.instagram.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Date;
import java.util.List;

public class CommentService {
    @Autowired
    private UserRepository userRepository;
    private PostRepository postRepository;
    private CommentRepository commentRepository;

    // 댓글 추가.
    public Comment save(Long postId, CommentRequest request, String userName) {
        User user = userRepository.findByEmail(userName)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다. userName=" + userName));

        Post post = postRepository.findPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. postId=" + postId));

        // CommentRequest에 유저와 포스트를 새로 설정해서 넘김
        CommentRequest updatedRequest = new CommentRequest(
                request.getComment(), //댓글 가져오기
                new Date(),
                user,
                post
        );
        return commentRepository.save(updatedRequest.toEntity()); // 댓글 저장
    }

    //댓글 읽어오기
    @Transactional
    public List<Comment> findAll(Long postId) {
        Post post = postRepository.findPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. postId=" + postId));
        List<Comment> comments = post.getComments();
        return comments;
    }

    //댓글 업데이트
    @Transactional
    public void update(Long postId, Long userId, CommentRequest dto) {
        Comment comment = commentRepository.findByPostIdAndUserId(postId, userId).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글이 존재하지 않습니다. " + userId));

        comment.update(dto.getComment());
    }
    //댓글 삭제
    @Transactional
    public void delete(Long postId, Long userId) {
        Comment comment = commentRepository.findByPostIdAndUserId(postId, userId).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글이 존재하지 않습니다. " + userId));

        commentRepository.delete(comment);
    }
    // 이후 게시글을 작성 유저인지 확인하는 메서드를 추가할 것.

}
