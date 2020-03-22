package com.zhuandian.express;

import android.app.Application;

import cn.bmob.v3.Bmob;

/**
 * desc :
 * author：xiedong
 * date：2020/03/20
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, "671ecd140c94619fe30e829b401540d6");
    }
}
