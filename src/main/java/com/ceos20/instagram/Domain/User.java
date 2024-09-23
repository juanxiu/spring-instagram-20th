//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ceos20.instagram.Domain;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Generated;
import lombok.Getter;

@Entity
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String email; // email로 refactor 할 것.
    private String password;

    @OneToMany(mappedBy = "user")
    private Set<Post> caption = new HashSet(); // 이게 뭐야

    @OneToMany(mappedBy = "fromUser", fetch = FetchType.LAZY)
    private List<Follow> followings; // 팔로우를 요청하는 사용자

    @OneToMany(mappedBy = "toUser", fetch = FetchType.LAZY)
    private List<Follow> followers; // 팔로우를 요청받는 사용자.

    protected User() {
    }

    public User(String userName, String password) {
        this.email = userName;
        this.password = password;
    }

//    @Generated // 오버라이드 아님???
//    public Long getUserId() {
//        return this.userId;
//    }
//
//    @Generated
//    public String getEmail() {
//        return this.email;
//    }
//
//    @Generated
//    public String getPassword() {
//        return this.password;
//    }
//
//    @Generated
//    public Set<Post> getCaption() {
//        return this.caption;
//    }
}
