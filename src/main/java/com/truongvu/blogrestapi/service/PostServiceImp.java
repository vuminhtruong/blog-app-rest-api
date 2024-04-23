package com.truongvu.blogrestapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.truongvu.blogrestapi.dto.CommentDTO;
import com.truongvu.blogrestapi.dto.ImageDTO;
import com.truongvu.blogrestapi.dto.PostDTO;
import com.truongvu.blogrestapi.entity.Category;
import com.truongvu.blogrestapi.entity.Comment;
import com.truongvu.blogrestapi.entity.Image;
import com.truongvu.blogrestapi.entity.Post;
import com.truongvu.blogrestapi.exception.BlogAPIException;
import com.truongvu.blogrestapi.exception.QueryException;
import com.truongvu.blogrestapi.exception.ResourceNotFoundException;
import com.truongvu.blogrestapi.repository.CategoryRepository;
import com.truongvu.blogrestapi.repository.CommentRepository;
import com.truongvu.blogrestapi.repository.PostRepository;
//import com.truongvu.blogrestapi.service.redis.RedisService;
import com.truongvu.blogrestapi.utils.PostMapping;
import com.truongvu.blogrestapi.validate.handler.ValidationHandler;
import com.truongvu.blogrestapi.validate.post.ValidatePostCategory;
import com.truongvu.blogrestapi.validate.post.ValidatePostImage;
import com.truongvu.blogrestapi.validate.post.ValidatePostLength;
import lombok.RequiredArgsConstructor;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.*;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.truongvu.blogrestapi.utils.PostMapping.mapToEntity;
import static com.truongvu.blogrestapi.utils.PostMapping.mapToDTO;

@Service
@EnableCaching
@RequiredArgsConstructor
public class PostServiceImp implements PostService {
    //    private final RedisService redisService;
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;
//    private final ObjectMapper objectMapper;


    private final ValidationHandler validationHandler = ValidationHandler.link(new ValidatePostLength(), new ValidatePostCategory(), new ValidatePostImage());


    @Transactional
    @Override
    @CacheEvict(value = "posts", allEntries = true, beforeInvocation = true)
    public PostDTO createPost(PostDTO postDTO) {
        Category category;
        try {
            long category_id = postDTO.getCategoryId();
            category = categoryRepository.findById(category_id).orElseThrow(() -> new ResourceNotFoundException("Category", "id", postDTO.getCategoryId()));
        } catch (Exception exception) {
            category = null;
        }
        Post post = mapToEntity(postDTO);
        post.setCategory(category);
        if (validationHandler.check(post) != null) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, validationHandler.check(post));
        }
        postRepository.insertNewPost(post);

        return postDTO;
    }

    @Override
    @Cacheable(value = "posts", key = "#pageNo + '::' + #pageSize + '::' + #sortBy")
    public List<PostDTO> getAllPosts(int pageNo, int pageSize, String sortBy) {
        System.out.println("All Posts Pagination Database: ");
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Post> page = postRepository.findAll(pageable);
        return page.getContent().stream().map(PostMapping::mapToDTO).collect(Collectors.toList());
//        String key = "posts:page:" + pageNo + ":size:" + pageSize + ":sortBy:" + sortBy;
//        String postByPagination = (String) redisService.get(key);
//        if (postByPagination == null) {
//            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
//            Page<Post> page = postRepository.findAll(pageable);
//            List<PostDTO> postDTOList = page.getContent().stream().map(PostMapping::mapToDTO).toList();
//            try {
//                postByPagination = objectMapper.writeValueAsString(postDTOList);
//                redisService.set(key, postByPagination);
//                redisService.setTimeToLive(key, 5);
//            } catch (JsonProcessingException exception) {
//                throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Error while setting value postPagination in Redis");
//            }
//            return postDTOList;
//        }
//
//        try {
//            return objectMapper.readValue(postByPagination, new TypeReference<List<PostDTO>>() {
//            });
//        } catch (JsonProcessingException e) {
//            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Error while reading value postPagination in Redis");
//        }
    }


    @Cacheable("posts")
    @Override
    public List<PostDTO> getAllPostsWithoutPageSize() {
        System.out.println("All Post - Database: ");
        return postRepository.findAllPost().stream().map(PostMapping::mapToDTO).collect(Collectors.toList());
//        String postsList_string = (String) redisService.get("posts");
//
//        if (postsList_string == null) {
//            List<PostDTO> postsDTO = postRepository.findAllPost().stream().map(PostMapping::mapToDTO).toList();
//            try {
//                postsList_string = objectMapper.writeValueAsString(postsDTO);
//                redisService.set("posts", postsList_string);
//                redisService.setTimeToLive("posts", 1);
//            } catch (JsonProcessingException exception) {
//                throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Error while setting value posts in Redis");
//            }
//
//            return postsDTO;
//        }
//
//        try {
//            return objectMapper.readValue(postsList_string, new TypeReference<List<PostDTO>>() {
//            });
//        } catch (JsonProcessingException e) {
//            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Error while reading value posts in Redis");
//        }
    }

    @Override
    @Cacheable(value = "posts", key = "#categoryId + '::' + #pageNo + '::' + #pageSize + '::' + #sortBy")
    public List<PostDTO> getPostsByCategoryWithPageSize(long categoryId, int pageNo, int pageSize, String sortBy) {
        List<Post> posts = postRepository.findByCategoryId(categoryId);

        List<List<Post>> listPostsArray = this.partitionList(posts, pageSize).stream().toList();

        List<Post> listPost = listPostsArray.get(pageNo);
        if (sortBy.equals("title")) {
            listPost.sort(Comparator.comparing(Post::getTitle));
        } else if (sortBy.equals("id")) {
            listPost.sort(Comparator.comparing(Post::getId));
        } else {
            listPost.sort(Comparator.comparing(Post::getContent));
        }

        return listPost.stream().map(PostMapping::mapToDTO).collect(Collectors.toList());
    }

    @Cacheable(value = "post", key = "#id")
    @Override
    public PostDTO findById(Long id) {
        System.out.println("Database");
        return mapToDTO(postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id)));
//        String postDTO_string = (String) redisService.get("post" + id);
//        if (postDTO_string == null) {
//            Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
//            PostDTO postDTO = mapToDTO(post);
//            try {
//                postDTO_string = objectMapper.writeValueAsString(postDTO);
//                redisService.set("post" + id, postDTO_string);
//                redisService.setTimeToLive("post" + id, 5);
//            } catch (JsonProcessingException exception) {
//                throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Error while setting value postId in Redis");
//            }
//            return postDTO;
//        }
//
//        try {
//            return objectMapper.readValue(postDTO_string, PostDTO.class);
//        } catch (Exception exception) {
//            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Error while reading value postId in Redis");
//        }
    }

    @Transactional
    @Override
    @Caching(
            evict = {@CacheEvict(value = "posts", allEntries = true)},
            put = {@CachePut(value = "post", key = "#postId")}
    )
    public PostDTO updatePost(PostDTO newPostDTO, long postId) {
        postRepository.updatePost(postId, newPostDTO.getTitle(), newPostDTO.getDescription(), newPostDTO.getContent());
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        return mapToDTO(post);
//        redisService.delete("post" + postId);
//
//        try {
//            postRepository.updatePost(postId, newPostDTO.getTitle(), newPostDTO.getDescription(), newPostDTO.getContent());
//        } catch (Exception exception) {
//            throw new QueryException(HttpStatus.BAD_REQUEST, "No change for this post");
//        }
//
//        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
//        PostDTO postDTO = mapToDTO(post);
//
//        try {
//            String postDTO_string = objectMapper.writeValueAsString(postDTO);
//            redisService.set("post" + postId, postDTO_string);
//            redisService.setTimeToLive("post" + postId, 1);
//        } catch (JsonProcessingException exception) {
//            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Error while updating value postId in Redis");
//        }
//
//        return newPostDTO;
    }

    @Transactional
    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "posts", allEntries = true, beforeInvocation = true),
                    @CacheEvict(value = "post", key = "#id")
            }
    )
    public void deletePost(Long id) {
        commentRepository.deleteCommentsByPostId(id);
        postRepository.deletePost(id);
//        redisService.delete("post" + id);
    }

    @Override
    @Cacheable(value = "posts", key = "'category' + #categoryId")
    public List<PostDTO> getPostsByCategory(long categoryId) {
        return postRepository.findByCategoryId(categoryId).stream().map(PostMapping::mapToDTO).toList();
//        String post_category = (String) redisService.get("postByCategory" + categoryId);
//
//        if (post_category == null) {
//            List<PostDTO> postsDTO = postRepository.findByCategoryId(categoryId).stream().map(PostMapping::mapToDTO).toList();
//            try {
//                post_category = objectMapper.writeValueAsString(postsDTO);
//                redisService.set("postByCategory" + categoryId, post_category);
//                redisService.setTimeToLive("postByCategory" + categoryId, 5);
//            } catch (JsonProcessingException exception) {
//                throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Error while setting value postByCategoryId in Redis");
//            }
//            return postsDTO;
//        }
//
//        try {
//            return objectMapper.readValue(post_category, new TypeReference<List<PostDTO>>() {
//            });
//        } catch (JsonProcessingException e) {
//            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Error while reading value posts in Redis");
//        }

    }

    private <T> Collection<List<T>> partitionList(List<T> inputList, int pageSize) {
        final AtomicInteger counter = new AtomicInteger(0);
        return inputList.stream()
                .collect(Collectors.groupingBy(value -> counter.getAndIncrement() / pageSize))
                .values();
    }
}
