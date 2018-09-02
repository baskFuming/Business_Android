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
import com.zwonline.top28.R;
import com.zwonline.top28.activity.CompanyActivity;
import com.zwonline.top28.activity.WantExamineActivity;
import com.zwonline.top28.bean.HotExamineBean;

import java.util.List;


/**
 * Created by YU on 2017/12/12.
 * 考察的适配器
 */

public class ExamineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<HotExamineBean.DataBean> list;
    private Context context;

    public ExamineAdapter(List<HotExamineBean.DataBean> list, Context context) {
        this.list = list;
        this.context = context;
    }
    private void overridePendingTransition(int activity_right_in, int activity_left_out) {
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.examine_item, parent, false);
        final ExamineViewHolder holder = new ExamineViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ExamineViewHolder examineViewHolder = (ExamineViewHolder) holder;
        examineViewHolder.title.setText(list.get(position).enterprise_name);
        examineViewHolder.subscribe_num.setText("已有"+list.get(position).num+"人预约");
        Glide.with(context).load(list.get(position).logo).into(examineViewHolder.image);
        examineViewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, CompanyActivity.class);
                intent.putExtra("uid",list.get(position).uid);
                intent.putExtra("enterprise_name",list.get(position).enterprise_name);
                context.startActivity(intent);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
            }
        });
        examineViewHolder.want_examine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, WantExamineActivity.class);
                intent.putExtra("uid",list.get(position).uid);
                context.startActivity(intent);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
            }


        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static class ExamineViewHolder extends RecyclerView.ViewHolder {
        TextView title,subscribe_num,want_examine;
        ImageView image;

        public ExamineViewHolder(View itemView) {
            super(itemView);
            subscribe_num= (TextView) itemView.findViewById(R.id.subscribe_num);
            title = (TextView) itemView.findViewById(R.id.title);
            image = (ImageView) itemView.findViewById(R.id.log);
            want_examine=(TextView)itemView.findViewById(R.id.want_examine);
        }
    }


}
