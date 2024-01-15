package com.truongvu.blogrestapi.controller;

import com.truongvu.blogrestapi.dto.PostDTO;
import com.truongvu.blogrestapi.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO) {
        return new ResponseEntity<>(postService.createPost(postDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public List<PostDTO> getAllPosts(
            @RequestParam(value = "pageNo",defaultValue = "0",required = false) int pageNo,
            @RequestParam(value = "pageSize",defaultValue = "5",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "id",required = false) String sortBy
    ) {
        return postService.getAllPosts(pageNo,pageSize,sortBy);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable(name = "id") long id) {
        return new ResponseEntity<>(postService.findById(id),HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> updatePost(@RequestBody PostDTO postDTO,@PathVariable(name = "id") long id) {
        return new ResponseEntity<>(postService.updatePost(postDTO,id),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") long id) {
        postService.deletePost(id);
        return new ResponseEntity<>("Post with id= " + id + " deleted successfully",HttpStatus.OK);
    }
}
