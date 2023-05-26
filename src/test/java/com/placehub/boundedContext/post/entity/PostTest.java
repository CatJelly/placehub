package com.placehub.boundedContext.post.entity;

import com.placehub.boundedContext.post.repository.PostRepository;
import com.placehub.boundedContext.post.service.PostService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
//@ActiveProfiles("test")
class PostTest {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostService postService;
    private static Post expected;

    @BeforeAll
    private static void makeExpectedPost() {
        expected = Post.builder()
                .member(1L)
                .place(1L)
                .content("content")
                .openToPublic(true)
//                        .deleteDate(now)
                .build();
    }

//    @Test
//    @DisplayName("Post 엔티티 CRUD")
////    @Transactional(propagation = Propagation.NOT_SUPPORTED)
//    void postCrudTest() {
//        // Create
//        postRepository.save(expected);
//        System.out.println(postRepository.findById(1L) + "===================");
//        // Read
//        assertThat(postRepository.findById(expected.getId()).get().toString()).isEqualTo(expected.toString());
//
////         Update
//        expected = expected.toBuilder()
//                .content("ReplacedContent")
//                .build();
//
//        postRepository.save(expected);
//        assertThat(postRepository.findById(1L).get().getContent()).isEqualTo("ReplacedContent");
//
//        // Delete
//        postRepository.deleteById(1L);
//        assertThat(postRepository.findById(1L).isPresent()).isFalse();
//    }

    @Test
    @DisplayName("Post 엔티티 Create Service")
    void postCrudServiceTest() {
        long savedId = postService.createPost(1L, 1L, "content", true);

        assertThat(postRepository.findById(savedId).get().toString()).isEqualTo(expected.toString());

    }

    @Test
    @DisplayName("장소에 따른 게시글 얻기")
    void getPostsByPlaceTest() {

        postRepository.save(expected);
        postService.createPost(1, 2, "No.2", false);
        postService.createPost(1, 1, "No.3", true);
        System.out.println(postRepository.findAll() + "========================");
        System.out.println(postRepository.findById(3L).get() + "========================");
        List<Post> posts = postService.getPostsByPlace(1L);

        assertThat(posts.get(0).toString()).isEqualTo(postRepository.findById(3L).get().toString());
        assertThat(posts.get(1).toString()).isEqualTo(postRepository.findById(1L).get().toString());
    }
}