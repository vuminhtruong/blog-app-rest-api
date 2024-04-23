package com.truongvu.blogrestapi.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.truongvu.blogrestapi.utils.LocalDateTimeDeserializer;
import com.truongvu.blogrestapi.utils.LocalDateTimeSerializer;
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
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createAt;
}
