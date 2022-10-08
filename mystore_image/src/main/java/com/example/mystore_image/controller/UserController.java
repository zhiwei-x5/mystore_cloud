package com.example.mystore_image.controller;

import com.example.mystore_image.service.IUserService;
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
@Transactional(readOnly=false)
@RequestMapping("usersupimage")
public class UserController extends BaseController implements Serializable {
    @Autowired
    private IUserService userService;

    /** 头像文件大小的上限值(10MB) */
    public static final int AVATAR_MAX_SIZE = 10 * 1024 * 1024;
    /** 允许上传的头像的文件类型 */
    public static final List<String> AVATAR_TYPES = new ArrayList<String>();
    /** 初始化允许上传的头像的文件类型 ，
     * String contentType = file.getContentType();
     * System.out.println("获取头像类型："+contentType);输出结果：image/jpeg
     * 因此下面的数值是根据file.getContentType()能获取图片的格式来编写的*/
    static {
        AVATAR_TYPES.add("image/jpeg");
        AVATAR_TYPES.add("image/png");
        AVATAR_TYPES.add("image/bmp");
        AVATAR_TYPES.add("image/gif");
    }


    /** MultipartFile是SpringMVC提供简化上传操作的工具类，该接口包装了获取文件类型的数据（任何类型的file都可以接收）
        springBoot它有整合了SpringMVC，只需要在处理请求的方法参数列表声明一个参数类型为MultipartFile的参数，然后
        spring Boot自动将传参传递给服务的文件数据赋值给这个参数
     */
    /** @RequestParam("file"):将前端表单的file传给请求列表中的file参数，url参数中的file必须要和@RequestParam("file")一致
     * session里面保存了登录是的uid，通过uid即可查询到数据库中的个人信息相关数据
     * */

    @PostMapping("change_avatar")
    public JsonResult<String> changeAvatar(@RequestParam("file") MultipartFile file, HttpSession session) throws IOException {
        /** 修改虚拟机（Linux系统）tomcat的server.xml下*/
        //<Context docBase ="/home/ubuntu/springboot/img" path ="/home/ubuntu/springboot/img" debug ="0" reloadable ="true"/>
        /** 1、拿到file后需要异常处理：文件为空异常、文件超过限制值异常、文件类型异常*/
        /** 2、设置文件大小、设置文件类型*/
        // 判断上传的文件是否为空
        if (file.isEmpty()) {
            // 是：抛出异常
            throw new FileEmptyException("上传的头像文件不允许为空");
        }

        // 判断上传的文件大小是否超出限制值
        if (file.getSize() > AVATAR_MAX_SIZE) { // getSize()：返回文件的大小，以字节为单位
            // 是：抛出异常
            throw new FileSizeException("不允许上传超过" + (AVATAR_MAX_SIZE / 1024) + "KB的头像文件");
        }

        // 判断上传的文件类型是否超出限制
        String contentType = file.getContentType();
        System.out.println("获取头像类型："+contentType);
        // boolean contains(Object o)：当前列表若包含某元素，返回结果为true；若不包含该元素，返回结果为false
        if (!AVATAR_TYPES.contains(contentType)) {
            // 是：抛出异常
            throw new FileTypeException("不支持使用该类型的文件作为头像，允许的文件类型：" + AVATAR_TYPES);
        }

        // 获取当前项目的绝对磁盘路径
        //String parent = session.getServletContext().getRealPath("upload");
        //window
        String parent = "E:\\mystore_image\\web";
        //linux
        //String parent = "http://192.168.131.128:8080/pic/";
        // 保存头像文件的文件夹
        File dir = new File(parent);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 保存的头像文件的文件名
        String suffix = "";
        String originalFilename = file.getOriginalFilename();
        //lastIndexOf() 方法返回指定元素在动态数组中最后一次出现的位置:"."最后出现在19位子（不同图片名字不同）
        int beginIndex = originalFilename.lastIndexOf(".");
        System.out.println("截取到的图片名称后缀："+beginIndex);
        if (beginIndex > 0) {
            //substring() 方法返回字符串的子字符串。:返回19位后的值
            suffix = originalFilename.substring(beginIndex);
            System.out.println("这里是suffix后缀："+suffix);
        }

        //这里先创建需要保存图片所用到的图片名字：随机生成+（前面截取出来）图片类型
        /** UUID是让分布式系统中的所有元素都能有唯一的辨识信息，而不要要通过中央控制端来做辨识信息的指定。
         如此一来，每个人都可以创建不与其他人冲突的UUID*/
        String filename = UUID.randomUUID().toString() + suffix;
        // 创建文件对象，表示保存的头像文件

        /**
         File 类提供了如下三种形式构造方法。
         File(String path)：如果 path 是实际存在的路径，则该 File 对象表示的是目录；如果 path 是文件名，则该 File 对象表示的是文件。
         File(String path, String name)：path 是路径名，name 是文件名。
         File(File dir, String name)：dir 是路径对象，name 是文件名。*/
        //此处未执行下面 file.transferTo(dest);前，是一个空白的图片文件
        //window
        File destwindow = new File(dir, filename);
        //linux
        //使用Jersey客户端上传文件
        /** 由于我们要在不同主机上上传文件，所以不能直接通过流的方式来写，
         需要通过webService来完成，这边借助Jersey来完成*/
        //Client client = Client.create();
        //WebResource destlinux = client.resource(parent + filename);
        //WebResource dest = client.resource(parent + URLEncoder.encode(filename,"utf-8"));
        /*try {
            *//** 将文件传到主机*//*
            destlinux.put(file.getBytes());
        } catch (IllegalStateException e) {
            // 抛出异常
            throw new FileStateException("文件状态异常，可能文件已被移动或删除");
        } catch (IOException e) {
            // 抛出异常
            throw new FileUploadIOException("上传文件时读写错误，请稍后重新尝试");
        }*/

        //winodws
         //执行保存头像文件
        try {
            /** transferto()方法，是springmvc封装的方法，用于图片上传时，把内存中图片写入磁盘*/
            file.transferTo(destwindow);
        } catch (IllegalStateException e) {
            // 抛出异常
            throw new FileStateException("文件状态异常，可能文件已被移动或删除");
        } catch (IOException e) {
            // 抛出异常
            throw new FileUploadIOException("上传文件时读写错误，请稍后重新尝试");
        }

        /** 将图片的名字作为数据保存到数据库*/
        // 头像路径
        //此处图片保存的本地路径返回给前端显示
        String avatar = filename;
        // 从Session中获取uid和username
        Integer uid = getUidFromSession(session);
        String username = getUsernameFromSession(session);
//        Integer uid = 5;
//        String username = "xzw";
        // 将头像写入到数据库中
        userService.changeAvatar(uid, username, avatar);

        /** properties中最后设置了上传的大小，请求的大小，请求中包含了文件的内容因此要比上传的大*/

        // 返回成功头像路径
        /** JsonResult设置一个变量保存访问路径然后返回给前端 */
        return new JsonResult<String>(OK, avatar);
    }
}
