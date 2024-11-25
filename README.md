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

## Spring JPA 심화 과제 

### 지난 주 코드 리팩토링 
- 대댓글 엔티티를 별도로 구현했는데, 그것을 삭제하고 Comment 엔티티 내부에 parentComment 부모댓글 필드를 만들었다. 
```dockerfile
@JoinColumn(name = "post_id")
    private Comment parentComment; // 자기참조

```

### N+1 문제
#### Post(many) 쪽 객체를 조회하는 상황.
- N은 Post의 수, 1은 Post를 조회하는 메인 쿼리
- 1개의 Post 조회 쿼리와 각 Post에 연결된 User를 가져오는 2개의 추가 쿼리가 발생
- Post의 수만큼 반복적으로 User 조회 쿼리가 발생하는 것이 N+1 문제. User는 한 번만 가져와도 되지만, Post 수만큼 불필요한 추가 쿼리가 실행
- 즉, Post가 2개라서 User를 조회하는 쿼리가 두 번 발생한 것, 각 Post에 연결된 User를 가져오기 위해 별도의 쿼리가 발생

-> Post와 User를 함께 한 번의 쿼리로 가져오도록 페치 전략을 수정하지 않으면, Post가 더 많아질수록 User를 조회하는 쿼리도 비례해서 계속 증가하게 된다.

프록시 객체가 생성된 경우에는, getUser() 을 실행할 때 실제 DB 에서 가져와서 해당 객체에 대한 쿼리를 나중에 가져와서 실행하므로 추가적인 쿼리 발생.
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
post = com.ceos20.instagram.post.domain.Post@6da53709
->post.getUser().getClass() = class com.ceos20.instagram.user.domain.User$HibernateProxy$SWmpqPqD
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
->post.getUser() = com.ceos20.instagram.user.domain.User@3f1eb1bc
post = com.ceos20.instagram.post.domain.Post@2b55ac77
->post.getUser().getClass() = class com.ceos20.instagram.user.domain.User$HibernateProxy$SWmpqPqD
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
->post.getUser() = com.ceos20.instagram.user.domain.User@4ff1b0d

```
### join fetch 사용하여 지연로딩 문제 해결
```dockerfile
@Transactional
    public List<Post> findAll() {
        return em.createQuery("SELECT p FROM Post p JOIN FETCH p.user", Post.class)
                .getResultList(); // User를 함께 즉시 로딩하여 반환
    }
```
- post.getUser()가 프록시 객체가 아닌 실제 User 엔티티로 로드
- post와 관련된 user 데이터를 한 번에 조회하여 n+1 문제 해결.
- 만약 JPA 를 사용했다면, @EntityGraph 사용 가능. 
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
```

### DTO 구현 
- DTO(Data Transfer Object)란: 계층간 데이터 교환을 위해 사용하는 객체
#### 사용 이유
- Entity 에 관한 비즈니스 로직을 외부에 노출시키지 않고, 직접적으로 사용하지 않기 위해서.
- Entity 클래스에서 필요한 데이터만 선택적으로 DTO에 담아서 생성해 사용
#### DTO 사용 예시 
```dockerfile
@Data
public class CommentRequest {
    private final String comment;

    @Builder
    public CommentRequest(String comment) {
        this.comment = comment;
    }

    public Comment toEntity(User writer, Post post, Comment parentComment) {
        return Comment.builder()
                .comment(comment) // DTO의 댓글 내용을 엔티티에 설정
                .user(writer)
                .parentComment(parentComment)
                .post(post)
                .build();
    }

}
```
- `toEntity` 메서드는 DTO 객체에 담긴 데이터를 바탕으로 `Comment` 엔티티를 생성
- User, Post, Comment 엔티티를 파라미터로 받아, 이 값들을 새로운 Comment 엔티티에 설정
- DTO 데이터를 기반으로 엔티티를 생성하여 데이터베이스에 저장하거나 비즈니스 로직에서 사용

### 댓글 업데이트에 대한 서비스 코드
```dockerfile
    @Transactional
    public void update(Long postId, Long userId, CommentRequest dto) {
        Comment comment = commentRepository.findByPostIdAndUserId(postId, userId).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글이 존재하지 않습니다. " + userId));

        comment.update(dto.getComment());
    }
```
- `dto.getComment()`를 통해 사용자가 입력한 새로운 댓글 내용을 가져온다. 
- Comment 엔티티에 update 메서드가 구현되어 있어, dto로부터 전달받은 댓글 내용을 통해 해당 엔티티를 갱신
- DTO 안에 `@Builder` 패턴을 사용하여 생성자에서 데이터를 깔끔하게 주입

### 예외 처리 
#### 람다 표현식과 `orElseThrow()`
- Optional이 비어있을 경우, 즉 값이 없을 때 예외를 던지도록 하는 메서드
- 값이 필수적으로 존재해야 하는 경우에 자주 사용

메시지 보내기 서비스 메서드. 
```dockerfile
@Transactional
    public void sendMessage(Long roomId, ChatDto chatDto) {
        Chatroom chatroom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 채팅방이 존재하지 않습니다. roomId=" + roomId));

        ChatMessage newMessage = chatDto.toEntity(chatroom);
        messageRepository.save(newMessage);
    }
```
#### `Optional.isPresent()`
- 값이 존재하는지 여부를 확인한 후, 존재하지 않으면 예외를 던지는 방식
- 존재할 경우 get()으로 값을 꺼낸다.
채팅방 생성 서비스 메서드 
```dockerfile
@Transactional
    public Chatroom createChatroom(String roomName, User sender, User receiver) {

        // 동일한 사용자 간의 채팅방이 있는지 확인
        Optional<Chatroom> existingChatroom = chatRoomRepository.findByUserIds(sender.getUserId(), receiver.getUserId());

        if (existingChatroom.isPresent()) {
            return existingChatroom.get(); // 존재하는 채팅방을 반환
        }
```
- 현재 채팅방이 존재하는 지 확인한 후, 참이라면 .get() 메서드로 존재하는 채팅방을 반환하도록 한다. 

##  단위 테스트
### Mockito
#### Mock 이란?
- 진짜 객체와 비슷하지만 물리적으로 같지 않고 프로그래머가 직접 행동을 관리하는 객체
#### Stubbing
- 테스트 코드에서 Mock 객체를 사용할 때, Mock의 특정 메서드 호출과 응답을 정의하는 것
#### Mocking 의존 객체 
- @Mock : Mock 객체의 인스턴스 내부는 비어있다. (Null)
- @InjectMock :해당 객체의 멤버 변수로 존재하는 의존된 다른 객체들이 Mock혹은 Spy로 생성된 객체라면 의존성 주입을 해주는 기능을 제공
```dockerfile
@InjectMocks
    private FollowService followService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FollowRepository followRepository;
```
### Junit
#### @BeforeEach 
- 매 테스트마다 초기화되어야 하는 클래스 등을 설정하기 위해 사용
- 모든 테스트에 한 번만 로딩되어야 하는 데이터가 있으면, @BeforeAll 을 사용하는 게 중복을 줄일 수 있음. 
```dockerfile
@BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Mockito 초기화

        // 테스트용 사용자 및 게시글 생성
        user = User.builder()
                .userId(userId)
                .username(userName)
                .build();

        post = Post.builder()
                .postId(postId)
                .caption("Sample Post Content")
                .build();

```
#### assertions
```dockerfile
 assertNotNull(savedComment);
        assertEquals(request.getComment(), savedComment.getComment());
        assertEquals(user, savedComment.getUser());
        assertEquals(post, savedComment.getPost());
```
- assertEquals
- 기대하는 값과 실제 값이 동일한 지 검사
- 첫 번째 인수 :expected. 기대하는 값을 넣어준다.
- 두 번째 인수 : actual. 실제 값을 넣어준다.


# WEEK 4
CRUD API 과제 

## 1. 정적 팩토리 메서드 패턴 이용한 dto 
## Record
```dockerfile
@Builder
public record PostResDto(User user, String caption, List<Comment> comments) {
}
```
- 생성자, accessors(getter), equals(), hashCode(), toString() 등 DTO 특성의 클래스를 개발할때 매번 개발자가 직접 구현해주어야 했던 반복적인 작업이 불필요
- final 클래스이므로 다른 클래스를 상속하거나/상속시킬 수 없다. 
### 정적팩토리 메서드 
- 객체를 생성하는 정적 메서드를 제공하여, 생성자 대신 이를 통해 객체를 반환하는 패턴
### of 메서드 
```dockerfile
public static PostResDto of(final Post post, List<Comment> comments) {
        return new PostResDto(post.getUser(), post.getCaption(), comments);
    }
```
-  entity에서 DTO로 변환하는 역할을 한다.
- 주로 entity를 조회한 결과를 클라이언트로 전송할 때나, DTO로 변환하여 컨트롤러에서 전달할 때 사용한다.
  (응답)
- 이 메서드는 entity에서 필요한 필드 값을 DTO에 설정하여 반환한다.

### toEntity 메서드 
```dockerfile
public Comment toEntity(User writer, Post post, Comment parentComment) {
        return Comment.builder()
                .comment(comment) 
                .user(writer)
                .parentComment(parentComment)
                .post(post)
                .build();
    }
```
- DTO에서 Entity로 변환하는 역할을 한다.
- 주로 데이터를 저장하거나 업데이트하는 작업에 사용된다.
- 이 메서드는 entity를 생성하고, DTO에서 받아온 필드 값을 entity에 설정하여 반환한다.

## 2.Stream.toList()
### `Stream`
- 람다를 활용해 배열과 컬렉션을 함수형으로 간단하게 처리할 수 있는 기술
- 기존의 for문과 Iterator 대신 사용한다.
- 데이터 소스를 추상화하고, 데이터를 다루는데 자주 사용되는 메소드를 정의해놓음.

### 중간연산 
`mapping`
- 스트림 내 요소들을 하나씩 특정 값으로 변환하는 작업, 값을 변환하기 위한 람다를 인자로 받는다.
- 스트림을 원하는 모양의 새로운 스트림으로 변환하고싶을 때 사용

```dockerfile
@Transactional
    public List<PostResDto> getPostByUser(final UserPostsReqDto request) {
        final List<Post> posts = postRepository.findAllByUserId(request.userId());

        return posts.stream()
                .map(post -> PostResDto.of(post, post.getComments()))
                .toList();
    }
```
- 람다 표현식과 함수형 인터페이스를 사용
  - `post -> PostResDto.of(post, post.getComments())`는 각 post 객체를 PostResDto로 변환하는 람다 표현식
- map 연산을 통해 `PostResDto`로 변환된 요소들이 새로운 스트림에 담기고, 최종 연산인 `toList()`를 통해 이 스트림이 리스트로 반환

### Collectors.toList() vs Steam.toList()
- 둘 다 스트림을 리스트로 바꾸어 주는 기능
- `collect(Collectors.toList()) ` 는 리턴되는 List가 수정이 가능하다는 문제 발생
- 이를 해결하고자 등장한 `toList() 메서드`는 **불변 리스트를 반환**
- 둘 다 NULL 허용. 

## Controller 테스트 

### @WebMvcTest
- 웹 계층의 테스트에 사용
- 웹 계층에 관련된 컴포넌트만을 로드하여 빠르게 테스트를 수행
- `Repository` 나 `Service`  계층은 @MockBean 사용해서 리포지토리와 서비스를 Mock 객체에 빈으로 등록해주어야 한다.

404 NOT FOUND 에러 
```dockerfile
	
Error: response status is 404

Response body
Download
{
  "status": "NOT_FOUND",
  "message": "해당 회원은 존재하지 않습니다."
}
```
에러 메시지 
```dockerfile
2024-11-04T17:54:09.190+09:00 ERROR 36161 --- [nio-8080-exec-5] c.c.i.exception.GlobalExceptionHandler   : 해당 회원은 존재하지 않습니다.

```

### 404 NotFound 해결 
#### 원인
- User 도메인이 양방향 매핑이었는데, 단방향 매핑으로 바꿈. `OneToMany`를 삭제하고 `Many` 에 해당하는 도메인에서만 `ManyToOne` 붙임. 
- 그리고  Mock 객체를 만들 때 `User` 도메인에서 필요한 필드만 가짜 객체로 만들었음. 
- `username` 이 응답 바디로 설정 안되어 있어서 서비스의 Creatpost에 전달 안되었음. 
- 컨트롤러에서 `@RequestBody ` 를 `PostCreateReqDto`만 받도록 수정하고, 그에 따라 `creatPost` 서비스의 메서드 파라미터도 수정함. 기존에는 `username` 파라미터가 있었는데, 이건 사용자 인증 단계에서 구현하기로 하고, 지금은 `PostCreateReqDto` 에 `username` 필드를 넣음. 

### Request Body
URI: `api/posts` 게시물 생성 API 
```dockerfile
{
  "username": "username",
  "caption": "caption",
  "imageUrl": "imageUrl"
}
```
### swagger 응답 성공 
<img width="1383" alt="스크린샷 2024-11-05 오후 4 11 30" src="https://github.com/user-attachments/assets/457c5fd2-a794-4604-bbd1-5c4af7a5e42e">

## WEEK 5 
### 지난 주 리팩토링 
### ResponseEntity
- Spring Framework에서 제공하는 클래스 중 HttpEntity라는 클래스가 존재한다. 이것은 HTTP 요청(Request) 또는 응답(Response)에 해당하는 HttpHeader와 HttpBody를 포함하는 클래스
- HttpEntity 클래스를 상속받아 구현한 클래스가 RequestEntity, ResponseEntity 클래스이다. ResponseEntity는 사용자의 HttpRequest에 대한 응답 데이터를 포함하는 클래스이다. 따라서 HttpStatus, HttpHeaders, HttpBody를 포함

성공 응답을 나눌 필요는? 200과 201

### @Transactional(readOnly = true)
- 트랜잭션 전역처리 
- Service 단에서 `@Transactional(readOnly=True)`를 클래스 레벨에 선언하면 자동으로 모든 서비스 내 메소드에 읽기전용 트랜잭션이 적용되기에, 삭제와 추가같이 디비를 조작할 일이 있는 메소드에만 @Transactional을 쓰는 방식으로 구현


## 1. JWT 인증(Authentication)
### 액세스토큰
- stateless 
#### payload 
- Payload 부분에는 토큰에 담을 정보가 들어있습니다. 여기에 담는 정보의 한 ‘조각’ 을 클레임(Claim) 이라고 부르고, 이는 Json(Key/Value) 형태의 한 쌍
- 생성된 토큰은 HTTP 통신을 할 때 Authorization이라는 key의 value로 사용된다. 일반적으로 value에는 Bearer가 앞에 붙여진다. 
### 리프레쉬 토큰 
- 평소에 API 통신할 때는 Access Token을 사용하고, Refresh Token은 Access Token이 만료되어 갱신될 때만 사용
1. 클라이언트에서 API를 호출하면 액세스 토큰이 유효한지 검사한다.
2. 액세스 토큰이 만료되었다면 클라이언트에서는 리프레시 토큰을 추가로 요청 헤더에 담아서 다시 한번 API를 호출한다.
3. 리프레시 토큰이 유효하면 새로운 액세스 토큰을 응답 헤더에 담아서 정상 응답을 반환한다.

### 세션, 쿠키
- 사용자가 로그인을 합니다.

- 서버에서는 계정 정보를 읽어 사용자를 확인한 후, 사용자에게 고유한 ID값을 부여하여 세션 저장소에 저장한 후, 이와 연결되는 Session ID를 발급합니다.

- 서버는 HTTP 응답 헤더에 발급된 Session ID를 실어 보냅니다. 이후 매 요청마다 HTTP 요청 헤더에 Session ID가 담킨 쿠키를 실어 보냅니다.

- 서버에서는 쿠키를 받아 세션 저장소에서 대조를 한 후 대응되는 정보를 가져옵니다.

- 인증이 완료되고 서버는 사용자에 맞는 데이터를 보내줍니다.
### OAuth
- Refresh Token이 탈취당하는 건 예방할 수 있을까? OAuth에서는 Refresh Token Rotation을 제시
- Refresh Token Rotation은 클라이언트가 Access Token를 재요청할 때마다 Refresh Token도 새로 발급받는 것이다.
- 이렇게 되면 탈취자가 가지고 있는 Refresh Token은 더이상 만료 기간이 긴 토큰이 아니게 된다.

### Redis를 통한 Access Token 재발급 ??
- Refresh 토큰 서버측 주도권 필요. 
- 쿠키 

그 토큰 프로바이더에 getaccess 헤더에서 토큰만 추출? 이러면 
문자열로 직접 bearer 로 떼줬잖아 그거 떼고 나머지는 구현해야 함. 
substring ? 
jwt가 왜 stateless? 디비에 접근 안함 . 매 순간마다 접근이 아니라서 stateless
서버가 상태를 관리하는 가.. 에 따라 stateless/ ful 나뉜다. 
세션은 매요청 접근이라 stateful . 


### SecurityConfig 설정 
- CustomUserDetails: Custom 로그인 객체로 사용 
- UserDetailsService 구현체의 loadUserByUsername() method의 반환 타입이 UserDetails
- UserDetailsService 구현 클래스에서는 loadUserByUsername() 메서드를 통해 로그인 객체에서 필요한 user 정보를 담은 CustomUserDetails 클래스를 반환
- csrf 비활성화 해도 되는 이유? 
- CORS 
- 프론트는 3000 포트 허용해야 함. 

## 3. 회원가입 및 로그인 API 구현
### Spring Security 와 로그인 테스트 
- Spring Security에서 인증된 사용자 정보는 UsernamePasswordAuthenticationToken 객체로 표현
- Principal(주체), Credentials(자격증명), Granted Authorities(부여된 권한)
- Principal은 인증된 사용자의 정보를 나타낸다. CustomUserDetails 객체를 Principal로 설정했는데, CustomUserDetails는 사용자의 세부 정보(예: 사용자명, 이메일, 비밀번호 등)를 담고 있는 사용자 정의 클래스이다. 
- Credentials는 사용자의 인증 정보를 나타낸다. 비밀번호와 같은 민감한 정보가 "PROTECTED"로 표시
- Granted Authorities=[ROLE_USER]: Granted Authorities는 사용자가 부여받은 권한 목록을 나타냅니다.



<img width="816" alt="스크린샷 2024-11-08 오후 5 05 40" src="https://github.com/user-attachments/assets/21e98091-9704-4857-bd8d-dbcf3de972bd">

<img width="816" alt="스크린샷 2024-11-08 오후 5 05 58" src="https://github.com/user-attachments/assets/8b9f3be3-11e3-4a75-88f2-56867cb0f497">


## 4. 게시물 생성 API 성공
### POST /api/posts 
요청 헤더
```dockerfile

POST /api/posts HTTP/1.1
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMTIzNCIsImF1dGgiOiJVU0VSIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE3MzExNzk0NjcsImV4cCI6MTczMTE4MzA2N30.fPhXjtRlRqOWBqHHFpz7SYkpVNxTbTYda9fcwlkBkwj5dSe788FXASt_JBpJ507MLqLGW2y4o5AQJyz9U7Vdog
User-Agent: PostmanRuntime/7.42.0
Accept: */*
Postman-Token: 8d9b4695-6009-40a2-be7d-a0e52acce2fb
Host: localhost:8080
Accept-Encoding: gzip, deflate, br
Connection: keep-alive
Content-Length: 53
 
{
"caption": "caption",
"imageUrl": "image"
}
```
응답 헤더
```
HTTP/1.1 201 Created
Vary: Origin
Vary: Access-Control-Request-Method
Vary: Access-Control-Request-Headers
X-Content-Type-Options: nosniff
X-XSS-Protection: 0
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Length: 0
Date: Sat, 09 Nov 2024 19:13:50 GMT
Keep-Alive: timeout=60
Connection: keep-alive
```

- @RequestBody
- @PathVariable
### @AuthenticationPrincipal
- `SecurityContextHolder`에 저장된 인증 객체의 principal을 가져와서 사용하는 것
```dockerfile
@Operation(summary = "게시글 작성", description = "게시글을 작성합니다")
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody final PostCreateReqDto request, @AuthenticationPrincipal final CustomUserDetails customUserDetails) {

        String username = customUserDetails.getUsername(); // 인증된 사용자 이름 가져오기
        postService.createPost(request, username);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
```

### 비밀번호 인코딩
#### passwordEncoder
- 클라이언트가 아이디 비밀번호를 입력한다면, 해싱 알고리즘을 통해 데이터베이스에서 비밀번호를 가져와 해시 값이 일치하는지 비교
- 스프링 시큐리티에서는 PasswordEncoder의 구현을 위해 BCryptPasswordEncoder, ScryptPasswordEncoder, Argaon2PasswordEncoder 등을 제공하는데, BCryptPasswordEncoder 를 권장함. 
- 기본 생성자일 경우 int인 strength는 -1로 선택되는데, -1일 시 보안의 강도는 10으로 자동으로 설정
## 5. 트러블슈팅 
1. 빈 이중 등록 
- @Component 
- @Bean 
2. 403 Forbidden 에러
- 에러 원인 
#### `Authorization : Bearer <token>`
- HTTP 표준과 Spring Security에서 토큰 기반 인증 정보를 전달하는 표준 방식
- 대부분의 클라이언트 라이브러리 (예: Axios, Postman, Swagger 등) 와 브라우저의 인증 토큰 관리 방식이 Authorization 헤더에 의존한다고 함

```dockerfile
// doFilterInternal 코드 
        String authorizationHeader = request.getHeader("Authorization");
        log.info("Authorization Header: {}", authorizationHeader);
        
// successfulAuthentication 코드 ( Login Filter 의미) 
         response.setHeader("Authorization", "Bearer " + accessToken);

```

### 다음 주에 추가할 것 
#### @ModelAttribute
- 클라이언트로부터 일반 HTTP 요청 파라미터나 multipart/form-data 형태의 파라미터를 받아 객체로 사용하고 싶을 때 이용
- 객체 생성 및 초기화 > Data Binding > Validation 순서로 진행

#### MultipartFile을 통한 파일 업로드 - S3 config 
#### BaseTimeEntity
- createAt 필드 선언할 때 @CreateTimestamp와 같이 자동으로 객체 생성시간을 기록해주게끔 할 수 있어요! 그럼 객체 생성할 때 createAt필드를 우리가 직접 넣어주지 않아도 된다. 

## WEEK 6
### 도커 
### 1. Docker Hub에서 이미지 pull 받기
`docker pull nginx`
- 별도의 태그를 지정하지 않으면 nginx:latest 이미지를 가져온다.
- docker hub vs docker registry 
- 

`docker images`
- 로컬 이미지 목록 조회 명령어

<img width="913" alt="스크린샷 2024-11-13 오후 7 16 23" src="https://github.com/user-attachments/assets/703ec846-1cc0-437e-8ad4-770a8e7a8e6c">

### 2. 이미지 실행시키기 
`dockerd run -p <포트포워딩> <image 이름>    // 이미지 실행 명령어`
#### 포트 포워딩
- Host OS(우리가 사용하는 컴퓨터 OS) 위에서 도커 engine 이 실행되면, 도커 container 를 실행시킬 수 있다. 이 때, Host 의 port 와 container 의 port 를 맵핑시켜줘야 외부에서 container 로 접근할 수 있는데, 이를 **포트 포워딩** 이라고 한다.
- 8080:80의 경우, 호스트의 8080 포트와 도커 컨테이너의 80 포트를 연결한 컨테이너를 실행한다는 의미
- `-p` 옵션 : container 의 port 를 host 에 publish 한다.
(cf. -p 에서 p 는 port 가 아니라 publish 의 축약어이다)
**- Host 의 8088번포트(외부포트) 와 Container 의 80번포트(내부포트) 를 포워딩 시켜준다.**
도커는 호스트 컴퓨터의 8088포트로 들어오는 네트워크 트래픽을 주시하다가 필요한 트래픽을 컨테이너의 80 포트로 전달한다.

`--publish`라는 플래그 덕분에 호스트 컴퓨터의 물리 네트워크 주소가 컨테이너의 가상 네트워크 주소에 접근할 수 있는 것이다. 왜냐하면 이 가상 주소는 도커 내부에만 존재하는 주소이기 때문이다. 하지만, `--publish`라는 플래그를 통해 컨테이너의 포트가 공개되었으므로 컨테이너로 트래픽을 전달할 수는 있는 것이다.
<img width="913" alt="스크린샷 2024-11-13 오후 7 21 25" src="https://github.com/user-attachments/assets/9fba1be5-3b74-4a49-bf08-8baaefa9d161">

### 3. 이미지 실행 결과 확인 
`http://localhost:<호스트 포트 번호>`

<img width="745" alt="스크린샷 2024-11-13 오후 7 24 35" src="https://github.com/user-attachments/assets/5d11d15b-ca76-477d-8322-a58a8d68bae2">

### 4. 컨테이너 실행시키기 
```
docker run --name <container 이름> -p <포트포워딩> <image 이름>    // 이미지를 담은 컨테이너 실행 명령어
```
<img width="854" alt="스크린샷 2024-11-13 오후 7 31 15" src="https://github.com/user-attachments/assets/d107d619-84d9-4975-a3c6-6704aad8e2ba">

### 5. 컨테이너 실행 결과 확인 
```dockerfile
docker ps       // 실행 중인 컨테이너 목록 확인 명령어
docker ps -a    // 전체 컨테이너 목록 확인 명령어

// 확인 후 CMD + C 로 실행 종료

```
<img width="935" alt="스크린샷 2024-11-13 오후 7 33 58" src="https://github.com/user-attachments/assets/217c8eab-9d69-494f-bf29-c5d84714b782">

### 6. Docker Desktop 내역 확인
<img width="949" alt="스크린샷 2024-11-13 오후 7 35 56" src="https://github.com/user-attachments/assets/2a107789-25bb-4f21-859b-6374bc2bb32b">

## 도커 기반 스프링부트 빌드 
```dockerfile
FROM openjdk:17
ARG JAR_FILE=/build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar", "/app.jar"]
```
1. `FROM openjdk:17`: 도커 이미지를 생성할 때 사용할 베이스(OpenJDK 17) 이미지를 지정합니다.
2. `ARG JAR_FILE=/build/libs/*.jar`: 도커 빌드 중에 사용되는 변수를 정의합니다. `JAR_FILE`은 나중에 `COPY` 명령에서 사용될 것입니다. 이 변수는 JAR 파일의  경로를 지정하는 역할을 합니다.
3. `COPY ${JAR_FILE} app.jar`: 빌드된 JAR 파일을 도커 이미지 내부로 복사합니다. 이 명령은 빌드된 JAR 파일을 도커 이미지의 `/app.jar` 경로로 복사합니다.
4. `ENTRYPOINT ["java","-jar","/app.jar"]`: 컨테이너가 실행될 때 실행되는 명령을 정의합니다. 여기서는 Java 실행 명령을 통해 `/app.jar` 경로에 있는 JAR 파일을 실행하도록 지정합니다.

### 도커 이미지 생성 
```dockerfile
docker build -t {docker image 이름} {Dockerfile의 위치}
```
- -t 옵션를 통해 docker 이미지 이름 및 태그를 지정할 수 있습니다. (태그 생략시 default: latest)

### 컨테이너 실행 
```docker run -p 8080:8080 {docker image 이름}```
## 도커 컴포즈 
- 단일 서버에서 여러 개의 컨테이너를 하나의 서비스로 정의해 컨테이너의 묶음으로 관리할 수 있는 작업 환경을 제공하는 관리 도구 
- 여러 개의 컨테이너가 하나의 애플리케이션으로 동작할 때, 도커 컴포즈를 사용하지 않으면 이를 테스트하기 위해 각 컨테이너를 하나씩 생성해야 한다. 

## WEEK 7
### 리팩토링 
- mysql 포트포워딩 에러 
- `netstat -anv | grep 3306` 명령어로 3306 포트가 이미 사용 중인 것을 알게 됨. 
```dockerfile
yeonsookim@gim-yeonsuui-MacBookPro spring-instagram-20th % lsof -i :3306

yeonsookim@gim-yeonsuui-MacBookPro spring-instagram-20th % netstat -anv | grep 3306

tcp46      0      0  *.3306                 *.*                    LISTEN       131072  131072   8834      0 00100 00000006 0000000000035d81 00000000 00000800      1      0 000001
tcp46      0      0  *.33060                *.*                    LISTEN       131072  131072   8834      0 00000 00000006 0000000000035d7e 00000000 00000800      1      0 000001

```
- `sudo` 명령어 사용해야 사용 중인 포트 PID 확인이 가능했음. 
```dockerfile
yeonsookim@gim-yeonsuui-MacBookPro spring-instagram-20th % sudo lsof -i :3306

COMMAND  PID   USER   FD   TYPE             DEVICE SIZE/OFF NODE NAME
mysqld  8834 _mysql   22u  IPv6 0x8828d6f1ac7013bb      0t0  TCP *:mysql (LISTEN)
yeonsookim@gim-yeonsuui-MacBookPro spring-instagram-20th % lsof -i :3306
```
- kill -9 명령어로 mysqld 프로세스를 종료했지만, 자동으로 재시작되는 문제 발생 
- ` sudo pkill -f mysqld` mysql 프로세스 강제 종료 후, 시스템이 더 이상 mysqld 재시작하지 않도록 하는 명령어. 

#### 스프링부트 빌드 시 sql 에러 
- 호스트네임은 컨테이너명으로 할 것. (mysql:3306)
```dockerfile
DB_URL=jdbc:mysql://mysql:3306/INSTAGRAMdb 
```
- docker compose 파일
```dockerfile
      MYSQL_DATABASE: INSTAGRAMdb
      MYSQL_ROOT_HOST: '%'
```
- 환경변수 의존성 추가
- dockerfile `COPY .env ./ ` 추가 
- docker compose 실행: `docker-compose -f docker-compose.yml up --build`
- 상호작용하는 이미지들이 하나의 네트워크 상에 있어야함
- MySQL 컨테이너 설정이 완료되고나서 Spring Boot 컨테이너가 실행되어야 DB 커넥션 에러가 발생하지 않는다. depends_on 명령어는 시작 순서만 db가 먼저 되도록 보장해주지, MySQL 설정이 끝나고나서 spring boot 컨테이너가 실행되는지는 보장해주진 않아서 DB 커넥션 에러가 발생할 수 있다.
- `restart: always` : 항상 설정해둘 것. 서버 터졌을 때 다시 시작해주도록.
- health check 를 통해 mysql 서버에 대한 상태 체크를 해야 함. (dockerfile, docker compose 둘 다 헬스체크 수정해야 함.)
- 
- 만약 빌드 이후 로컬에 변경사항이 발생했다면, `./gradlew build` 다시 할 필요 없이, `docker-compose up --build` 이미지만 재빌드해주면 된다. 

- 실행 중인 컨테이너 확인 
```dockerfile
yeonsookim@gim-yeonsuui-MacBookPro spring-instagram-20th % docker ps

CONTAINER ID   IMAGE                               COMMAND                  CREATED          STATUS                    PORTS                               NAMES
85015ba05038   spring-instagram-20th-application   "java -jar /app.jar"     3 minutes ago    Up 3 minutes              0.0.0.0:8080->8080/tcp              instagram
2eba64752c93   mysql:8.0                           "docker-entrypoint.s…"   19 minutes ago   Up 19 minutes (healthy)   0.0.0.0:3306->3306/tcp, 33060/tcp   mysql

```

#### image layer
- image 구성하는 파일 전체를 수정하는 것이 아닌 마지막으로 수정된 layer만 변경
- 캐시에 저장된다? 
#### 트러블 슈팅 해결법 
1. 디비 컨테이너 생성 
- mysql 도커 이미지 생성 및 실행. 이미지 만들 필요 없이 docker hub에서 가져올 것.
- instagram 이미지, mysql 이미지 둘 다 생성 및 실행 해야 함. 
- .yml 파일 수정: URL 호스트명을 컨테이너 이름으로 바꾸기. 
- 두 컨테이너를 연결해주어야 함. 
- .yml 못 읽는 것 같으면 .env 파일로 환경변수 넣어줄 것? 
- 이미지 여러 개 하기 귀찮으면 docker compose

## 배포 
![1*k1HJRbSK-r0hoXxuEjjVLw](https://github.com/user-attachments/assets/aeecca81-708b-472f-88e5-5c18ad811f39)

## Amazon EC2 
#### 보안그룹 
- 보안그룹:가상방화벽 역할을 하여 인스턴스로의 트래픽을 제어. 
- 인바운드와 아웃바운드 규칙을 정의하여 특정 트래픽만을 허용하도록 설정할 수 있다
#### 키페어 
- AWS에서는 공개 키 인프라(PKI)를 사용하여 인스턴스에 로그인함.
- 키 페어는 사용자가 EC2 인스턴스의 암호화된 로그인 정보를 안전하게 전달할 수 있는 방법을 제공
#### Elastic IP
- 인스턴스 재시작 시 IP 주소가 바뀌는데, 이를 고정하기 위해 Elastic IP를 사용. 

## RDS
### ECS 태스크 인스턴스와 AWS RDS의 데이터베이스를 연결
- 데이터베이스 접속 정보는 AWS 시크릿 매니저(Secrets Manager)에서 관리
- RDB는 자동 백업, 모니터링, 다중 AZ등 다양한 부가 기능을 제공하기 때문에 주로 사용한다. 
  물론 EC2에 백엔드 서버와 MySQL을 같이 설치해서 사용하면, 비용 절감 등의 장점도 있다.
- **하지만, 백엔드 서버의 장애로 서버가 다운되었을 때 DB도 같이 날아가는 불상사를 겪을 수 있다.**
  현업에서도 EC2와 RDS를 분리해서 인프라를 구성하는 경우가 대부분이라 한다.

### 새로운 데이터베이스 환경변수로 변경
- 데이터베이스를 생성할 때 함께 만들어진 시큐리티 그룹의 인바운드 규칙(inbound rule)을 변경해야 한다. 데이터베이스로 접속하는 ECS 태스크들은 전부 VPC 네트워크 내부에 위치하기 때문에 VPC 네트워크에 속한 IP 주소에서 3306 포트로 접근하는 트래픽을 허용해야 한다.
- DATABASE_URL 을 `jdbc:mysql://<RDS 엔드포인트>:3306/INSTAGRAMdb` 로 바꾸어야 할 것.
- 또한 username과 password도 rds의 것으로 바꾸어야 함.
- 참고로, rds 생성 시 secret manager 를 사용하면 username과 password 자동 생성 가능하고 ecs 에서 가져오기도 편리하나, 과금 이슈로 사용하진 않았음.
```dockerfile
DB_PASSWORD=rladustn0417!
DB_URL=jdbc:mysql://ceos-prac-database.cn8uymykiw76.ap-northeast-2.rds.amazonaws.com:3306/INSTAGRAMdb
DB_USERNAME=admin
DB_DRIVER_CLASS=com.mysql.cj.jdbc.Driver
```
### 데이터베이스 생성 
- **RDS 에 접속하여 INSTAGRAMdb 데이터베이스를 미리 생성해두어야 한다.**
- **로컬 mysql workbench 에서 새로운 connection 을 셋업하면 되는데, 이때 호스트네임은 RDS의 엔드포인트이다.**

### 궁금한 점 
- 데이터베이스를 로컬에서 생성해주는 방법밖에 없나? 
- EC2 에 mysql 을 설치한 후에 RDS 에서 접근하는 게 더 나은 방법인가?

## ECR registry 
- 컨테이너 이미지를 손쉽게 저장, 공유 및 배포할 수 있는 완전관리형 Docker 컨테이너 레지스트리
#### 환경변수 포함하고 플랫폼도 변경하여 도커 이미지 빌드
```dockerfile
docker build --platform linux/amd64 --build-arg DB_URL=jdbc:mysql://ceos-prac-database.cn8uymykiw76.ap-northeast-2.rds.amazonaws.com:3306/INSTAGRAMdb --build-arg DB_USERNAME=admin --build-arg DB_PASSWORD=rladustn0417! --build-arg DB_DRIVER_CLASS=com.mysql.cj.jdbc.Driver -t your-image-name .
```
#### 이미지 태그 추가 
```dockerfile
docker tag [이미지 이름]  905418373142.dkr.ecr.ap-northeast-2.amazonaws.com/ceos-registry:latest
```
#### 리포지토리에 푸시 
```dockerfile
docker push 905418373142.dkr.ecr.ap-northeast-2.amazonaws.com/ceos-registry:latest
```
#### 궁금한 점 
- EC2 를 사용해서 ECR에 접근하는 게 맞는 건가? 
- EC2 사용 안하면 환경변수 파일을 따로 못 만들고 도커 명령에서 -e 옵션을 주어야만 하는 것 같다. 


## ECS 
### ECS Fargate 
-  별도로 인스턴스를 생성 관리하지 않고, 완전한 매니지드 서비스의 형태로 도커 컨테이너를 실행시킬 수 있는 아마존의 서비리스 컨테이너 상품이다.
- Docker 이미지가 리파지터리에 푸시되어 있다면, **클러스터 → 작업 정의 → 서비스**의 순서로 생성하여 완전히 24시간 서비스 가능한 애플리케이션을 기동할 수 있다.
#### Task
Task는 ECS에서 컨테이너를 실행하는 최소 단위라고 이해할 수 있습니다. 컨테이너를 ECS 상에서 실행시키기 위해서는 Task 안에서 정의되어야하며, 하나의 Task는 하나 이상의 컨테이너를 포함하고 있습니다.
#### Task Definition
위에서 설명한 Task를 만들어내기 위한 것으로 Task Definition은 설명서, Task는 Task Definition에 대한 인스턴스로 볼 수 있습니다. Task와 Task Definition의 관계를 Docker의 Docker container와 Docker image와 비슷한 개념으로 볼 수 있습니다. Task Definition을 통해, Task가 실행될 때의 CPU, Memory 성능, 실행할 컨테이너에 대한 image 정의 등을 설정할 수 있습니다.
#### Service
Service는 Task Defintion과 1:1 관계로 하나의 Service에는 하나의 Task Definition을 연결할 수 있습니다. 이때, 하나의 ECS cluster에 몇 개의 해당 Task Definition을 바탕으로 한 Task를 생성할 것인지를 설정할 수 있습니다. 예를 들어, Service를 생성하여 2개의 Task를 항상 띄워놓는 설정을 했을 때, ECS cluster 안에는 항상 2개의 Task가 띄워져 있도록 스케줄링 됩니다.
#### Cluster
Cluster는 내부에 여러 Cluster instance가 실행될 수 있는 가상의 공간이며, 같은 Cluster 내부의 태스크와 서비스는 같은 네트워킹 정보와 인프라 설정 (fargate or EC2)를 가지게 됩니다.

### 컨테이너 구성하기
- 테스트 정의에서 컨테이너를 띄울 수 있는데, 컨테이너의 이미지 URL에 ECR에 아까 올린 이미지 repository의 URL과 태그를 복사해 붙여넣는다. (ex. imageurl:{tag}) 
- 이때 tag 자리에 latest를 적으면, 해당 url을 가진 repository에서 가장 최근에 올린 이미지를 자동으로 가져오게 됩니다.
- **포트 매핑에는 image에 올린 어플리케이션 서버의 포트 번호(8080)를 입력해 포트포워딩을 해준다.**
- 환경변수의 키값을 추가한다. 

### ECS Fargate vs ECS EC2
#### Launch Type 중 Fargate 로 배포한 이유는? 
- CloudWatch 이용이 편리해서
<img width="836" alt="스크린샷 2024-11-25 오후 6 41 35" src="https://github.com/user-attachments/assets/87772034-3272-4a0c-bc75-899368ceb1b4">

### API 테스트
- 실행 성공한 테스크의 퍼블릭IP `43.203.222.239`
- `http://<퍼블릭_IP>:<포트>/<엔드포인트> Postman 테스트`
- 오잉 왜 connect ETIMEDOUT 43.203.222.239:8080 이런 에러가? 심지어 로컬에서도 접속이 안된다.
- 테스크가 연결된 서브넷의 VPC의 보안그룹에서 외부 모든 트래픽의 8080 포트 허용해줌. 이전에는 VPC 내부 트래픽만 허용하고 있었음.
- 회원가입 API 테스트 성공.
  <img width="871" alt="스크린샷 2024-11-24 오전 1 07 12" src="https://github.com/user-attachments/assets/ce4522bf-5b1e-4f55-bbd8-7bb8e3345186">


## 트러블슈팅 
### ECS Service 생성 에러 로그 

```dockerfile
Caused by: org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'customUserDetailService' defined in URL [jar:nested:/app.jar/!BOOT-INF/classes/!/com/ceos20/instagram/auth/service/CustomUserDetailService.class]: Unsatisfied dependency expressed through constructor parameter 0: Error creating bean with name 'userRepository' defined in com.ceos20.instagram.user.repository.UserRepository defined in @EnableJpaRepositories declared on JpaRepositoriesRegistrar.EnableJpaRepositoriesConfiguration: Cannot resolve reference to bean 'jpaSharedEM_entityManagerFactory' while setting bean property 'entityManager'
```


jar 파일 확인 
```dockerfile
ls build/libs/

instagram-0.0.1-SNAPSHOT-plain.jar      instagram-0.0.1-SNAPSHOT.jar

```
jar 파일 안에 mysql 드라이버 설치 확인 
```
jar tf build/libs/instagram-0.0.1-SNAPSHOT.jar | grep mysql 

BOOT-INF/lib/mysql-connector-j-8.3.0.jar
```

- mysql 드라이버 설치 잘 되어있음. 아 그러면 RDS 연결을 안해줘서 그런가? 
- env.properties 로 리팩터링. 
- yml 파일에서 환경변수 잘 인식되도록 해줌. 
```dockerfile
@Configuration
@PropertySources({
        @PropertySource("classpath:properties/env.properties") // env.properties 파일 소스 등록
})
public class PropertyConfig {
}
```
- 도커 파일 변경
- ARG를 사용하여 빌드 시에 값을 전달하고, 이를 ENV로 설정하여 컨테이너 실행 시에도 환경 변수로 사용
- 이렇게 설정하거나 빌드시 -e 옵션으로 환경변수 주입할 수도 있음. 
```dockerfile
# 환경 변수를 ARG로 정의하고, 이를 ENV로 전달
ARG DB_URL
ARG DB_USERNAME
ARG DB_PASSWORD
ARG DB_DRIVER_CLASS


ENV DB_URL=$DB_URL
ENV DB_USERNAME=$DB_USERNAME
ENV DB_PASSWORD=$DB_PASSWORD
ENV DB_DRIVER_CLASS=$DB_DRIVER_CLASS


COPY src/main/resources/properties/env.properties ./
```
#### 현재 yml 파일 
- url 연결이 도커 mysql 컨테이너명으로 되어있음. 
```dockerfile
spring:
  datasource:
    url: jdbc:mysql://mysql:3306/INSTAGRAMdb
    username: root
    password: juanxiu14!
    driver-class-name: com.mysql.cj.jdbc.Driver
```

#### 과정 정리 
- RDS에 디비 생성 완료 
- 도커 이미지에 환경변수, 플랫폼 변경해서 ecr 푸시 완료 
- ecs 테스크 생성 시 환경변수 전달.
- ECS Service 배포 성공 

