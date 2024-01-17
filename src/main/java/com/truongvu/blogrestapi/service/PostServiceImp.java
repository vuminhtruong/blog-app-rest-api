package com.truongvu.blogrestapi.service;

import com.truongvu.blogrestapi.dto.PostDTO;
import com.truongvu.blogrestapi.entity.Category;
import com.truongvu.blogrestapi.entity.Post;
import com.truongvu.blogrestapi.exception.ResourceNotFoundException;
import com.truongvu.blogrestapi.repository.CategoryRepository;
import com.truongvu.blogrestapi.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImp implements PostService{
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public PostDTO createPost(PostDTO postDTO) {
        Category category = categoryRepository.findById(postDTO.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Category","id",postDTO.getCategoryId()));

        Post post = mapToEntity(postDTO);
        post.setCategory(category);
        Post savedPost = postRepository.save(post);

        return mapToDTO(savedPost);
    }

    @Override
    public List<PostDTO> getAllPosts(int pageNo,int pageSize,String sortBy) {
        Pageable pageable = PageRequest.of(pageNo,pageSize, Sort.by(sortBy));
        Page<Post> page = postRepository.findAll(pageable);

        List<Post> listPost = page.getContent();

        return listPost.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public PostDTO findById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post","id", id));

        return mapToDTO(post);
    }

    @Override
    public PostDTO updatePost(PostDTO newPostDTO, long id) {
        Category category = categoryRepository.findById(newPostDTO.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Category","id", newPostDTO.getCategoryId()));
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post","id", id));

        post.setTitle(newPostDTO.getTitle());
        post.setDescription(newPostDTO.getDescription());
        post.setContent(newPostDTO.getContent());
        post.setCategory(category);

        Post updatedPost = postRepository.save(post);

        return mapToDTO(updatedPost);
    }

    @Override
    public void deletePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post","id",id));
        postRepository.delete(post);
    }

    @Override
    public List<PostDTO> getPostsByCategory(long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category","id",categoryId));
        List<Post> posts = postRepository.findByCategoryId(categoryId);
        return posts.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private PostDTO mapToDTO(Post post) {
        return modelMapper.map(post,PostDTO.class);
    }

    private Post mapToEntity(PostDTO postDTO) {
        return modelMapper.map(postDTO,Post.class);
    }
}
