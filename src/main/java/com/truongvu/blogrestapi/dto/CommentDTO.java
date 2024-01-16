package com.truongvu.blogrestapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentDTO {
    private long id;

    @NotNull
    @Size(min = 2,message = "Name must be minimum 2 characters")
    private String name;

    @Email
    @NotNull
    private String email;

    @NotNull
    @Size(min = 10,message = "Body must be minimum 10 characters")
    private String body;
}
