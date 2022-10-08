package com.example.mystore_login.service;


import com.example.mystore_login.entity.User;

public interface IUserService {
    void reg(User user);
    User login(String username, String password);
}
