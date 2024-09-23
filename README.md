# spring-instagram-20th
CEOS 20th BE study - instagram clone coding

![세오스_인스타그램](https://github.com/user-attachments/assets/febf859f-3fbd-4009-9248-e408e1f4b0ac)

## 인스타그램 서비스 소개 
- 사용자가 글과 사진을 업로드하고, 게시물에 대한 댓글과 대댓글을 작성하거나 좋아요를 표시할 수 있는 SNS 서비스입니다. 그리고 유저 간에 1: 1 메시지 기능을 사용할 수 있습니다. 


## ERD 설명 
#### 1. User (사용자 테이블)
모든 활동(게시물 작성, 댓글 작성, 좋아요 클릭 등)은 사용자의 행위로 기록됩니다.
- UserID: 각 사용자를 식별하는 고유 ID. 
- UserName: 사용자의 이름(계정 이름).
- Password: 사용자 로그인에 필요한 비밀번호.
#### 2. Post (게시글 테이블)
사용자가 작성한 게시글을 저장합니다. 각 게시글은 사용자에 의해 작성되고, 다른 사용자가 좋아요나 댓글을 남길 수 있습니다.
- PostID: 게시글을 식별하는 고유 ID.
- Caption: 게시글의 내용.
- ImageURL: 게시글에 포함된 이미지 URL.
- CreatedAt: 게시글이 작성된 날짜와 시간.
- UserID: 게시글을 작성한 사용자의 ID.
#### 3. Comment (댓글 테이블)
게시글에 달린 댓글을 저장하며, 각 댓글은 특정 게시글에 속하고 작성자와 연결됩니다. 또한, 대댓글을 지원하기 위해 부모 댓글 ID도 저장됩니다.
- CommentID: 댓글을 식별하는 고유 ID.
- Content: 댓글 내용.
- CreatedAt: 댓글이 작성된 날짜와 시간.
- ParentCommentID: 대댓글인 경우, 부모 댓글의 ID.
- PostID: 댓글이 달린 게시글의 ID.
- UserID: 댓글을 작성한 사용자의 ID.
#### 4. Like (좋아요 테이블)
사용자가 게시글에 대해 남긴 좋아요를 저장합니다. 좋아요는 게시글과 사용자 간의 관계를 저장하는 테이블입니다.
- LikeID: 좋아요를 식별하는 고유 ID.
- CreatedAt: 좋아요를 누른 날짜와 시간.
- PostID: 좋아요를 누른 게시글의 ID.
- UserID: 좋아요를 누른 사용자의 ID.
#### 5. DM (메시지 테이블)
사용자 간의 사적인 대화인 DM(Direct Message)을 저장합니다. 각 메시지는 송신자와 수신자가 명시되며, 이들이 나눈 대화를 저장합니다.
- DMID: 메시지를 식별하는 고유 ID.
- Message: 메시지 내용.
- SenderID: 메시지를 보낸 사용자의 ID.
- ReceiverID: 메시지를 받은 사용자의 ID.
## 요구사항 명세서 
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

