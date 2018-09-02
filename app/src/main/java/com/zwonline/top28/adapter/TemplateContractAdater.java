package com.zwonline.top28.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zwonline.top28.R;
import com.zwonline.top28.bean.OptionContractBean;

import java.util.List;


/**
 * 描述：合同模板
 * @author YSG
 * @date 2017/12/27
 */
public class TemplateContractAdater extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<OptionContractBean.DataBean> list;
    private Context context;

    public TemplateContractAdater(List<OptionContractBean.DataBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.add_contract_item, parent, false);
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
        myViewHolder.contractName.setText(list.get(position).title);
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView contractName;

        public MyViewHolder(View itemView) {
            super(itemView);
            contractName = (TextView) itemView.findViewById(R.id.contract_name);
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
