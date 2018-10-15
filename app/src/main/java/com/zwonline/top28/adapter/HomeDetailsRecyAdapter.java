package com.zwonline.top28.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.ClipboardManager;
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

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * 动态详情的适配器
 */
public class HomeDetailsRecyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ArticleCommentBean.DataBean> list;
    private Context context;
    private SharedPreferencesUtils sp;
    private boolean islogins;
    private PopupWindow mCurPopupWindow;
    private int dian = 0;
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    private View mHeaderView;
    public HomeDetailsRecyAdapter(List<ArticleCommentBean.DataBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyDataSetChanged();
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    //根据pos返回不同的ItemViewType
    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null) return TYPE_NORMAL;
        if (position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER)
            return new MyViewHolder(mHeaderView);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        final MyViewHolder myViewHolder = new MyViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItemListener.setOnItemClick(v, myViewHolder.getPosition());
            }
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int pos) {
        sp = SharedPreferencesUtils.getUtil();
        islogins = (boolean) sp.getKey(context, "islogin", false);
        String uid = (String) sp.getKey(context, "uid", "");
        if (getItemViewType(pos) == TYPE_HEADER) return;
        final int position = pos-1;
//        final int pos = getRealPosition(holder);
//        if (holder instanceof MyViewHolder) {
        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        if (list.size() == 0 && list == null) {
//            ToastUtils.showToast(context, "无评论！");
            myViewHolder.no_comment.setVisibility(View.VISIBLE);
            myViewHolder.have_comment.setVisibility(View.GONE);
        } else {
//            ToastUtils.showToast(context, "有评论");
            myViewHolder.no_comment.setVisibility(View.GONE);
            myViewHolder.have_comment.setVisibility(View.VISIBLE);
            myViewHolder.comment_user_name.setText(list.get(position).member.nickname);
            myViewHolder.comment_issue_time.setText(list.get(position).ctime);
            myViewHolder.praise_num.setText(list.get(position).zan);
            int comment_count = Integer.parseInt(list.get(position).comment_count);
//            ToastUtils.showToast(context, list.get(position).comment_count);

            if (comment_count == 0) {
                myViewHolder.linear_child_comments.setVisibility(View.GONE);
            }

            if (comment_count == 1) {
                SpannableStringBuilder spannable = new SpannableStringBuilder(list.get(position).commentsExcerpt.get(0).member.nickname);
                spannable.append(":");
                spannable.append(stringFilter(list.get(position).commentsExcerpt.get(0).content));
                if (list.get(position).uid.equals(uid)) {
                    spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#228FFE")), 0, list.get(position).member.nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                } else {
                    spannable.setSpan(new TextClick(context, list.get(position).uid), 0, list.get(position).commentsExcerpt.get(0).member.nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                }
                myViewHolder.comment_user1.setMovementMethod(LinkMovementMethod.getInstance());
                myViewHolder.linear_child_comments.setVisibility(View.VISIBLE);
                myViewHolder.look_more_comment.setVisibility(View.GONE);
                myViewHolder.comment_user2.setVisibility(View.GONE);
                myViewHolder.comment_user1.setText(spannable);

            }
            if (comment_count == 2) {
//                SpannableStringBuilder spannable1 = new SpannableStringBuilder(list.get(position).commentsExcerpt.get(0).member.nickname + ":" + list.get(position).commentsExcerpt.get(0).content);
//                SpannableStringBuilder spannable2 = new SpannableStringBuilder(list.get(position).commentsExcerpt.get(1).member.nickname + ":" + list.get(position).commentsExcerpt.get(1).content);
                SpannableStringBuilder spannable1 = new SpannableStringBuilder(list.get(position).commentsExcerpt.get(0).member.nickname);
                SpannableStringBuilder spannable2 = new SpannableStringBuilder(list.get(position).commentsExcerpt.get(1).member.nickname);
                spannable1.append(":");
                spannable1.append(stringFilter(list.get(position).commentsExcerpt.get(0).content));
                spannable2.append(":");
                spannable2.append(stringFilter(list.get(position).commentsExcerpt.get(1).content));
                if (list.get(position).uid.equals(uid)) {
                    spannable1.setSpan(new ForegroundColorSpan(Color.parseColor("#228FFE")), 0, list.get(position).commentsExcerpt.get(0).member.nickname.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannable2.setSpan(new ForegroundColorSpan(Color.parseColor("#228FFE")), 0, list.get(position).member.nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                } else {
                    spannable1.setSpan(new TextClick(context, list.get(position).uid), 0, list.get(position).commentsExcerpt.get(0).member.nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    spannable2.setSpan(new TextClick(context, list.get(position).uid), 0, list.get(position).commentsExcerpt.get(1).member.nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                }
                myViewHolder.linear_child_comments.setVisibility(View.VISIBLE);
                myViewHolder.look_more_comment.setVisibility(View.GONE);
                myViewHolder.comment_user1.setMovementMethod(LinkMovementMethod.getInstance());
                myViewHolder.comment_user2.setMovementMethod(LinkMovementMethod.getInstance());
                myViewHolder.comment_user1.setText(spannable1);
                myViewHolder.comment_user2.setText(spannable2);
                myViewHolder.comment_user1.setVisibility(View.VISIBLE);
                myViewHolder.comment_user2.setVisibility(View.VISIBLE);
            }

            if (comment_count > 2) {
//                SpannableStringBuilder spannable1 = new SpannableStringBuilder(list.get(position).commentsExcerpt.get(0).member.nickname + ":" + list.get(position).commentsExcerpt.get(0).content);
//                SpannableStringBuilder spannable2 = new SpannableStringBuilder(list.get(position).commentsExcerpt.get(1).member.nickname + ":" + list.get(position).commentsExcerpt.get(1).content);
                SpannableStringBuilder spannable1 = new SpannableStringBuilder(list.get(position).commentsExcerpt.get(0).member.nickname);
                SpannableStringBuilder spannable2 = new SpannableStringBuilder(list.get(position).commentsExcerpt.get(1).member.nickname);
                spannable1.append(":");
                spannable1.append(stringFilter(list.get(position).commentsExcerpt.get(0).content));
                spannable2.append(":");
                spannable2.append(stringFilter(list.get(position).commentsExcerpt.get(1).content));
                if (list.get(position).uid.equals(uid)) {
                    spannable1.setSpan(new ForegroundColorSpan(Color.parseColor("#228FFE")), 0, list.get(position).member.nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    spannable2.setSpan(new ForegroundColorSpan(Color.parseColor("#228FFE")), 0, list.get(position).member.nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                } else {
                    spannable1.setSpan(new TextClick(context, list.get(position).uid), 0, list.get(position).commentsExcerpt.get(0).member.nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    spannable2.setSpan(new TextClick(context, list.get(position).uid), 0, list.get(position).commentsExcerpt.get(1).member.nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                }
                myViewHolder.look_more_comment.setVisibility(View.VISIBLE);
                myViewHolder.look_more_comment.setText("查看" + list.get(position).comment_count + "条评论");
                myViewHolder.comment_user1.setMovementMethod(LinkMovementMethod.getInstance());
                myViewHolder.comment_user2.setMovementMethod(LinkMovementMethod.getInstance());
                myViewHolder.comment_user1.setText(spannable1);
                myViewHolder.comment_user2.setText(spannable2);
            }
            if (list.get(position).pp_user == null) {
                myViewHolder.comment_content.setText(list.get(position).content);
            } else if (list.get(position).pp_user != null) {
                String nickname = "//@" + list.get(position).pp_user.nickname;
                SpannableStringBuilder spannable1 = new SpannableStringBuilder(nickname + ":" + list.get(position).content);
                if (list.get(position).uid.equals(uid)) {
                    spannable1.setSpan(new ForegroundColorSpan(Color.parseColor("#228FFE")), 0, list.get(position).pp_user.nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                } else {
                    spannable1.setSpan(new TextClick(context, list.get(position).pp_user.user_id), 2, list.get(position).pp_user.nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                }
                myViewHolder.comment_content.setMovementMethod(LinkMovementMethod.getInstance());
                myViewHolder.comment_content.setText(spannable1);
            }
            RequestOptions options = new RequestOptions().placeholder(R.mipmap.no_photo_male).error(R.mipmap.no_photo_male);
            Glide.with(context).load(list.get(position).member.avatars).apply(options).into(myViewHolder.comment_user_head);
            myViewHolder.comment_user_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, HomePageActivity.class);
                    intent.putExtra("uid", list.get(position).uid);
                    context.startActivity(intent);
                }
            });
            if (list.get(position).did_i_vote.equals("1")) {

            }
            if (list.get(position).did_i_vote.equals("1")) {
                myViewHolder.praise_num.setTextColor(Color.parseColor("#FF2B2B"));
                myViewHolder.comment_like.setChecked(true);
                myViewHolder.praise_like.setEnabled(false);
            } else if (list.get(position).did_i_vote.equals("0")) {
                myViewHolder.praise_num.setTextColor(Color.parseColor("#C2C2C2"));
                myViewHolder.comment_like.setChecked(false);
                myViewHolder.praise_like.setEnabled(true);
            }
            myViewHolder.comment_content.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mCurPopupWindow = showTipPopupWindow(myViewHolder.comment_content, new View.OnClickListener() {
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
                                    cm.setText(list.get(position).content);
                                    mCurPopupWindow.dismiss();
                                    ToastUtils.showToast(context, "复制成功");
                                    break;
                            }
                        }
                    });
                    return false;
                }
            });
            myViewHolder.praise_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (islogins) {

                        String token = (String) sp.getKey(context, "dialog", "");
                        try {
                            int vote = Integer.parseInt(list.get(position).zan) + 1;
                            dianZan(list.get(position).comment_id, token);
                            myViewHolder.comment_like.setChecked(true);
                            myViewHolder.praise_num.setText(vote + "");
                            myViewHolder.praise_num.setTextColor(Color.parseColor("#FF2B2B"));
                            myViewHolder.praise_like.setEnabled(false);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        myViewHolder.comment_like.setChecked(false);
                        ToastUtils.showToast(context, "请先登录！");
                    }

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() + 1 : 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView comment_user_name, comment_issue_time, praise_num,  look_more_comment, comment_content,comment_user1, comment_user2;
        ImageViewPlus comment_user_head;
        CheckBox comment_like;
        LinearLayout linear_child_comments, have_comment, no_comment;
        RelativeLayout praise_like;

        public MyViewHolder(View itemView) {
            super(itemView);
            comment_user_name = (TextView)itemView.findViewById(R.id.comment_user_name);
            comment_issue_time = (TextView)itemView.findViewById(R.id.comment_issue_time);
            praise_num = (TextView)itemView.findViewById(R.id.praise_num);
            comment_user1 = (TextView)itemView.findViewById(R.id.comment_user1);
            comment_user2 = (TextView)itemView.findViewById(R.id.comment_user2);
            look_more_comment = (TextView)itemView.findViewById(R.id.look_more_comment);
            comment_content = (TextView)itemView.findViewById(R.id.comment_content);
            linear_child_comments = (LinearLayout)itemView.findViewById(R.id.linear_child_comments);
            have_comment = (LinearLayout)itemView.findViewById(R.id.have_comment);
            no_comment = (LinearLayout)itemView.findViewById(R.id.no_comment_list);
            comment_user_head = (ImageViewPlus)itemView.findViewById(R.id.comment_user_head);
            comment_like = (CheckBox)itemView.findViewById(R.id.comment_like);
            praise_like = (RelativeLayout)itemView.findViewById(R.id.praise_like);

        }
    }
    public OnClickItemListener onClickItemListener;

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }

    public interface OnClickItemListener {
        void setOnItemClick(View view, int position);
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

}
