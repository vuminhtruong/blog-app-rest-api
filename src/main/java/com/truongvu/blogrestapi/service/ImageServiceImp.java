package com.truongvu.blogrestapi.service;

import com.truongvu.blogrestapi.dto.ImageDTO;
import com.truongvu.blogrestapi.entity.Image;
import com.truongvu.blogrestapi.exception.ResourceNotFoundException;
import com.truongvu.blogrestapi.repository.ImageRepository;
import com.truongvu.blogrestapi.validate.ValidateFile;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageServiceImp implements ImageService {
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
    public ImageDTO uploadImage(MultipartFile file) throws IOException {
        try {
            validateFile.validateFile(file);
        } catch (Exception exception) {
            throw new RuntimeException("Uploading process image fail!");
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Image image = new Image(fileName, file.getContentType(), file.getBytes(), LocalDateTime.now());
        imageRepository.save(image);
        return mapToDTO(image);
    }

    @Override
    public String deleteImage(long id) {
        Image image = imageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Image", "id", id));
        imageRepository.delete(image);
        return "Deleted image with id=" + id;
    }

    private ImageDTO mapToDTO(Image image) {
        return modelMapper.map(image, ImageDTO.class);
    }

    private Image mapToEntity(ImageDTO imageDTO) {
        return modelMapper.map(imageDTO, Image.class);
    }
}
