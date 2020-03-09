package com.blog.aigetcode.service;

import com.blog.aigetcode.DTO.UserDto;
import com.blog.aigetcode.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Set;

public interface UserService extends UserDetailsService {

    Set getAuthority(User user);
    List findAll();
    void delete(long id);
    User findOne(String username);
    User findById(Long id);
    User save(UserDto user);

}
