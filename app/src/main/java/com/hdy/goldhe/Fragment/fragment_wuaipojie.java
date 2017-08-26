package com.hdy.goldhe.Fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hdy.goldhe.Adapter.WuaipojieAdapter;
import com.hdy.goldhe.Base.Fragment_Base;
import com.hdy.goldhe.MainActivity2;
import com.hdy.goldhe.R;
import com.hdy.goldhe.webActivity;
import com.jingewenku.abrahamcaijin.commonutil.AppToastMgr;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by hdy on 2017/8/17.
 */

public class fragment_wuaipojie extends Fragment_Base {
    private ListView listView;
    private RequestQueue requestQueue;
    private View footer;
    private int number=2;
    private JSONArray dataarry=new JSONArray();
    private SwipeRefreshLayout swipeRefreshLayout;
    private WuaipojieAdapter WuaipojieAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_wuaipojie;
    }

    @Override
    protected void initView() {
        listView=findView(R.id.wuaipojie_listview);
        requestQueue = Volley.newRequestQueue(getContext());
        footer = LayoutInflater.from(getContext()).inflate(R.layout.loadmorefooter,null);
        swipeRefreshLayout=findView(R.id.swipeRefreshLayout7);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.redColor),getResources().getColor(R.color.colorAccent),getResources().getColor(R.color.halfblack));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getlist(2,true);
            }
        });
    }

    @Override
    protected void initData() {
        getlist(2,false);
        listView.addFooterView(footer);
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView load= findView(R.id.loadmre_footer);
                load.setText(R.string.loading);
                number++;
                getlist(number,false);
                WuaipojieAdapter.notifyDataSetChanged();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    String url=dataarry.getJSONObject(i).getString("url");
                    Intent intent=new Intent();
                    intent.setClass(getActivity(),webActivity.class);
                    intent.putExtra("url",url);
                    intent.putExtra("loadpicture",((MainActivity2)getActivity()).Loadpicture());
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i){
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if(absListView.getLastVisiblePosition()==(absListView.getCount()-1)&&listView.getFooterViewsCount()<1) {
                            listView.addFooterView(footer);
                        }
                        break;
                }
            }
            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
            }
        });
    }
    public void getlist(int thisnumber,boolean isrefresh){
        swipeRefreshLayout.setRefreshing(true);
        if(isrefresh) {
            dataarry =new JSONArray();
            number=2;
        }
        String url = "https://www.52pojie.cn/forum.php?mod=forumdisplay&fid=16&page="+thisnumber+"&mobile=2";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Document doc = Jsoup.parse(response);
                        Elements threadlist = doc.select("li");
                        for(Element thread:threadlist){
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("title",thread.text().replace(thread.select("span.by").text(),"").replace(thread.select("span.num").text(),""));
                                jsonObject.put("url","https://www.52pojie.cn/"+thread.select("a").attr("href"));
                                jsonObject.put("detail",thread.select("span.num").text()+"人查看");
                                dataarry.put(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if(number==2){
                            WuaipojieAdapter = new WuaipojieAdapter(getContext(), dataarry);
                            listView.setAdapter(WuaipojieAdapter);
                        }
                        TextView load=findView(R.id.loadmre_footer);
                        load.setText("下一页");
                        listView.removeFooterView(footer);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Error handling
                swipeRefreshLayout.setRefreshing(false);
                AppToastMgr.ToastShortCenter(getContext(),"加载失败！");

            }
        });
        requestQueue.add(stringRequest);
    }

}
