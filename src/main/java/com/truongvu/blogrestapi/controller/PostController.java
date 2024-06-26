package com.truongvu.blogrestapi.controller;

import com.truongvu.blogrestapi.dto.PostDTO;
import com.truongvu.blogrestapi.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PostController {
    private final PostService postService;
    private final RabbitTemplate rabbitTemplate;
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Value("${spring.rabbitmq.exchange}")
    String exchange;

    @Value("${spring.rabbitmq.routingkey}")
    String routingKey;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PostDTO> createPost(@Valid @RequestBody PostDTO postDTO) {
//        logger.info("Creating post with title: " + postDTO.getTitle());
        return new ResponseEntity<>(postService.createPost(postDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public List<PostDTO> getAllPosts(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy
    ) {
        logger.info(String.format("Getting all post with pageNo: %s, pageSize: %s, sortBy: %s", pageNo, pageSize, sortBy));
        return postService.getAllPosts(pageNo, pageSize, sortBy);
    }

    @GetMapping("/all-post")
    public List<PostDTO> getAllPostsWithoutPageSize() {
        logger.info("Getting all posts");
        return postService.getAllPostsWithoutPageSize();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable(name = "id") long id) {
        logger.info("Finding post with id = " + id);
        return new ResponseEntity<>(postService.findById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> updatePost(@Valid @RequestBody PostDTO postDTO, @PathVariable(name = "id") long id) {
        logger.info("Updating post with id = " + id);
        return new ResponseEntity<>(postService.updatePost(postDTO, id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") long id) {
        logger.info("Deleting post with id = " + id);
        postService.deletePost(id);
        return new ResponseEntity<>("Post with id= " + id + " deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<List<PostDTO>> getAllPostsByCategory(@PathVariable("id") long id) {
        logger.info("Getting all posts by category");
        return new ResponseEntity<>(postService.getPostsByCategory(id), HttpStatus.OK);
    }

    @GetMapping("/category/{id}/filter")
    public ResponseEntity<List<PostDTO>> getPostsByCategory(
            @PathVariable("id") long id,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy
    ) {
        logger.info("Getting posts by category and pagination");
        return new ResponseEntity<>(postService.getPostsByCategoryWithPageSize(id, pageNo, pageSize, sortBy), HttpStatus.OK);
    }
}
