package com.hdy.goldhe.Util;

import android.content.Context;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by hdy on 2017/5/23.
 */

public class FileUtil {
    //加载美女分类标签
    public static String load(Context context,String path) {
        FileInputStream in;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            in =context.openFileInput(path);
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }
    public static boolean save(Context context,String path,String inputData) {
        FileOutputStream out;
        BufferedWriter writer = null;
        try {
            out = context.openFileOutput(path, Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(inputData);
        } catch (IOException e) {
            return false;
        }
        finally {
            try {
                if
                        (writer != null) { writer.close();
                } } catch (IOException e) {
                return false; } }
        return true;
    }

    //创建方法将输入的内容发出去
    public static void sendtext(Context context,String str){
        Intent intent = new Intent();
        /*设置action为发送分享，
        *并判断要发送分享的内容是否为空
         */
        intent.setAction(Intent.ACTION_SEND);
        if(str!=null){
            intent.putExtra(Intent.EXTRA_TEXT,str);
        }else{
            intent.putExtra(Intent.EXTRA_TEXT,"");
        }
        intent.setType("text/plain");//设置分享发送的数据类型
        //未指定选择器，部分定制系统首次选择后，后期将无法再次改变
        //        startActivity(intent);
        //指定选择器选择使用有发送文本功能的App
        context.startActivity(Intent.createChooser(intent,"分享"));
    }
}
