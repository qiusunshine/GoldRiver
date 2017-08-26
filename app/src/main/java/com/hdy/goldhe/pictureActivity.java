package com.hdy.goldhe;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hdy.goldhe.Base.BaseActivity;
import com.hdy.goldhe.Util.DownPicUtil;
import com.hdy.goldhe.View.TouchImageView;
import com.jingewenku.abrahamcaijin.commonutil.AppToastMgr;
import com.umeng.analytics.MobclickAgent;
import java.io.FileNotFoundException;
import cn.refactor.lib.colordialog.ColorDialog;

public class pictureActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        /**
         * 设置actionbar
         */
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("妹子图");
        Intent intent=this.getIntent();
        final String picurl=intent.getStringExtra("picurl");
        /**
        * 设置图片内容和长按下载事件
        */
        TouchImageView touchImageView=(TouchImageView)findViewById(R.id.touchimageview);
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.bizhi)
                .error(R.drawable.bizhi);
        Glide.with(pictureActivity.this)
                .load(picurl)
                .apply(options)
                .into(touchImageView);
        touchImageView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                // 弹出保存图片的对话框
                ColorDialog dialog = new ColorDialog(pictureActivity.this);
                dialog.setColor("#009688");
                dialog.setAnimationEnable(true);
                dialog.setTitle("下载图片");
                dialog.setContentText("是否下载图片到本地?");
                dialog.setPositiveListener("下载", new ColorDialog.OnPositiveListener() {
                    @Override
                    public void onClick(ColorDialog dialog) {
                        dialog.dismiss();
                        // 下载图片到本地
                        DownPicUtil.downPic(picurl, new DownPicUtil.DownFinishListener() {
                            public void getDownPath(String s) {
                                AppToastMgr.ToastShortBottomCenter(pictureActivity.this,"下载完成");
                                Message msg = Message.obtain();
                                msg.obj = s;
                                handler.sendMessage(msg);
                            }
                        });
                    }
                });
                dialog.setNegativeListener("取消",new ColorDialog.OnNegativeListener(){
                    @Override
                    public void onClick(ColorDialog dialog) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return true;
            }
        });


    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String picFile = (String) msg.obj;
            String[] split = picFile.split("/");
            String fileName = split[split.length - 1];
            try {
                MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), picFile, fileName, null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // 最后通知图库更新
            getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + picFile)));
            AppToastMgr.ToastShortBottomCenter(pictureActivity.this,"图片已保存到图库");
        }
    };

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // TODO Auto-generated method stub
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
