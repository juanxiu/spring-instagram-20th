# spring-instagram-20th
CEOS 20th BE study - instagram clone coding

![세오스_인스타그램](https://github.com/user-attachments/assets/febf859f-3fbd-4009-9248-e408e1f4b0ac)

## 인스타그램 서비스 소개 
- 사용자가 글과 사진을 업로드하고, 게시물에 대한 댓글과 대댓글을 작성하거나 좋아요를 표시할 수 있는 SNS 서비스입니다. 그리고 유저 간에 1: 1 메시지 기능을 사용할 수 있습니다. 

## ERD 설명 

## 기능목록
- 게시글 조회
- 게시글에 사진과 함께 글 작성하기
- 게시글에 댓글 및 대댓글 기능
- 게시글에 좋아요 기능
- 게시글, 댓글, 좋아요 삭제 기능
- 유저 간 1:1 DM 기능
## 추가 과제: Spring Data JPA에서 EntityManager를 어떻게 주입하는가? 
### 1. EntityManager 란? 
- JPA를 사용하기 위해 database 구조와 매핑된 JPA Entity를 먼저 생성하게 된다. 그리고, 모든 JPA의 동작은 이 Entity들을 기준으로 돌아가게 되는데, 이 때 Entity들을 관리하는 역할을 하는 것이 바로 EntityManager이다. 
- 비상태성(stateless) 컴포넌트로, 요청마다 새로운 영속성 컨텍스트와 연결.
- 각 요청에 따라 새로운 EntityManager가 생성됩니다. 하지만, 이 생성된 EntityManager는 트랜잭션이 시작될 때 자동으로 주입되고, 트랜잭션이 끝나면 자동으로 종료

### 2. SimpleJpaRepository에서 EntityManager 주입
- SimpleJpaRepository는 싱글톤으로 관리되는 Spring Bean입니다. 그런데 여기서 사용되는 EntityManager는 트랜잭션에 따라 달라질 수 있기 때문에, 단일 EntityManager 인스턴스를 고정적으로 사용하는 것이 아니라 프록시를 통해 처리된다. 
- 즉, Spring은 EntityManager를 직접 주입하지 않고, EntityManager 프록시 객체를 주입.
- 이 프록시는 트랜잭션이 시작될 때마다 올바른 EntityManager를 연결

### 3. 프록시를 이용한 동작 방식
- EntityManager 프록시는 실제 EntityManager를 요청할 때 동적 바인딩을 통해 트랜잭션 컨텍스트에 맞는 실제 EntityManager를 연결.
- SimpleJpaRepository 같은 싱글톤 객체에서 EntityManager를 생성자 주입으로 받더라도, 프록시 인스턴스로 트랜잭션 별로 실제 다른 EntityManager를 사용하여 tread-safe 하다. 
- @PersistContext 를 이용하여 EntityManager 를 주입받게 되면 컨테이너가 EntityManger 가 1개의 스레드에 할당되도록 제한해준다. 


## 추가 과제: Spring Data JPA에서 EntityManager를 어떻게 주입하는가? 
### 1. EntityManager 란? 
- JPA를 사용하기 위해 database 구조와 매핑된 JPA Entity를 먼저 생성하게 된다. 그리고, 모든 JPA의 동작은 이 Entity들을 기준으로 돌아가게 되는데, 이 때 Entity들을 관리하는 역할을 하는 것이 바로 EntityManager이다. 
- 비상태성(stateless) 컴포넌트로, 요청마다 새로운 영속성 컨텍스트와 연결.
- 각 요청에 따라 새로운 EntityManager가 생성됩니다. 하지만, 이 생성된 EntityManager는 트랜잭션이 시작될 때 자동으로 주입되고, 트랜잭션이 끝나면 자동으로 종료

### 2. SimpleJpaRepository에서 EntityManager 주입
- SimpleJpaRepository는 싱글톤으로 관리되는 Spring Bean입니다. 그런데 여기서 사용되는 EntityManager는 트랜잭션에 따라 달라질 수 있기 때문에, 단일 EntityManager 인스턴스를 고정적으로 사용하는 것이 아니라 프록시를 통해 처리된다. 
- 즉, Spring은 EntityManager를 직접 주입하지 않고, EntityManager 프록시 객체를 주입.
- 이 프록시는 트랜잭션이 시작될 때마다 올바른 EntityManager를 연결

### 3. 프록시를 이용한 동작 방식
- EntityManager 프록시는 실제 EntityManager를 요청할 때 동적 바인딩을 통해 트랜잭션 컨텍스트에 맞는 실제 EntityManager를 연결.
- SimpleJpaRepository 같은 싱글톤 객체에서 EntityManager를 생성자 주입으로 받더라도, 프록시 인스턴스로 트랜잭션 별로 실제 다른 EntityManager를 사용하여 tread-safe 하다. 
- @PersistContext 를 이용하여 EntityManager 를 주입받게 되면 컨테이너가 EntityManger 가 1개의 스레드에 할당되도록 제한해준다. 
- 

- 
extends 하지말고 엔티티 매니저로 구현할 것.

EntityManager는 영속성 컨텍스트이다
영속성 컨텐스트란?
엔티티를 영구 저장하는 환경을 뜻한다.
애플리케이션과 데이터베이스 사이에서 객체를 보관하는 가상의 데이터베이스 같은 역할을 한다.
엔티티 매니저를 통해 엔티티를 저장하거나 조회하면 에티티 매니저 영속성 컨텍스트에 엔티티를 보관하고 관리


<팔로우 걸기>
A 사용자가 B 사용자 프로필에서 '팔로우' 버튼을 누르면 버튼은 '팔로잉'으로 변한다.
A 사용자의 '팔로잉' 목록에 B 사용자가 추가된다.
B 사용자의 '팔로워' 목록에 A 사용자가 추가된다

<팔로우 취소>
A 사용자가 B 사용자를 그만 '팔로우'하고 싶으면,
B 사용자 프로필 또는 '팔로잉' 목록에서 '팔로잉' 버튼을 누른다.
버튼을 누르면 '팔로우'로 변한다. '팔로잉' 목록을 다시 들어가 보면 B 사용자가 목록에 존재하지 않는다.

다시 할 것 .. 
지연로딩 코드와 다 다름. 그거 참고해서 
1. 지연로딩 - 확인: 프록시 객체 생성, 이후에 db에서 초기화(추가 쿼리 발생), 같은 팀 조회 시 별도 쿼리 안 나감
2. n+1 문제- 확인: 추가 쿼리 여러 개. (간단함) 

3. n+1 문제 해결한 것. 

그 다음에 서비스 계층 
gitignore에 yml 제외할 것 
리드미 잘 작성할 것. 

### N+1 문제
#### Post(many) 쪽 객체를 조회하는 상황.
- N은 Post의 수, 1은 Post를 조회하는 메인 쿼리
- 두 개의 Post가 조회되었고, 1개의 Post 조회 쿼리와 각 Post에 연결된 User를 가져오는 2개의 추가 쿼리가 발생
- Post의 수만큼 반복적으로 User 조회 쿼리가 발생하는 것이 N+1 문제. User는 한 번만 가져와도 되지만, Post 수만큼 불필요한 추가 쿼리가 실행
- 즉, Post가 2개라서 User를 조회하는 쿼리가 두 번 발생한 것, 각 Post에 연결된 User를 가져오기 위해 별도의 쿼리가 발생

-> Post와 User를 함께 한 번의 쿼리로 가져오도록 페치 전략을 수정하지 않으면, Post가 더 많아질수록 User를 조회하는 쿼리도 비례해서 계속 증가하게 된다. 

cf, 그리고 여기서 프록시 객체 생성 이후에 getUser() 을 실행할 때 실제 DB 에서 가져와서 객체 초기화
```
Hibernate: 
    insert 
    into
        user
        (email, password) 
    values
        (?, ?)
Hibernate: 
    insert 
    into
        user
        (email, password) 
    values
        (?, ?)
Hibernate: 
    insert 
    into
        post
        (caption, created_at, image_url, user_id) 
    values
        (?, ?, ?, ?)
Hibernate: 
    insert 
    into
        post
        (caption, created_at, image_url, user_id) 
    values
        (?, ?, ?, ?)
Hibernate: 
    select
        p1_0.post_id,
        p1_0.caption,
        p1_0.created_at,
        p1_0.image_url,
        p1_0.user_id 
    from
        post p1_0
post = com.ceos20.instagram.Domain.Post@6da53709
->post.getUser().getClass() = class com.ceos20.instagram.Domain.User$HibernateProxy$SWmpqPqD
Hibernate: 
    select
        u1_0.user_id,
        u1_0.email,
        u1_0.password,
        p1_0.user_id,
        p1_0.post_id,
        p1_0.caption,
        p1_0.created_at,
        p1_0.image_url 
    from
        user u1_0 
    left join
        post p1_0 
            on u1_0.user_id=p1_0.user_id 
    where
        u1_0.user_id=?
->post.getUser() = com.ceos20.instagram.Domain.User@3f1eb1bc
post = com.ceos20.instagram.Domain.Post@2b55ac77
->post.getUser().getClass() = class com.ceos20.instagram.Domain.User$HibernateProxy$SWmpqPqD
Hibernate: 
    select
        u1_0.user_id,
        u1_0.email,
        u1_0.password,
        p1_0.user_id,
        p1_0.post_id,
        p1_0.caption,
        p1_0.created_at,
        p1_0.image_url 
    from
        user u1_0 
    left join
        post p1_0 
            on u1_0.user_id=p1_0.user_id 
    where
        u1_0.user_id=?
->post.getUser() = com.ceos20.instagram.Domain.User@4ff1b0d

```
### join fetch 사용하여 지연로딩 문제 해결 
```dockerfile
@Transactional
    public List<Post> findAll() {
        return em.createQuery("SELECT p FROM Post p JOIN FETCH p.user", Post.class)
                .getResultList(); // User를 함께 즉시 로딩하여 반환
    }
```
post.getUser()가 프록시 객체가 아닌 실제 User 엔티티로 로드
```dockerfile
Hibernate: 
    select
        p1_0.post_id,
        p1_0.caption,
        p1_0.created_at,
        p1_0.image_url,
        u1_0.user_id,
        u1_0.email,
        u1_0.password,
        u1_0.username 
    from
        post p1_0 
    join
        user u1_0 
            on u1_0.user_id=p1_0.user_id
Hibernate: 
    select
        p1_0.user_id,
        p1_0.post_id,
        p1_0.caption,
        p1_0.created_at,
        p1_0.image_url 
    from
        post p1_0 
    where
        p1_0.user_id=?
Hibernate: 
    select
        p1_0.user_id,
        p1_0.post_id,
        p1_0.caption,
        p1_0.created_at,
        p1_0.image_url 
    from
        post p1_0 
    where
        p1_0.user_id=?
post = com.ceos20.instagram.Domain.User@47c019d7
->post.getUser().getClass() = class com.ceos20.instagram.Domain.User
->post.getUser() = com.ceos20.instagram.Domain.User@47c019d7
post = com.ceos20.instagram.Domain.User@47c019d7
->post.getUser().getClass() = class com.ceos20.instagram.Domain.User
->post.getUser() = com.ceos20.instagram.Domain.User@47c019d7
post = com.ceos20.instagram.Domain.User@16124894
->post.getUser().getClass() = class com.ceos20.instagram.Domain.User
->post.getUser() = com.ceos20.instagram.Domain.User@16124894
post = com.ceos20.instagram.Domain.User@16124894
->post.getUser().getClass() = class com.ceos20.instagram.Domain.User
->post.getUser() = com.ceos20.instagram.Domain.User@16124894

```
 