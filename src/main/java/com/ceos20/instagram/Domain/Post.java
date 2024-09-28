//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ceos20.instagram.Domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.Builder;
import lombok.Generated;
import lombok.Getter;
import org.apache.catalina.User;
import org.hibernate.metamodel.mapping.MappingModelExpressible;

@Entity
@Table(name = "post")
@Getter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    private String caption;
    private String imageUrl;
    private LocalDateTime createdAt;

    // XToOne 은 기본이 EAGER 이므로 LAZY 로 설정
    @ManyToOne(fetch = FetchType.LAZY) // 연관관계 주인
    @JoinColumn(name = "user_id") // DB 의 FK명
    private User user;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<Likes> likes = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    @OrderBy("id asc") // 댓글 정렬
    private List<Comment> comments;


    public Post(){

    }

    //테스트를 위한 빌더.
    @Builder
    public Post(String caption, String imageUrl, LocalDateTime createdAt, User user) {

        this.caption = caption;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.user= user;
    }

    public void changeContent(String caption, String imageUrl) {
        this.caption = caption;
        this.imageUrl=imageUrl;
    }

    public int getLikesCount() {
        return likes.size();
    }

}
