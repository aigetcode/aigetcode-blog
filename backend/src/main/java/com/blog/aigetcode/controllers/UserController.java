package com.blog.aigetcode.controllers;

import com.blog.aigetcode.entity.Authority;
import com.blog.aigetcode.entity.User;
import com.blog.aigetcode.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Secured({Authority.Role.Code.USER, Authority.Role.Code.ADMIN})
    public User getUserByToken(Principal principal) throws Exception {
        String email = principal.getName();
        Optional<User> optionalUser = Optional.ofNullable(userService.findOne(email));
        User user = optionalUser.orElseThrow(Exception::new);
        user.setPassword(null);
        return user;
    }

}
