package com.hdy.goldhe.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hdy.goldhe.Adapter.meiziadapter;
import com.hdy.goldhe.MainActivity2;
import com.hdy.goldhe.R;
import com.hdy.goldhe.pictureActivity;
import com.hdy.goldhe.recylerview.RecyclerViewClickListener;
import com.hdy.goldhe.webActivity;
import com.jingewenku.abrahamcaijin.commonutil.AppToastMgr;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hdy on 2017/5/23.
 */

public class fragment_meizi extends Fragment {
    private boolean isVisible = false, isInitView = false, isFirstLoad = true;
    private View convertView;
    private SparseArray<View> mViews;
    private RecyclerView recyclerView;
    private JSONArray dataarry=new JSONArray();
    private int number=1;
    private  RequestQueue requestQueue;
    private meiziadapter meiziadapter;
    private View footer;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private int[] lastPositions;
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
        return R.layout.fragment_meizi;
    }

    /**
     * 让布局中的view与fragment中的变量建立起映射
     */
    protected void initView() {
        recyclerView = findView(R.id.meizi_recylerview);
        requestQueue = Volley.newRequestQueue(getContext());
        footer = LayoutInflater.from(getContext()).inflate(R.layout.loadmorefooter,null);
        staggeredGridLayoutManager=new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        initData();
    }

    /**
     * 加载要显示的数据
     */
    protected void initData() {
        getlist(number);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener(){
            //用来标记是否正在向最后一个滑动，既是否向下滑动
            boolean isSlidingToLast = false;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                staggeredGridLayoutManager.invalidateSpanAssignments();
                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取最后一个完全显示的ItemPosition
                    int[] lastVisiblePositions = staggeredGridLayoutManager.findLastVisibleItemPositions(new int[staggeredGridLayoutManager.getSpanCount()]);
                    int lastVisiblePos = getMaxElem(lastVisiblePositions);
                    int totalItemCount = staggeredGridLayoutManager.getItemCount();
                    // 判断是否滚动到底部
                    if (lastVisiblePos == (totalItemCount -1) && isSlidingToLast) {
                        //加载更多功能的代码
                        number++;
                        getlist(number);
                        meiziadapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //dx用来判断横向滑动方向，dy用来判断纵向滑动方向
                if(dy > 0){
                    //大于0表示，正在向下滚动
                    isSlidingToLast = true;
                }else{
                    //小于等于0 表示停止或向上滚动
                    isSlidingToLast = false;
                }

            }
        });
       recyclerView.addOnItemTouchListener(new RecyclerViewClickListener(getContext(),new RecyclerViewClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                try {
                    String url=dataarry.getJSONObject(position).getString("pic");
                    Intent intent=new Intent();
                    intent.setClass(getContext(),pictureActivity.class);
                    intent.putExtra("picurl",url);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }
    private int getMaxElem(int[] arr) {
        int size = arr.length;
        int maxVal = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            if (arr[i]>maxVal)
                maxVal = arr[i];
        }
        return maxVal;
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

    public void getlist(int j) {
       String url="http://gank.io/api/data/%E7%A6%8F%E5%88%A9/20/"+j;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray data;
                try {
                    data = response.getJSONArray("results");
                    for (int i = 0; i < data.length(); i++) {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            JSONObject netjsonObject = data.getJSONObject(i);
                            jsonObject.put("pic", netjsonObject.getString("url"));
                            dataarry.put(jsonObject);
                        } catch (JSONException ignored) {
                        }
                    }
                    if(number==1){
                        meiziadapter=new meiziadapter(getContext(),dataarry);
                        recyclerView.setAdapter(meiziadapter);
                        recyclerView.setLayoutManager(staggeredGridLayoutManager);
                    }

                } catch (JSONException e) {
                    AppToastMgr.ToastShortBottomCenter(getContext(),"加载失败！");
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