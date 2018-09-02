package com.zwonline.top28.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zwonline.top28.R;
import com.zwonline.top28.bean.EarnIntegralBean;
import com.zwonline.top28.utils.StringUtil;

import java.util.List;

/**
 * 描述：赚取积分适配器
 * @author YSG
 * @date 2017/12/27
 */
public class EarnIntegralAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<EarnIntegralBean.DataBean> list;
    private Context context;

    public EarnIntegralAdapter(List<EarnIntegralBean.DataBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.earn_item,parent,false);
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
        String once = list.get(position).once;
        String completeTimes = list.get(position).complete_times;
        String times = list.get(position).times;
        String completed = list.get(position).completed;
        String title = list.get(position).title;

        if (StringUtil.isNotEmpty(once)&&once.equals("1")){
            myViewHolder.earnOnce.setText("一次性");
        }else {
            myViewHolder.earnOnce.setText("每日");
        }
        if (StringUtil.isNotEmpty(completed)&&completed.equals("1")){
            myViewHolder.earnTtitle.setText(title+" (已完成)");
        }else if (StringUtil.isNotEmpty(completed)&&completed.equals("0")){
            myViewHolder.earnTtitle.setText(title+" ("+completeTimes+"/"+times+")");
//            ToastUtils.showToast(context,list.get(position).title+"("+completeTimes+"/"+times+")");
        }else if (StringUtil.isNotEmpty(completed)&&completed.equals("-1")){
            myViewHolder.earnTtitle.setText(title+" (无限次)");
        }
        myViewHolder.earnPoints.setText("+"+list.get(position).points);

    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }
    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView earnTtitle,earnPoints,earnOnce;
        public MyViewHolder(View itemView) {
            super(itemView);
            earnTtitle=(TextView)itemView.findViewById(R.id.earn_title);
            earnPoints=(TextView)itemView.findViewById(R.id.earn_points);
            earnOnce=(TextView)itemView.findViewById(R.id.earn_once);
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
