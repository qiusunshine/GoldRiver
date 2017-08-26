package com.hdy.goldhe.web;

import android.content.Context;
import android.content.res.Resources;

import com.hdy.goldhe.R;


/**
 * Created by BrainWang on 05/01/2016.
 */
public class ADFilterTool {
    public static boolean hasAd(Context context,String url) {
        Resources res =context.getResources();
        String[] adUrls = res.getStringArray(R.array.adBlockUrl);
        for (String adUrl : adUrls) {
            if (url.contains(adUrl)) {
                return true;
            }
        }
        return false;
    }


}