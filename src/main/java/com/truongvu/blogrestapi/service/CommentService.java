package com.truongvu.blogrestapi.service;

import com.truongvu.blogrestapi.dto.CommentDTO;

import java.util.List;

public interface CommentService {
    CommentDTO createComment(long postId,CommentDTO commentDTO);
    List<CommentDTO> getAllCommentsByPost(long postId);
    CommentDTO getCommentByEmail(long postId,long commentId);
}
