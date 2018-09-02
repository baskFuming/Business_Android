package com.zwonline.top28.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zwonline.top28.R;
import com.zwonline.top28.bean.IntegralBean;

import java.util.List;

/**
 * 描述：收付款记录适配器
 * @author YSG
 * @date 2017/12/27
 */
public class RecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<IntegralBean.DataBean.ListBean> list;
    private Context context;

    public RecordAdapter(List<IntegralBean.DataBean.ListBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.record_item,parent,false);
        final MyViewHolder holder=new MyViewHolder(view);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onClickItemListener.setOnItemClick(holder.getPosition());
//            }
//        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder= (MyViewHolder) holder;
        String htype=list.get(position).htype_cn;
        myViewHolder.htype.setText(list.get(position).htype_cn);
        myViewHolder.recordTime.setText(list.get(position).addtime);
        if (list.get(position).operate.equals("1")){
            myViewHolder.recordNum.setText("+"+list.get(position).points);
        }else {
            myViewHolder.recordNum.setText("-"+list.get(position).points);
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }
    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView htype,recordTime,recordNum;
        public MyViewHolder(View itemView) {
            super(itemView);
            htype=(TextView)itemView.findViewById(R.id.htype_cn);
            recordTime=(TextView)itemView.findViewById(R.id.record_time);
            recordNum=(TextView)itemView.findViewById(R.id.record_num);
        }
    }

//    public OnClickItemListener onClickItemListener;
//
//    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
//        this.onClickItemListener = onClickItemListener;
//    }
//
//    public interface OnClickItemListener{
//        void setOnItemClick(int position);
//    }
}
