package com.hdy.goldhe.NewOne.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.hdy.goldhe.Fragment.FragmentController;
import com.hdy.goldhe.NewOne.Base.BaseActivityOneMain;
import com.hdy.goldhe.NewOne.Util.BottomNavigationViewHelper;
import com.hdy.goldhe.R;
import com.hdy.goldhe.SettingsActivity;
import com.hdy.goldhe.activity_search;
import com.jingewenku.abrahamcaijin.commonutil.AppToastMgr;

public class OneMainActivity extends BaseActivityOneMain {
    private boolean loadpicture=true;
    private  FragmentController fragmentController;
    @Override
    protected void initView(Bundle savedInstanceState) {
        Toolbar toolbar=findView(R.id.one_toolbar);
        setSupportActionBar(toolbar);
        setContentView(R.layout.one_activity_main);
        fragmentController=new FragmentController(OneMainActivity.this,R.id.one_fragment_child);
        fragmentController.showFragment(0);
    }

    @Override
    protected void setListener() {
        //toolbbar
        final Toolbar toolbar=findView(R.id.one_toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle(R.string.read);
        toolbar.inflateMenu(R.menu.main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_picture) {
                    if (loadpicture) {
                        item.setTitle("正常加载图片");
                        loadpicture = false;
                        AppToastMgr.ToastShortBottomCenter(OneMainActivity.this, "已开启自动无图");
                    } else {
                        item.setTitle("自动无图");
                        loadpicture = true;
                        AppToastMgr.ToastShortBottomCenter(OneMainActivity.this, "已关闭自动无图");
                    }
                }
                else if(id==R.id.action_search){
                    Intent newintent=new Intent();
                    newintent.setClass(OneMainActivity.this,activity_search.class);
                    startActivity(newintent);
                }
                else if(id==R.id.nav_setting){
                    //设置
                    Intent intent4=new Intent();
                    intent4.setClass(OneMainActivity.this,SettingsActivity.class);
                    startActivity(intent4);
                }
                return true;
            }
        });
        //BottomNavigationView
        BottomNavigationView bottomNavigationView=findView(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.readbottom:
                        fragmentController.showFragment(0);
                            toolbar.setTitle(R.string.read);
                        break;
                    case R.id.kaifabottom:
                        fragmentController.showFragment(2);
                            toolbar.setTitle(R.string.kaifazhe);
                        break;
                    case R.id.meizitubottom:
                        fragmentController.showFragment(3);
                            toolbar.setTitle(R.string.meizitu);
                        break;
                    case R.id.movie2bottom:
                        fragmentController.showFragment(1);
                            toolbar.setTitle(R.string.jumovie);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
    public boolean Loadpicture(){
        return loadpicture;
    }
}
