package com.zwonline.top28.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zwonline.top28.R;
import com.zwonline.top28.activity.HomePageActivity;
import com.zwonline.top28.bean.AtentionDynamicHeadBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.utils.ImageViewPlus;

import java.util.List;

/**
 * 关注动态头列表
 */
public class AttentionDynamicHeadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<AtentionDynamicHeadBean.DataBean.ListBean> list;
    private Context context;

    public AttentionDynamicHeadAdapter(List<AtentionDynamicHeadBean.DataBean.ListBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.attention_dynamic_head_item, parent, false);
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        RequestOptions requestOptions = new RequestOptions().placeholder(R.mipmap.no_photo_male).error(R.mipmap.no_photo_male).centerCrop();
        if (position == list.size()) {
            //用户头像
            Glide.with(context).load(R.mipmap.friends_more)
                    .into(myViewHolder.busi_user);
            myViewHolder.busin_nkname.setText("查看更多");

        } else {
            myViewHolder.busin_nkname.setText(list.get(position).nickname + "");
            if (list.get(position).identity_type.equals(BizConstant.IS_FAIL)) {
                myViewHolder.da_v.setVisibility(View.GONE);
            } else {
                myViewHolder.da_v.setVisibility(View.VISIBLE);
            }

            Glide.with(context).load(list.get(position).avatars)
                    .apply(requestOptions)
                    .into(myViewHolder.busi_user);
            myViewHolder.busi_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, HomePageActivity.class);
                    intent.putExtra("uid", list.get(position).uid);
                    context.startActivity(intent);
                }
            });
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageViewPlus busi_user;
        ImageView da_v;
        TextView busin_nkname;

        public MyViewHolder(View itemView) {
            super(itemView);
            da_v = itemView.findViewById(R.id.da_v);
            busi_user = itemView.findViewById(R.id.busi_user);
            busin_nkname = itemView.findViewById(R.id.busin_nkname);
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() + 1 : 0;
    }

    public BusinessProductAdapter.OnClickItemListener onClickItemListener;

    public void setOnClickItemListener(BusinessProductAdapter.OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }

    public interface OnClickItemListener {
        void setOnItemClick(View view, int position);
    }

}
