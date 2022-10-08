package com.example.mystore_login.controller;

import com.example.mystore_login.entity.User;
import com.example.mystore_login.service.IUserService;
import com.mystore.controller.BaseController;
import com.mystore.controller.ex.*;
import com.mystore.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//@RestController=@ResponseBody+@Controller
//@CrossOrigin(origins ="http://localhost:8080",allowCredentials = "true") //允许跨域，同时允许使用cookie
@RestController
@Transactional(readOnly=false)//即将捕捉到的Exception异常全部回滚
@RequestMapping("users")
public class UserController extends BaseController implements Serializable {
    @Autowired
    private IUserService userService;

    @RequestMapping("reg")
    //@ResponseBody //表示此方法的响应结果以json格式进行数据的响应给前端
    public JsonResult<Void> reg(@RequestBody User user) {
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        // 调用业务对象执行注册
        userService.reg(user);
        // 以上如果未抛出异常即返回正常的状态码：OK=200
        return new JsonResult<Void>(OK);
    }

    @RequestMapping("log")
    public JsonResult<User> log(@RequestBody User user, HttpSession session) {
//    public JsonResult<User> log(String username, String password, HttpSession session) {
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        // 调用业务对象执行注册
//        String username=user.getUsername();
//        String password=user.getPassword();
        User data = userService.login(user.getUsername(), user.getPassword());
        //登录成功后，将uid和username存入到HttpSession中
        System.out.println("获取session会话值，此处的session是全局变量的，以便登录后使用");
        session.setAttribute("uid", data.getUid());
        session.setAttribute("username", data.getUsername());
        System.out.println("uid:"+getUidFromSession(session));
        System.out.println("username:"+getUsernameFromSession(session));
        // 返回
        return new JsonResult<User>(OK,data);
    }


}
