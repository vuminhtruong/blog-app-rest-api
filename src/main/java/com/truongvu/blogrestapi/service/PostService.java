package com.truongvu.blogrestapi.service;

import com.truongvu.blogrestapi.dto.PostDTO;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.QueryHints;
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

    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    List<PostDTO> getAllPostsWithoutPageSize();
    List<PostDTO> getPostsByCategoryWithPageSize(long categoryId, int pageNo, int pageSize, String sortBy);
}
