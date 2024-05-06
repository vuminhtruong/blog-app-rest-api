package com.truongvu.blogrestapi.repository;

import com.truongvu.blogrestapi.entity.Image;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    List<Image> findAll();
}
