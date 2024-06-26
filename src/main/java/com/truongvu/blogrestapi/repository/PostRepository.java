package com.truongvu.blogrestapi.repository;

import com.truongvu.blogrestapi.entity.Post;
import jakarta.persistence.QueryHint;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "select * from post where category_id = :categoryId", nativeQuery = true)
    List<Post> findByCategoryId(long categoryId);

    @Override
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<Post> findAll();

//    @Query(value = "select * from post order by title", nativeQuery = true)
//    @QueryHints(value = {
//            @QueryHint(name = "org.hibernate.cacheable", value = "true"),
//            @QueryHint(name = "org.hibernate.cacheRegion", value = "queryCacheRegion")
//    })
//    List<Post> findAllPost();

//    @Query(value = "select * from post where id=:id", nativeQuery = true)
    @Override
    @QueryHints(value = {
            @QueryHint(name = "org.hibernate.cacheable", value = "true")
    })
    Optional<Post> findById(Long id);

    @Modifying
    @Query(value = "insert into post (title, description, content, category_id) values" +
            "( " +
            ":#{#post.title}, " +
            ":#{#post.description}, " +
            ":#{#post.content}, " +
            ":#{#post.category.id} " +
            ")", nativeQuery = true)
    void insertNewPost(@Param("post") Post post);

    @Modifying
    @Query(value = "call update_post(:p_id, :p_title, :p_description, :p_content)", nativeQuery = true)
    void updatePost(@Param("p_id") Long id, @Param("p_title") String title, @Param("p_description") String description, @Param("p_content") String content);

    @Modifying
    @Query(value = "delete from post where post.id = :postId", nativeQuery = true)
    void deletePost(Long postId);
}
