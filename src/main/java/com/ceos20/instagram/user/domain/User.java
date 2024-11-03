//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ceos20.instagram.user.domain;

import com.ceos20.instagram.chatRoom.domain.Chatroom;
import com.ceos20.instagram.follower.Follow;
import com.ceos20.instagram.post.domain.Post;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String username;
    private String email;
    private String password;

    // @OneToMany 관계에서는 일반적으로 외래 키를 자식 엔티티(즉, Post 엔티티) 쪽에서 관리
    // 따라서 User 엔티티에는 @JoinColumn이 필요하지 않으며, mappedBy 속성을 사용
    // no Session 에러는 Lazy Loading 시도 시 세션이 없을 때 발생. -> FetchType.EAGER 사용. 연관된 엔티티를 항상 함께 로드.
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @Builder.Default
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "toUser", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Follow> followings = new ArrayList<>();

    @OneToMany(mappedBy = "fromUser", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Follow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "sender")
    @Builder.Default
    private List<Chatroom> sentChatrooms = new ArrayList<>();

    @OneToMany(mappedBy = "receiver")
    @Builder.Default
    private List<Chatroom> receivedChatrooms = new ArrayList<>();

    public User(){
        //Hibernate 가 엔티티를 지연로딩 하기 위해 프록시 객체를 생성하려고 할 때 기본 생성자가 필요.
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }


}
