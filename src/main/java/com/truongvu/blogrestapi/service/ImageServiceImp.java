package com.truongvu.blogrestapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.truongvu.blogrestapi.dto.ImageDTO;
import com.truongvu.blogrestapi.entity.Image;
import com.truongvu.blogrestapi.entity.Post;
import com.truongvu.blogrestapi.exception.BlogAPIException;
import com.truongvu.blogrestapi.exception.QueryException;
import com.truongvu.blogrestapi.exception.ResourceNotFoundException;
import com.truongvu.blogrestapi.repository.ImageRepository;
import com.truongvu.blogrestapi.repository.PostRepository;
import com.truongvu.blogrestapi.service.redis.RedisService;
import com.truongvu.blogrestapi.validate.ValidateFile;
import lombok.RequiredArgsConstructor;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageServiceImp implements ImageService {
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final ValidateFile validateFile;
    private final RedisService redisService;
    private final ObjectMapper objectMapper;

    @Override
    public List<ImageDTO> getAllImage() {
        String imageList_string = (String) redisService.get("images");
        if (imageList_string == null) {
            List<ImageDTO> imageDTOList = imageRepository.findAll().stream().map(this::mapToDTO).toList();
            try {
                imageList_string = objectMapper.writeValueAsString(imageDTOList);
                redisService.set("images", imageList_string);
                redisService.setTimeToLive("images", 5);
            } catch (Exception exception) {
                throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Error while setting value images in Redis");
            }
            return imageDTOList;
        }

        try {
            return objectMapper.readValue(imageList_string, new TypeReference<List<ImageDTO>>() {});
        } catch (JsonProcessingException e) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Error while reading value images in Redis");
        }
    }

    @Override
    public ImageDTO getImage(long id) {
        String imageId_string = (String) redisService.get("image" + id);
        if(imageId_string == null) {
            ImageDTO imageDTO = mapToDTO(imageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Image", "id", id)));
            try {
                imageId_string = objectMapper.writeValueAsString(imageDTO);
                redisService.set("image" + id, imageId_string);
                redisService.setTimeToLive("image" + id, 2);
            } catch (Exception exception) {
                throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Error while setting value imageId in Redis");
            }
            return imageDTO;
        }

        try {
            return objectMapper.readValue(imageId_string, ImageDTO.class);
        } catch (JsonProcessingException e) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Error while reading value images in Redis");
        }
    }

    @Override
    public List<ImageDTO> uploadImage(MultipartFile[] files) throws IOException {
        redisService.delete("images");
        List<ImageDTO> imageDTOList = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                validateFile.validateFile(file);
            }
        } catch (Exception exception) {
            throw new RuntimeException("Uploading process image fail!");
        }

        for (MultipartFile file : files) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            Image image = new Image(fileName, file.getContentType(), file.getBytes(), LocalDateTime.now());
            imageRepository.save(image);
            imageDTOList.add(mapToDTO(image));
        }

        return imageDTOList;
    }

    @Override
    public String deleteImage(long id) {
        Image image = imageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Image", "id", id));
        imageRepository.delete(image);
        return "Deleted image with id=" + id;
    }

    @Override
    public ImageDTO addImageForPost(long postId, ImageDTO imageDTO) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        Image image = mapToEntity(imageDTO);
        post.setImage(image);
        redisService.delete("post" + postId);
        try {
            postRepository.save(post);
        } catch (Exception exception) {
            throw new QueryException(HttpStatus.BAD_REQUEST, "Image has been selected for another post");
        }

        return mapToDTO(image);
    }

    private ImageDTO mapToDTO(Image image) {
        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setId(image.getId());
        imageDTO.setName(image.getName());
        imageDTO.setType(image.getType());
        imageDTO.setData(Base64.getEncoder().encodeToString(image.getData()));
        imageDTO.setCreateAt(image.getCreateAt());

        return imageDTO;
    }

    private Image mapToEntity(ImageDTO imageDTO) {
        Image image = new Image();
        image.setId(imageDTO.getId());
        image.setData(Base64.getDecoder().decode(imageDTO.getData()));
        image.setName(imageDTO.getName());
        image.setCreateAt(imageDTO.getCreateAt());
        image.setType(imageDTO.getType());

        return image;
    }
}
