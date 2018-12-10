package com.zwonline.top28.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.zwonline.top28.R;
import com.zwonline.top28.bean.RecommendTeamsBean;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;

import java.util.List;

/**
 * 群推荐标签适配器
 */
public class RecommendTagsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<RecommendTeamsBean.DataBean> list;
    private Context context;
    //点击
    public OnClickItemListener onClickItemListener;
    private TagsInterface tagsInterface;
    private DeleteInterface deleteInterface;

    public RecommendTagsAdapter(List<RecommendTeamsBean.DataBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recommend_tags_item, null);
        final MyViewHolder viewHolder = new MyViewHolder(view);
//        //点击事件
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onClickItemListener.setOnItemClick(v, viewHolder.getPosition());
//            }
//        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        if (StringUtil.isEmpty(list.get(position).did_isChecked)) {
            list.get(position).did_isChecked = "0";
        }
        if (position > 8) {
            myViewHolder.tags.setEnabled(false);
            myViewHolder.deleteBt.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.deleteBt.setVisibility(View.GONE);
            myViewHolder.tags.setEnabled(true);
        }

        myViewHolder.tags.setText(list.get(position).name);
        if (StringUtil.isNotEmpty(list.get(position).did_isChecked) &&
                list.get(position).did_isChecked.equals("0")) {
            myViewHolder.tags.setChecked(false);
            myViewHolder.tags.setTextColor(Color.parseColor("#ff2b2b"));

        } else {
            myViewHolder.tags.setChecked(true);
            myViewHolder.tags.setTextColor(Color.parseColor("#ffffff"));
        }
        myViewHolder.tags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagsInterface.onclick(v, position, myViewHolder.tags);
            }
        });
        myViewHolder.deleteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteInterface.onclick(v, position, myViewHolder.tags);
//                list.remove(position);
//                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox tags;
        ImageView deleteBt;

        public MyViewHolder(View itemView) {
            super(itemView);
            tags = itemView.findViewById(R.id.tags);
            deleteBt = itemView.findViewById(R.id.delete_bt);
        }

    }


    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }

    //设置监听接口
    public interface OnClickItemListener {
        void setOnItemClick(View view, int position);

    }


    /**
     * `
     * 按钮点击事件需要的方法
     */
    public void tagsSetOnclick(TagsInterface tagsInterface) {
        this.tagsInterface = tagsInterface;
    }

    /**
     * 按钮点击事件对应的接口
     */
    public interface TagsInterface {
        public void onclick(View view, int position, CheckBox choose_like);
    }

    /**
     * `
     * 按钮点击事件需要的方法
     */
    public void deleteSetOnclick(DeleteInterface deleteInterface) {
        this.deleteInterface = deleteInterface;
    }

    /**
     * 按钮点击事件对应的接口
     */
    public interface DeleteInterface {
        public void onclick(View view, int position, CheckBox choose_like);
    }

}
