package com.easemob.custommessage.bean;

import java.io.Serializable;

public class AllQuestBean implements Serializable {
    private String cookie = "";
    private String mobile = "";
    private String userName = "";
    //课程信息
    private String uuid = "";
    private String onlineFlag = "1";
    private String createBy = "";
    private String chatId = "";


    private int flowerCount;
    private String headFileId;
    private int thumbupCount;
    private int isThumbup;
    private String cover;

    public int getFlowerCount() {
        return flowerCount;
    }

    public void setFlowerCount(int flowerCount) {
        this.flowerCount = flowerCount;
    }

    public String getHeadFileId() {
        return headFileId;
    }

    public void setHeadFileId(String headFileId) {
        this.headFileId = headFileId;
    }

    public int getThumbupCount() {
        return thumbupCount;
    }

    public void setThumbupCount(int thumbupCount) {
        this.thumbupCount = thumbupCount;
    }

    public int getIsThumbup() {
        return isThumbup;
    }

    public void setIsThumbup(int isThumbup) {
        this.isThumbup = isThumbup;
    }



    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }


    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getOnlineFlag() {
        return onlineFlag;
    }

    public void setOnlineFlag(String onlineFlag) {
        this.onlineFlag = onlineFlag;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

}
