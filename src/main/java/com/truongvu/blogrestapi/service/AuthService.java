package com.truongvu.blogrestapi.service;

import com.truongvu.blogrestapi.dto.LoginDTO;
import com.truongvu.blogrestapi.dto.RegisterDTO;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    String login(LoginDTO loginDTO);
    String register(RegisterDTO registerDTO);
}
