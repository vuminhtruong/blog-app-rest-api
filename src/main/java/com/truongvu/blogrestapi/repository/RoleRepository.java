package com.truongvu.blogrestapi.repository;

import com.truongvu.blogrestapi.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
}
