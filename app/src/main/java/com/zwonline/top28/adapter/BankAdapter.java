package com.zwonline.top28.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zwonline.top28.R;
import com.zwonline.top28.bean.BankBean;
import com.zwonline.top28.utils.StringReplaceUtil;

import java.util.List;


/**
 * 描述：银行卡列表
 *
 * @author YSG
 * @date 2017/12/27
 */
public class BankAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<BankBean.DataBean> list;
    private Context context;

    public BankAdapter(List<BankBean.DataBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bank_item, parent, false);
        final MyViewHolder holder = new MyViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItemListener.setOnItemClick(v,holder.getPosition());
            }
        });
        return  holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.card_bank.setText(list.get(position).card_bank);
        String cardNamber = list.get(position).card_number;
        if (cardNamber.length() >= 4) {// 判断是否长度大于等于4
//            String cardNamber1 = cardNamber.substring(cardNamber.length() - 4);//一个参数表示截取传递的序号之后的部分
//            String cardNamber2 = cardNamber.substring(cardNamber.length() - 4, cardNamber.length());//截取两个数字之间的部分
            myViewHolder.card_number.setText(StringReplaceUtil.bankCardReplaceWithStar(cardNamber));
        } else {
            myViewHolder.card_number.setText(R.string.bank_tail_number + list.get(position).card_number);
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }
    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView card_bank, card_number;

        public MyViewHolder(View itemView) {
            super(itemView);
            card_bank = (TextView) itemView.findViewById(R.id.card_bank);
            card_number = (TextView) itemView.findViewById(R.id.card_number);
        }
    }


    public OnClickItemListener onClickItemListener;

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }

    public interface OnClickItemListener {
        void setOnItemClick(View view,int position);
    }
}
