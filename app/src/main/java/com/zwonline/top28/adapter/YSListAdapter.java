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
import com.zwonline.top28.bean.YSListBean;
import com.zwonline.top28.utils.ImageViewPlu;

import java.util.List;

public class YSListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<YSListBean.DataBean.ListBean> list;
    private Context context;

    public YSListAdapter(List<YSListBean.DataBean.ListBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.yslist_item, parent, false);
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
        myViewHolder.name.setText(list.get(position).name);
        myViewHolder.prices.setText("￥" + list.get(position).price);
        myViewHolder.desc.setText(list.get(position).brief);
        RequestOptions requestOption = new RequestOptions().placeholder(R.mipmap.gray_logo).error(R.mipmap.gray_logo);
        Glide.with(context).load(list.get(position).image).apply(requestOption).into(myViewHolder.image);
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, desc, prices;
        ImageViewPlu image;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            desc = itemView.findViewById(R.id.desc);
            prices = itemView.findViewById(R.id.prices);
            image = itemView.findViewById(R.id.image);
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
