package com.truongvu.blogrestapi.controller;

import com.truongvu.blogrestapi.dto.CommentDTO;
import com.truongvu.blogrestapi.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDTO> createComment(@Valid @PathVariable(name = "postId") long postId, @RequestBody CommentDTO commentDTO) {
        return new ResponseEntity<>(commentService.createComment(postId,commentDTO), HttpStatus.CREATED);
    }

    @GetMapping("/posts/{postId}/comments")
    public List<CommentDTO> getCommentsByPostId(@PathVariable(name = "postId") long postId) {
        return commentService.getAllCommentsByPost(postId);
    }

    @GetMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable(name = "postId") long postId,@PathVariable(name = "commentId") long commentId) {
        return new ResponseEntity<>(commentService.getCommentByEmail(postId,commentId),HttpStatus.OK);
    }
}
