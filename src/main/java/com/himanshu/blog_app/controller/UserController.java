package com.himanshu.blog_app.controller;

import com.himanshu.blog_app.entity.User;
import com.himanshu.blog_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/loginPage")
    public String login() {
        return "login";
    }

    @GetMapping("/registerForm")
    public String showRegistrationPage(Model model) {
        model.addAttribute("newUser", new User());
        return "register_page";
    }

    @PostMapping("/registerNewUser")
    public String processRegistrationForm(@ModelAttribute("newUser") User user) {
        userService.saveUser(user);
        return "redirect:/loginPage";
    }
}
