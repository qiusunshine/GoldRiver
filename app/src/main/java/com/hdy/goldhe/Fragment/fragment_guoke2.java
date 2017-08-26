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
import com.hdy.goldhe.Adapter.guokeadapter;
import com.hdy.goldhe.MainActivity2;
import com.hdy.goldhe.R;
import com.hdy.goldhe.webActivity;
import com.jingewenku.abrahamcaijin.commonutil.AppToastMgr;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hdy on 2017/5/23.
 */

public class fragment_guoke2 extends Fragment {
    private boolean isVisible = false, isInitView = false, isFirstLoad = true;
    private View convertView;
    private SparseArray<View> mViews;
    private ListView listView;
    private JSONArray dataarry=new JSONArray();
    private int number=0;
    private  RequestQueue requestQueue;
    private guokeadapter guokeadapter;
    private View footer;
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
        return R.layout.fragment_guoke;
    }

    /**
     * 让布局中的view与fragment中的变量建立起映射
     */
    protected void initView() {
        listView = findView(R.id.guoke_listview);
        requestQueue = Volley.newRequestQueue(getContext());
        footer = LayoutInflater.from(getContext()).inflate(R.layout.loadmorefooter,null);
        swipeRefreshLayout=findView(R.id.swipeRefreshLayout3);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.redColor),getResources().getColor(R.color.colorAccent),getResources().getColor(R.color.halfblack));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getlist(0,true);
            }
        });
    }

    /**
     * 加载要显示的数据
     */
    protected void initData() {
        getlist(number,false);
        listView.addFooterView(footer);
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView load= findView(R.id.loadmre_footer);
                load.setText(R.string.loading);
                number=number+10;
                getlist(number,false);
                guokeadapter.notifyDataSetChanged();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    String id=dataarry.getJSONObject(i).getString("id");
                    Intent intent=new Intent();
                    intent.setClass(getActivity(),webActivity.class);
                    intent.putExtra("url","http://www.guokr.com/article/"+id+"/");
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

    public void getlist(int j, final boolean isrefresh) {
        swipeRefreshLayout.setRefreshing(true);
        if(isrefresh) {
            dataarry = new JSONArray();
            number=0;
        }
        String url="http://apis.guokr.com/minisite/article.json?retrieve_type=by_minisite&channel_key=&offset="+j+"&limit=20";
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray data;
                try {
                    data = response.getJSONArray("result");
                    for (int i = 0; i < data.length(); i++) {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            JSONObject netjsonObject = data.getJSONObject(i);
                            jsonObject.put("title", netjsonObject.getString("title"));
                            jsonObject.put("id", netjsonObject.getString("id"));
                            jsonObject.put("pic", netjsonObject.getJSONObject("image_info").getString("url"));
                            dataarry.put(jsonObject);
                        } catch (JSONException ignored) {
                        }
                    }
                    if(number==0) {
                            guokeadapter = new guokeadapter(getContext(), dataarry, ((MainActivity2) getActivity()).Loadpicture());
                            listView.setAdapter(guokeadapter);
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