package com.zwonline.top28.adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.zwonline.top28.bean.AddFollowBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.utils.ImageViewPlus;
import com.zwonline.top28.utils.StringUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述：用户列表
 *
 * @author YSG
 * @date 2017/12/24
 */
public class AddReListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<AddFollowBean.DataBean.ListBean> dlist;
    private Context context;
    private boolean isAttention = true;
    private AttentionSetOnclick attentionSetOnclick;

    public AddReListAdapter(List<AddFollowBean.DataBean.ListBean> dlist, Context context) {
        this.dlist = dlist;
        this.context = context;
    }

    //数据刷新
    public void changeData(List<AddFollowBean.DataBean.ListBean> list) {
        if (list == null) {
            dlist = list;
        } else {
            dlist.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.add_relist_item, parent, false);
        final MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final AddFollowBean.DataBean.ListBean bean = dlist.get(position);
        //用户头像
        RequestOptions requestOptions = new RequestOptions().placeholder(R.mipmap.no_photo_male).error(R.mipmap.no_photo_male).centerCrop();
        Glide.with(context).load(bean.avatars)
                .apply(requestOptions)
                .into(((MyViewHolder) holder).imageViewPlus);
        if (StringUtil.isNotEmpty(bean.signature)) {

            ((MyViewHolder) holder).signature.setText(bean.signature);
        } else {
            ((MyViewHolder) holder).signature.setText("暂无签名");
        }
        ((MyViewHolder) holder).nickname.setText(bean.nickname);
        if (dlist.get(position).identity_type.equals(BizConstant.IS_FAIL)) {
            ((MyViewHolder) holder).daV.setVisibility(View.GONE);
        } else {
            ((MyViewHolder) holder).daV.setVisibility(View.VISIBLE);
        }
        if (bean.did_i_follow.equals("0")) {
            isAttention = false;
            ((MyViewHolder) holder).busi_add.setText("+");
            ((MyViewHolder) holder).busi_add.setTextColor(Color.parseColor("#ff2d2d2d"));
            ((MyViewHolder) holder).busi_add.setBackgroundResource(R.drawable.back_action);
        } else {
            isAttention = true;
            ((MyViewHolder) holder).busi_add.setText("√");
            ((MyViewHolder) holder).busi_add.setTextColor(Color.parseColor("#FFFFFF"));
            ((MyViewHolder) holder).busi_add.setBackgroundResource(R.drawable.btn_ganzhu_red);
        }
        /**
         * 关注的点击事件
         */
        ((MyViewHolder) holder).busi_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attentionSetOnclick.onclick(v, position);
            }
        });
        ((MyViewHolder) holder).imageViewPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomePageActivity.class);
                intent.putExtra("uid", dlist.get(position).uid);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dlist != null ? dlist.size() : 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.avatars)
        ImageViewPlus imageViewPlus;
        @BindView(R.id.signature)
        TextView signature;
        @BindView(R.id.busi_add)
        TextView busi_add;
        @BindView(R.id.nickname)
        TextView nickname;
        @BindView(R.id.da_v)
        ImageView daV;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * `
     * 按钮点击事件需要的方法
     */
    public void attentionSetOnclick(AttentionSetOnclick attentionSetOnclick) {
        this.attentionSetOnclick = attentionSetOnclick;
    }

    /**
     * 按钮点击事件对应的接口
     */
    public interface AttentionSetOnclick {
        public void onclick(View view, int position);
    }
//    public OnClickItemListener onClickItemListener;
//    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
//        this.onClickItemListener = onClickItemListener;
//    }
//    public interface OnClickItemListener {
//        void setOnItemClick(int position, View view);
//    }
}
