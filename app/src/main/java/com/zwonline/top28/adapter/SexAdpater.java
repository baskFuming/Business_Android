package com.zwonline.top28.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zwonline.top28.R;
import com.zwonline.top28.bean.SexBean;

import java.util.List;

public class SexAdpater extends BaseAdapter{
    private Context context;
    private List<SexBean> dlist;

    public SexAdpater(Context context, List<SexBean> dlist) {
        this.context = context;
        this.dlist = dlist;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object getItem(int position) {
        return dlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MyHolder holder;
        if (convertView == null) {
            holder = new MyHolder();
            convertView = View.inflate(context,R.layout.sex_item, null);
            holder.textView_sex = convertView.findViewById(R.id.sex_name);
            convertView.setTag(holder);
        }else {
            holder = (MyHolder) convertView.getTag();
        }
        holder.textView_sex.setText(dlist.get(position).sex);
        return convertView;
    }
    static class MyHolder{
        TextView textView_sex;
    }
}
