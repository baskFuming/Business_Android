package com.zwonline.top28.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zwonline.top28.R;
import com.zwonline.top28.activity.HomePageActivity;
import com.zwonline.top28.bean.DynamicDetailsBean;
import com.zwonline.top28.bean.LikeListBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.utils.ImageViewPlus;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.TimeUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.TextClick;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DynamicDetailsComentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<DynamicDetailsBean.DataBean> list;
    private Context context;
    private SharedPreferencesUtils sp;
    private boolean islogins;
    private CommentsContentInterface commentsContentInterface;
    private CommentLikeContentInterface commentLikeContentInterface;
    private ItemContentInterface itemContentInterface;

    public DynamicDetailsComentAdapter(Context context, List<DynamicDetailsBean.DataBean> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_details_item, parent, false);
        final MyViewHolder holder = new MyViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItemListener.setOnItemClick(v, holder.getPosition());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        sp = SharedPreferencesUtils.getUtil();
        islogins = (boolean) sp.getKey(context, "islogin", false);
        String uid = (String) sp.getKey(context, "uid", "");
        if (list != null && list.size() > 0) {
//            myViewHolder.commentsTime.setText(list.get(position).add_time);
            //时间转换
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
            Date date = null;
            try {
                date = formatter.parse(list.get(position).add_time);
                myViewHolder.commentsTime.setText(TimeUtil.getTimeFormatText(date));
                ToastUtils.showToast(context, list.get(position).add_time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            myViewHolder.commentsUserName.setText(list.get(position).member.nickname);
            myViewHolder.commentsContent.setText(list.get(position).content);
            RequestOptions options = new RequestOptions().placeholder(R.mipmap.no_photo_male).error(R.mipmap.no_photo_male);
            Glide.with(context).load(list.get(position).member.avatars).apply(options).into(myViewHolder.commentsUserHead);
            List<DynamicDetailsBean.DataBean.CommentsExcerptBean> commentsExcerpt = list.get(position).commentsExcerpt;
            //判断有没有子评论
            if (commentsExcerpt != null && commentsExcerpt.size() > 0) {
                myViewHolder.linear_child_comments.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemContentInterface.onclick(v, position);
                    }
                });
                int comment_count = Integer.parseInt(list.get(position).comment_count);
                myViewHolder.linear_child_comments.setVisibility(View.VISIBLE);
                if (comment_count == 1) {
                    myViewHolder.comment_user1.setVisibility(View.VISIBLE);
                    myViewHolder.comment_user2.setVisibility(View.GONE);
                    myViewHolder.look_more_comment.setVisibility(View.GONE);
                    SpannableStringBuilder spannable = new SpannableStringBuilder(list.get(position).commentsExcerpt.get(0).member.nickname);
                    spannable.append(":");
                    spannable.append(stringFilter(list.get(position).commentsExcerpt.get(0).content));
//                    if (list.get(position).user_id.equals(uid)) {
//                        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#228FFE")), 0, list.get(position).member.nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//                    } else {
                        spannable.setSpan(new TextClick(context, list.get(position).commentsExcerpt.get(0).user_id), 0, list.get(position).commentsExcerpt.get(0).member.nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//                    }
                    myViewHolder.comment_user1.setMovementMethod(LinkMovementMethod.getInstance());
                    myViewHolder.comment_user1.setText(spannable);
                } else if (comment_count > 1 && comment_count == 2) {
                    SpannableStringBuilder spannable1 = new SpannableStringBuilder(list.get(position).commentsExcerpt.get(0).member.nickname);
                    SpannableStringBuilder spannable2 = new SpannableStringBuilder(list.get(position).commentsExcerpt.get(1).member.nickname);
                    spannable1.append(":");
                    spannable1.append(stringFilter(list.get(position).commentsExcerpt.get(0).content));
                    spannable2.append(":");
                    spannable2.append(stringFilter(list.get(position).commentsExcerpt.get(1).content));
//                    if (list.get(position).user_id.equals(uid)) {
//                        spannable1.setSpan(new ForegroundColorSpan(Color.parseColor("#228FFE")), 0, list.get(position).commentsExcerpt.get(0).member.nickname.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        spannable2.setSpan(new ForegroundColorSpan(Color.parseColor("#228FFE")), 0, list.get(position).member.nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//                    } else {
                        spannable1.setSpan(new TextClick(context, list.get(position).commentsExcerpt.get(0).user_id), 0, list.get(position).commentsExcerpt.get(0).member.nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        spannable2.setSpan(new TextClick(context, list.get(position).commentsExcerpt.get(1).user_id), 0, list.get(position).commentsExcerpt.get(1).member.nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//                    }
                    myViewHolder.comment_user1.setMovementMethod(LinkMovementMethod.getInstance());
                    myViewHolder.comment_user2.setMovementMethod(LinkMovementMethod.getInstance());
                    myViewHolder.comment_user1.setText(spannable1);
                    myViewHolder.comment_user2.setText(spannable2);
                    myViewHolder.comment_user1.setVisibility(View.VISIBLE);
                    myViewHolder.comment_user2.setVisibility(View.VISIBLE);
                    myViewHolder.look_more_comment.setVisibility(View.GONE);
//                    myViewHolder.comment_user1.setText(commentsExcerpt.get(0).member.nickname + ":" + commentsExcerpt.get(0).content);
//                    myViewHolder.comment_user2.setText(commentsExcerpt.get(1).member.nickname + ":" + commentsExcerpt.get(1).content);
                } else if (comment_count > 2) {
                    SpannableStringBuilder spannable1 = new SpannableStringBuilder(list.get(position).commentsExcerpt.get(0).member.nickname);
                    SpannableStringBuilder spannable2 = new SpannableStringBuilder(list.get(position).commentsExcerpt.get(1).member.nickname);
                    spannable1.append(":");
                    spannable1.append(stringFilter(list.get(position).commentsExcerpt.get(0).content));
                    spannable2.append(":");
                    spannable2.append(stringFilter(list.get(position).commentsExcerpt.get(1).content));
//                    if (list.get(position).user_id.equals(uid)) {
//                        spannable1.setSpan(new ForegroundColorSpan(Color.parseColor("#228FFE")), 0, list.get(position).member.nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//                        spannable2.setSpan(new ForegroundColorSpan(Color.parseColor("#228FFE")), 0, list.get(position).member.nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//                    } else {
                        spannable1.setSpan(new TextClick(context, list.get(position).commentsExcerpt.get(0).user_id), 0, list.get(position).commentsExcerpt.get(0).member.nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        spannable2.setSpan(new TextClick(context, list.get(position).commentsExcerpt.get(1).user_id), 0, list.get(position).commentsExcerpt.get(1).member.nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//                    }
                    myViewHolder.comment_user1.setMovementMethod(LinkMovementMethod.getInstance());
                    myViewHolder.comment_user2.setMovementMethod(LinkMovementMethod.getInstance());
                    myViewHolder.comment_user1.setText(spannable1);
                    myViewHolder.comment_user2.setText(spannable2);
                    myViewHolder.comment_user1.setVisibility(View.VISIBLE);
                    myViewHolder.comment_user2.setVisibility(View.VISIBLE);
                    myViewHolder.look_more_comment.setVisibility(View.VISIBLE);
//                    myViewHolder.comment_user1.setText(commentsExcerpt.get(0).member.nickname + ":" + commentsExcerpt.get(0).content);
//                    myViewHolder.comment_user2.setText(commentsExcerpt.get(1).member.nickname + ":" + commentsExcerpt.get(1).content);
                    myViewHolder.look_more_comment.setText("查看全部" + list.get(position).comment_count + "条评论");
                }
            } else {
                myViewHolder.linear_child_comments.setVisibility(View.GONE);
            }
        }
        myViewHolder.commentsUserHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomePageActivity.class);
                intent.putExtra("uid", list.get(position).user_id);
                context.startActivity(intent);
            }
        });

        myViewHolder.commentsContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                commentsContentInterface.onclick(v, position, myViewHolder.commentsContent);
                return false;
            }
        });
        String did_i_vote = list.get(position).did_i_vote;
        if (StringUtil.isNotEmpty(did_i_vote) && did_i_vote.equals(BizConstant.IS_FAIL)) {
            myViewHolder.choose_like.setChecked(false);
            myViewHolder.choose_like.setEnabled(false);
            myViewHolder.like_num.setTextColor(Color.parseColor("#1d1d1d"));
        } else {
            myViewHolder.choose_like.setChecked(true);
            myViewHolder.choose_like.setEnabled(false);
            myViewHolder.like_num.setTextColor(Color.parseColor("#FF2B2B"));
        }
        myViewHolder.like_num.setText(list.get(position).like_count);//点赞数量
        myViewHolder.linear_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentLikeContentInterface.onclick(v, position, myViewHolder.choose_like, myViewHolder.like_num);
            }
        });

        myViewHolder.commentsContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemContentInterface.onclick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageViewPlus commentsUserHead;
        TextView commentsUserName, commentsTime, commentsContent, comment_user1, comment_user2, look_more_comment, like_num;
        LinearLayout linear_like;
        LinearLayout linear_child_comments;
        CheckBox choose_like;


        public MyViewHolder(View itemView) {
            super(itemView);
            commentsUserName = (TextView) itemView.findViewById(R.id.comments_username);
            commentsTime = (TextView) itemView.findViewById(R.id.comments_time);
            commentsContent = (TextView) itemView.findViewById(R.id.comments_content);
            commentsUserHead = (ImageViewPlus) itemView.findViewById(R.id.comments_userhead);
            comment_user2 = (TextView) itemView.findViewById(R.id.comment_user2);
            comment_user1 = (TextView) itemView.findViewById(R.id.comment_user1);
            like_num = (TextView) itemView.findViewById(R.id.like_num);
            look_more_comment = (TextView) itemView.findViewById(R.id.look_more_comment);
            linear_child_comments = (LinearLayout) itemView.findViewById(R.id.linear_child_comments);
            linear_like = (LinearLayout) itemView.findViewById(R.id.linear_like);
            choose_like = (CheckBox) itemView.findViewById(R.id.choose_like);

        }
    }


    public OnClickItemListener onClickItemListener;

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }

    public interface OnClickItemListener {
        void setOnItemClick(View view, int position);
    }


    /**
     * `
     * 按钮点击事件需要的方法
     */
    public void commentsContentSetOnclick(CommentsContentInterface commentsContentInterface) {
        this.commentsContentInterface = commentsContentInterface;
    }

    /**
     * 按钮点击事件对应的接口
     */
    public interface CommentsContentInterface {
        public void onclick(View view, int position, TextView textView);
    }

    /**
     * `
     * 按钮点击事件需要的方法
     */
    public void commentLikeSetOnclick(CommentLikeContentInterface commentLikeContentInterface) {
        this.commentLikeContentInterface = commentLikeContentInterface;
    }

    /**
     * 按钮点击事件对应的接口
     */
    public interface CommentLikeContentInterface {
        public void onclick(View view, int position, CheckBox checkBox, TextView textView);
    }

    /**
     * `
     * 按钮点击事件需要的方法
     */
    public void itemContentSetOnclick(ItemContentInterface itemContentInterface) {
        this.itemContentInterface = itemContentInterface;
    }


    /**
     * 按钮点击事件对应的接口
     */
    public interface ItemContentInterface {
        public void onclick(View view, int position);
    }


    /**
     * 去除特殊字符或将所有中文标号替换为英文标号
     *
     * @param str
     * @return
     */
    public static String stringFilter(String str) {
        str = str.replaceAll("【", "[").replaceAll("】", "]")
                .replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
}

