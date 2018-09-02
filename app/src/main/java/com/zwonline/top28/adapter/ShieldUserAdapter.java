package com.zwonline.top28.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zwonline.top28.R;
import com.zwonline.top28.bean.BankBean;
import com.zwonline.top28.bean.ShieldUserBean;
import com.zwonline.top28.utils.ImageViewPlus;
import com.zwonline.top28.utils.StringReplaceUtil;

import java.util.List;

public class ShieldUserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ShieldUserBean.DataBean> list;
    private Context context;

    public ShieldUserAdapter(List<ShieldUserBean.DataBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.shield_user_item, parent, false);
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
        myViewHolder.nickname.setText(list.get(position).nickname);
        RequestOptions options = new RequestOptions().error(R.mipmap.no_photo_male).placeholder(R.mipmap.no_photo_male);
        Glide.with(context).load(list.get(position).avatars).apply(options).into(myViewHolder.avatars);

    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nickname;
        ImageViewPlus avatars;

        public MyViewHolder(View itemView) {
            super(itemView);
            nickname = (TextView) itemView.findViewById(R.id.nickname);
            avatars = (ImageViewPlus) itemView.findViewById(R.id.avatars);
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