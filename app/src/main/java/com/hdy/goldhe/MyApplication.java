package com.hdy.goldhe;

import android.app.Application;

import com.jingewenku.abrahamcaijin.commonutil.application.AppUtils;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackManager;

/**
 * Created by hdy on 2017/4/17.
 */

public class MyApplication extends Application {
    private static MyApplication singleton;

    public static MyApplication getInstance() {
        return singleton;
    }

    @Override
    public final void onCreate() {
        super.onCreate();
        AppUtils.init(this);
        singleton = this;
// 必须在 Application 的 onCreate 方法中执行 BGASwipeBackManager.getInstance().init(this) 来初始化滑动返回
        BGASwipeBackManager.getInstance().init(this);
    }
}