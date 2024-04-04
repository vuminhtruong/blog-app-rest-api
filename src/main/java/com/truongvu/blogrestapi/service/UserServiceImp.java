package com.truongvu.blogrestapi.service;

import com.truongvu.blogrestapi.dto.PostDTO;
import com.truongvu.blogrestapi.dto.UserDTO;
import com.truongvu.blogrestapi.entity.Post;
import com.truongvu.blogrestapi.entity.User;
import com.truongvu.blogrestapi.exception.ResourceNotFoundException;
import com.truongvu.blogrestapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService{
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Cannot find username: " + username));

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setRoles(user.getRoles());
        userDTO.setName(user.getName());
        userDTO.setId(user.getId());

        return userDTO;
    }

    private UserDTO mapToDTO(User user) {
        return modelMapper.map(user,UserDTO.class);
    }
}
