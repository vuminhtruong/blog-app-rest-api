package com.truongvu.blogrestapi.utils;

import com.truongvu.blogrestapi.dto.CommentDTO;
import com.truongvu.blogrestapi.dto.ImageDTO;
import com.truongvu.blogrestapi.dto.PostDTO;
import com.truongvu.blogrestapi.entity.Comment;
import com.truongvu.blogrestapi.entity.Image;
import com.truongvu.blogrestapi.entity.Post;

import java.util.Base64;
import java.util.stream.Collectors;

public class PostMapping {
    public static PostDTO mapToDTO(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setContent(post.getContent());
        postDTO.setDescription(post.getDescription());
        postDTO.setCategoryId(post.getCategory().getId());
        if (post.getComments() != null) {
            postDTO.setComments(post.getComments().stream().map(PostMapping::convertComment).collect(Collectors.toSet()));
        }
        postDTO.setImageDTO(convertImage(post.getImage()));

//        return modelMapper.map(post, PostDTO.class);
        return postDTO;
    }

    public static Post mapToEntity(PostDTO postDTO) {
        Post post = new Post();
        post.setId(postDTO.getId());
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setDescription(postDTO.getDescription());
//        post.setImage(covertImageDTO(postDTO.getImageDTO()));

//        return modelMapper.map(postDTO, Post.class);
        return post;
    }

    public static CommentDTO convertComment(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setName(comment.getName());
        commentDTO.setEmail(comment.getEmail());
        commentDTO.setBody(comment.getBody());

        return commentDTO;
    }

    public static ImageDTO convertImage(Image image) {
        ImageDTO imageDTO = new ImageDTO();
        if (image != null) {
            imageDTO.setId(image.getId());
            imageDTO.setName(image.getName());
            imageDTO.setType(image.getType());
            imageDTO.setData(Base64.getEncoder().encodeToString(image.getData()));
            imageDTO.setCreateAt(image.getCreateAt());
        }

        return imageDTO;
    }

    public static Image covertImageDTO(ImageDTO imageDTO) {
        Image image = new Image();
        if (imageDTO != null) {
            image.setId(imageDTO.getId());
            image.setName(imageDTO.getName());
            image.setType(imageDTO.getType());
            image.setData(Base64.getDecoder().decode(imageDTO.getData()));
            image.setCreateAt(imageDTO.getCreateAt());
        }

        return image;
    }
}
