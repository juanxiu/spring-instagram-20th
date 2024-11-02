package com.ceos20.instagram.Service;

import com.ceos20.instagram.Domain.Comment;
import com.ceos20.instagram.post.domain.Post;
import com.ceos20.instagram.Domain.User;
import com.ceos20.instagram.Dto.CommentRequest;
import com.ceos20.instagram.Repository.CommentRepository;
import com.ceos20.instagram.Repository.PostRepository;
import com.ceos20.instagram.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private UserRepository userRepository;
    private PostRepository postRepository;
    private CommentRepository commentRepository;

    /*
    1. mapper 사용해서 dto <-> entity 해보 것.
    2. 유효성 검사를 어떻게 할 건지. globalexceptionhandler?
     */

    // 댓글 추가.
    public Comment save(Long postId, CommentRequest request, String userName, Long commentid) {
        User user = userRepository.findByuserName(userName)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다. userName=" + userName));

        Post post = postRepository.findPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. postId=" + postId));

        // 부모 댓글이 있을 경우 찾기 - 대댓글 달기.
        Comment parentComment = null;
        if (commentid != null) {
            parentComment = commentRepository.findById(commentid)
                    .orElseThrow(() -> new IllegalArgumentException("대댓글을 달 수 없습니다."));
        }

        Comment comment = request.toEntity(user, post, parentComment); // parentComment는 null일 수도 있고, 특정 Comment일 수도 있음
        commentRepository.save(comment);
        return comment;
    }

    //댓글 조회
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
