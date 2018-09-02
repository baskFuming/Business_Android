package com.zwonline.top28.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zwonline.top28.R;
import com.zwonline.top28.bean.WithdrawRecordBean;

import java.util.List;

/**
     * 描述：提现记录
     * @author YSG
     * @date 2018/1/10
     */
public class WithdrawRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<WithdrawRecordBean.DataBean>list;
    private Context context;
    public WithdrawRecordAdapter(List<WithdrawRecordBean.DataBean>list,Context context){
        this.list=list;
        this.context=context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.withdrawrecord_item,parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder= (MyViewHolder) holder;
        myViewHolder.cash.setText("￥"+list.get(position).cash);
        if (list.get(position).status.equals("0")){
            myViewHolder.dispose.setText(R.string.pay_pending_withdraw);
        }else if (list.get(position).status.equals("1")){
            myViewHolder.dispose.setText(R.string.pay_audit_suc_withdraw);
        }
        myViewHolder.cash.setText("￥"+list.get(position).cash);
        myViewHolder.review_time.setText(list.get(position).add_time);
    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }
    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView cash,dispose,review_time;
        public MyViewHolder(View itemView) {
            super(itemView);
            cash=(TextView)itemView.findViewById(R.id.cash);
            dispose=(TextView)itemView.findViewById(R.id.dispose);
            review_time=(TextView)itemView.findViewById(R.id.review_time);
        }
    }
}
