package com.truongvu.blogrestapi.service;

import com.truongvu.blogrestapi.dto.LoginDTO;
import com.truongvu.blogrestapi.dto.RegisterDTO;
import com.truongvu.blogrestapi.entity.Role;
import com.truongvu.blogrestapi.entity.User;
import com.truongvu.blogrestapi.exception.BlogAPIException;
import com.truongvu.blogrestapi.exception.ResourceNotFoundException;
import com.truongvu.blogrestapi.repository.RoleRepository;
import com.truongvu.blogrestapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService{
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public String login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(),loginDTO.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "User logged-in successfully";
    }

    @Override
    public String register(RegisterDTO registerDTO) {
        if(userRepository.existsByUsername(registerDTO.getUsername())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Username is already exists.");
        }

        if(userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Email is already exists.");
        }

        User user = new User();
        user.setName(registerDTO.getName());
        user.setEmail(registerDTO.getEmail());
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByRole("ROLE_USER").orElseThrow(() -> new BlogAPIException(HttpStatus.BAD_REQUEST,"Role not found")));

        user.setRoles(roles);
        userRepository.save(user);

        return "User registered successfully";
    }
}
