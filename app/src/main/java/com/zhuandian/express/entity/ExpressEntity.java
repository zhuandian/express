package com.zhuandian.express.entity;

import cn.bmob.v3.BmobObject;

/**
 * desc :
 * author：xiedong
 * date：2020/03/21
 */
public class ExpressEntity extends BmobObject {
    private int state;  //1，正常，2.超时
    private String Type;
    private String title;
    private String content;
    private String endTime;
    private String userId;
    private String local;
    private String expressId;
    private String password;

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getExpressId() {
        return expressId;
    }

    public void setExpressId(String expressId) {
        this.expressId = expressId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }


    public String getType() {
        return Type;
    }

    public void setType(String type) {
        this.Type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
