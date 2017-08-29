package com.hdy.goldhe.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.hdy.goldhe.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hdy on 2017/5/23.
 */

public class zhihuadapter extends BaseAdapter{
        private Context context;
        private JSONArray array;
        private LayoutInflater inflater=null;
        private boolean loadpicture=true;
        public zhihuadapter(Context context, JSONArray array, boolean loadpicture)
        {
            this.inflater=LayoutInflater.from(context);
            this.context = context;
            this.array=array;
            this.loadpicture=loadpicture;
        }
        @Override
        public int getCount() {
            // How many items are in the data set represented by this Adapter.(在此适配器中所代表的数据集中的条目数)
            try {
                return array.length();
            } catch (Exception e) {
            }
            return 0;
        }
        @Override
        public Object getItem(int position) {
            // Get the data item associated with the specified position in the data set.(获取数据集中与指定索引对应的数据项)
            return null;
        }
        @Override
        public long getItemId(int position) {
            // Get the row id associated with the specified position in the list.(取在列表中与指定索引对应的行id)
            return 0;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.zhihu_item, null);
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.zhihu_imageview);
                viewHolder.title = (TextView) convertView.findViewById(R.id.zhihu_title);
                convertView.setTag(viewHolder);// 通过setTag将ViewHolder和convertView绑定
            }  else {
                viewHolder = (ViewHolder) convertView.getTag(); // 获取，通过ViewHolder找到相应的控件
            }
            try {
                JSONObject Object = array.getJSONObject(position);
               viewHolder.title.setText(Object.get("title").toString());
                if(loadpicture) {
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.bizhi)
                            .error(R.drawable.bizhi);
                    Glide.with(context)
                            .asBitmap()
                            .load(Object.get("pic").toString())
                            .apply(options)
                            .into( new BitmapImageViewTarget(viewHolder.imageView){
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable roundedBitmapDrawable= RoundedBitmapDrawableFactory.create(context.getResources(),resource);
                                    roundedBitmapDrawable.setCircular(true);
                                    viewHolder.imageView.setImageDrawable(roundedBitmapDrawable);
                                }
                            });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return convertView;
        }

    private class ViewHolder {
        ImageView imageView;
        TextView title;
    }

}
