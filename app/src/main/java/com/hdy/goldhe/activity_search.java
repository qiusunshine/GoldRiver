package com.hdy.goldhe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hdy.goldhe.Base.BaseActivity;
import com.hdy.goldhe.Util.ClipBoard;
import com.hdy.goldhe.Util.FileUtil;
import com.jingewenku.abrahamcaijin.commonutil.AppKeyBoardMgr;
import com.jingewenku.abrahamcaijin.commonutil.AppToastMgr;
import com.jingewenku.abrahamcaijin.commonutil.AppValidationMgr;
import com.umeng.analytics.MobclickAgent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class activity_search extends BaseActivity {

    @InjectView(R.id.searchn_EditText)
    AutoCompleteTextView searchnEditText;
    @InjectView(R.id.search_jiantieban)
    TextView jiantieban;
    @InjectView(R.id.search_listview)
    ListView listview;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> string=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        ButterKnife.inject(this);
        initjiantieban();
        inithistory();
        jiantieban.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                search(2, ClipBoard.getClipboard(activity_search.this));
                return true;
            }
        });

        initAutoCompleteEditText();
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    @OnClick({R.id.search_text1, R.id.search_text2, R.id.search_text3,R.id.search_jiantieban,R.id.search_text4,R.id.search_text_csdn,R.id.search_text_weibo,R.id.search_text_github,R.id.search_text_zhihu})
    public void onViewClicked(View view) {
        String word=searchnEditText.getText().toString();
        switch (view.getId()) {
            case R.id.search_text1:
                savehistory();
                search(1,word);
                break;
            case R.id.search_text2:
                savehistory();
                search(2,word);
                break;
            case R.id.search_text3:
                savehistory();
                search(3,word);
                break;
            case R.id.search_text4:
                savehistory();
                search(4,word);
                break;
            case R.id.search_text_csdn:
                savehistory();
                search(5,word);
                break;
            case R.id.search_text_weibo:
                savehistory();
                search(6,word);
                break;
            case R.id.search_text_github:
                savehistory();
                search(7,word);
                break;
            case R.id.search_text_zhihu:
                savehistory();
                search(8,word);
                break;
            case R.id.search_jiantieban:
                search(1,ClipBoard.getClipboard(this));
                break;
            default:break;

        }
    }
    public void initjiantieban(){
       String clip=ClipBoard.getClipboard(this);
        String text="点击搜索网盘："+clip;
        if(AppValidationMgr.isChinese(clip)||AppValidationMgr.isLetter(clip)||AppValidationMgr.isContainChinese(clip))
        jiantieban.setText(text);
        else jiantieban.setText("这里是快捷搜索键-识别剪贴板");
    }
    public void search(int type,String string){
        String url="http://www.baidu.com";
        switch (type){
            case 1:
                //搜索网盘
                url="https://www.panc.cc/m/s/?s="+string;
                break;
            case 2:
                //搜索磁力
                url="http://www.btkuaisou.org/word/"+string+".html";
                break;
            case 3:
                //搜索豆瓣
                url="https://m.douban.com/search/?query="+string;
                break;
            case 5:
                //搜索CSDN
                url="http://m.blog.csdn.net/search/index?keyword="+string;
                break;
            case 6:
                //搜索微博
                url="http://s.weibo.com/weibo/"+string;
                break;
            case 7:
                //搜索github
                url="https://github.com/search?utf8=%E2%9C%93&q="+string+"&ref=simplesearch";
                break;
            case 8:
                //搜索知乎
                url="https://www.zhihu.com/search?type=content&q="+string;
                break;
            case 4:
                //搜索小马
                getxiaoma("http://nav.api.sbxia.com/nav/search/list?version_code=21&api_version=1&q="+string);
                return;
        }
        Intent intent=new Intent();
        intent.putExtra("url",url);
        intent.setClass(this,webActivity.class);
        startActivity(intent);
    }
    public void getxiaoma(String url){
        RequestQueue requestQueue= Volley.newRequestQueue(activity_search.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject data3=response.getJSONObject("result");
                    JSONArray data = data3.getJSONArray("list");
                    JSONObject data2=data.getJSONObject(0);
                    String back= data2.getString("link");
                    Intent intent=new Intent();
                    intent.putExtra("url",back);
                    intent.setClass(activity_search.this,webActivity.class);
                    startActivity(intent);
                } catch (JSONException e) {
                    AppKeyBoardMgr.closeKeybord(searchnEditText,activity_search.this);
                   AppToastMgr.ToastShortBottomCenter(activity_search.this,"加载失败!");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
    public void inithistory(){
        List<Map<String, Object>> listItems5 = new ArrayList<Map<String, Object>>();
        String history= FileUtil.load(this,"searchhistory");
        if(history.length()>1) {

            if(!history.contains("#")){
                    Map<String, Object> map3 = new HashMap<String, Object>();
                    map3.put("text",history );  //图片资源
                    //物品详情
                    listItems5.add(map3);
            }
            else {
                String[] historyitems=history.split("#");
                for(int i=0;i<historyitems.length;i++){
                    Map<String, Object> map3 = new HashMap<String, Object>();
                    map3.put("text",historyitems[i]);  //图片资源
                    //物品详情
                    listItems5.add(map3);
                    string.add(historyitems[i]);
                }
            }

            SimpleAdapter mSimpleAdapter2 = new SimpleAdapter(activity_search.this, listItems5, R.layout.history_item, new String[]{"text"}, new int[]{R.id.historyheader});
            listview.setAdapter(mSimpleAdapter2);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                    TextView kkk = (TextView) view.findViewById(R.id.historyheader);
                    searchnEditText.setText(kkk.getText().toString());
                }
            });
            adapter = new ArrayAdapter<>(activity_search.this, android.R.layout.simple_list_item_1, string);
            searchnEditText.setAdapter(adapter);
        }
    }
    public void savehistory(){
        String history=FileUtil.load(this,"searchhistory");
        String hisitem=searchnEditText.getText().toString();
        //存在历史记录
        if (history.length()>1) {
            if(!(history.contains(hisitem))) {
                if (!history.contains("#")) {
                    String hist3 = hisitem + "#" + history;
                    FileUtil.save(this, "searchhistory", hist3);
                } else {
                    String[] hisstr = history.split("#");
                    //超过10条历史记录
                    if (hisstr.length >= 10) {
                        String hist = hisitem + "#" + hisstr[0] + "#" + hisstr[1] + "#" + hisstr[2] + "#" + hisstr[3] + "#" + hisstr[4] + "#" + hisstr[5] + "#" + hisstr[6] + "#" + hisstr[7] + "#" + hisstr[8];
                        FileUtil.save(this, "searchhistory", hist);
                    }
                    //小雨十条历史记录
                    else {
                        String hist2 = hisitem + "#" + history;
                        FileUtil.save(this, "searchhistory", hist2);
                    }
                }
            }
        }
        //还不存在历史记录
        else {
            FileUtil.save(this,"searchhistory",hisitem);
        }
    }
    public void initAutoCompleteEditText(){
        StringRequest stringRequest=new StringRequest(Request.Method.GET,"https://so.ygyhg.com/",new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    String[] a=response.split("戳我查看更多内容");
                    String[] b=a[1].split("友情链接");
                    String[] c=b[0].split("\" target=\"_blank\">");
                    for (int i=1;i<c.length;i++){
                        String[] title1=c[i].split("</a><div");
                        String title=title1[0].trim();
                        if(!(string.toString().contains(title)))
                        string.add(title);
                    }
                    adapter.notifyDataSetChanged();
                }catch (Exception ignored) {
                }
            }
        },new Response.ErrorListener(){
            @Override
        public void onErrorResponse(VolleyError error) {

            }});
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
