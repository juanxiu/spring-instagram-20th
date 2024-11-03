package com.ceos20.instagram.Repository;


import com.ceos20.instagram.Domain.Post;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import com.ceos20.instagram.Domain.User;
import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor // Lombok 어노테이션으로, final 필드만을 매개변수로 받는 생성자를 자동으로 생성
public class PostRepository {

    //엔티티 매니저 생성자 주입
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public List<Post> findAll() {
        return em.createQuery("SELECT p FROM Post p JOIN FETCH p.user", Post.class)
                .getResultList(); // User를 함께 즉시 로딩하여 반환
    }
    /* JPA 사용 시.
    @EntityGraph(attributePaths = {"user"})
    List<Post> findAll();

     */

    // Lazy Loading을 사용할 때는 해당 메소드가 실행되는 동안 Hibernate의 세션이 열려 있어야 하므로.
    @Transactional
    public Optional<Post> findPostById(Long postId) {
        Post post = em.find(Post.class, postId); // 특정 ID로 게시물 조회
        return Optional.ofNullable(post);
    }

    //특정 사용자 게시물 조회.
    @Transactional
    public List<Post> findAllByUser(User user) {
        return em.createQuery("SELECT p FROM Post p WHERE p.user = :user", Post.class)
                .setParameter("user", user)
                .getResultList();
    }

    @Transactional
    public Post save(Post post) { // insert, update
        return em.merge(post); // merge는 저장된 엔티티를 반환함
    }


    @Transactional
    public void delete(Post post){
        em.remove(post);
    }
}
