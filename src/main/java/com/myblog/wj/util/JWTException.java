package com.myblog.wj.util;

public class JWTException extends Exception {
    private  String msg;

    public JWTException(String msg) {
        this.msg = msg;
    }

    public JWTException(String message, String msg) {
        super(message);
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public JWTException(String message, Throwable cause, String msg) {
        super(message, cause);
        this.msg = msg;
    }

    public JWTException(Throwable cause, String msg) {
        super(cause);
        this.msg = msg;
    }

    public JWTException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String msg) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.msg = msg;
    }
}
