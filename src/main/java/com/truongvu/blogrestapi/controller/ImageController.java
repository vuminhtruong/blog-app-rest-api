
package com.truongvu.blogrestapi.controller;
import com.truongvu.blogrestapi.dto.ImageDTO;
import com.truongvu.blogrestapi.service.ImageService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ImageController {
    private final ImageService imageService;

    @GetMapping
    public ResponseEntity<List<ImageDTO>> getAllImage() {
        return new ResponseEntity<>(imageService.getAllImage(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImageDTO> getImageById(@PathVariable(name = "id") long id) {
        return new ResponseEntity<>(imageService.getImage(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ImageDTO> uploadImage(@ModelAttribute("file") MultipartFile file) throws IOException {
        return new ResponseEntity<>(imageService.uploadImage(file), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteImage(@PathVariable(name = "id") long id) {
        return new ResponseEntity<>(imageService.deleteImage(id), HttpStatus.OK);
    }
}