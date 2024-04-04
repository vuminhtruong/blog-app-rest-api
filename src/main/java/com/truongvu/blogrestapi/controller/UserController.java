package com.truongvu.blogrestapi.controller;

import com.truongvu.blogrestapi.dto.UserDTO;
import com.truongvu.blogrestapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    private final UserService userService;

    @GetMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getUserByUserName(@PathVariable(name = "username") String username) {
        return new ResponseEntity<>(userService.getUserByUsername(username), HttpStatus.OK);
    }

    @GetMapping(value = "/{username}/role")
    public ResponseEntity<Boolean> getUserRole(@PathVariable(name = "username") String username) {
        return new ResponseEntity<>(userService.getUserByUsername(username).getRoles().stream().anyMatch(role -> "ROLE_ADMIN".equals(role.getRole())), HttpStatus.OK);
    }
}
