//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ceos20.instagram;

import com.ceos20.instagram.post.domain.Post;
import com.ceos20.instagram.Domain.User;
import com.ceos20.instagram.Repository.UserRepository;
import com.ceos20.instagram.Repository.PostRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@Transactional // 테스트가 실행되는 동안 세션이 유지
class PostRepositoryTest {

    @PersistenceContext
    private  EntityManager em;

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostRepositoryTest(PostRepository postRepository, UserRepository userRepository){
        this.postRepository = postRepository;
        this.userRepository=userRepository;
    }

    @Test
    void save_test() throws Exception {
        //given - 유저 생성
        User user1 = User.builder()
                .username("testUser1")
                .email("gmail.com")
                .password("password1")
                .build();

        User user2 = User.builder()
                .username("testUser2")
                .email("ewha.ac.kr")
                .password("password2")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        // given - 게시물 생성
        LocalDateTime date = LocalDateTime.of(2024, 9, 11, 0, 0, 0);
        Post post1 = Post.builder()
                .caption("Post Caption 1")
                .imageUrl("ImageURL1")
                .createdAt(date)
                .user(user1)
                .build();

        Post post2 = Post.builder()
                .caption("Post Caption 1")
                .imageUrl("ImageURL1")
                .createdAt(date)
                .user(user1)
                .build();

        Post post3 = Post.builder()
                .caption("Post Caption 2")
                .imageUrl("ImageURL2")
                .createdAt(date)
                .user(user2)
                .build();
        Post post4 = Post.builder()
                .caption("Post Caption 2")
                .imageUrl("ImageURL2")
                .createdAt(date)
                .user(user2)
                .build();

        //when - 게시물 저장
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
        postRepository.save(post4);

        em.flush(); // 쿼리 즉시 실행
        em.clear(); // 영속성 컨텍스트 캐시 초기화

        //then - 게시물 조회 및 검증(User를 즉시 로딩하여 지연 로딩 문제 해결)
        List<Post> posts = postRepository.findAll();

        for (Post post : posts){
            System.out.println("post = " + post.getUser()); // 이 부분 지연로딩 시 post임. 참고
            System.out.println("->post.getUser().getClass() = " + post.getUser().getClass()); // 프록시 객체 리턴
            System.out.println(("->post.getUser() = "+ post.getUser())); // 실제 객체 가져옴.
        }

    }
}



