package com.zwonline.top28.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zwonline.top28.R;
import com.zwonline.top28.bean.MyPageBean;
import com.zwonline.top28.utils.ImageViewPlus;

import java.util.List;

public class MyTwoMunuAdapter extends BaseAdapter {
    private List<MyPageBean.DataBean.FunctionsBean> list;
    private Context context;

    public MyTwoMunuAdapter(List<MyPageBean.DataBean.FunctionsBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.my_two_menu_item, null);
            holder.function_name = convertView.findViewById(R.id.function_name);
            holder.icon = convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        holder.icon.setImageResource();
        int resID = context.getResources().getIdentifier(list.get(position).image, "mipmap", context.getPackageName());
        Drawable drawable = context.getResources().getDrawable(resID);
        holder.icon.setImageDrawable(drawable);
        holder.function_name.setText(list.get(position).title);
        return convertView;
    }

    class ViewHolder {
        TextView function_name;
        ImageView icon;
    }
}
