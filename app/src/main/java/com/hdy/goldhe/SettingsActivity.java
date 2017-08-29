package com.hdy.goldhe;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.hdy.goldhe.Base.BaseActivity;
import com.hdy.goldhe.Util.GlideCacheUtil;
import com.jingewenku.abrahamcaijin.commonutil.AppToastMgr;
import com.leon.lib.settingview.LSettingItem;
import com.umeng.analytics.MobclickAgent;

import cn.refactor.lib.colordialog.ColorDialog;

public class SettingsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("设置");
        setContentView(R.layout.activity_settings2);
        LSettingItem mSettingItemOne = (LSettingItem) findViewById(R.id.item_one);
        mSettingItemOne.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                //分享
                AppToastMgr.ToastShortBottomCenter(SettingsActivity.this, "还没开发啦，用QQ分享试试");
            }
        });
        LSettingItem mSettingItemTwo = (LSettingItem) findViewById(R.id.item_two);
        mSettingItemTwo.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                //加群
                joinQQGroup("06-yvbCCDaR4OlcM9NayzcR1akyzMWsv");
            }
        });
        LSettingItem mSettingItembanben = (LSettingItem) findViewById(R.id.item_banben);
        mSettingItembanben.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                //版本更新
                Intent intent=new Intent();
                intent.setClass(SettingsActivity.this,webActivity.class);
                intent.putExtra("url","https://qiusunshine.github.io/HdyLove/goldriver/update.html");
                startActivity(intent);
            }
        });
        final LSettingItem mSettingItemhuancun = (LSettingItem) findViewById(R.id.item_huancun);
        mSettingItemhuancun.setRightText(GlideCacheUtil.getInstance().getCacheSize(SettingsActivity
                .this));
        mSettingItemhuancun.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                //清除缓存
                AppToastMgr.ToastShortBottomCenter(SettingsActivity.this,"正在清理缓存");
                GlideCacheUtil.getInstance().clearImageDiskCache(SettingsActivity.this);
                AppToastMgr.ToastShortBottomCenter(SettingsActivity.this,"已清理完毕");
                mSettingItemhuancun.setRightText("0.0Byte");

            }
        });

        LSettingItem mSettingItemabout = (LSettingItem) findViewById(R.id.item_about);
        mSettingItemabout.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                //关于的内容
                // 弹出保存图片的对话框
                ColorDialog dialog = new ColorDialog(SettingsActivity.this);
                dialog.setColor("#009688");
                dialog.setAnimationEnable(true);
                dialog.setTitle(R.string.about);
                dialog.setContentText(getString(R.string.theabout));
                dialog.setPositiveListener("确定", new ColorDialog.OnPositiveListener() {
                    @Override
                    public void onClick(ColorDialog dialog) {
                        dialog.dismiss();
                             }
                        });
                dialog.show();

            }
        });
    }
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
    //加入MYXQQ交流群
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }
}
