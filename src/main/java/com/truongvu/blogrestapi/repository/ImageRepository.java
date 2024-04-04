package com.truongvu.blogrestapi.repository;

import com.truongvu.blogrestapi.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
