package com.mystore.controller;



import com.mystore.service.ex.PasswordNotMatchException;
import com.mystore.service.ex.ServiceException;
import com.mystore.service.ex.UserNotFoundException;
import com.mystore.service.ex.UsernameDuplicateException;
import com.mystore.util.JsonResult;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpSession;


/** 控制器类的基类 */
public class BaseController {
    /** 操作成功的状态码 */
    public static final int OK = 200;

    //请求处理方法，该方法的返回值就是需要传递给前端的数据
    //自动将异常对象传递给此方法的参数列表上
    //当前项目中产生了异常，被统一拦截到此方法中，这个方法此时就充当的是请求处理方法，方法的返回值直接给到前端
    /**
     * @ExceptionHandler用于统一处理方法抛出的异常
     * 整个项目的异常（参数:需要处理异常的类，此处为ServiceException.class类，
     * 因为所有service中的ex包下的类都继承于ServiceException.class作为基类，
     * 也就是说所有向上抛出的异常都会到此处）抛出的类中已经携带了状态的描述信息，
     * 并在向上抛出的异常中 */
    @ExceptionHandler({ServiceException.class, FileUploadException.class})
    public JsonResult<Void> handleException(Throwable e) {//Throwable类是 Java 语言中所有错误和异常的超类。
        JsonResult<Void> result = new JsonResult<Void>(e);
        System.out.println("e:----------------------------------"+e);
        if (e instanceof UsernameDuplicateException) {
            result.setState(4000);
            //result.setMessage("状态的描述信息");
        } else if (e instanceof UserNotFoundException) {
            result.setState(4001);
        } else if (e instanceof PasswordNotMatchException) {
            result.setState(4002);
        }
        return result;
    }

    /**
     * 从HttpSession对象中获取uid
     * @param session HttpSession对象
     * @return 当前登录的用户的id
     */
    protected final Integer getUidFromSession(HttpSession session) {
        System.out.println(session.getAttribute("uid"));
        return Integer.valueOf(session.getAttribute("uid").toString());
    }
    /**
     * 从HttpSession对象中获取用户名
     * @param session HttpSession对象
     * @return 当前登录的用户名
     */
    protected final String getUsernameFromSession(HttpSession session) {
        return session.getAttribute("username").toString();
    }
}
