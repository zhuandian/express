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
