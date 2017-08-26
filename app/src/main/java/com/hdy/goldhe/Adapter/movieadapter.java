package com.hdy.goldhe.Adapter;

/**
 * Created by hdy on 2017/5/24.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hdy.goldhe.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hdy on 2017/5/23.
 */

public class movieadapter extends BaseAdapter{
    private Context context;
    private JSONArray array;
    private LayoutInflater inflater=null;
    public movieadapter(Context context, JSONArray array)
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
            convertView = inflater.inflate(R.layout.movie_item, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.douban_ItemImageView);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.douban_ItemTextView);
            viewHolder.tvTitle2 = (TextView) convertView.findViewById(R.id.douban_ItemTextView2);
            convertView.setTag(viewHolder);// 通过setTag将ViewHolder和convertView绑定
        }  else {
            viewHolder = (ViewHolder) convertView.getTag(); // 获取，通过ViewHolder找到相应的控件
        }
        try {
            JSONObject Object = array.getJSONObject(position);
            viewHolder.tvTitle.setText(Object.getString("title"));
            viewHolder.tvTitle2.setText("上映:"+Object.get("year").toString()+"\n评分:"+Object.get("rating").toString()+"\n"+Object.get("collect_count").toString()+"人看过\n类型:"+Object.get("genres").toString());
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.bizhi)
                    .error(R.drawable.bizhi);
            Glide.with(context)
                    .load(Object.get("pic").toString())
                    .apply(options)
                    .into(viewHolder.imageView);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView tvTitle;
        TextView tvTitle2;
    }

}