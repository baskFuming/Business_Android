package com.zwonline.top28.adapter;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zwonline.top28.R;
import com.zwonline.top28.activity.HomePageActivity;
import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.service.PayService;
import com.zwonline.top28.bean.ArticleCommentBean;
import com.zwonline.top28.bean.ZanBean;
import com.zwonline.top28.utils.ImageViewPlus;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.SignUtils;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.TextClick;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class ArticleCommentDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ArticleCommentBean.DataBean> list ;
    private Context context;
    private SharedPreferencesUtils sp;
    private boolean islogins;
    private PopupWindow mCurPopupWindow;
    private int dian = 0;
    private View view;

    public ArticleCommentDetailsAdapter(List<ArticleCommentBean.DataBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false);
        return new MyViewHolder(view);
    }
    //((MyViewHolder)holder)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final ArticleCommentBean.DataBean bean = list.get(position);
        sp = SharedPreferencesUtils.getUtil();
        islogins = (boolean) sp.getKey(context, "islogin", false);
        String uid = (String) sp.getKey(context, "uid", "");
        if (list.size()==0&&list==null){
            ((MyViewHolder)holder).no_comment.setVisibility(View.VISIBLE);
            ((MyViewHolder)holder).have_comment.setVisibility(View.GONE);
        }else {
            ToastUtils.showToast(context, "有评论");
            ((MyViewHolder)holder).no_comment.setVisibility(View.GONE);
            ((MyViewHolder)holder).have_comment.setVisibility(View.VISIBLE);
            ((MyViewHolder)holder).comment_user_name.setText(bean.member.nickname);
            ((MyViewHolder)holder).comment_issue_time.setText(bean.ctime);
            ((MyViewHolder)holder).praise_num.setText(bean.zan);
            int comment_count = Integer.parseInt(bean.comment_count);
            if (comment_count == 0) {
                ((MyViewHolder)holder).linear_child_comments.setVisibility(View.GONE);
            }
            if (comment_count == 1) {
                SpannableStringBuilder spannable = new SpannableStringBuilder(bean.commentsExcerpt.get(0).member.nickname);
                spannable.append(":");
                spannable.append(stringFilter(bean.commentsExcerpt.get(0).content));
                if (bean.uid.equals(uid)) {
                    spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#228FFE")), 0, bean.member.nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                } else {
                    spannable.setSpan(new TextClick(context, bean.uid), 0, bean.commentsExcerpt.get(0).member.nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                }
                ((MyViewHolder)holder).comment_user1.setMovementMethod(LinkMovementMethod.getInstance());
                ((MyViewHolder)holder).linear_child_comments.setVisibility(View.VISIBLE);
                ((MyViewHolder)holder).look_more_comment.setVisibility(View.GONE);
                ((MyViewHolder)holder).comment_user2.setVisibility(View.GONE);
                ((MyViewHolder)holder).comment_user1.setText(spannable);
            }
            if (comment_count == 2) {
                SpannableStringBuilder spannable1 = new SpannableStringBuilder(bean.commentsExcerpt.get(0).member.nickname);
                SpannableStringBuilder spannable2 = new SpannableStringBuilder(bean.commentsExcerpt.get(1).member.nickname);
                spannable1.append(":");
                spannable1.append(stringFilter(bean.commentsExcerpt.get(0).content));
                spannable2.append(":");
                spannable2.append(stringFilter(bean.commentsExcerpt.get(1).content));
                if (bean.uid.equals(uid)) {
                    spannable1.setSpan(new ForegroundColorSpan(Color.parseColor("#228FFE")), 0, bean.commentsExcerpt.get(0).member.nickname.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannable2.setSpan(new ForegroundColorSpan(Color.parseColor("#228FFE")), 0, bean.member.nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                } else {
                    spannable1.setSpan(new TextClick(context, bean.uid), 0, bean.commentsExcerpt.get(0).member.nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    spannable2.setSpan(new TextClick(context, bean.uid), 0, bean.commentsExcerpt.get(1).member.nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                }
                ((MyViewHolder)holder).linear_child_comments.setVisibility(View.VISIBLE);
                ((MyViewHolder)holder).look_more_comment.setVisibility(View.GONE);
                ((MyViewHolder)holder).comment_user1.setMovementMethod(LinkMovementMethod.getInstance());
                ((MyViewHolder)holder).comment_user2.setMovementMethod(LinkMovementMethod.getInstance());
                ((MyViewHolder)holder).comment_user1.setText(spannable1);
                ((MyViewHolder)holder).comment_user2.setText(spannable2);
                ((MyViewHolder)holder).comment_user1.setVisibility(View.VISIBLE);
                ((MyViewHolder)holder).comment_user2.setVisibility(View.VISIBLE);
            }
            if (comment_count > 2) {
                SpannableStringBuilder spannable1 = new SpannableStringBuilder(bean.commentsExcerpt.get(0).member.nickname);
                SpannableStringBuilder spannable2 = new SpannableStringBuilder(bean.commentsExcerpt.get(1).member.nickname);
                spannable1.append(":");
                spannable1.append(stringFilter(bean.commentsExcerpt.get(0).content));
                spannable2.append(":");
                spannable2.append(stringFilter(bean.commentsExcerpt.get(1).content));
                if (bean.uid.equals(uid)) {
                    spannable1.setSpan(new ForegroundColorSpan(Color.parseColor("#228FFE")), 0, bean.member.nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    spannable2.setSpan(new ForegroundColorSpan(Color.parseColor("#228FFE")), 0, bean.member.nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                } else {
                    spannable1.setSpan(new TextClick(context, bean.uid), 0, bean.commentsExcerpt.get(0).member.nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    spannable2.setSpan(new TextClick(context, bean.uid), 0, bean.commentsExcerpt.get(1).member.nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                }
                ((MyViewHolder)holder).look_more_comment.setVisibility(View.VISIBLE);
                ((MyViewHolder)holder).look_more_comment.setText("查看" + bean.comment_count + "条评论");
                ((MyViewHolder)holder).comment_user1.setMovementMethod(LinkMovementMethod.getInstance());
                ((MyViewHolder)holder).comment_user2.setMovementMethod(LinkMovementMethod.getInstance());
                ((MyViewHolder)holder).comment_user1.setText(spannable1);
                ((MyViewHolder)holder).comment_user2.setText(spannable2);
            }
            if (bean.pp_user == null) {
                ((MyViewHolder)holder).comment_content.setText(bean.content);
            } else if (bean.pp_user != null) {
                String nickname = "//@" + bean.pp_user.nickname;
                SpannableStringBuilder spannable1 = new SpannableStringBuilder(nickname + ":" + bean.content);
                if (bean.uid.equals(uid)) {
                    spannable1.setSpan(new ForegroundColorSpan(Color.parseColor("#228FFE")), 0, bean.pp_user.nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                } else {
                    spannable1.setSpan(new TextClick(context, bean.pp_user.user_id), 2, bean.pp_user.nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                }
                ((MyViewHolder)holder).comment_content.setMovementMethod(LinkMovementMethod.getInstance());
                ((MyViewHolder)holder).comment_content.setText(spannable1);
            }
            RequestOptions options = new RequestOptions().placeholder(R.mipmap.no_photo_male).error(R.mipmap.no_photo_male);
            Glide.with(context).load(bean.member.avatars).apply(options).into(((MyViewHolder)holder).comment_user_head);
            ((MyViewHolder)holder).comment_user_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, HomePageActivity.class);
                    intent.putExtra("uid", bean.uid);
                    context.startActivity(intent);
                }
            });
            if (bean.did_i_vote.equals("1")) {
                ((MyViewHolder)holder).praise_num.setTextColor(Color.parseColor("#FF2B2B"));
                ((MyViewHolder)holder).comment_like.setChecked(true);
                ((MyViewHolder)holder).praise_like.setEnabled(false);
            } else if (bean.did_i_vote.equals("0")) {
                ((MyViewHolder)holder).praise_num.setTextColor(Color.parseColor("#C2C2C2"));
                ((MyViewHolder)holder).comment_like.setChecked(false);
                ((MyViewHolder)holder).praise_like.setEnabled(true);
            }
            ((MyViewHolder)holder).comment_content.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mCurPopupWindow = showTipPopupWindow(((MyViewHolder)holder).comment_content, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (v.getId()) {
                                case R.id.report:
                                    mCurPopupWindow.dismiss();
                                    ToastUtils.showToast(context, "举报成功");
                                    break;
                                case R.id.cory_eomment:
                                    ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                                    // 将文本内容放到系统剪贴板里。
                                    cm.setText(bean.content);
                                    mCurPopupWindow.dismiss();
                                    ToastUtils.showToast(context, "复制成功");
                                    break;
                            }
                        }
                    });
                    return false;
                }
            });
            ((MyViewHolder)holder).praise_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (islogins) {

                        String token = (String) sp.getKey(context, "dialog", "");
                        try {
                            int vote = Integer.parseInt(bean.zan) + 1;
                            dianZan(bean.comment_id, token);
                            ((MyViewHolder)holder).comment_like.setChecked(true);
                            ((MyViewHolder)holder).praise_num.setText(vote + "");
                            ((MyViewHolder)holder).praise_num.setTextColor(Color.parseColor("#FF2B2B"));
                            ((MyViewHolder)holder).praise_like.setEnabled(false);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        ((MyViewHolder)holder).comment_like.setChecked(false);
                        ToastUtils.showToast(context, "请先登录！");
                    }

                }
            });

        }
        //点击处理这里可以随机修改
        ((MyViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickItemListener != null) {
                    onClickItemListener.setOnItemClick(v, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    //绑定数据
    static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.comment_user_name)
        TextView comment_user_name;
        @BindView(R.id.comment_issue_time)
        TextView comment_issue_time;
        @BindView(R.id.praise_num)
        TextView praise_num;
        @BindView(R.id.look_more_comment)
        TextView look_more_comment;
        @BindView(R.id.comment_content)
        TextView comment_content;
        @BindView(R.id.comment_user1)
        TextView comment_user1;
        @BindView(R.id.comment_user2)
        TextView comment_user2;
        @BindView(R.id.comment_user_head)
        ImageViewPlus comment_user_head;
        @BindView(R.id.comment_like)
        CheckBox comment_like;
        @BindView(R.id.linear_child_comments)
        LinearLayout linear_child_comments;
        @BindView(R.id.have_comment)
        LinearLayout have_comment;
        @BindView(R.id.no_comment_list)
        LinearLayout no_comment;
        @BindView(R.id.praise_like)
        RelativeLayout praise_like;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //点赞的网络请求
    public void dianZan(String comment_id, String token) throws IOException {
        long timestamp = new Date().getTime() / 1000;
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("comment_id", comment_id);
        map.put("token", token);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<ZanBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(PayService.class, Api.url)
                .iZan(String.valueOf(timestamp), token, comment_id, sign);
        flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<ZanBean>() {
                    @Override
                    public void onNext(ZanBean attentionBean) {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public PopupWindow showTipPopupWindow(final View anchorView, final View.OnClickListener onClickListener) {
        final View contentView = LayoutInflater.from(anchorView.getContext()).inflate(R.layout.pop_top_window, null);
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 创建PopupWindow时候指定高宽时showAsDropDown能够自适应
        // 如果设置为wrap_content,showAsDropDown会认为下面空间一直很充足（我以认为这个Google的bug）
        // 备注如果PopupWindow里面有ListView,ScrollView时，一定要动态设置PopupWindow的大小
        final PopupWindow popupWindow = new PopupWindow(contentView,
                contentView.getMeasuredWidth(), contentView.getMeasuredHeight(), false);
        contentView.findViewById(R.id.cory_eomment).setOnClickListener(onClickListener);
        contentView.findViewById(R.id.report).setOnClickListener(onClickListener);
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 自动调整箭头的位置
                autoAdjustArrowPos(popupWindow, contentView, anchorView);
                contentView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        // 如果不设置PopupWindow的背景，有些版本就会出现一个问题：无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(new ColorDrawable());

        // setOutsideTouchable设置生效的前提是setTouchable(true)和setFocusable(false)
        popupWindow.setOutsideTouchable(true);

        // 设置为true之后，PopupWindow内容区域 才可以响应点击事件
        popupWindow.setTouchable(true);

        // true时，点击返回键先消失 PopupWindow
        // 但是设置为true时setOutsideTouchable，setTouchable方法就失效了（点击外部不消失，内容区域也不响应事件）
        // false时PopupWindow不处理返回键
        popupWindow.setFocusable(false);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;   // 这里面拦截不到返回键
            }
        });
        // 如果希望showAsDropDown方法能够在下面空间不足时自动在anchorView的上面弹出
        // 必须在创建PopupWindow的时候指定高度，不能用wrap_content
        popupWindow.showAsDropDown(anchorView);
        return popupWindow;
    }

    private void autoAdjustArrowPos(PopupWindow popupWindow, View contentView, View anchorView) {
        View upArrow = contentView.findViewById(R.id.up_arrow);
        View downArrow = contentView.findViewById(R.id.down_arrow);
        int pos[] = new int[2];
        contentView.getLocationOnScreen(pos);
        int popLeftPos = pos[0];
        anchorView.getLocationOnScreen(pos);
        int anchorLeftPos = pos[0];
        int arrowLeftMargin = anchorLeftPos - popLeftPos + anchorView.getWidth() / 2 - upArrow.getWidth() / 2;
        upArrow.setVisibility(popupWindow.isAboveAnchor() ? View.INVISIBLE : View.VISIBLE);
        downArrow.setVisibility(popupWindow.isAboveAnchor() ? View.VISIBLE : View.INVISIBLE);

        RelativeLayout.LayoutParams upArrowParams = (RelativeLayout.LayoutParams) upArrow.getLayoutParams();
        upArrowParams.leftMargin = arrowLeftMargin;
        RelativeLayout.LayoutParams downArrowParams = (RelativeLayout.LayoutParams) downArrow.getLayoutParams();
        downArrowParams.leftMargin = arrowLeftMargin;
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
    public OnClickItemListener onClickItemListener;

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }

    public interface OnClickItemListener {
        void setOnItemClick(View view, int position);
    }

}
