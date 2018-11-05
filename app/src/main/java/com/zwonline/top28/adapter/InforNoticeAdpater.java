package com.zwonline.top28.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zwonline.top28.R;
import com.zwonline.top28.bean.InforNoticeBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.utils.ImageViewPlus;
import com.zwonline.top28.utils.TimeUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 1：动态被赞通知；2：动态评论被赞通知 3: 动态被分享通知 4: 动态被评论通知 5： 评论被评论通知
 * subject_type	int	1文字2图片
 * identity_type	int	1 大v
 */
public class InforNoticeAdpater extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<InforNoticeBean.DataBean> dlist;
    private Context context;
    private String imagetype = "1";

    public InforNoticeAdpater(Context context, List<InforNoticeBean.DataBean> dlist) {
        this.context = context;
        this.dlist = dlist;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notice_adpater_item, parent, false);
        final MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    //数据刷新
    public void changeData(List<InforNoticeBean.DataBean> list) {
        if (list == null) {
            dlist = list;
        } else {
            this.dlist.clear();
            dlist.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final InforNoticeBean.DataBean dlistbean = dlist.get(position);
        RequestOptions requestOption = new RequestOptions().placeholder(R.mipmap.gray_logo).error(R.mipmap.gray_logo);
        //用户名称
        ((MyViewHolder) holder).textView_notice_myself.setText(dlistbean.from_user.nickname);
        RequestOptions requestOptions = new RequestOptions().placeholder(R.color.backgroud_zanwei).error(R.mipmap.no_photo_male).centerCrop();
        //用户头像
        Glide.with(context).load(dlistbean.from_user.avatars).apply(requestOptions).into(((MyViewHolder) holder).notice_tou);
        if (dlistbean.type == 1) {
            //动态被赞通知
            ((MyViewHolder) holder).image_post_Thumb.setVisibility(View.VISIBLE);
            Glide.with(context).load(R.drawable.post_thumb2).apply(requestOptions).into(((MyViewHolder) holder).image_post_Thumb);
        } else if (dlistbean.type == 2) {
            //动态评论被赞通知
            ((MyViewHolder) holder).image_post_Thumb.setVisibility(View.VISIBLE);
            Glide.with(context).load(R.drawable.post_thumb2).apply(requestOptions).into(((MyViewHolder) holder).image_post_Thumb);
        } else if (dlistbean.type == 3) {
            //动态被分享通知
            ((MyViewHolder) holder).image_post_Thumb.setVisibility(View.VISIBLE);
            Glide.with(context).load(R.mipmap.share_gray).apply(requestOptions).into(((MyViewHolder) holder).image_post_Thumb);
        } else if (dlistbean.type == 4) {
            // 动态被评论通知
            ((MyViewHolder) holder).textView_notice_title.setText(dlistbean.content);
        } else if (dlistbean.type == 5) {
            //评论被评论通知
            ((MyViewHolder) holder).textView_notice_title.setText(dlistbean.content);
        }
        ((MyViewHolder) holder).textView_notice_title.setText(dlistbean.content);
        //判断文字 图片
        if (dlistbean.subject_type == 1) {
            ((MyViewHolder) holder).textView_notice_intro.setVisibility(View.VISIBLE);
            ((MyViewHolder) holder).imageView_notice_Image.setVisibility(View.GONE);
            ((MyViewHolder) holder).textView_notice_intro.setText(dlistbean.subject);
        } else if (dlistbean.subject_type == 2) {
            ((MyViewHolder) holder).textView_notice_intro.setVisibility(View.GONE);
            ((MyViewHolder) holder).imageView_notice_Image.setVisibility(View.VISIBLE);
            Glide.with(context).load(dlistbean.subject).apply(requestOption).into(((MyViewHolder) holder).imageView_notice_Image);
        }
        //判断大V
        if (dlistbean.from_user.identity_type == 1) {
            ((MyViewHolder) holder).notice_user_V.setVisibility(View.VISIBLE);
        } else if (dlistbean.from_user.identity_type == 2) {
            ((MyViewHolder) holder).notice_user_V.setVisibility(View.GONE);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
        Date date = null;
        try {
            date = formatter.parse(dlistbean.add_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ((MyViewHolder) holder).textView_notice_time.setText(TimeUtil.getTimeFormatText(date));
        //判断消息是否读取
        if (dlistbean.is_read.equals(BizConstant.ENTERPRISE_tRUE)) {
            ((MyViewHolder) holder).image_notice_tip.setVisibility(View.VISIBLE);
        } else {
            ((MyViewHolder) holder).image_notice_tip.setVisibility(View.GONE);
        }
        //点击处理这里可以随机修改
        ((MyViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickItemListener != null) {
                    ((MyViewHolder) holder).image_notice_tip.setVisibility(View.GONE);
                    onClickItemListener.setOnItemClick(v, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dlist != null ? dlist.size() : 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.notice_tip)
        ImageView image_notice_tip;
        @BindView(R.id.notice_tou)
        ImageViewPlus notice_tou;
        @BindView(R.id.notice_user)
        ImageView notice_user_V;
        @BindView(R.id.notice_myself)
        TextView textView_notice_myself;
        @BindView(R.id.notice_post_thumb)
        ImageView image_post_Thumb;
        @BindView(R.id.notice_title)
        TextView textView_notice_title;
        @BindView(R.id.notice_words)
        TextView textView_notice_world;
        @BindView(R.id.notice_time)
        TextView textView_notice_time;
        @BindView(R.id.notice_Image)
        ImageView imageView_notice_Image;
        @BindView(R.id.reward_image)
        ImageView reward_image;
        @BindView(R.id.notice_intro)
        TextView textView_notice_intro;
        @BindView(R.id.boc_reward)
        TextView boc_reward;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    } //你说的对

    public OnClickItemListener onClickItemListener;

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }

    public interface OnClickItemListener {
        void setOnItemClick(View view, int position);
    }

    public static String getDateToString(long milSecond, String pattern) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }
}
