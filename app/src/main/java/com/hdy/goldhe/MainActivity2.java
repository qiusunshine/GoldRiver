package com.hdy.goldhe;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.hdy.goldhe.Fragment.FragmentController;
import com.jingewenku.abrahamcaijin.commonutil.AppToastMgr;
import com.umeng.analytics.MobclickAgent;

public class MainActivity2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
private FragmentController fragmentController;
    private Toolbar toolbar;
    private boolean loadpicture=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MainAppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置toolbar
        toolbar= (Toolbar) findViewById(R.id.new2toolbar);
        setSupportActionBar(toolbar);
        fragmentController=new FragmentController(this,R.id.kaifafragment);
        fragmentController.showFragment(0);
//设置侧边栏展开
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //设置标题为阅读
        setTitle("轻阅读");
        //设置侧滑菜单view
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        /**设置MenuItem的字体颜色**/
        Resources resource= getBaseContext().getResources();
        ColorStateList csl= resource.getColorStateList(R.color.navigatation_item_color);
        navigationView.setItemTextColor(csl);
        navigationView.setItemIconTintList(csl);
        try {
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.colorPrimary));
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Snackbar.make(drawer,"确定退出软件？",Snackbar.LENGTH_SHORT).setAction("退出", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   finish();
                }
            }).show();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_picture) {
            if(loadpicture) {
                item.setTitle("正常加载图片");
                loadpicture=false;
                AppToastMgr.ToastShortBottomCenter(MainActivity2.this,"已开启自动无图");
            }
            else {
                item.setTitle("自动无图");
                loadpicture=true;
                AppToastMgr.ToastShortBottomCenter(MainActivity2.this,"已关闭自动无图");
            }
            return true;
        }
        else if(id==R.id.action_search){
            Intent newintent=new Intent();
            newintent.setClass(MainActivity2.this,activity_search.class);
            startActivity(newintent);
            return true;
        }
        else if(id==R.id.nav_setting){
            //设置
            Intent intent4=new Intent();
            intent4.setClass(MainActivity2.this,SettingsActivity.class);
            startActivity(intent4);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id){
            case R.id.kaifabottom:
               fragmentController.showFragment(2);
                setTitle(R.string.kaifazhe);
                break;
            case R.id.readbottom:
             fragmentController.showFragment(0);
                setTitle(R.string.read);
                break;
            case R.id.movie2bottom:
                fragmentController.showFragment(1);
                setTitle(R.string.jumovie);
                break;
            case R.id.meizitubottom:
                fragmentController.showFragment(3);
                setTitle(R.string.meizitu);
                break;
            case R.id.kaifadaohangbottom:
                //音乐搜索
                Intent intentr=new Intent();
                intentr.setClass(MainActivity2.this,webActivity.class);
                intentr.putExtra("url","http://www.jcodecraeer.com/hao");
                intentr.putExtra("loadpicture",Loadpicture());
                startActivity(intentr);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.nav_searchmusic:
                //音乐搜索
                Intent intent=new Intent();
                intent.setClass(MainActivity2.this,webActivity.class);
                intent.putExtra("url","http://music.2333.me/");
                intent.putExtra("loadpicture",Loadpicture());
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.nav_qqfm:
                //企鹅点歌台
                Intent intent3=new Intent();
                intent3.setClass(MainActivity2.this,webActivity.class);
                intent3.putExtra("url","http://fm.qq.com/album/rd002NEekz2YrFWg");
                intent3.putExtra("loadpicture",Loadpicture());
                startActivity(intent3);
               overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.nav_doubiekan:
                //逗别看嘿嘿电影啦
                Intent intent2=new Intent();
                intent2.setClass(MainActivity2.this,webActivity.class);
                intent2.putExtra("url","http://www.doubiekan.org/");
                intent2.putExtra("loadpicture",Loadpicture());
                startActivity(intent2);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            default: break;

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public boolean Loadpicture(){
        return loadpicture;
    }
    public void settitle(String title){
        toolbar.setTitle(title);
    }

}
