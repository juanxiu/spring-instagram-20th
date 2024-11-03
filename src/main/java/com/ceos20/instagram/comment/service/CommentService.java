package com.ceos20.instagram.comment.service;

import com.ceos20.instagram.comment.domain.Comment;
import com.ceos20.instagram.comment.repository.CommentRepository;
import com.ceos20.instagram.post.domain.Post;
import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.comment.dto.CommentRequest;
import com.ceos20.instagram.post.repository.PostRepository;
import com.ceos20.instagram.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CommentService {
    @Autowired
    private UserRepository userRepository;
    private PostRepository postRepository;
    private CommentRepository commentRepository;

    // 댓글 추가.
    public Comment save(Long postId, CommentRequest request, String userName, Long commentid) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다. userName=" + userName));

        Post post = postRepository.findPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. postId=" + postId));

        // 부모 댓글이 있을 경우 찾기 - 대댓글 달기.
        Comment parentComment = null;
        if (commentid != null) {
            parentComment = commentRepository.findById(commentid)
                    .orElseThrow(() -> new IllegalArgumentException("대댓글을 달 수 없습니다."));
        }

        Comment comment = request.toEntity(user, post, parentComment); // // parentComment는 null일 수도 있고, 특정 Comment일 수도 있음
        commentRepository.save(comment);
        return comment;
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

    @Transactional
    public void delete(Long postId, Long userId) {
        Comment comment = commentRepository.findByPostIdAndUserId(postId, userId).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글이 존재하지 않습니다. " + userId));

        commentRepository.delete(comment);
    }

}
