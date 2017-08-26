package com.hdy.goldhe.web;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hdy.goldhe.R;
import com.jingewenku.abrahamcaijin.commonutil.AppToastMgr;

/**
 * Created by hdy on 2017/5/24.
 */

public class BaseWebviewClient extends WebViewClient {
    private Context context;
    private WebView cc;
    private Activity  activity;
         public BaseWebviewClient(Activity activity,Context context, WebView cc) {
                this.cc=cc;
                this.context=context;
                this.activity=activity;
          }
        public WebResourceResponse shouldInterceptRequest(WebView view, String url){
            try {
                //判断是否含有广告，广告文件在values/adblock文件里面
                if (!ADFilterTool.hasAd(context,url)) {

                    return super.shouldInterceptRequest(view, url);
                }else{
                    return new WebResourceResponse(null,null,null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return super.shouldInterceptRequest(view, url);
            }
        }
        @Override
        public void onPageFinished(WebView view, String url) {
                String js;
                Resources res =context.getResources();
                String[] adDivs = res.getStringArray(R.array.adBlockDiv);
                for(String adDiv : adDivs){
                    js = "javascript:function setTop(){document.querySelector('"+adDiv+"').style.display=\"none\";}setTop();";
                    cc.loadUrl(js);
                }
        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
        }
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            cc.loadUrl("file:///android_asset/error.html");
        }
        //加载https错误让它继续加载而不是显示空白
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//      一定要注释掉！
//      super.onReceivedSslError(view, handler, error);
            handler.proceed();
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, final String url) {

            if( url.startsWith("http:") || url.startsWith("https:")||url.contains("file:///android_asset") ) {
                return false;
            }
            else {
                Snackbar.make(cc, "允许网页打开相应软件？", Snackbar.LENGTH_LONG)
                        .setAction("打开", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    context.startActivity(intent);
                                } catch (Exception e) {
                                    AppToastMgr.ToastShortBottomCenter(context,"您的手机没有安装该软件！");
                                }
                            }
                        })
                        .show();
                return true;
            }
        }
}
