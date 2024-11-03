//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ceos20.instagram.post.domain;

import com.ceos20.instagram.comment.domain.Comment;
import com.ceos20.instagram.exception.BadRequestException;
import com.ceos20.instagram.exception.ExceptionCode;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import com.ceos20.instagram.user.domain.User;

@Entity
@Table(name = "post")
@Getter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String caption;
    private String imageUrl;
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private int likeNum;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    @OrderBy("id asc")
    private List<Comment> comments;


    public Post(){}


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

    public void increaseLikeNumber(){
        likeNum++;
    }
    public void decreaseLikeNumber() {
        if (likeNum <= 0) {
            throw new BadRequestException(ExceptionCode.INVALID_LIKE_NUMBER);
        }
        likeNum--;
    }
}
