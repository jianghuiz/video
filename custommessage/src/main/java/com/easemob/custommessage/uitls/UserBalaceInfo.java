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
    private String msg;
    private String detail;

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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
