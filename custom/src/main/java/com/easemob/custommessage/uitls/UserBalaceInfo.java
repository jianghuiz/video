package com.easemob.custommessage.uitls;

public class UserBalaceInfo {

    /**
     * success : true
     * code : 0
     * msg : null
     * detail : null
     */

    private boolean success;
    private int code;
    private Object msg;
    private Object detail;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public Object getDetail() {
        return detail;
    }

    public void setDetail(Object detail) {
        this.detail = detail;
    }
}
