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
import com.zwonline.top28.bean.MyIssueBean;

import java.util.List;


/**
 * 描述：企业主页的适配器
 * @author YSG
 * @date 2018/1/3
 */
public class CompanyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MyIssueBean.DataBean> list;
    private Context context;

    public CompanyAdapter(List<MyIssueBean.DataBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.company_image, parent, false);
        final MyViewHolder holder = new MyViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItemListener.setOnItemClick(holder.getPosition());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.title.setText(list.get(position).title);
        myViewHolder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(context).load(list.get(position).path).into(myViewHolder.imageView);
        myViewHolder.viewcount.setText(list.get(position).view);
        myViewHolder.cdatetime.setText(list.get(position).cdatetime);
    }

    @Override
    public int getItemCount() {
        return list.size() <= 5 ? list.size() : 5;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title, viewcount, cdatetime;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            viewcount = (TextView) itemView.findViewById(R.id.viewcount);
            cdatetime = (TextView) itemView.findViewById(R.id.cdatetime);
        }
    }

    public OnClickItemListener onClickItemListener;

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }

    public interface OnClickItemListener {
        void setOnItemClick(int position);
    }
}
