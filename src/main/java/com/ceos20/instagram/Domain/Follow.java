package com.ceos20.instagram.Domain;

import jakarta.persistence.*;
/*
fromUser: 팔로우 관계를 요청하는 사용자
toUser: 다른 사용자에 의해 팔로우 관계가 생성되는 사용자
```
private List<Long> followerList;
    private List<Long> followingList;
    ```
    위와 같이 "유저 엔티티 안에서 리스트로 "구현하지 않은 이유는?
    만약


 */
@Entity
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_user")
    private User fromUser;

    @ManyToOne  // ManyToMany 안 쓰려고 유저 엔티티 안에 onetomany 리스트로 받음.
    @JoinColumn(name = "to_user")
    private User toUser;



    public Follow(User toUser, User fromUser){
        this.toUser = toUser;
        this.fromUser = fromUser;
    }
}
