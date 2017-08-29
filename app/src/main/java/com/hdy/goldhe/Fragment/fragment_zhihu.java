package com.hdy.goldhe.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.hdy.goldhe.Adapter.zhihuadapter;
import com.hdy.goldhe.NewOne.Activity.OneMainActivity;
import com.hdy.goldhe.R;
import com.hdy.goldhe.webActivity;
import com.jingewenku.abrahamcaijin.commonutil.AppDateMgr;
import com.jingewenku.abrahamcaijin.commonutil.AppToastMgr;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * Created by hdy on 2017/5/23.
 */

public class fragment_zhihu extends Fragment {
    private boolean isVisible = false, isInitView = false, isFirstLoad = true;
    private View convertView;
    private SparseArray<View> mViews;
    private ListView listView;
    private JSONArray dataarry=new JSONArray();
    private int number=1;
    private  RequestQueue requestQueue;
    private zhihuadapter zhihuadapter;
    private View footer;
    private int date=20170710;
    private View header;
    private SwipeRefreshLayout swipeRefreshLayout;
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
        if (!isVisible || !isInitView) {
            return;
        }
        initData();
        isFirstLoad = false;
    }

    /**
     * 加载页面布局文件
     *
     * @return
     */
    protected int getLayoutId() {
        return R.layout.fragment_zhihu;
    }

    /**
     * 让布局中的view与fragment中的变量建立起映射
     */
    protected void initView() {
        listView = findView(R.id.zhihu_listview);
        requestQueue = Volley.newRequestQueue(getContext());
        footer = LayoutInflater.from(getContext()).inflate(R.layout.loadmorefooter,null);
        header = LayoutInflater.from(getContext()).inflate(R.layout.listviewhearder, null);
        swipeRefreshLayout=findView(R.id.swipeRefreshLayout2);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.redColor),getResources().getColor(R.color.colorAccent),getResources().getColor(R.color.halfblack));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                number=1;
                dataarry=new JSONArray();
                getlist(1,date,true);
            }
        });
    }

    /**
     * 加载要显示的数据
     */
    protected void initData() {
        getlist(1,date,false);
        listView.addFooterView(footer);
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView load= findView(R.id.loadmre_footer);
                load.setText(R.string.loading);
                number++;
                getlist(number,date,false);
                zhihuadapter.notifyDataSetChanged();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    if(i==0)return;
                    String id=dataarry.getJSONObject(i-1).getString("id");
                    Intent intent=new Intent();
                    intent.setClass(getActivity(),webActivity.class);
                    intent.putExtra("url","http://daily.zhihu.com/story/"+id);
                    intent.putExtra("loadpicture",((OneMainActivity)getActivity()).Loadpicture());
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

    /**
     * fragment中可以通过这个方法直接找到需要的view，而不需要进行类型强转
     *
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

    public void getlist(int j, int thisdate, final boolean isrefresh) {
        swipeRefreshLayout.setRefreshing(true);
        String url;
        if(j==1) {
            url = "https://news-at.zhihu.com/api/4/news/latest";
            if(!isrefresh)listView.addHeaderView(header);
            String date2= AppDateMgr.todayYyyyMmDd();
            TextView dateheader = findView(R.id.listviewheader);
            dateheader.setText(date2);
        }
        else url="https://news-at.zhihu.com/api/4/news/before/"+thisdate;
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    date=response.getInt("date");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONArray data;
                try {
                    data = response.getJSONArray("stories");
                    for (int i = 0; i < data.length(); i++) {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            JSONObject netjsonObject = data.getJSONObject(i);
                            jsonObject.put("title", netjsonObject.getString("title"));
                            jsonObject.put("id", netjsonObject.getString("id"));
                            jsonObject.put("pic", netjsonObject.getJSONArray("images").getString(0));
                            dataarry.put(jsonObject);
                        } catch (JSONException ignored) {
                        }
                    }
                    if(number==1) {
                            zhihuadapter = new zhihuadapter(getContext(), dataarry, ((OneMainActivity) getActivity()).Loadpicture());
                            listView.setAdapter(zhihuadapter);
                    }
                    TextView load=findView(R.id.loadmre_footer);
                    load.setText("下一页");
                    listView.removeFooterView(footer);
                } catch (JSONException e) {
                    AppToastMgr.ToastShortBottomCenter(getContext(),"加载失败！");

                }
                swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                swipeRefreshLayout.setRefreshing(false);
                AppToastMgr.ToastShortCenter(getContext(),"加载失败！");
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

}