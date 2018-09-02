package com.zwonline.top28.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zwonline.top28.R;
import com.zwonline.top28.bean.IndustryBean;

import java.util.List;

/**
 * @author YSG
 * @desc
 * @date ${Date}
 */
public class IndustryAdapter extends BaseAdapter {
       private List<IndustryBean.DataBean> list;
           private Context context;

           public IndustryAdapter(List<IndustryBean.DataBean> list, Context context) {
               this.list = list;
               this.context = context;
           }

           @Override
           public int getCount() {
               return list.size();
           }

           @Override
           public Object getItem(int position) {
               return list.get(position);
           }

           @Override
           public long getItemId(int position) {
               return position;
           }

           @Override
           public View getView(int position, View convertView, ViewGroup parent) {
               ViewHolder holder;
               if (convertView==null){
                   holder=new ViewHolder();
                   convertView=View.inflate(context, R.layout.industry_item,null);
                  holder.type= (TextView) convertView.findViewById(R.id.type);
                    convertView.setTag(holder);
               }else{
                   holder= (ViewHolder) convertView.getTag();
               }
              holder.type.setText(list.get(position).cate_name);

               return convertView;
           }
           class  ViewHolder{
               TextView type;
           }


}
