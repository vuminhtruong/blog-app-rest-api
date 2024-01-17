package com.truongvu.blogrestapi.repository;

import com.truongvu.blogrestapi.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
