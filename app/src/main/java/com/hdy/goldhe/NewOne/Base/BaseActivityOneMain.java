package com.hdy.goldhe.NewOne.Base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import com.hdy.goldhe.R;
import com.umeng.analytics.MobclickAgent;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

/**
 * Created by hdy on 2017/8/12.
 */

    public abstract class BaseActivityOneMain extends AppCompatActivity implements BGASwipeBackHelper.Delegate, View.OnClickListener {
        protected BGASwipeBackHelper mSwipeBackHelper;
        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackManager.getInstance().init(this) 来初始化滑动返回」
            // 在 super.onCreate(savedInstanceState) 之前调用该方法
            setTheme(R.style.MainAppTheme);
            initSwipeBackFinish();
            super.onCreate(savedInstanceState);
            initView(savedInstanceState);
            setListener();
            processLogic(savedInstanceState);
        }
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
        /**
         * 初始化滑动返回。在 super.onCreate(savedInstanceState) 之前调用该方法
         */
        private void initSwipeBackFinish() {
            mSwipeBackHelper = new BGASwipeBackHelper(this, this);

            // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackManager.getInstance().init(this) 来初始化滑动返回」
            // 下面几项可以不配置，这里只是为了讲述接口用法。

            // 设置滑动返回是否可用。默认值为 true
            mSwipeBackHelper.setSwipeBackEnable(true);
            // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
            mSwipeBackHelper.setIsOnlyTrackingLeftEdge(true);
            // 设置是否是微信滑动返回样式。默认值为 true
            mSwipeBackHelper.setIsWeChatStyle(true);
            // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
            mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow);
            // 设置是否显示滑动返回的阴影效果。默认值为 true
            mSwipeBackHelper.setIsNeedShowShadow(true);
            // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
            mSwipeBackHelper.setIsShadowAlphaGradient(true);
            // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
            mSwipeBackHelper.setSwipeBackThreshold(0.3f);
        }

        /**
         * 是否支持滑动返回。这里在父类中默认返回 true 来支持滑动返回，如果某个界面不想支持滑动返回则重写该方法返回 false 即可
         *
         * @return
         */
        @Override
        public boolean isSupportSwipeBack() {
            return true;
        }

        /**
         * 正在滑动返回
         *
         * @param slideOffset 从 0 到 1
         */
        @Override
        public void onSwipeBackLayoutSlide(float slideOffset) {
        }

        /**
         * 没达到滑动返回的阈值，取消滑动返回动作，回到默认状态
         */
        @Override
        public void onSwipeBackLayoutCancel() {
        }

        /**
         * 滑动返回执行完毕，销毁当前 Activity
         */
        @Override
        public void onSwipeBackLayoutExecuted() {
            mSwipeBackHelper.swipeBackward();
        }

        @Override
        public void onBackPressed() {
            // 正在滑动返回的时候取消返回按钮事件
            if (mSwipeBackHelper.isSliding()) {
                return;
            }
            mSwipeBackHelper.backward();
        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            if (item.getItemId() == android.R.id.home) {
                onBackPressed();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        /**
         * 初始化布局以及View控件
         */
        protected abstract void initView(Bundle savedInstanceState);

        /**
         * 给View控件添加事件监听器
         */
        protected abstract void setListener();

        /**
         * 处理业务逻辑，状态恢复等操作
         *
         * @param savedInstanceState
         */
        protected abstract void processLogic(Bundle savedInstanceState);

        /**
         * 需要处理点击事件时，重写该方法
         *
         * @param v
         */
        public void onClick(View v) {
        }

        /**
         * 查找View
         *
         * @param id   控件的id
         * @param <VT> View类型
         * @return
         */
        protected <VT extends View> VT findView(@IdRes int id) {
            return (VT) findViewById(id);
        }

        @Override
        protected void onStop() {
            super.onStop();
            Log.i(this.getClass().getSimpleName(), "onStop " + this.getClass().getSimpleName());
        }
}
