package com.hdy.goldhe.web;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.Toast;

import static android.view.View.VISIBLE;

/**
 * Created by hdy on 2017/5/25.
 */

public class BaseWebChromeClient extends WebChromeClient{
    private WebView cc;
    private Context context;
    private Activity activity;
    private ValueCallback<Uri[]> uploadMessage;
    private static final int REQUEST_SELECT_FILE = 100;
    /** 视频全屏参数 */
    private static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    private View customView;
    private FrameLayout fullscreenContainer;
    private CustomViewCallback customViewCallback;
    public BaseWebChromeClient(Activity activity, Context context, WebView cc) {
        this.cc=cc;
        this.context=context;
        this.activity=activity;
    }
        /*** 视频播放相关的方法 **/
        @Override
        public View getVideoLoadingProgressView() {
            FrameLayout frameLayout = new FrameLayout(context);
            frameLayout.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));
            return frameLayout;
        }
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            showCustomView(view, callback);
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        @Override
        public void onHideCustomView() {
            hideCustomView();
        }
//支持图片上传
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public boolean onShowFileChooser(WebView mWebView, ValueCallback< Uri[]> filePathCallback, FileChooserParams
        fileChooserParams) {
            if (uploadMessage != null) {
                uploadMessage.onReceiveValue(null);
                uploadMessage = null;
            }
            uploadMessage = filePathCallback;
            Intent intent = fileChooserParams.createIntent();
            try {
                activity.startActivityForResult(intent, REQUEST_SELECT_FILE);
            } catch (ActivityNotFoundException e) {
                uploadMessage = null;
                Toast.makeText(context, "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
                return false;
            }
            return true;
        }
    /** 视频播放全屏 函数集合**/
    private void showCustomView(View view, CustomViewCallback callback) {

        // if a view already exists then immediately terminate the new one
        if (customView != null) {
            callback.onCustomViewHidden();
            return;
        }
        activity.getWindow().getDecorView();
        FrameLayout decor = (FrameLayout)activity.getWindow().getDecorView();
        fullscreenContainer = new FullscreenHolder(context);
        fullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
        decor.addView(fullscreenContainer, COVER_SCREEN_PARAMS);
        customView = view;
        setStatusBarVisibility(false);
        customViewCallback = callback;
    }
    /** 隐藏视频全屏 */
    private void hideCustomView() {
        if (customView == null) {
            return;
        }
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setStatusBarVisibility(true);
        FrameLayout decor = (FrameLayout) activity.getWindow().getDecorView();
        decor.removeView(fullscreenContainer);
        fullscreenContainer = null;
        customView = null;
        customViewCallback.onCustomViewHidden();
        cc.setVisibility(VISIBLE);
    }
    /** 全屏容器界面 */
    static class FullscreenHolder extends FrameLayout {

        public FullscreenHolder(Context ctx) {
            super(ctx);
            setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
        }
        @Override
        public boolean onTouchEvent(MotionEvent evt) {
            return true;
        }
    }
    private void setStatusBarVisibility(boolean visible) {
        int flag = visible ? 0 : WindowManager.LayoutParams.FLAG_FULLSCREEN;
        activity.getWindow().setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
