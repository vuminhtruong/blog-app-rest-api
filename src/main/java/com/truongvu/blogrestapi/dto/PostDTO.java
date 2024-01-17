package com.truongvu.blogrestapi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class PostDTO {
    private long id;

    @NotNull
    @Size(min = 2,message = "Title must be minimum 2 characters")
    private String title;

    @NotNull
    @Size(min = 10,message = "Description must be minimum 10 characters")
    private String description;

    @NotNull
    @Size(min = 10,message = "Content must be minimum 10 characters")
    private String content;

    private Set<CommentDTO> comments;
    private Long categoryId;
}
