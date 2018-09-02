package com.zwonline.top28.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zwonline.top28.R;
import com.zwonline.top28.bean.PaymentBean;

import java.util.List;

/**
 * 描述：收付款记录适配器
 * @author YSG
 * @date 2017/12/27
 */
public class PayMentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<PaymentBean.DataBean> list;
    private Context context;

    public PayMentAdapter(List<PaymentBean.DataBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.wallet_item,parent,false);
        final MyViewHolder holder=new MyViewHolder(view);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onClickItemListener.setOnItemClick(holder.getPosition());
//            }
//        });
        return holder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder= (MyViewHolder) holder;
        RequestOptions options=new RequestOptions().placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher);
        Glide.with(context).load(list.get(position).create_member.avatar)
                .apply(options).into(myViewHolder.avatar);
        if (list.get(position).is_confirm.equals("1")){
            myViewHolder.confirm.setText(context.getString(R.string.pay_completed , list.get(position).create_member.username));
        }else {
            myViewHolder.confirm.setText(R.string.pay_paymenting);
            myViewHolder.confirm.setTextColor(Color.RED);
        }
        if (list.get(position).is_paid.equals("1")){
            myViewHolder.payment.setText(R.string.pay_payment);
        }else {
            myViewHolder.payment.setText(R.string.pay_collection);
        }
        myViewHolder.ctime.setText(list.get(position).ctime);
        myViewHolder.money.setText(list.get(position).amount+"");
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }
    static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView avatar;
        TextView payment,confirm,money,ctime;
        public MyViewHolder(View itemView) {
            super(itemView);
            avatar=(ImageView)itemView.findViewById(R.id.userhead);
            payment=(TextView)itemView.findViewById(R.id.payment);
            confirm=(TextView)itemView.findViewById(R.id.confirm);
            money=(TextView)itemView.findViewById(R.id.money);
            ctime=(TextView)itemView.findViewById(R.id.ctime);
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
