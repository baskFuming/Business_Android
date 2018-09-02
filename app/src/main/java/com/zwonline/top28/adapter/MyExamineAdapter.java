package com.zwonline.top28.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zwonline.top28.R;
import com.zwonline.top28.bean.MyExamine;

import java.util.List;


/**
 * 描述：我的考察适配器
 *
 * @author YSG
 * @date 2017/12/21
 */
public class MyExamineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MyExamine.DataBean> list;
    private Context context;

    public MyExamineAdapter(List<MyExamine.DataBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.myexamine_item,parent,false);
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
        Glide.with(context).load(list.get(position).logo).into(myViewHolder.logo);
        myViewHolder.subscribe_date.setText(list.get(position).subscribe_date);
        myViewHolder.examine_name.setText(list.get(position).enterprise_name);
        myViewHolder.examine_num.setText(context.getString(R.string.inspect_booked_number, list.get(position).num));
        myViewHolder.phone.setText(R.string.inspect_phone + "："+list.get(position).mobile);
        myViewHolder.address.setText(R.string.inspect_address + "："+list.get(position).address);
        if (list.get(position).review_status.equals("1")){
            myViewHolder.inspect.setText(R.string.inspect_already_booked);
        }else {
            myViewHolder.inspect.setText(R.string.inspect_already_inspect);
            myViewHolder.inspect.setTextColor(Color.parseColor("#BBBBBB"));
            myViewHolder.inspect.setBackgroundResource(R.drawable.btn_guanzhu_shape);
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView subscribe_date, examine_name, examine_num, phone, address;
        ImageView logo;
        Button inspect;

        public MyViewHolder(View itemView) {
            super(itemView);
            subscribe_date = (TextView) itemView.findViewById(R.id.subscribe_date);
            examine_name = (TextView) itemView.findViewById(R.id.examine_name);
            examine_num = (TextView) itemView.findViewById(R.id.examine_num);
            phone = (TextView) itemView.findViewById(R.id.phone);
            address = (TextView) itemView.findViewById(R.id.address);
            logo = (ImageView) itemView.findViewById(R.id.logo);
            inspect = (Button) itemView.findViewById(R.id.inspect);
        }
    }
    public OnClickItemListener onClickItemListener;
    public void setOnClickItemListener(OnClickItemListener onClickItemListener){
        this.onClickItemListener=onClickItemListener;
    }
    public interface OnClickItemListener{
        void setOnItemClick(int position,View view);
    }
}
