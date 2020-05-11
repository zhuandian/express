package com.zhuandian.express.entity;

import cn.bmob.v3.BmobUser;

/**
 * desc :
 * author：xiedong
 * date：2020/03/20
 */
public class UserEntity extends BmobUser {
    private String nikeName;
    private String userInfo;
    private int type;  //1。快递员  非 1 普通用户

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNikeName() {
        return nikeName;
    }

    public void setNikeName(String nikeName) {
        this.nikeName = nikeName;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }
}
