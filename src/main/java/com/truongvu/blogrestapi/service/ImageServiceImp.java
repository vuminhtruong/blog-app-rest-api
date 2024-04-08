package com.truongvu.blogrestapi.service;

import com.truongvu.blogrestapi.dto.ImageDTO;
import com.truongvu.blogrestapi.entity.Image;
import com.truongvu.blogrestapi.entity.Post;
import com.truongvu.blogrestapi.exception.ResourceNotFoundException;
import com.truongvu.blogrestapi.repository.ImageRepository;
import com.truongvu.blogrestapi.repository.PostRepository;
import com.truongvu.blogrestapi.validate.ValidateFile;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    @Override
    public List<ImageDTO> getAllImage() {
        List<Image> imageList = imageRepository.findAll();
        return imageList.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public ImageDTO getImage(long id) {
        return mapToDTO(imageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Image", "id", id)));
    }

    @Override
    public List<ImageDTO> uploadImage(MultipartFile[] files) throws IOException {
        List<ImageDTO> imageDTOList = new ArrayList<>();
        try {
            for(MultipartFile file : files) {
                validateFile.validateFile(file);
            }
        } catch (Exception exception) {
            throw new RuntimeException("Uploading process image fail!");
        }

        for(MultipartFile file : files) {
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
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post","id",postId));
        Image image = mapToEntity(imageDTO);
        post.setImage(image);

        postRepository.save(post);
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
