//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ceos20.instagram;

import com.ceos20.instagram.Domain.Post;
import com.ceos20.instagram.Domain.User;
import com.ceos20.instagram.Repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@Nested
@SpringBootTest
@AutoConfigureTestDatabase(
        replace = Replace.NONE
)
@Transactional
class PostRepositoryTest {
    @Autowired
    private com.ceos20.instagram.Repository.postRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    PostRepositoryTest() {
    }

    @DirtiesContext
    @Test
    void save_test() throws Exception {
        //given - 유저 생성
        User user = new User("testUser1", "password1");
        userRepository.save(user);
        // given - 게시물 생성
        LocalDateTime date = LocalDateTime.of(2024, 9, 11, 0, 0, 0);
        Post post1 = new Post("Post Caption 1", "ImageURL1", date, user);
        Post post2 = new Post("Post Caption 2", "ImageURL2", date, user);
        Post post3 = new Post("Post Caption 3", "ImageURL3", date, user);

        //when - 게시물 저장
        postRepository.deleteAll(); // 중복 방지 위해서 모든 게시물 삭제.
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        //then - 게시물 조회 및 검증
        List<Post> posts = postRepository.findAll(Post.class);

        Assertions.assertThat(posts).hasSize(3);
        Assertions.assertThat(posts)
                .extracting(Post::getCaption)
                .containsExactlyInAnyOrder("Post Caption 1", "Post Caption 2", "Post Caption 3");

        Assertions.assertThat(posts)
                .extracting(post -> post.getUser().getEmail())
                .containsOnly("testUser1");
    } // 테스트 성공.

    @Test
    public void update_test() throws Exception { //mockito?
        // given: 유저와 게시물 생성 및 저장
        User user = Mockito.spy(new User("testUser1", "password1"));
        userRepository.save(user);

        LocalDateTime date = LocalDateTime.of(2024, 9, 11, 0, 0, 0);
        Post post1 = Mockito.spy(new Post("Post Caption 1", "ImageURL1", date, user));
        postRepository.save(post1);

        // when: 게시물 업데이트
        // setter?? 없음. -> lombok builder 사용할 것.
        // 엔티티의 id를 어떻게 테스트에서 세팅해야 할까??>?

        // Mockito를 사용해 post1의 getId() 메서드가 1L을 반환하도록 설정
        when(post1.getId()).thenReturn(1L);

        Post updatedPost = Mockito.spy(Post.builder()
                .caption("Updated Caption")  // 업데이트할 캡션
                .imageUrl(post1.getImageUrl())  // 기존 이미지 URL 유지
                .createdAt(post1.getCreatedAt())  // 기존 생성일 유지
                .user(post1.getUser())  // 기존 유저 유지
                .build());

        // updatedPost의 getId() 메서드를 mock 처리하여 post1의 id와 동일하게 설정
        Mockito.doReturn(1L).when(updatedPost).getId();

        postRepository.save(updatedPost);

        // then: 업데이트된 게시물 검증
        assertEquals(1L, updatedPost.getId());
        assertEquals("Updated Caption", updatedPost.getCaption());

    }


}



