package com.truongvu.blogrestapi.controller;

import com.truongvu.blogrestapi.dto.LoginDTO;
import com.truongvu.blogrestapi.dto.RegisterDTO;
import com.truongvu.blogrestapi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        return new ResponseEntity<>(authService.login(loginDTO), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDTO registerDTO) {
        return new ResponseEntity<>(authService.register(registerDTO), HttpStatus.CREATED);
    }
}
