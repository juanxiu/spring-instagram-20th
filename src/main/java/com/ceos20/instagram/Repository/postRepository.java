package com.ceos20.instagram.Repository;


import com.ceos20.instagram.Domain.Post;
import com.ceos20.instagram.Domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/*
extends 하지말고 엔티티 매니저로 구현할 것.

EntityManager는 영속성 컨텍스트이다
영속성 컨텐스트란?
엔티티를 영구 저장하는 환경을 뜻한다.
애플리케이션과 데이터베이스 사이에서 객체를 보관하는 가상의 데이터베이스 같은 역할을 한다.
엔티티 매니저를 통해 엔티티를 저장하거나 조회하면 에티티 매니저 영속성 컨텍스트에 엔티티를 보관하고 관리
 */

@Repository
@RequiredArgsConstructor // Lombok 어노테이션으로, final 필드만을 매개변수로 받는 생성자를 자동으로 생성
public class postRepository {

    //엔티티 매니저 생성자 주입
    private final EntityManager em;

    //ID로 엔티티 조회.
    public <T> Optional<T> findById(Class<T> clazz, Long id) {
        return Optional.ofNullable(em.find(clazz, id));
    }

    // 모든 엔티티 조회.
    public <T> List<T> findAll(Class<T> clazz){
        return em.createQuery("select u from "+clazz.getSimpleName()+" u", clazz).getResultList();
    }

    @Transactional
    public void save(Post post) { // inset, update
        em.persist(post); // 2. managed 상태
    }
    public void deleteAll(){
        em.createQuery("delete from Post").executeUpdate(); // em.remove(post) 수정. 모든 게시물을 삭제할 수 있도록.
    }
}
