package com.zwonline.top28.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.service.PayService;
import com.zwonline.top28.bean.ArticleCommentBean;
import com.zwonline.top28.bean.ZanBean;
import com.zwonline.top28.utils.ImageViewPlus;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.SignUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class CommentsAdapter extends BaseAdapter {
    private List<ArticleCommentBean.DataBean> list;
    private Context context;

    public CommentsAdapter(List<ArticleCommentBean.DataBean> list, Context context) {
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
            holder.comment_issue_time.setText(list.get(position).commentsExcerpt.get(position).ctime);
            holder.praise_num.setText(list.get(position).commentsExcerpt.get(position).zan);
            holder.comment_content.setText(list.get(position).commentsExcerpt.get(position).content);
            RequestOptions options = new RequestOptions().placeholder(R.mipmap.no_photo_male).error(R.mipmap.no_photo_male);
            Glide.with(context).load(list.get(position).commentsExcerpt.get(position).member.avatars).apply(options).into(holder.comment_user_head);
            holder.comment_user_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, HomePageActivity.class);
                    intent.putExtra("uid", list.get(position).commentsExcerpt.get(position).uid);
                    context.startActivity(intent);
                }
            });
            if (list.get(position).did_i_vote.equals("1")) {
                holder.praise_num.setTextColor(Color.parseColor("#FF2B2B"));
                holder.comment_like.setChecked(true);
                holder.comment_like.setEnabled(false);
            } else if (list.get(position).did_i_vote.equals("0")) {
                holder.praise_num.setTextColor(Color.parseColor("#C2C2C2"));
                holder.comment_like.setChecked(false);
                holder.comment_like.setEnabled(true);
            }
            holder.comment_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SharedPreferencesUtils sp = SharedPreferencesUtils.getUtil();
                    String token = (String) sp.getKey(context, "dialog", "");
                    try {
                        int vote = Integer.parseInt(list.get(position).zan) + 1;
                        dianZan(list.get(position).comment_id, token);
                        holder.comment_like.setEnabled(false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


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

    //点赞的网络请求
    public void dianZan(String comment_id, String token) throws IOException {
        long timestamp = new Date().getTime() / 1000;
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("comment_id", comment_id);
        map.put("token", token);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<ZanBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(PayService.class, Api.url)
                .iZan(String.valueOf(timestamp), token, comment_id, sign);
        flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<ZanBean>() {
                    @Override
                    public void onNext(ZanBean attentionBean) {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}
