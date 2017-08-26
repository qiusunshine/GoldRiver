package com.hdy.goldhe.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hdy.goldhe.Adapter.Fragmentadapter;
import com.hdy.goldhe.R;

/**
 * Created by hdy on 2017/5/16.
 */

public class kaifa extends Fragment {
    private boolean isVisible = false, isInitView = false, isFirstLoad = true;
    private View convertView;
    private SparseArray<View> mViews;
    private ViewPager viewPager;
    private TabLayout tabLayout;

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
        return R.layout.kaifa_main;
    }

    /**
     * 让布局中的view与fragment中的变量建立起映射
     */
    protected void initView() {
        viewPager = findView(R.id.kaifa_viewpager);
        tabLayout = findView(R.id.kaifatab);
        initData();
        initTab();
    }

    /**
     * 加载要显示的数据
     */
    protected void initData() {
        initViewPagerAdapter();
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

    public void initViewPagerAdapter() {
        //设置tablayout与viewpager联动
        tabLayout.setupWithViewPager(viewPager);
        //设置viewpager的适配器
        Fragment[] fragments = {new fragment_juejin(),new fragment_csdn(),new fragment_day_of_net(),new fragment_gank()};
        Fragmentadapter adapter = new Fragmentadapter(getActivity().getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
    }

    public void initTab(){
        try {
            tabLayout.getTabAt(0).setText("掘金");
            tabLayout.getTabAt(1).setText("CSDN");
            tabLayout.getTabAt(2).setText("泡网");
            tabLayout.getTabAt(3).setText("干货");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}