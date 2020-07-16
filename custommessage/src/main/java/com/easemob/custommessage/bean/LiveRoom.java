package com.easemob.custommessage.bean;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by wei on 2016/5/27.
 */
public class LiveRoom extends BaseBean implements Serializable {
    @SerializedName(value = "liveroom_id", alternate = {"id"})
    private String id;
    private String name;
    @SerializedName("affiliations_count")
    private Integer audienceNum;
    private String cover;
    @SerializedName("chatroom_id")
    private String chatroomId;
    private String owner;
    private String description;
    @SerializedName("mobile_push_url")
    private String livePushUrl;
    @SerializedName("mobile_pull_url")
    private String livePullUrl;
    private String status;
    private Map ext;
    private Integer maxusers;
    private String[] members;
    //@SerializedName("liveshow_id")
    //private String showId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAudienceNum() {
        return audienceNum == null?0:audienceNum;
    }

    public void setAudienceNum(Integer audienceNum) {
        this.audienceNum = audienceNum;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getChatroomId() {
        return chatroomId;
    }

    public void setChatroomId(String chatroomId) {
        this.chatroomId = chatroomId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLivePushUrl() {
        return livePushUrl;
    }

    public void setLivePushUrl(String livePushUrl) {
        this.livePushUrl = livePushUrl;
    }

    public String getLivePullUrl() {
        return livePullUrl;
    }

    public void setLivePullUrl(String livePullUrl) {
        this.livePullUrl = livePullUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map getExt() {
        return ext;
    }

    public void setExt(Map ext) {
        this.ext = ext;
    }

    public int getMaxusers() {
        return maxusers == null ? 0 : maxusers;
    }

    public void setMaxusers(Integer maxusers) {
        this.maxusers = maxusers;
    }

    public String[] getMembers() {
        return members;
    }

    public void setMembers(String[] members) {
        this.members = members;
    }

    //public String getShowId() {
    //    return showId;
    //}
    //
    //public void setShowId(String showId) {
    //    this.showId = showId;
    //}

    public boolean isLiving() {
        return !TextUtils.isEmpty(status) && TextUtils.equals(status, "ongoing");
    }
}
