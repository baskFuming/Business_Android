package com.zwonline.top28.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zwonline.top28.R;
import com.zwonline.top28.activity.HomePageActivity;
import com.zwonline.top28.bean.DynamicDetailsBean;
import com.zwonline.top28.utils.ImageViewPlus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 动态评论的适配器
 */
public class DynamicCommentsAdapter extends BaseAdapter {
    private List<DynamicDetailsBean.DataBean> list;
    private Context context;

    public DynamicCommentsAdapter(List<DynamicDetailsBean.DataBean> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @Override
    public int getCount() {
        return list.size();
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.comment_details_item, null);
            holder.comment_user_name = (TextView) convertView.findViewById(R.id.comment_user_name);
            holder.no_comment = (TextView) convertView.findViewById(R.id.no_comment);
            holder.comment_issue_time = (TextView) convertView.findViewById(R.id.comment_issue_time);
            holder.praise_num = (TextView) convertView.findViewById(R.id.praise_num);
            holder.comment_content = (TextView) convertView.findViewById(R.id.comment_content);
            holder.comment_user_head = (ImageViewPlus) convertView.findViewById(R.id.comment_user_head);
            holder.comment_like = (CheckBox) convertView.findViewById(R.id.comment_like);
            holder.comment_dian = (RelativeLayout) convertView.findViewById(R.id.comment_dian);
            holder.comment_linear = (LinearLayout) convertView.findViewById(R.id.comment_linear);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (list.get(position).commentsExcerpt == null) {
            holder.comment_linear.setVisibility(View.GONE);
            holder.no_comment.setVisibility(View.VISIBLE);
        } else {
            holder.comment_user_name.setText(list.get(position).commentsExcerpt.get(position).member.nickname);
            holder.comment_user_name.setText(list.get(position).member.nickname);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
            Date date = null;
            try {
                date = formatter.parse(list.get(position).commentsExcerpt.get(position).add_time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
//            holder.comment_issue_time.setText(list.get(position).commentsExcerpt.get(position).add_time);
            holder.comment_content.setText(list.get(position).commentsExcerpt.get(position).content);
            RequestOptions options = new RequestOptions().placeholder(R.mipmap.no_photo_male).error(R.mipmap.no_photo_male);
            Glide.with(context).load(list.get(position).commentsExcerpt.get(position).member.avatars).apply(options).into(holder.comment_user_head);
            holder.comment_user_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, HomePageActivity.class);
                    intent.putExtra("uid", list.get(position).commentsExcerpt.get(position).user_id);
                    context.startActivity(intent);
                }
            });
        }


        return convertView;
    }

    class ViewHolder {
        TextView comment_user_name, comment_issue_time, praise_num, comment_content, no_comment;
        ImageViewPlus comment_user_head;
        CheckBox comment_like;
        LinearLayout comment_linear;
        RelativeLayout comment_dian;
    }


}
