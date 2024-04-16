package com.truongvu.blogrestapi.repository;

import com.truongvu.blogrestapi.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByPostId(long id);

    @Modifying
    @Query(value = "delete from comment where post_id = :postId", nativeQuery = true)
    void deleteCommentsByPostId(long postId);


    // native query crud
    // transactional
}
