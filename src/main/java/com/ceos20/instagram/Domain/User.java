//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ceos20.instagram.Domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import lombok.Generated;

@Entity
public class User {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "user_id"
    )
    private Long UserId;
    private String UserName;
    private String Password;
    @OneToMany(
            mappedBy = "user"
    )
    private Set<Post> caption = new HashSet();

    protected User() {
    }

    public User(String userName, String password) {
        this.UserName = userName;
        this.Password = password;
    }

    @Generated
    public Long getUserId() {
        return this.UserId;
    }

    @Generated
    public String getUserName() {
        return this.UserName;
    }

    @Generated
    public String getPassword() {
        return this.Password;
    }

    @Generated
    public Set<Post> getCaption() {
        return this.caption;
    }
}
