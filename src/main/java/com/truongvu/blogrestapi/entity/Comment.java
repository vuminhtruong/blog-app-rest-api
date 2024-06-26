package com.truongvu.blogrestapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comment")
@org.hibernate.annotations.Cache(region = "commentCache", usage = CacheConcurrencyStrategy.READ_WRITE)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "email",nullable = false)
    private String email;

    @Column(name = "body",nullable = false)
    private String body;

    @ManyToOne
    @JoinColumn(name = "post_id",nullable = false)
    private Post post;
}
