package com.zwonline.top28.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.zwonline.top28.R;
import com.zwonline.top28.activity.CompanyActivity;
import com.zwonline.top28.activity.CustomContractActivity;
import com.zwonline.top28.bean.AddClauseBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.popwindow.ContractPopuWindow;

import java.util.List;

/**
 * @author YSG
 * @desc添加条款的适配器
 * @date ${Date}
 */
public class AddClauseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<AddClauseBean.DataBean.TermsBean> list;

    private Context context;
    private ContractPopuWindow contractPopuWindow;
    private EditText clauseName;
    private EditText clauseRatio;
    private EditText clauseContent;
    private String clause_names;
    private String clause_ratios;
    private String clause_contents;
    private MyViewHolder myViewHolder;
    private int pos;
    private List<AddClauseBean.DataBean.TermsBean> tarmsList;

    public AddClauseAdapter(List<AddClauseBean.DataBean.TermsBean> list, Context context) {
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

        clause_names = list.get(position).title;
        clause_ratios = list.get(position).percent;
        clause_contents = list.get(position).content;

        myViewHolder.clauseNames.setText(clause_names);
        myViewHolder.clauseRatios.setText(clause_ratios);
        myViewHolder.clauseContents.setText(clause_contents);
        myViewHolder.clauseDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNormalDialogFollow(position);
            }
        });
        myViewHolder.clauseEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos = position;
                contractPopuWindow = new ContractPopuWindow((Activity) context, listener);
                contractPopuWindow.showAtLocation(v, Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                View contentView = contractPopuWindow.getContentView();
                clauseName = (EditText) contentView.findViewById(R.id.clause_name);
                clauseRatio = (EditText) contentView.findViewById(R.id.clause_ratio);
                clauseContent = (EditText) contentView.findViewById(R.id.clause_content);
                TextView sure = (TextView) contentView.findViewById(R.id.sure);
                clauseName.setText(list.get(position).title);
                clauseName.setSelection(clauseName.getText().length());
                clauseRatio.setText(list.get(position).percent);
                clauseContent.setText(list.get(position).content);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    //  添加数据
    public void addData(int position, String clauseNames, String clauseRatios, String clauseContents) {
//      在list中添加数据，并通知条目加入一条
        list.add(position, new AddClauseBean.DataBean.TermsBean(clauseNames, clauseRatios, clauseContents));
        //添加动画
    }

    //  删除数据
    public void removeData(int position) {
        list.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView clauseNames, clauseRatios, clauseContents, clauseDel, clauseEdit;

        public MyViewHolder(View itemView) {
            super(itemView);
            clauseNames = (TextView) itemView.findViewById(R.id.contract_clause_name);
            clauseRatios = (TextView) itemView.findViewById(R.id.contract_clause_ratio);
            clauseContents = (TextView) itemView.findViewById(R.id.contract_clause_content);
            clauseDel = (TextView) itemView.findViewById(R.id.clause_del);
            clauseEdit = (TextView) itemView.findViewById(R.id.clause_edit);
        }
    }

    /**
     * popwindow弹窗
     */
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {//关闭
                case R.id.clause_close:
                    contractPopuWindow.dismiss();
                    contractPopuWindow.backgroundAlpha((Activity) context, 1f);
                    break;
                case R.id.sure://保存
                    if (StringUtil.isNotEmpty(clauseName.getText().toString())) {
                        if (StringUtil.isNotEmpty(clauseRatio.getText().toString())) {
                            if (StringUtil.isNotEmpty(clauseContent.getText().toString())) {
                                contractPopuWindow.dismiss();
                                contractPopuWindow.backgroundAlpha((Activity) context, 1f);
                                list.remove(pos);
                                AddClauseBean.DataBean.TermsBean termsBean = new AddClauseBean.DataBean.TermsBean(clauseName.getText().toString().trim(),
                                        clauseRatio.getText().toString().trim(),
                                        clauseContent.getText().toString().trim());
                                list.add(pos, termsBean);
//                    list.clear();
                                notifyDataSetChanged();
                            } else {
                                ToastUtils.showToast(context, context.getString(R.string.clause_content_not_empty));
                            }
                        } else {
                            ToastUtils.showToast(context, context.getString(R.string.clause_proportion_not_empty));
                        }
                    } else {
                        ToastUtils.showToast(context, context.getString(R.string.clause_title_not_empty));
                    }


                    break;
            }
        }
    };

    //删除条款的弹框
    private void showNormalDialogFollow(final int position) {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("普通的对话框的标题");
        builder.setMessage(context.getString(R.string.ensure_delete));
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setPositiveButton(context.getString(R.string.ensure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeData(position);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
