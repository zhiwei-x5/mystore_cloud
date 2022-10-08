package com.example.mystore_login.service.impl;

import com.example.mystore_login.entity.User;
import com.example.mystore_login.mapper.UserMapper;
import com.example.mystore_login.service.IUserService;
import com.mystore.service.ex.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.UUID;

@Service
public class IUserServiceImpl implements IUserService {
    /**
     * StringRedisTemplate继承了RedisTemplate。继承RedisTempalte，
     * 与RedisTemplate不同的是设置了序列化策略，使用StringRedisSerializer类来
     * 序列化key-value，以及List、Hash、Set。在这里，我们直接用就行了。
     */
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserMapper userMapper;
    @Override
    public void reg(User user) {
        // 根据参数user对象获取注册的用户名
        String username = user.getUsername();
        // 调用持久层的User findByUsername(String username)方法，根据用户名查询用户数据
        User result = userMapper.findByUsername(username);
        // 判断查询结果是否不为null
        if (result != null) {
            // 是：表示用户名已被占用，则抛出UsernameDuplicateException异常
            throw new UsernameDuplicateException("尝试注册的用户名[" + username + "]已经被占用");
        }
        // 补全数据：加密后的密码
        /**
         UUID（Universally Unique Identifier）：通用唯一识别码，是一种软件建构的标准。
         当前日期和时间，UUID的第一个部分与时间有关
         标准的UUID格式为：xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx (8-4-4-4-12)
         */
        String salt = UUID.randomUUID().toString().toUpperCase();
        String md5Password = getMd5Password(user.getPassword(), salt);
        System.out.println("注册时md5加密后密码："+md5Password);
        user.setPassword(md5Password);
        // 补全数据：盐值
        user.setSalt(salt);
        // 补全数据：isDelete(0)
        user.setIsDelete(0);

        // 表示用户名没有被占用，则允许注册
        // 调用持久层Integer insert(User user)方法，执行注册并获取返回值(受影响的行数)
        Integer rows = userMapper.insert(user);
        // 判断受影响的行数是否不为1
        if (rows != 1) {
            // 是：插入数据时出现某种错误，则抛出InsertException异常
            throw new InsertException("添加用户数据出现未知错误，请联系系统管理员");
        }
    }

    /**
     * 执行密码加密
     * @param password 原始密码
     * @param salt 盐值
     * @return 加密后的密文
     */
    private String getMd5Password(String password, String salt) {
        /*
         * 加密规则：
         * 1、无视原始密码的强度
         * 2、使用UUID作为盐值，在原始密码的左右两侧拼接
         * 3、循环加密3次
         */
        for (int i = 0; i < 3; i++) {
            password = DigestUtils.md5DigestAsHex((salt + password + salt).getBytes()).toUpperCase();
        }
        return password;
    }

    @Override
    public User login(String username, String password) {
        String str1 = stringRedisTemplate.opsForValue().get("uid");
        String str2 = stringRedisTemplate.opsForValue().get("username");
        String str3 = stringRedisTemplate.opsForValue().get("password");
        String str4 = stringRedisTemplate.opsForValue().get("avatar");
        String str5 = stringRedisTemplate.opsForValue().get("salt");
        System.out.println("uid:" + str1 + "、username:" + str2 + "、password:" + str3+ "、avatar:" + str4+ "、salt:" + str5);
        // 从查询结果中获取盐值str5
        // 调用getMd5Password()方法，将参数password和salt结合起来进行加密
        String md5Password1 = getMd5Password(password, str5);
        System.out.println("md5Password1:"+md5Password1);
        if (str2.equals(username) && str3.equals(md5Password1) ) {

            // 创建新的User对象
            User user = new User();
            // 将查询结果中的uid、username、avatar封装到新的user对象中
            user.setUid(Integer.valueOf(str1));
            user.setUsername(str2);
            user.setPassword(str3);
            user.setAvatar(str4);

            return user;
        } else {

            // 调用userMapper的findByUsername()方法，根据参数username查询用户数据
            User result = userMapper.findByUsername(username);
            // 判断查询结果是否为null
            if (result == null) {
                // 是：抛出UserNotFoundException异常
                throw new UserNotFoundException("用户数据不存在的错误");
            }

            // 判断查询结果中的isDelete是否为1
            if (result.getIsDelete() == 1) {
                // 是：抛出UserNotFoundException异常
                throw new UserNotFoundException("用户数据不存在的错误");
            }

            // 从查询结果中获取盐值
            String salt = result.getSalt();
            // 调用getMd5Password()方法，将参数password和salt结合起来进行加密
            String md5Password = getMd5Password(password, salt);
            System.out.println("输入的密码：" + md5Password);
            System.out.println("数据库的密码：" + result.getPassword());
            // 判断查询结果中的密码，与以上加密得到的密码是否不一致
            if (!result.getPassword().equals(md5Password)) {
                // 是：抛出PasswordNotMatchException异常
                throw new PasswordNotMatchException("密码验证失败的错误");
            }

            // 创建新的User对象
            User user = new User();
            // 将查询结果中的uid、username、avatar封装到新的user对象中
            user.setUid(result.getUid());
            user.setUsername(result.getUsername());
            user.setPassword(result.getPassword());
            user.setAvatar(result.getAvatar());
            user.setSalt(result.getSalt());

            //存入redis
            stringRedisTemplate.opsForValue().set("uid", String.valueOf(user.getUid()));
            stringRedisTemplate.opsForValue().set("username", user.getUsername());
            stringRedisTemplate.opsForValue().set("password", user.getPassword());
            stringRedisTemplate.opsForValue().set("avatar", user.getAvatar());
            stringRedisTemplate.opsForValue().set("salt", user.getSalt());

            // 返回新的user对象
            return user;
        }

    }
}
