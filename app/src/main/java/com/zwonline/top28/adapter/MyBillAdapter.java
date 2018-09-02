package com.zwonline.top28.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zwonline.top28.R;
import com.zwonline.top28.bean.MyBillBean;

import java.util.List;


/**
 * 描述：我的账单
 *
 * @author YSG
 * @date 2017/12/26
 */
public class MyBillAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MyBillBean.DataBean> list;
    private Context context;

    public MyBillAdapter(List<MyBillBean.DataBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.mybill_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder= (MyViewHolder) holder;
        myViewHolder.title.setText(list.get(position).title);
        myViewHolder.add_time.setText(list.get(position).add_time);
        myViewHolder.balance.setText(list.get(position).before_balance);
        if (list.get(position).type.equals("1")){
            myViewHolder.amount.setText("+"+list.get(position).amount);
        }else {
            myViewHolder.amount.setText("-"+list.get(position).amount);
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }
    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title,balance,amount,add_time;
        public MyViewHolder(View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.title);
            balance=(TextView)itemView.findViewById(R.id.before_balance);
            amount=(TextView)itemView.findViewById(R.id.amount);
            add_time=(TextView)itemView.findViewById(R.id.add_time);
        }
    }
}
