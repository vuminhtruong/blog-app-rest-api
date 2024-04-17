package com.truongvu.blogrestapi.service;

import com.truongvu.blogrestapi.dto.PostDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface PostService {
    PostDTO createPost(PostDTO postDTO);
    List<PostDTO> getAllPosts(int pageNo,int pageSize,String sortBy);
    PostDTO findById(Long id);
    PostDTO updatePost(PostDTO newPostDTO, long postId);
    void deletePost(Long id);
    List<PostDTO> getPostsByCategory(long categoryId);

    List<PostDTO> getAllPostsWithoutPageSize();
    List<PostDTO> getPostsByCategoryWithPageSize(long categoryId, int pageNo, int pageSize, String sortBy);
}
