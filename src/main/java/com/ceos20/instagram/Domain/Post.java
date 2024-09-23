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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

import lombok.Builder;
import lombok.Generated;
import lombok.Getter;

@Entity
@Table(name = "post")
@Getter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long  id; // 포스트 고유 아이디는 필드값 받는 생성자에 들어가지 않음.
    // 포스트 아이디를 uuid로 할 것?

    private String caption;
    private String imageUrl; // 이미지 링크 필요???
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id") // 하나의 유저가 여러 개의 게시글을 올릴 수 있다.
    private User user;



    /*
    빌더패턴은 생성자의 안정성(생성자의 장점)과 자바빈즈의 가독성(setter의 장점)을 다 가진 패턴이라 볼 수 있다.
    필드를 초기화 하는 코드를 보면 매개변수가 무엇을 의미하는 지 알 수 있기 때문에 생성자 생성보다 가독성이 좋아졌다.

     */

    @Builder
    public Post(String caption, String imageUrl, LocalDateTime createdAt, User user) {
        this.caption = caption;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.user = user;
    }

    public Post(){
        //기본 생성자.
    }
    /*
    근데 여기에서
    public boolean isSameMember(final Member member){
		return this.getId().equals(member.getId());
	}
	public void validOwner(final Member member){
		if(this.member.isSameMember(member)){
		    throw new EventException(EvetExceptionType.PARTICIPANT_NOT_ONWER);
		}
		이런 메서드 작성하고 이걸 테스트해도 되나?
     */

}
