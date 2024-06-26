package com.truongvu.blogrestapi.service;

import com.truongvu.blogrestapi.dto.ImageDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {
    List<ImageDTO> getAllImage();
    ImageDTO getImage(long id);
    List<ImageDTO> uploadImage(MultipartFile[] file) throws IOException;
    String deleteImage(long id);

    ImageDTO addImageForPost(long postId, ImageDTO imageDTO);
}
