//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ceos20.instagram;

import com.ceos20.instagram.Domain.Post;
import com.ceos20.instagram.Domain.User;
import com.ceos20.instagram.Repository.PostRepository;
import com.ceos20.instagram.Repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@AutoConfigureTestDatabase(
        replace = Replace.NONE
)
@Transactional
class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    PostRepositoryTest() {
    }

    @DirtiesContext
    @Test
    void saveAndQueryPost_withUser() {
        User user = new User("testUser1", "password1");
        this.userRepository.save(user);
        LocalDateTime date = LocalDateTime.of(2024, 9, 11, 0, 0, 0);
        Post post1 = new Post("Post Caption 1", "ImageURL1", date, user);
        Post post2 = new Post("Post Caption 2", "ImageURL2", date, user);
        Post post3 = new Post("Post Caption 3", "ImageURL3", date, user);
        this.postRepository.deleteAll();
        this.postRepository.save(post1);
        this.postRepository.save(post2);
        this.postRepository.save(post3);
        List<Post> posts = this.postRepository.findAll();
        Assertions.assertThat(posts).hasSize(3);
        Assertions.assertThat(posts).extracting(Post::getCaption).containsExactlyInAnyOrder(new String[]{"Post Caption 1", "Post Caption 2", "Post Caption 3"});
        Assertions.assertThat(posts).extracting((post) -> {
            return post.getUser().getUserName();
        }).containsOnly(new String[]{"testUser1"});
    }
}
