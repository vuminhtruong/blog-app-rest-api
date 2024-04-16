package com.truongvu.blogrestapi.dto;

import com.truongvu.blogrestapi.entity.Image;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class PostDTO {
    private long id;

    private String title;

    private String description;

    private String content;

    private Set<CommentDTO> comments;

    private Long categoryId;

    private ImageDTO imageDTO;
}
