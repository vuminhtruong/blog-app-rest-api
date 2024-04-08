package com.truongvu.blogrestapi.service;

import com.truongvu.blogrestapi.dto.CommentDTO;
import com.truongvu.blogrestapi.dto.ImageDTO;
import com.truongvu.blogrestapi.dto.PostDTO;
import com.truongvu.blogrestapi.entity.Category;
import com.truongvu.blogrestapi.entity.Comment;
import com.truongvu.blogrestapi.entity.Image;
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

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImp implements PostService {
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public PostDTO createPost(PostDTO postDTO) {
        Category category = categoryRepository.findById(postDTO.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Category", "id", postDTO.getCategoryId()));

        Post post = mapToEntity(postDTO);
        post.setCategory(category);
        Post savedPost = postRepository.save(post);

        return mapToDTO(savedPost);
    }

    @Override
    public List<PostDTO> getAllPosts(int pageNo, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Post> page = postRepository.findAll(pageable);

        List<Post> listPost = page.getContent();

        return listPost.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public List<PostDTO> getAllPostsWithoutPageSize() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public List<PostDTO> getPostsByCategoryWithPageSize(long categoryId, int pageNo, int pageSize, String sortBy) {
        List<Post> posts = postRepository.findByCategoryId(categoryId);

        double totalPage = Math.ceil((double) posts.size() / pageSize);
        System.out.println("Total page: " + totalPage);

        List<List<Post>> listPostsArray = this.partitionList(posts, pageSize).stream().toList();
        System.out.println("Partition list: " + listPostsArray);

        List<Post> listPost = listPostsArray.get(pageNo);
        if(sortBy.equals("title")) {
            listPost.sort(Comparator.comparing(Post::getTitle));
        } else if (sortBy.equals("id")) {
            listPost.sort(Comparator.comparing(Post::getId));
        } else {
            listPost.sort(Comparator.comparing(Post::getContent));
        }
        System.out.println("List after sorting: " + listPost);

        return  listPost.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public PostDTO findById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        return mapToDTO(post);
    }

    @Override
    public PostDTO updatePost(PostDTO newPostDTO, long id) {
        Category category = categoryRepository.findById(newPostDTO.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Category", "id", newPostDTO.getCategoryId()));
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        post.setTitle(newPostDTO.getTitle());
        post.setDescription(newPostDTO.getDescription());
        post.setContent(newPostDTO.getContent());
        post.setCategory(category);

        Post updatedPost = postRepository.save(post);

        return mapToDTO(updatedPost);
    }

    @Override
    public void deletePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        postRepository.delete(post);
    }

    @Override
    public List<PostDTO> getPostsByCategory(long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        List<Post> posts = postRepository.findByCategoryId(categoryId);
        return posts.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private PostDTO mapToDTO(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setContent(post.getContent());
        postDTO.setDescription(post.getDescription());
        postDTO.setCategoryId(post.getCategory().getId());
        postDTO.setComments(post.getComments().stream().map(this::convertComment).collect(Collectors.toSet()));
        postDTO.setImageDTO(convertImage(post.getImage()));

//        return modelMapper.map(post, PostDTO.class);
        return postDTO;
    }

    private Post mapToEntity(PostDTO postDTO) {
        Category category = categoryRepository.findById(postDTO.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Category", "id", postDTO.getCategoryId()));

        Post post = new Post();
        post.setId(postDTO.getId());
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setDescription(postDTO.getDescription());
        post.setCategory(category);
        post.setImage(covertImageDTO(postDTO.getImageDTO()));

//        return modelMapper.map(postDTO, Post.class);
        return post;
    }

    private <T> Collection<List<T>> partitionList(List<T> inputList, int pageSize) {
        final AtomicInteger counter = new AtomicInteger(0);
        return inputList.stream()
                .collect(Collectors.groupingBy(value -> counter.getAndIncrement()/pageSize))
                .values();
    }

    private CommentDTO convertComment(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setName(comment.getName());
        commentDTO.setEmail(comment.getEmail());
        commentDTO.setBody(comment.getBody());

        return commentDTO;
    }

    private ImageDTO convertImage(Image image) {
        ImageDTO imageDTO = new ImageDTO();
        if(image != null) {
            imageDTO.setId(image.getId());
            imageDTO.setName(image.getName());
            imageDTO.setType(image.getType());
            imageDTO.setData(Base64.getEncoder().encodeToString(image.getData()));
            imageDTO.setCreateAt(image.getCreateAt());
        }

        return imageDTO;
    }

    private Image covertImageDTO(ImageDTO imageDTO) {
        Image image = new Image();
        if(imageDTO != null) {
            image.setId(imageDTO.getId());
            image.setName(imageDTO.getName());
            image.setType(imageDTO.getType());
            image.setData(Base64.getDecoder().decode(imageDTO.getData()));
            image.setCreateAt(imageDTO.getCreateAt());
        }

        return image;
    }

}
