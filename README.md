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


# CRUD API 과제 

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
```dockerfile
@WebMvcTest(PostController.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @MockBean
    private PostLikeService postLikeService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PostRepository postRepository; // 여기를 MockBean으로 등록해야 합니다.

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock User 객체 생성
        User mockUser = new User();
        mockUser.setId(1L); // 적절한 ID 값
        mockUser.setUsername("testUser"); // 사용자의 username을 설정
        mockUser.setEmail("test@example.com"); // 이메일도 설정 (필요시)

        // userRepository의 findByUsername 메소드가 호출될 때 mockUser를 반환하도록 설정
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(mockUser));

        // Save 메소드 Mock 설정
        when(userRepository.save(any(User.class))).thenReturn(mockUser); // save 메소드가 호출될 때 mockUser 반환

        // Post 객체 생성
        Post newPost = new Post(); // 새로운 Post 객체 생성
        newPost.setId(1L); // ID 설정 (적절한 값)
        newPost.setCaption("Sample Caption");
        newPost.setImageUrl("SampleImageUrl.jpg");
        newPost.setCreatedAt(LocalDateTime.now());

        // PostRepository의 save 메소드 mocking
        when(postRepository.save(any(Post.class))).thenReturn(newPost); // save 메소드가 호출될 때 newPost 반환
    }


    @Test
    public void testCreatePost() throws Exception {
        // Given
        PostCreateReqDto request = new PostCreateReqDto("Sample Caption", "SampleImageUrl.jpg");
        String username = "testUser"; // 대소문자 일치

        // When
        doNothing().when(postService).createPost(any(PostCreateReqDto.class), eq(username)); // createPost 메서드가 호출될 때 아무것도 하지 않도록 설정

        // Act and Assert
        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"caption\": \"Sample Caption\", \"imageUrl\": \"SampleImageUrl.jpg\"}")
                        .param("username", username)) // param에서 username을 지정
                .andExpect(status().isCreated());
    }


}
```

### 게시물 생성 API 
URI: `api/posts`
```dockerfile
{
  "caption": "Sample Caption",
  "imageUrl": "SampleImageUrl.jpg"
}
```

404 NOT FOUND
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


