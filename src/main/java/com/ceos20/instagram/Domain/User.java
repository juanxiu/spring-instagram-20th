//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ceos20.instagram.Domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long userId;

    private String username;
    private String email;
    private String password;

    // @OneToMany 관계에서는 일반적으로 외래 키를 자식 엔티티(즉, Post 엔티티) 쪽에서 관리
    // 따라서 User 엔티티에는 @JoinColumn이 필요하지 않으며, mappedBy 속성을 사용
    // no Session 에러는 Lazy Loading 시도 시 세션이 없을 때 발생. -> FetchType.EAGER 사용. 연관된 엔티티를 항상 함께 로드.
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "toUser", fetch = FetchType.LAZY)
    private List<Follow> followings; // 팔로우를 요청하는 사용자

    @OneToMany(mappedBy = "fromUser", fetch = FetchType.LAZY)
    private List<Follow> followers; // 팔로우를 요청받는 사용자.

    public User(){
        //Hibernate 가 엔티티를 지연로딩 하기 위해 프록시 객체를 생성하려고 할 때 기본 생성자가 필요.
    }

    @Builder
    public User(Long userId, String username, String email, String password) {
        this.userId=userId;
        this.username = username;
        this.email = email;
        this.password = password;
    }

}
