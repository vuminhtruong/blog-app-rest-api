package com.truongvu.blogrestapi.dto;

import com.truongvu.blogrestapi.entity.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserDTO {
    private long id;
    private String email;
    private String name;
    private String username;

    private Set<Role> roles;

}
