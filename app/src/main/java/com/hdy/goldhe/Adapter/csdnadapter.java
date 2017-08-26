package com.hdy.goldhe.Adapter;

import android.content.Context;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hdy.goldhe.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hdy on 2017/5/23.
 */

public class csdnadapter extends BaseAdapter{
        private Context context;
        private JSONArray array;
        private LayoutInflater inflater=null;
        public csdnadapter(Context context, JSONArray array)
        {
            this.inflater=LayoutInflater.from(context);
            this.context = context;
            this.array=array;
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
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.douban_item, null);
                viewHolder.title = (TextView) convertView.findViewById(R.id.douban_title);
                viewHolder.detail = (TextView) convertView.findViewById(R.id.douban_detail);
                convertView.setTag(viewHolder);// 通过setTag将ViewHolder和convertView绑定
            }  else {
                viewHolder = (ViewHolder) convertView.getTag(); // 获取，通过ViewHolder找到相应的控件
            }
            try {
                JSONObject Object = array.getJSONObject(position);
               viewHolder.title.setText(Object.get("title").toString());
                TextPaint tp=viewHolder.title.getPaint();
                tp.setFakeBoldText(true);
                String text="查看人数："+Object.get("viewsCount").toString()+"\n得分："+Object.get("score").toString()+"\n"+Object.get("update_time").toString();
                viewHolder.detail.setText(text);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return convertView;
        }

    private class ViewHolder {
        TextView title;
        TextView detail;
    }

}
