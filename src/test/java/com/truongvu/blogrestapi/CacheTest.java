package com.truongvu.blogrestapi;

import com.truongvu.blogrestapi.dto.PostDTO;
import com.truongvu.blogrestapi.entity.Post;
import com.truongvu.blogrestapi.repository.PostRepository;
import com.truongvu.blogrestapi.service.PostService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
public class CacheTest {
    @Autowired
    PostRepository postRepository;

    private Post readPost(long id) {
        log.info(String.format("query post #%d", id));
        Optional<Post> post = postRepository.findById(id);
        assertTrue(post.isPresent());
        log.info("post found");
        return post.get();
    }

    private List<Post> getAllPost() {
        log.info("Get All Post");
        List<Post> list = postRepository.findAll();
        assertFalse(list.isEmpty());
        log.info("list found");
        return list;
    }

    @Test
    @Transactional
    void testReadPost() {
        Post post = readPost(1);
        assertEquals("Giới thiệu về PHP cho Người Mới Bắt Đầu", post.getTitle());
    }

    @Test
    @Transactional
    void testReadAllPost() {
        assertEquals(22, getAllPost().size());
    }
}
