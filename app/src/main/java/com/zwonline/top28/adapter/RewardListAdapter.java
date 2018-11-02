package com.zwonline.top28.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zwonline.top28.R;
import com.zwonline.top28.bean.LikeListBean;
import com.zwonline.top28.bean.RewardListBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.utils.ImageViewPlus;
import com.zwonline.top28.utils.StringUtil;

import java.util.List;

/**
 * 打赏列表的适配器
 */
public class RewardListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<RewardListBean.DataBean.ListBean> list;
    private Context context;

    public RewardListAdapter(List<RewardListBean.DataBean.ListBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rewardlistfragment, parent, false);
        final MyViewHolder holder = new MyViewHolder(view);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onClickItemListener.setOnItemClick(v, holder.getPosition());
//            }
//        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.reward_username.setText(list.get(position).reward_user_nickname);
        if (StringUtil.isNotEmpty(list.get(position).gift_name)) {
            myViewHolder.reward_type.setText("打赏" + list.get(position).gift_name);
        }
        myViewHolder.gift_num.setText("x" + list.get(position).gift_count);
        RequestOptions options = new RequestOptions().error(R.mipmap.no_photo_male).placeholder(R.mipmap.no_photo_male);
        Glide.with(context).load(list.get(position).reward_user_avatar).apply(options).into(myViewHolder.reward_userhead);
        String gift_id = list.get(position).gift_id;
        if (StringUtil.isNotEmpty(gift_id)&&gift_id.equals(BizConstant.IS_SUC)){
            myViewHolder.reward_image.setImageResource(R.mipmap.reward_gift1);
        }else if (StringUtil.isNotEmpty(gift_id)&&gift_id.equals(BizConstant.RECOMMEND)){
            myViewHolder.reward_image.setImageResource(R.mipmap.reward_gift2);
        }else if (StringUtil.isNotEmpty(gift_id)&&gift_id.equals(BizConstant.ATTENTION)){
            myViewHolder.reward_image.setImageResource(R.mipmap.reward_gift3);
        }else if (StringUtil.isNotEmpty(gift_id)&&gift_id.equals(BizConstant.MY)){
            myViewHolder.reward_image.setImageResource(R.mipmap.reward_gift5);
        }

    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView reward_username, reward_type, gift_num;
        ImageViewPlus reward_userhead;
        ImageView reward_image;

        public MyViewHolder(View itemView) {
            super(itemView);
            gift_num = (TextView) itemView.findViewById(R.id.gift_num);
            reward_username = (TextView) itemView.findViewById(R.id.reward_username);
            reward_type = (TextView) itemView.findViewById(R.id.reward_type);
            reward_userhead = (ImageViewPlus) itemView.findViewById(R.id.reward_userhead);
            reward_image = (ImageView) itemView.findViewById(R.id.reward_image);
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
