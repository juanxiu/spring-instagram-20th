//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ceos20.instagram.user.domain;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "member")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String username;
    private String email;

    @NotNull // 컨트롤러의 응답 바디 앞에 valid 어노테이션 추가해야 유효성 검사 가능.
    // DTO 클래스에 유효성 검사 어노테이션 @NotBlank , @NotEmpty
    private String password;

    @Enumerated(value = EnumType.STRING) // varchar 로 저장되도록?? 근데 안되네.
    private UserRole role;


    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }


}
