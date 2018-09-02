package com.zwonline.top28.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zwonline.top28.R;
import com.zwonline.top28.bean.RecommendBean;

import java.util.List;

/**
 * @author YSG
 * @desc商机推荐项目的适配器
 * @date ${Date}
 */
public class RecommendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<RecommendBean.DataBean> list;
    private Context context;

    public RecommendAdapter(List<RecommendBean.DataBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.recommend_item, parent, false);
        final MyViewHolder holder=new MyViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItemListener.setOnItemClick(holder.getPosition(),v);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder= (MyViewHolder) holder;
        myViewHolder.title.setText(list.get(position).title);
        myViewHolder.cate_name.setText(list.get(position).cate_name);
        myViewHolder.logo.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(context).load(list.get(position).logo).into(myViewHolder.logo);
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }
    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title,cate_name;
        ImageView logo;
        public MyViewHolder(View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.title);
            cate_name=(TextView)itemView.findViewById(R.id.cate_name);
            logo=(ImageView)itemView.findViewById(R.id.logo);
        }
    }
    public RecommendAdapter.OnClickItemListener onClickItemListener;

    public void setOnClickItemListener(RecommendAdapter.OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }

    public interface OnClickItemListener{
        void setOnItemClick(int position,View view);
    }
}
