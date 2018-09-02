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
import com.zwonline.top28.bean.MyCollectBean;

import java.util.List;

/**
 * 描述：收藏
 *
 * @author YSG
 * @date 2017/12/24
 */
public class MyCollectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MyCollectBean.DataBean> list;
    private Context context;

    public MyCollectAdapter(List<MyCollectBean.DataBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mycollect_item, parent, false);
        final MyViewHolder holder = new MyViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItemListener.setOnItemClick(holder.getPosition(), v);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.title.setText(list.get(position).title);
        myViewHolder.cdate.setText(list.get(position).cdate);
        myViewHolder.image.setScaleType(ImageView.ScaleType.FIT_XY);
        myViewHolder.viewcount.setText(list.get(position).view + context.getString(R.string.favorite_read));
        RequestOptions options = new RequestOptions().override(110, 60);
        Glide.with(context).load(list.get(position).path).apply(options).into(myViewHolder.image)
        ;
        if (list.get(position).write_type.equals("1")) {
            myViewHolder.type.setText(R.string.favorite_article_org);
        } else {
            myViewHolder.type.setText(R.string.favorite_article_reprint);
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, cdate, type, viewcount;
        ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            type = (TextView) itemView.findViewById(R.id.type);
            cdate = (TextView) itemView.findViewById(R.id.cdatetime);
            image = (ImageView) itemView.findViewById(R.id.image);
            viewcount = (TextView) itemView.findViewById(R.id.viewcount);
        }
    }

    public OnClickItemListener onClickItemListener;

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }

    public interface OnClickItemListener {
        void setOnItemClick(int position, View view);
    }
}
