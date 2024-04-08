package com.truongvu.blogrestapi.dto;

import com.truongvu.blogrestapi.entity.Image;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
@Schema(
        description = "PostDTO Model Information"
)
public class PostDTO {
    private long id;

    @Schema(
            description = "Blog Post Title"
    )
    @NotNull
    @Size(min = 2,message = "Title must be minimum 2 characters")
    private String title;

    @Schema(
            description = "Blog Post Description"
    )
    @NotNull
    @Size(min = 10,message = "Description must be minimum 10 characters")
    private String description;

    @Schema(
            description = "Blog Post Content"
    )
    @NotNull
    @Size(min = 10,message = "Content must be minimum 10 characters")
    private String content;

    private Set<CommentDTO> comments;

    @Schema(
            description = "Blog Post Category"
    )
    private Long categoryId;

    private ImageDTO imageDTO;
}
