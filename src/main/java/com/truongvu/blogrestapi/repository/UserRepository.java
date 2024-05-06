package com.truongvu.blogrestapi.repository;

import com.truongvu.blogrestapi.entity.User;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
