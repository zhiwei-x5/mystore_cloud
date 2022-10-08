package com.mystore.util;

import java.io.Serializable;

/**
 * 响应结果类
 * @param <E> 响应数据的类型
 */
//Serializable传输数据需要结果序列化和反序列化，即转为二进制
public class JsonResult<E> implements Serializable{
    /** 状态码 */
    private Integer state;
    /** 状态描述信息 */
    private String message;
    /** 数据 */
    private E data;

    public JsonResult() {
        super();
    }

    public JsonResult(Integer state) {
        super();
        this.state = state;
    }
    public JsonResult(Integer state, E data) {
        super();
        this.state = state;
        this.data = data;
    }
    /** 在类的继承中，子类的构造方法中默认会有super()语句存在，相当于执行父类的相应构造方法中的语句,
     * 当子类的构造方法内第一行没有出现“super()”时，系统会默认给它加上无参数的"super()"方法。
     * 实质上，添加 super(); 的目的是为了确保构造子类前，能成功地构造父类。也就是说，既然子类继承了父类的，只有当父类确实存在，子类才能够存在
     * */
    /** 出现异常时调用 */
    public JsonResult(Throwable e) {
        super();
        // 获取异常对象中的异常信息
        this.message = e.getMessage();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }
}
