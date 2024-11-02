//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ceos20.instagram.post.domain;

import com.ceos20.instagram.Domain.Comment;
import com.ceos20.instagram.Domain.User;
import com.ceos20.instagram.exception.ExceptionCode;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.ceos20.instagram.exception.BadRequestException;

import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "post")
@Getter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE Post SET deleted = true WHERE id = ?") //엔티티에 적용하는 soft delete.
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    private String caption;
    // imageUrl의 경우, 컬렉션을 리스트로?
    private String imageUrl;
    private LocalDateTime createdAt;

    // XToOne 은 기본이 EAGER 이므로 LAZY 로 설정
    @ManyToOne(fetch = FetchType.LAZY) // 연관관계 주인
    @JoinColumn(name = "user_id") // DB 의 FK명
    private User user;

    /*
    @onetomany 는 default가 lazy기 떄문에 굳이 직접 설정해주지 않아도 돼요!
     */
    @Column(nullable = false)
    private int likeNumber;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    @OrderBy("id asc") // 댓글 정렬
    private List<Comment> comments;


    //테스트를 위한 빌더.
    //allargs - 모든 생성자
    //noargs -기본 생성자
    @Builder
    public Post(final Long postId, final String caption, final String imageUrl, final LocalDateTime createdAt, User user) {
        this.postId=postId;
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
        likeNumber++;
    }

    public void decreaseLikeNumber() {
        if (likeNumber<=0){
            throw new BadRequestException(ExceptionCode.INVALID_LIKE_NUMBER);
        }
        likeNumber--;
    }

}
