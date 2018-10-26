package com.yimukeji.yuelaoge.bean;

import java.util.List;

public class Domain<T> {
    public static final int ERROR_CODE_LOGIN = 401;
    public boolean success;
    public int code;
    public String message;
    public T data;
    public List<T> datalist;

    public Domain() {
        code = 0;
        success = false;
        message = "数据错误";
    }


}
