package com.hdy.goldhe.Util;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * Created by hdy on 2017/5/23.
 */

public class ClipBoard {
    //获取剪贴板函数封装
    public static String getClipboard(Activity activity) {
        ClipboardManager clipManager = (ClipboardManager)activity.getSystemService(Context.CLIPBOARD_SERVICE);
        if(!clipManager.hasPrimaryClip()) {
            return "";
        }
        ClipData clip = clipManager.getPrimaryClip();
//获取 text
        String text = clip.getItemAt(0).getText().toString();
        return text;
    }
    public static void setClipboard(Context context,String string){
        ClipboardManager clipManager = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("Label", string);
        // 将ClipData内容放到系统剪贴板里。
        clipManager.setPrimaryClip(mClipData);
    }
}
