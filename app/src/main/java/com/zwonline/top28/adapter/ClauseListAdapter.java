package com.zwonline.top28.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zwonline.top28.R;
import com.zwonline.top28.bean.AddClauseBean;

import java.util.List;

/**
 * @author YSG
 * @desc添加条款的适配器
 * @date ${Date}
 */
public class ClauseListAdapter extends BaseAdapter{
        private List<AddClauseBean> list;
            private Context context;

            public ClauseListAdapter(List<AddClauseBean> list, Context context) {
                this.list = list;
                this.context = context;
            }

            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder;
                if (convertView==null){
                    holder=new ViewHolder();
                    convertView=View.inflate(context, R.layout.contract_clause_item,null);

                     convertView.setTag(holder);
                }else{
                    holder= (ViewHolder) convertView.getTag();
                }


                return convertView;
            }
            class  ViewHolder{

            }


}
