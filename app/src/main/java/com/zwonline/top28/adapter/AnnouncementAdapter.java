package com.zwonline.top28.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zwonline.top28.R;
import com.zwonline.top28.bean.AnnouncementBean;
import com.zwonline.top28.constants.BizConstant;

import java.util.List;

/**
 * 公告列表的适配器
 */
public class AnnouncementAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<AnnouncementBean.DataBean> list;
    private Context context;

    public AnnouncementAdapter(List<AnnouncementBean.DataBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notify_list_item, parent, false);
        final MyViewHolder holder = new MyViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItemListener.setOnItemClick(v, holder.getPosition());
                MyViewHolder myViewHolder = (MyViewHolder) holder;
                myViewHolder.isread.setVisibility(View.GONE);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.notify_date.setText(list.get(position).date);
        myViewHolder.notify_title.setText(list.get(position).title);
        myViewHolder.notify_cotent.setText(list.get(position).resume);
        if (list.get(position).is_read.equals(BizConstant.ENTERPRISE_tRUE)) {
            myViewHolder.isread.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.isread.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView notify_date, notify_title, notify_cotent;
        RelativeLayout noread_linear;
        ImageView isread;

        public MyViewHolder(View itemView) {
            super(itemView);
            noread_linear = (RelativeLayout) itemView.findViewById(R.id.noread_linear);
            notify_date = (TextView) itemView.findViewById(R.id.notify_date);
            notify_title = (TextView) itemView.findViewById(R.id.notify_title);
            notify_cotent = (TextView) itemView.findViewById(R.id.notify_cotent);
            isread = (ImageView) itemView.findViewById(R.id.isread);
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

