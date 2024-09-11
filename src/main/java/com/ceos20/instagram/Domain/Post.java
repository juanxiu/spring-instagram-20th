//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ceos20.instagram.Domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Generated;

@Entity
@Table(
        name = "post"
)
public class Post {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "post_id"
    )
    private Long postId;
    private String caption;
    private String imageUrl;
    private LocalDateTime createdAt;
    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "user_id"
    )
    private User user;

    protected Post() {
    }

    public Post(String caption, String imageUrl, LocalDateTime createdAt, User user) {
        this.caption = caption;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.user = user;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Post post = (Post)o;
            return Objects.equals(this.postId, post.postId);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.postId});
    }

    @Generated
    public Long getPostId() {
        return this.postId;
    }

    @Generated
    public String getImageUrl() {
        return this.imageUrl;
    }

    @Generated
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    @Generated
    public String getCaption() {
        return this.caption;
    }

    @Generated
    public User getUser() {
        return this.user;
    }
}
