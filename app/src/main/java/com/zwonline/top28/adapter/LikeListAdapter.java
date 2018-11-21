package com.zwonline.top28.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zwonline.top28.R;
import com.zwonline.top28.activity.HomePageActivity;
import com.zwonline.top28.bean.BankBean;
import com.zwonline.top28.bean.LikeListBean;
import com.zwonline.top28.utils.ImageViewPlus;
import com.zwonline.top28.utils.StringReplaceUtil;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.click.AntiShake;

import java.util.List;

/**
 * 点赞列表的适配器
 */
public class LikeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<LikeListBean.DataBean> list;
    private Context context;
    private HomePageInterface homePageInterface;

    public LikeListAdapter(List<LikeListBean.DataBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.like_list_item, parent, false);
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
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.like_username.setText(list.get(position).nickname);
        if (StringUtil.isNotEmpty(list.get(position).signature)) {

            myViewHolder.like_time.setText(list.get(position).signature);
        } else {
            myViewHolder.like_time.setText("暂无签名");
        }
//        myViewHolder.like_userhead.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (AntiShake.check(v.getId())) {    //判断是否多次点击
//                    return;
//                }
//                homePageInterface.onclick(v, position);
//            }
//        });
        RequestOptions options = new RequestOptions().error(R.mipmap.no_photo_male).placeholder(R.mipmap.no_photo_male);
        Glide.with(context).load(list.get(position).avatars).apply(options).into(myViewHolder.like_userhead);
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView like_username, like_time;
        ImageViewPlus like_userhead;

        public MyViewHolder(View itemView) {
            super(itemView);
            like_username = (TextView) itemView.findViewById(R.id.like_username);
            like_time = (TextView) itemView.findViewById(R.id.like_time);
            like_userhead = (ImageViewPlus) itemView.findViewById(R.id.like_userhead);
        }
    }


    public OnClickItemListener onClickItemListener;

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }


    /**
     * `
     * 按钮点击事件需要的方法
     */
    public void homePageSetOnclick(HomePageInterface homePageInterface) {
        this.homePageInterface = homePageInterface;
    }

    /**
     * 按钮点击事件对应的接口
     */
    public interface HomePageInterface {
        public void onclick(View view, int position);
    }

    public interface OnClickItemListener {
        void setOnItemClick(View view, int position);
    }
}

