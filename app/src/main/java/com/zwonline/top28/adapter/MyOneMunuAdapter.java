package com.zwonline.top28.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.zwonline.top28.R;
import com.zwonline.top28.activity.MainActivity;
import com.zwonline.top28.bean.MyPageBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.utils.ScrollGridView;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.web.BaseWebViewActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单的适配器
 */
public class MyOneMunuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MyPageBean.DataBean> list;
    private Context context;
    private MainActivity mainActivity;
    private SharedPreferencesUtils sp;

    public MyOneMunuAdapter(List<MyPageBean.DataBean> list, Context context,MainActivity mainActivity) {
        this.list = list;
        this.context = context;
        this.mainActivity=mainActivity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_one_menu_item, parent, false);
        final MyViewHolder holder = new MyViewHolder(view);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onClickItemListener.setOnItemClick(v, holder.getPosition());
//            }
//        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        sp = SharedPreferencesUtils.getUtil();
        final String uid = (String) sp.getKey(context, "uid", "");
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.type_section.setText(list.get(position).section);
        final List<MyPageBean.DataBean.FunctionsBean> twoList = new ArrayList<>();
        for (int i = 0; i < list.get(position).functions.size(); i++) {
            twoList.addAll(list.get(position).functions.get(i));
        }

        MyTwoMunuAdapter myTwoMunuAdapter = new MyTwoMunuAdapter(twoList, context);
        myViewHolder.fuction_gridview.setAdapter(myTwoMunuAdapter);
        myViewHolder.fuction_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int positions, long id) {
                if (AntiShake.check(view.getId())) {    //判断是否多次点击
                    return;
                }
                String activityName = twoList.get(positions).link;
                if (StringUtil.isNotEmpty(activityName) && activityName.contains("http")) {
                    Intent intent = new Intent(context, BaseWebViewActivity.class);
                    intent.putExtra("eventId", twoList.get(positions).eventId);
                    intent.putExtra("weburl", activityName);
                    intent.putExtra("weburl", activityName);
                    intent.putExtra("titleBarColor", twoList.get(positions).titleBarColor);
                    intent.putExtra("project", BizConstant.ALIPAY_METHOD);
                    context.startActivity(intent);
                    if (StringUtil.isNotEmpty(twoList.get(positions).eventId)&&twoList.get(positions).eventId.equals("click_change_account")){
                    mainActivity.finish();
                    }
                } else {
                    Class clazz = null;
                    try {
                        clazz = Class.forName(BizConstant.PACKGE + activityName);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(context, clazz);
                    intent.putExtra("uid", uid);
                    intent.putExtra("jumPath", BizConstant.RECOMMENTUSER);
                    intent.putExtra("project", BizConstant.ALIPAY_METHOD);
                    context.startActivity(intent);

                }

            }
        });
        myTwoMunuAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView type_section;
        ScrollGridView fuction_gridview;

        public MyViewHolder(View itemView) {
            super(itemView);
            type_section = (TextView) itemView.findViewById(R.id.type_section);
            fuction_gridview = (ScrollGridView) itemView.findViewById(R.id.fuction_gridview);
        }
    }

//    public OnClickItemListener onClickItemListener;
//
//    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
//        this.onClickItemListener = onClickItemListener;
//    }
//
//    public interface OnClickItemListener {
//        void setOnItemClick(View view, int position);
//    }
}
