package com.hdy.goldhe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hdy.goldhe.Base.BaseActivity;
import com.jingewenku.abrahamcaijin.commonutil.AppToastMgr;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by hdy on 2017/6/3.
 */

public class activity_moviedetail extends BaseActivity {
    private String titletext;
    @InjectView(R.id.imageView)
    ImageView imageView;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.detail)
    TextView detail;
    @InjectView(R.id.detail2)
    TextView detail2;
    @InjectView(R.id.newtoobar)
    Toolbar toolbar;
    @InjectView(R.id.newtoobar_title)
    TextView newtoobar_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_douban_detail);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);//初始化toolbar
        String id=getId();    //获取前一个界面传来的id
        getdetail(id,getIntent().getStringExtra("detail")); //获取detail
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    @OnClick({R.id.searchpan,R.id.searchci,R.id.newtoobar_back,R.id.newtoobar_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.searchpan:
                String url="https://www.panc.cc/m/s/?s="+titletext;
                Intent intent=new Intent();
                intent.putExtra("url",url);
                intent.setClass(this,webActivity.class);
                startActivity(intent);
                break;
            case R.id.searchci:
                String url2="http://www.btkuaisou.org/word/"+titletext+".html";
                Intent intent2=new Intent();
                intent2.putExtra("url",url2);
                intent2.setClass(this,webActivity.class);
                startActivity(intent2);
                break;
            case R.id.newtoobar_menu:
                AppToastMgr.ToastShortBottomCenter(this,"未开发");
                break;
            case R.id.newtoobar_back:
                finish();
                break;
        }
    }
    //获取前一个界面传来的id
    public String getId(){
        Intent intent=getIntent();
        return intent.getStringExtra("id");
    }
    //获取detail
    public void getdetail(String id, final String detailget){
       final RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url="http://api.douban.com/v2/movie/subject/" + id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String pic=response.getJSONObject("images").getString("large");
                    titletext=response.getString("title");
                    String detailtext=response.getString("summary");
                    title.setText(titletext);
                    detail.setText(detailget);
                    detail2.setText("简介:\n\n"+detailtext+"\n\n\n");
                    newtoobar_title.setText(titletext);
                    ImageRequest imageRequest=new ImageRequest(
                            pic,
                            new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap response) {
                                    imageView.setImageBitmap(response);
                                    final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.movie_detail_Linearlayout);
                                    Palette.from(response).generate(new Palette.PaletteAsyncListener() {
                                        @Override
                                        public void onGenerated(Palette palette) {
                                            Palette.Swatch swatch = palette.getMutedSwatch();
                                            if (swatch != null) {
                                                linearLayout.setBackgroundColor(swatch.getRgb());
                                                toolbar.setBackgroundColor(swatch.getRgb());
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                                    getWindow().setStatusBarColor(swatch.getRgb());
                                                }
                                            }
                                        }
                                    });

                                }
                            }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });
                    RequestQueue requestQueue = Volley.newRequestQueue(activity_moviedetail.this);
                    requestQueue.add(imageRequest);
                  /*  RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.bizhi)
                            .error(R.drawable.bizhi)
                            .diskCacheStrategy(DiskCacheStrategy.NONE);
                    Glide.with(activity_moviedetail.this)
                            .asBitmap()
                            .load(pic)
                            .apply(options)
                            .into(imageView);
                    */
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
