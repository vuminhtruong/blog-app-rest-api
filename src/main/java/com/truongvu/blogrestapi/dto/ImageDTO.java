package com.truongvu.blogrestapi.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ImageDTO {
    private long id;
    @NotNull
    private String name;
    private String type;
    @NotNull
    private String data;
    @NotNull
    private LocalDateTime createAt;
}
