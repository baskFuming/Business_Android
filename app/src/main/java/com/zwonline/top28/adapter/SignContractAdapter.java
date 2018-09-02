package com.zwonline.top28.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zwonline.top28.R;
import com.zwonline.top28.bean.AddClauseBean;
import com.zwonline.top28.bean.SignContractBean;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.popwindow.ContractPopuWindow;

import java.util.List;

/**
 * @author YSG
 * @desc签署合同的适配器
 * @date ${Date}
 */
public class SignContractAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SignContractBean.DataBean.TermsBean> list;

    private Context context;
    private MyViewHolder myViewHolder;
    private int pos;
    private List<AddClauseBean.DataBean.TermsBean> tarmsList;

    public SignContractAdapter(List<SignContractBean.DataBean.TermsBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contract_clause_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        myViewHolder = (MyViewHolder) holder;
        myViewHolder.clauseNames.setText(list.get(position).title);
        myViewHolder.clauseRatios.setText(list.get(position).percent);
        myViewHolder.clauseContents.setText(list.get(position).content);
        myViewHolder.linear.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView clauseNames, clauseRatios, clauseContents;
        LinearLayout linear;
        public MyViewHolder(View itemView) {
            super(itemView);
            clauseNames = (TextView) itemView.findViewById(R.id.contract_clause_name);
            clauseRatios = (TextView) itemView.findViewById(R.id.contract_clause_ratio);
            clauseContents = (TextView) itemView.findViewById(R.id.contract_clause_content);
            linear = (LinearLayout) itemView.findViewById(R.id.line);
        }
    }
}
