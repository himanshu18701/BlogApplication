package com.himanshu.blog_app.rest;

import com.himanshu.blog_app.entity.User;
import com.himanshu.blog_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/register")
    public String processRegistrationForm(@RequestBody User user) {
        userService.saveUser(user);
        return "User successfully registered";
    }
}
