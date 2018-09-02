package com.zwonline.top28.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zwonline.top28.R;
import com.zwonline.top28.bean.AnnouncementBean;
import com.zwonline.top28.bean.RecommendTeamsBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.tip.toast.ToastUtil;
import com.zwonline.top28.utils.ImageViewPlus;

import java.util.List;

/**
 * 群推荐
 */
public class RecommentTeamsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<RecommendTeamsBean.DataBean> list;
    private Context context;

    public RecommentTeamsAdapter(List<RecommendTeamsBean.DataBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recomment_group_item, parent, false);
        final MyViewHolder holder = new MyViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItemListener.setOnItemClick(v, holder.getPosition());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        SpannableStringBuilder spannable = new SpannableStringBuilder(list.get(position).team_name);
        myViewHolder.recommentName.setText(spannable);
        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.nim_avatar_group).error(R.drawable.nim_avatar_group);
        Glide.with(context).load(list.get(position).team_avatar).apply(requestOptions).into(myViewHolder.recommendHead);

    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView recommentName;
        ImageViewPlus recommendHead;

        public MyViewHolder(View itemView) {
            super(itemView);
            recommentName = (TextView) itemView.findViewById(R.id.recomment_name);
            recommendHead = (ImageViewPlus) itemView.findViewById(R.id.recommend_head);
        }
    }


    public OnClickItemListener onClickItemListener;

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;

    }

    public interface OnClickItemListener {
        void setOnItemClick(View view, int position);
    }
}

