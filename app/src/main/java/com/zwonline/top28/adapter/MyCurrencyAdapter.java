package com.zwonline.top28.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zwonline.top28.R;
import com.zwonline.top28.bean.MyCurrencyBean;

import java.util.List;

/**
 * 描述：我的创业币
 *
 * @author YSG
 * @date 2017/12/25
 */
public class MyCurrencyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MyCurrencyBean.DataBean.ListBean> list;
    private Context context;

    public MyCurrencyAdapter(List<MyCurrencyBean.DataBean.ListBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mycurrency_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
//            Date date = sdf.parse(list.get(position).addtime);
            myViewHolder.addtime.setText(list.get(position).addtime);
            myViewHolder.htype_cn.setText(list.get(position).htype_cn);
            myViewHolder.number.setText("+"+list.get(position).points);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView addtime, htype_cn, number;

        public MyViewHolder(View itemView) {
            super(itemView);
            addtime = (TextView) itemView.findViewById(R.id.addtime);
            htype_cn = (TextView) itemView.findViewById(R.id.htype_cn);
            number = (TextView) itemView.findViewById(R.id.number);

        }
    }
}
