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
    만약 apple 사용자가 kiwi 사용자를 팔로우 하려고 한다. 이를 처리하기 위해 아래 과정을 거처야 한다.

apple 사용자에 followingList 에 kiwi 사용자를 추가한다.
kiwi 사용자에 followerList 에 apple 사용자를 추가한다.
즉, apple 사용자에 의해 발생되는 문제를 해결하기 위해 kiwi 사용자의 원본 객체에 접근한다는 것은 안전하지 않다고 판단해 해당 방법으로 구현하지 않았다.

(근데 .. 하 ... followId(receiverId) / followingId(senderId) 로 해야 하나..? )
이유: 한 사람은 여러 명에게 팔로우 요청을 할 수 있을뿐만 아니라 여러명에게 팔로우 요청을 받을 수 있다.
즉, from_user와 to_user는 다대다 관계라는 것이다.

이때, 우리는 일대다, 일대일 관계에서 @OneToMany, @OneToOne 어노테이션을 사용하는 것처럼
다대다 관계에서도 @ManyToMany 어노테이션을 사용하는 것을 고려할 수 있다.

하지만 @ManyToMany 어노테이션은 사용을 권장하지 않는다.
왜냐하면 @ManyToMany 어노테이션을 사용할 경우 내부적으로 중간 테이블이 생성되어 개발자가 알지 못하는 쿼리가 발생하기 때문이다.
또한 생성된 테이블에 필요한 컬럼을 추가할 수 없어 유지·보수가 불편하다.

이런 경우 연결 Entity를 직접 생성하는 것이 권장된다.

   <팔로우 걸기>
A 사용자가 B 사용자 프로필에서 '팔로우' 버튼을 누르면 버튼은 '팔로잉'으로 변한다.
A 사용자의 '팔로잉' 목록에 B 사용자가 추가된다.
B 사용자의 '팔로워' 목록에 A 사용자가 추가된다

<팔로우 취소>
A 사용자가 B 사용자를 그만 '팔로우'하고 싶으면,
B 사용자 프로필 또는 '팔로잉' 목록에서 '팔로잉' 버튼을 누른다.
버튼을 누르면 '팔로우'로 변한다. '팔로잉' 목록을 다시 들어가 보면 B 사용자가 목록에 존재하지 않는다.
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
