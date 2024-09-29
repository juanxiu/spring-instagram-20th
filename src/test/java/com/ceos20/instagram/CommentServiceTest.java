package com.ceos20.instagram;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.ceos20.instagram.Domain.Comment;
import com.ceos20.instagram.Domain.Post;
import com.ceos20.instagram.Domain.User;
import com.ceos20.instagram.Dto.CommentRequest;
import com.ceos20.instagram.Repository.CommentRepository;
import com.ceos20.instagram.Repository.PostRepository;
import com.ceos20.instagram.Repository.UserRepository;
import com.ceos20.instagram.Service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

public class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private CommentRepository commentRepository;

    private Long postId = 1L;
    private Long userId = 1L;
    private String userName = "yeonsoo";
    private CommentRequest request;
    private Comment parentComment;
    private User user;
    private Post post;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Mockito 초기화

        // 테스트용 사용자 및 게시글 생성
        user = User.builder()
                .userId(userId)
                .username(userName)
                .build();

        post = Post.builder()
                .postId(postId)
                .caption("Sample Post Content")
                .build();

        // 댓글 요청 생성
        request = new CommentRequest("This is a test comment.");

        // 부모 댓글 생성
        parentComment = Comment.builder()
                .commentid(2L)
                .comment("Parent Comment")
                .user(user)
                .post(post)
                .createdAt(new Date())
                .build();
    }

    @Test
    public void testSaveComment() {
        when(userRepository.findByuserName(userName)).thenReturn(Optional.of(user));
        when(postRepository.findPostById(postId)).thenReturn(Optional.of(post));

        Comment savedComment = commentService.save(post.getPostId(), request,user.getUsername(), null);

        assertNotNull(savedComment);
        assertEquals(request.getComment(), savedComment.getComment());
        assertEquals(user, savedComment.getUser());
        assertEquals(post, savedComment.getPost());

        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    public void testSaveComment_WithParentComment() {
        Long parentCommentId = 2L;

        when(userRepository.findByuserName(userName)).thenReturn(Optional.of(user));
        when(postRepository.findPostById(postId)).thenReturn(Optional.of(post));
        when(commentRepository.findById(parentCommentId)).thenReturn(Optional.of(parentComment));

        Comment savedComment = commentService.save(postId, request, userName, parentCommentId);

        assertNotNull(savedComment);
        assertEquals(request.getComment(), savedComment.getComment());
        assertEquals(user, savedComment.getUser());
        assertEquals(post, savedComment.getPost());
        assertEquals(parentComment, savedComment.getParentComment()); // 부모 댓글 검증

        verify(commentRepository, times(1)).save(any(Comment.class));
    }
}