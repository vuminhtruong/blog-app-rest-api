package com.truongvu.blogrestapi.service;

import com.truongvu.blogrestapi.dto.LoginDTO;
import com.truongvu.blogrestapi.dto.RegisterDTO;

public interface AuthService {
    String login(LoginDTO loginDTO);
    String register(RegisterDTO registerDTO);
}
