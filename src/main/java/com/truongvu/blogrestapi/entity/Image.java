package com.truongvu.blogrestapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "image")
@Cache(region = "imageCache", usage = CacheConcurrencyStrategy.READ_WRITE)
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Lob
    @Column(name = "data", columnDefinition = "MEDIUMBLOB")
    private byte[] data;

    @Column(name = "createAt")
    private LocalDateTime createAt;

    public Image(String name, String type, byte[] data, LocalDateTime localDateTime) {
        this.name = name;
        this.type = type;
        this.data = data;
        this.createAt = localDateTime;
    }
}
