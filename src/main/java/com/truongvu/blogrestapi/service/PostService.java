package com.truongvu.blogrestapi.service;

import com.truongvu.blogrestapi.dto.PostDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface PostService {
    PostDTO createPost(PostDTO postDTO);
    List<PostDTO> getAllPosts();
    PostDTO findById(Long id);
    PostDTO updatePost(PostDTO newPostDTO,long id);
    void deletePost(Long id);
}
