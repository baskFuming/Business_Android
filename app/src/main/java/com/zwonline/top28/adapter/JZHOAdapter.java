package com.zwonline.top28.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zwonline.top28.R;
import com.zwonline.top28.bean.JZHOBean;

import java.util.List;

/**
 * @author YSG
 * @desc
 * @date ${Date}
 */
public class JZHOAdapter extends BaseAdapter {
        private List<JZHOBean.DataBean> list;
            private Context context;

            public JZHOAdapter(List<JZHOBean.DataBean> list, Context context) {
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
                    convertView=View.inflate(context, R.layout.jzho_item,null);
                   holder.tv_jzho= (TextView) convertView.findViewById(R.id.tv_jzho);
                     convertView.setTag(holder);
                }else{
                    holder= (ViewHolder) convertView.getTag();
                }

               holder.tv_jzho.setText(list.get(position).title);
                return convertView;
            }
            class  ViewHolder{
                TextView tv_jzho;
            }


}
