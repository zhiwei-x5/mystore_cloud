package com.example.mystore_image.service;


import com.example.mystore_image.entity.User;

public interface IUserService {


    void changeAvatar(Integer uid, String username, String avatar);
}
