package com.truongvu.blogrestapi.service;

import com.truongvu.blogrestapi.dto.UserDTO;

import java.util.Optional;

public interface UserService {
    UserDTO getUserByUsername(String username);
}
