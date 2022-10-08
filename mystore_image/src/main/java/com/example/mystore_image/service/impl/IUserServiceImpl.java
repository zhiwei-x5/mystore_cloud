package com.example.mystore_image.service.impl;


import com.example.mystore_image.entity.User;
import com.example.mystore_image.mapper.ImageMapper;
import com.example.mystore_image.service.IUserService;
import com.mystore.service.ex.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class IUserServiceImpl implements IUserService {

    @Autowired
    private ImageMapper userMapper;

    @Override
    public void changeAvatar(Integer uid, String username, String avatar) {
// 调用userMapper的findByUid()方法，根据参数uid查询用户数据
        User result = userMapper.findByUid(uid);
        System.out.println(result);
        // 检查查询结果是否为null
        if (result == null) {
            // 是：抛出UserNotFoundException
            throw new UserNotFoundException("用户数据不存在");
        }

        /**
         删除后会将Delete字段设为1：表示数据已删除
         */
        // 检查查询结果中的isDelete是否为1
        if (result.getIsDelete().equals(1)) {
            // 是：抛出UserNotFoundException
            throw new UserNotFoundException("用户数据不存在");
        }
        // 创建当前时间对象
        Date now = new Date();
        // 调用userMapper的updateAvatarByUid()方法执行更新，并获取返回值
        Integer rows = userMapper.updateAvatarByUid(uid, avatar, username, now);
        // 判断以上返回的受影响行数是否不为1
        if (rows != 1) {
            // 是：抛出UpdateException
            throw new UpdateException("更新用户数据时出现未知错误，请联系系统管理员");
        }
    }
}
