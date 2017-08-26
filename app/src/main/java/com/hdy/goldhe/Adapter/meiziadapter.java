package com.hdy.goldhe.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.hdy.goldhe.R;
import com.jingewenku.abrahamcaijin.commonutil.AppToastMgr;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hdy on 2017/8/10.
 */
    public class meiziadapter extends RecyclerView.Adapter<meiziadapter.ViewHolder> {

        private Context mContext;
        private List<Integer> mHeights;
        private JSONArray array;

        public meiziadapter(Context context,JSONArray array){
            this.mContext = context;
            this.array=array;
        }
        public void getRandomHeight(){
            mHeights = new ArrayList<>();
            for(int i=0; i < array.length();i++){
                //随机的获取一个范围为200-600直接的高度
                mHeights.add((int)(400+Math.random()*400));
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            getRandomHeight();
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.image_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ViewGroup.LayoutParams layoutParams = holder.imageView.getLayoutParams();
            try {
                layoutParams.height = mHeights.get(position);
                holder.imageView.setLayoutParams(layoutParams);
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONObject Object = null;
            try {
                Object = array.getJSONObject(position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.bizhi)
                    .error(R.drawable.bizhi)
                    .override(layoutParams.width,layoutParams.height);
            try {
                Glide.with(mContext)
                        .load(Object.get("pic").toString())
                        .apply(options)
                        .into(holder.imageView);
            } catch (JSONException e) {
                AppToastMgr.ToastShortBottomCenter(mContext,"图片加载错误：\n"+e);
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return array.length();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder{
            ImageView imageView;
            public ViewHolder(View view){
                //需要设置super
                super(view);
                imageView=(ImageView) view.findViewById(R.id.image_item);
            }
        }


}
