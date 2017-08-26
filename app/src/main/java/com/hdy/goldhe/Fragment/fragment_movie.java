package com.hdy.goldhe.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hdy.goldhe.Adapter.movieadapter;
import com.hdy.goldhe.R;
import com.hdy.goldhe.activity_moviedetail;
import com.jingewenku.abrahamcaijin.commonutil.AppToastMgr;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hdy on 2017/5/23.
 */

public class fragment_movie extends Fragment {
    private boolean isVisible = false, isInitView = false, isFirstLoad = true;
    private View convertView;
    private SparseArray<View> mViews;
    private ListView listView;
    private JSONArray dataarry=new JSONArray();
    private int number=0;
    private RequestQueue requestQueue;
    private movieadapter vmovieadapter;
    private View footer;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        convertView = inflater.inflate(getLayoutId(), container, false);
        mViews = new SparseArray<>();
        initView();
        isInitView = true;
        lazyLoadData();
        return convertView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            isVisible = true;
            lazyLoadData();

    } else {
        isVisible = false;
    }
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void lazyLoadData() {
        if (isFirstLoad) {
        } else {
            return;
        }
        if ( !isVisible || !isInitView) {
            return;
        }
        initData();
        isFirstLoad = false;
    }

    /**
     * 加载页面布局文件
     * @return
     */
    protected  int getLayoutId(){
        return R.layout.fragment_movie;
    }

    /**
     * 让布局中的view与fragment中的变量建立起映射
     */
    protected  void initView() {
        listView = findView(R.id.movie_listview);
        requestQueue = Volley.newRequestQueue(getContext());
        footer = LayoutInflater.from(getContext()).inflate(R.layout.loadmorefooter,null);
        initData();
    }

    /**
     * 加载要显示的数据
     */
    protected  void initData(){
        getlist(0);
        listView.addFooterView(footer);
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView load= findView(R.id.loadmre_footer);
                load.setText(R.string.loading);
                number=number+10;
                getlist(number);
                vmovieadapter.notifyDataSetChanged();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    String id=dataarry.getJSONObject(i).getString("id");
                    String title=dataarry.getJSONObject(i).getString("title");
                    Intent intent=new Intent();
                    intent.setClass(getContext(), activity_moviedetail.class);
                    intent.putExtra("id",id);
                    JSONObject Object=dataarry.getJSONObject(i);
                    String detail="上映："+Object.get("year").toString()+"\n评分："+Object.get("rating").toString()+"\n看过人数："+Object.get("collect_count").toString()+"\n类型："+Object.get("genres").toString();
                    intent.putExtra("detail",detail);
                    getContext().startActivity(intent);
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
    /**
     * fragment中可以通过这个方法直接找到需要的view，而不需要进行类型强转
     * @param viewId
     * @param <E>
     * @return
     */
    protected <E extends View> E findView(int viewId) {
        if (convertView != null) {
            E view = (E) mViews.get(viewId);
            if (view == null) {
                view = (E) convertView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return view;
        }
        return null;
    }
    public void getlist(int j) {
        String url="https://api.douban.com/v2/movie/in_theaters?city=%E5%8C%97%E4%BA%AC&start=" + j + "&count=10";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray data;
                try {
                    data = response.getJSONArray("subjects");
                    for (int i = 0; i < data.length(); i++) {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            JSONObject netjsonObject = data.getJSONObject(i);
                            jsonObject.put("title", netjsonObject.getString("title"));
                            jsonObject.put("rating", netjsonObject.getJSONObject("rating").getString("average"));
                            jsonObject.put("year",netjsonObject.getString("year"));
                            jsonObject.put("id",netjsonObject.getString("id"));
                            jsonObject.put("collect_count",netjsonObject.getString("collect_count"));
                            jsonObject.put("genres",netjsonObject.getString("genres").replace("\"", "").replace("\\", "").replace("\"", "").replace("[", "").replace("]", "").replace(",", "/"));
                            jsonObject.put("pic", netjsonObject.getJSONObject("images").getString("large").replace("\"", "").replace("\\", "").replace("\"", "").replace("[", "").replace("]", ""));
                            dataarry.put(jsonObject);
                        } catch (JSONException e) {
                            Log.i("", "onResponse: 加载错误!");
                        }
                    }
                    if(number==0){
                        vmovieadapter = new movieadapter(getContext(), dataarry);
                        listView.setAdapter(vmovieadapter);
                    }
                    TextView load=findView(R.id.loadmre_footer);
                    load.setText("下一页");
                    listView.removeFooterView(footer);
                } catch (JSONException e) {
                    AppToastMgr.ToastShortBottomCenter(getContext(),"加载失败!");
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
