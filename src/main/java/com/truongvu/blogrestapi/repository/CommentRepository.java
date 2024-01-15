package com.truongvu.blogrestapi.repository;

import com.truongvu.blogrestapi.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByPostId(long id);
}