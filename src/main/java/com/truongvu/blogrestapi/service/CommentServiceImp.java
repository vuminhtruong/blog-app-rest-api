package com.truongvu.blogrestapi.service;

import com.truongvu.blogrestapi.dto.CommentDTO;
import com.truongvu.blogrestapi.entity.Comment;
import com.truongvu.blogrestapi.entity.Post;
import com.truongvu.blogrestapi.exception.BlogAPIException;
import com.truongvu.blogrestapi.exception.ResourceNotFoundException;
import com.truongvu.blogrestapi.repository.CommentRepository;
import com.truongvu.blogrestapi.repository.PostRepository;
//import com.truongvu.blogrestapi.service.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImp implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
//    private final RedisService redisService;

    @Override
    public CommentDTO createComment(long postId, CommentDTO commentDTO) {
        return null;
//        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post","id",postId));
//        Comment comment = mapToEntity(commentDTO);
//        comment.setPost(post);
//        redisService.delete("post" + postId);
//
//        Comment savedComment = commentRepository.save(comment);
//        return mapToDTO(savedComment);
    }

    @Override
    public List<CommentDTO> getAllCommentsByPost(long postId) {
        List<Comment> commentList = commentRepository.findByPostId(postId);

        return commentList.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public CommentDTO getCommentByEmail(long postId, long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post","id",postId));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment","id",commentId));

        if(comment.getPost().getId() != post.getId()) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
        }

        return mapToDTO(comment);
    }

    private CommentDTO mapToDTO(Comment comment) {
        return modelMapper.map(comment,CommentDTO.class);
    }

    private Comment mapToEntity(CommentDTO commentDTO) {
        return modelMapper.map(commentDTO,Comment.class);
    }
}
