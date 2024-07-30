package com.himanshu.blog_app.service;

import com.himanshu.blog_app.entity.User;

public interface UserService {
    void saveUser(User user);

    User findUserByEmail(String email);
}
