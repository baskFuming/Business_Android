package com.zwonline.top28.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zwonline.top28.R;
import com.zwonline.top28.bean.HongBaoLogBean;
import com.zwonline.top28.bean.YfRecordBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.utils.ImageViewPlus;
import com.zwonline.top28.utils.StringUtil;

import java.util.List;

/**
 * 抢到总鞅分记录
 */
public class YFHBRecordAdpter extends BaseAdapter {
    private List<YfRecordBean.DataBean.ListBean> list;
    private Context context;
    private String type;

    public YFHBRecordAdpter(List<YfRecordBean.DataBean.ListBean> list, Context context, String type) {
        this.context = context;
        this.list = list;
        this.type = type;
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
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.snatch_item, null);
            viewHolder.snatch_user_head = (ImageViewPlus) convertView.findViewById(R.id.snatch_user_head);
            viewHolder.snatch_user_name = (TextView) convertView.findViewById(R.id.snatch_user_name);
            viewHolder.snatch_yangfen_num = (TextView) convertView.findViewById(R.id.snatch_yangfen_num);
            viewHolder.snatch_yangfen_time = (TextView) convertView.findViewById(R.id.snatch_yangfen_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }

        viewHolder.snatch_user_name.setText(list.get(position).nickname);


        viewHolder.snatch_yangfen_time.setText(list.get(position).add_time);
        if (StringUtil.isNotEmpty(type)&&type.equals(BizConstant.IS_SUC)){
            viewHolder.snatch_yangfen_num.setText(list.get(position).amount + "商机币");
        }else {
            viewHolder.snatch_yangfen_num.setText(list.get(position).amount + "鞅分");
        }

        viewHolder.snatch_user_head.setVisibility(View.GONE);
        return convertView;
    }

    static class ViewHolder {
        TextView snatch_user_name, snatch_yangfen_num, snatch_yangfen_time;
        ImageViewPlus snatch_user_head;
    }
}
