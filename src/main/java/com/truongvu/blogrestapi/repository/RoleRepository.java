package com.truongvu.blogrestapi.repository;

import com.truongvu.blogrestapi.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByRole(String role);
}
