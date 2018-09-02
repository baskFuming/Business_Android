package com.zwonline.top28.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zwonline.top28.R;
import com.zwonline.top28.activity.DynamicDetailsActivity;
import com.zwonline.top28.activity.HomePageActivity;
import com.zwonline.top28.bean.DynamicDetailsBean;
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

public class DynamicDetailsAdapter extends BaseAdapter {
    private List<DynamicDetailsBean.DataBean> list;
    private Context context;
    private SharedPreferencesUtils sp;
    private boolean islogins;
    private CommentsContentInterface commentsContentInterface;
    private CommentLikeContentInterface commentLikeContentInterface;

    public DynamicDetailsAdapter(Context context, List<DynamicDetailsBean.DataBean> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        sp = SharedPreferencesUtils.getUtil();
        islogins = (boolean) sp.getKey(context, "islogin", false);
        String uid = (String) sp.getKey(context, "uid", "");
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.dynamic_details_item, null);
            holder.commentsUserName = (TextView) convertView.findViewById(R.id.comments_username);
            holder.commentsTime = (TextView) convertView.findViewById(R.id.comments_time);
            holder.commentsContent = (TextView) convertView.findViewById(R.id.comments_content);
            holder.commentsUserHead = (ImageViewPlus) convertView.findViewById(R.id.comments_userhead);
            holder.comment_user2 = (TextView) convertView.findViewById(R.id.comment_user2);
            holder.comment_user1 = (TextView) convertView.findViewById(R.id.comment_user1);
            holder.like_num = (TextView) convertView.findViewById(R.id.like_num);
            holder.look_more_comment = (TextView) convertView.findViewById(R.id.look_more_comment);
            holder.linear_child_comments = (LinearLayout) convertView.findViewById(R.id.linear_child_comments);
            holder.linear_like = (LinearLayout) convertView.findViewById(R.id.linear_like);
            holder.choose_like = (CheckBox) convertView.findViewById(R.id.choose_like);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (list != null && list.size() > 0) {
//            holder.commentsTime.setText(list.get(position).add_time);
            //时间转换
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
            Date date = null;
            try {
                date = formatter.parse(list.get(position).add_time);
                holder.commentsTime.setText(TimeUtil.getTimeFormatText(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.commentsUserName.setText(list.get(position).member.nickname);
            holder.commentsContent.setText(list.get(position).content);
            RequestOptions options = new RequestOptions().placeholder(R.mipmap.no_photo_male).error(R.mipmap.no_photo_male);
            Glide.with(context).load(list.get(position).member.avatars).apply(options).into(holder.commentsUserHead);
            List<DynamicDetailsBean.DataBean.CommentsExcerptBean> commentsExcerpt = list.get(position).commentsExcerpt;
            //判断有没有子评论
            if (commentsExcerpt != null && commentsExcerpt.size() > 0) {
                int comment_count = Integer.parseInt(list.get(position).comment_count);
                holder.linear_child_comments.setVisibility(View.VISIBLE);
                if (comment_count == 1) {
                    holder.comment_user1.setVisibility(View.VISIBLE);
                    holder.comment_user2.setVisibility(View.GONE);
                    holder.look_more_comment.setVisibility(View.GONE);
                    SpannableStringBuilder spannable = new SpannableStringBuilder(list.get(position).commentsExcerpt.get(0).member.nickname);
                    spannable.append(":");
                    spannable.append(stringFilter(list.get(position).commentsExcerpt.get(0).content));
                    if (list.get(position).user_id.equals(uid)) {
                        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#228FFE")), 0, list.get(position).member.nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    } else {
                        spannable.setSpan(new TextClick(context, list.get(position).user_id), 0, list.get(position).commentsExcerpt.get(0).member.nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    }
                    holder.comment_user1.setMovementMethod(LinkMovementMethod.getInstance());
                    holder.comment_user1.setText(spannable);
                } else if (comment_count > 1 && comment_count == 2) {
                    SpannableStringBuilder spannable1 = new SpannableStringBuilder(list.get(position).commentsExcerpt.get(0).member.nickname);
                    SpannableStringBuilder spannable2 = new SpannableStringBuilder(list.get(position).commentsExcerpt.get(1).member.nickname);
                    spannable1.append(":");
                    spannable1.append(stringFilter(list.get(position).commentsExcerpt.get(0).content));
                    spannable2.append(":");
                    spannable2.append(stringFilter(list.get(position).commentsExcerpt.get(1).content));
                    if (list.get(position).user_id.equals(uid)) {
                        spannable1.setSpan(new ForegroundColorSpan(Color.parseColor("#228FFE")), 0, list.get(position).commentsExcerpt.get(0).member.nickname.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        spannable2.setSpan(new ForegroundColorSpan(Color.parseColor("#228FFE")), 0, list.get(position).member.nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    } else {
                        spannable1.setSpan(new TextClick(context, list.get(position).user_id), 0, list.get(position).commentsExcerpt.get(0).member.nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        spannable2.setSpan(new TextClick(context, list.get(position).user_id), 0, list.get(position).commentsExcerpt.get(1).member.nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    }
                    holder.comment_user1.setMovementMethod(LinkMovementMethod.getInstance());
                    holder.comment_user2.setMovementMethod(LinkMovementMethod.getInstance());
                    holder.comment_user1.setText(spannable1);
                    holder.comment_user2.setText(spannable2);
                    holder.comment_user1.setVisibility(View.VISIBLE);
                    holder.comment_user2.setVisibility(View.VISIBLE);
                    holder.look_more_comment.setVisibility(View.GONE);
//                    holder.comment_user1.setText(commentsExcerpt.get(0).member.nickname + ":" + commentsExcerpt.get(0).content);
//                    holder.comment_user2.setText(commentsExcerpt.get(1).member.nickname + ":" + commentsExcerpt.get(1).content);
                } else if (comment_count > 2) {
                    SpannableStringBuilder spannable1 = new SpannableStringBuilder(list.get(position).commentsExcerpt.get(0).member.nickname);
                    SpannableStringBuilder spannable2 = new SpannableStringBuilder(list.get(position).commentsExcerpt.get(1).member.nickname);
                    spannable1.append(":");
                    spannable1.append(stringFilter(list.get(position).commentsExcerpt.get(0).content));
                    spannable2.append(":");
                    spannable2.append(stringFilter(list.get(position).commentsExcerpt.get(1).content));
                    if (list.get(position).user_id.equals(uid)) {
                        spannable1.setSpan(new ForegroundColorSpan(Color.parseColor("#228FFE")), 0, list.get(position).member.nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        spannable2.setSpan(new ForegroundColorSpan(Color.parseColor("#228FFE")), 0, list.get(position).member.nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    } else {
                        spannable1.setSpan(new TextClick(context, list.get(position).user_id), 0, list.get(position).commentsExcerpt.get(0).member.nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        spannable2.setSpan(new TextClick(context, list.get(position).user_id), 0, list.get(position).commentsExcerpt.get(1).member.nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    }
                    holder.comment_user1.setMovementMethod(LinkMovementMethod.getInstance());
                    holder.comment_user2.setMovementMethod(LinkMovementMethod.getInstance());
                    holder.comment_user1.setText(spannable1);
                    holder.comment_user2.setText(spannable2);
                    holder.comment_user1.setVisibility(View.VISIBLE);
                    holder.comment_user2.setVisibility(View.VISIBLE);
                    holder.look_more_comment.setVisibility(View.VISIBLE);
//                    holder.comment_user1.setText(commentsExcerpt.get(0).member.nickname + ":" + commentsExcerpt.get(0).content);
//                    holder.comment_user2.setText(commentsExcerpt.get(1).member.nickname + ":" + commentsExcerpt.get(1).content);
                    holder.look_more_comment.setText("查看全部" + list.get(position).comment_count + "条评论");
                }
            } else {
                holder.linear_child_comments.setVisibility(View.GONE);
            }
        }
        holder.commentsUserHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomePageActivity.class);
                intent.putExtra("uid", list.get(position).user_id);
                context.startActivity(intent);
            }
        });

        holder.commentsContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                commentsContentInterface.onclick(v, position, holder.commentsContent);
                return false;
            }
        });
        String did_i_vote = list.get(position).did_i_vote;
        if (StringUtil.isNotEmpty(did_i_vote) && did_i_vote.equals(BizConstant.IS_FAIL)) {
            holder.choose_like.setChecked(false);
            holder.choose_like.setEnabled(false);
            holder.like_num.setTextColor(Color.parseColor("#1d1d1d"));
        } else {
            holder.choose_like.setChecked(true);
            holder.choose_like.setEnabled(false);
            holder.like_num.setTextColor(Color.parseColor("#FF2B2B"));
        }
        holder.like_num.setText(list.get(position).like_count);//点赞数量
        holder.linear_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentLikeContentInterface.onclick(v, position, holder.choose_like, holder.like_num);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        ImageViewPlus commentsUserHead;
        TextView commentsUserName, commentsTime, commentsContent, comment_user1, comment_user2, look_more_comment, like_num;
        LinearLayout linear_child_comments, linear_like;
        CheckBox choose_like;

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
