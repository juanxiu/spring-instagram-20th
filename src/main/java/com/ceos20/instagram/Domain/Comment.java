package com.ceos20.instagram.Domain;

import com.ceos20.instagram.post.domain.Post;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Entity
@Table(name = "Comment")
@Getter
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentid;

    /*
    또한 엔티티 생성시 자동으로 생성되는 시간이 생성되게끔 하기 위해 @CreatedDate나 @CreationTimeStamp같은 어노테이션을
    사용하는 게 더 편할 것 같아용
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    private String comment;

    @ManyToOne
    @JoinColumn(name = "user_id") //
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @JoinColumn(name = "post_id")
    private Comment parentComment; // 자기참조


    //댓글 수정
    public void update(String comment) {
        this.comment = comment;
    }
    // 대댓글 처리 필요 ....


}
