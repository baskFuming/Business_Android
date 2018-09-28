package com.zwonline.top28.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.ClipboardManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.widget.SpringView;
import com.zwonline.top28.R;
import com.zwonline.top28.adapter.DynamicCommentDetailsAdapter;
import com.zwonline.top28.adapter.DynamicCommentsAdapter;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.AddBankBean;
import com.zwonline.top28.bean.AtentionDynamicHeadBean;
import com.zwonline.top28.bean.AttentionBean;
import com.zwonline.top28.bean.BusinessCircleBean;
import com.zwonline.top28.bean.DynamicDetailsBean;
import com.zwonline.top28.bean.DynamicDetailsesBean;
import com.zwonline.top28.bean.DynamicShareBean;
import com.zwonline.top28.bean.LikeListBean;
import com.zwonline.top28.bean.NewContentBean;
import com.zwonline.top28.bean.PictursBean;
import com.zwonline.top28.bean.RefotPasswordBean;
import com.zwonline.top28.bean.SendNewMomentBean;
import com.zwonline.top28.bean.SettingBean;
import com.zwonline.top28.bean.ShieldUserBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.RecordUserBehavior;
import com.zwonline.top28.presenter.SendFriendCirclePresenter;
import com.zwonline.top28.tip.toast.ToastUtil;
import com.zwonline.top28.utils.ImageViewPlus;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.TimeUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.view.ISendFriendCircleActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 商机圈动态评论详情
 */
public class DynamicCommentDetailsActivity extends BaseActivity<ISendFriendCircleActivity, SendFriendCirclePresenter> implements ISendFriendCircleActivity, View.OnClickListener, View.OnLayoutChangeListener {

    private RelativeLayout back;

    private ImageViewPlus commentHead;
    private TextView commentName;
    private TextView commentTime;
    private CheckBox commentLikes;
    private TextView praiseNums;
    private RelativeLayout commentDians;
    private TextView commentContents;
    private TextView noComments;
    private ListView commentDetailsList;
    private SpringView detailsSpring;
    private EditText editText;
    private Button send;
    private RelativeLayout commentDetailsRelat;
    private SharedPreferencesUtils sp;
    private String ownAvatar;
    private String ownName;
    private boolean islogins;
    private String token;
    private String uid;
    private String commentId;
    private String article_id;
    private String zans;
    private String did_i_votes;
    private String nicnames;
    private String isuue_times;
    private String avatarss;
    private String contents;
    private PopupWindow mCurPopupWindow;
    private List<DynamicDetailsBean.DataBean.CommentsExcerptBean> list;
    private List<DynamicDetailsBean.DataBean> lists;
    private List<DynamicDetailsBean.DataBean.CommentsExcerptBean> commentsExcerpt;
    private DynamicCommentsAdapter adapters;
    private DynamicCommentDetailsAdapter adapter;
    private int page = 1;
    private String ppid;
    private RelativeLayout activityRootView;
    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;
    private LinearLayout linearLike;
    private String content_num;

    @Override
    protected void init() {
        list = new ArrayList<>();
        lists = new ArrayList<>();
        sp = SharedPreferencesUtils.getUtil();
        islogins = (boolean) sp.getKey(this, "islogin", false);
        ownAvatar = (String) sp.getKey(this, "avatar", "");
        token = (String) sp.getKey(this, "dialog", "");
        ownName = (String) sp.getKey(this, "nickname", "");
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        content_num = intent.getStringExtra("content_num");
        commentId = intent.getStringExtra("comment_id");
        article_id = intent.getStringExtra("article_id");
        zans = intent.getStringExtra("zan");
        did_i_votes = intent.getStringExtra("did_i_vote");
        nicnames = intent.getStringExtra("nicname");
        isuue_times = intent.getStringExtra("isuue_time");
        avatarss = intent.getStringExtra("avatarss");
        contents = intent.getStringExtra("content");
        presenter.mDynamicComment(this, page, article_id, commentId, "", "");
        initView();
    }

    @Override
    protected SendFriendCirclePresenter getPresenter() {
        return new SendFriendCirclePresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_comment_details;
    }

    private void initView() {
        back = (RelativeLayout) findViewById(R.id.back);
        back.setOnClickListener(this);
        activityRootView = (RelativeLayout) findViewById(R.id.comment_details_relat);
        commentHead = (ImageViewPlus) findViewById(R.id.comment_head);
        commentName = (TextView) findViewById(R.id.comment_name);
        commentTime = (TextView) findViewById(R.id.comment_time);
        commentLikes = (CheckBox) findViewById(R.id.comment_likes);
        praiseNums = (TextView) findViewById(R.id.praise_nums);
        commentDians = (RelativeLayout) findViewById(R.id.comment_dians);
        commentContents = (TextView) findViewById(R.id.comment_contents);
        noComments = (TextView) findViewById(R.id.no_comments);
        commentDetailsList = (ListView) findViewById(R.id.comment_details_list);
        detailsSpring = (SpringView) findViewById(R.id.details_spring);
        detailsSpring.setType(SpringView.Type.FOLLOW);
        detailsSpring.setFooter(new DefaultFooter(this));
        editText = (EditText) findViewById(R.id.editText);
        send = (Button) findViewById(R.id.send);
        linearLike = (LinearLayout) findViewById(R.id.linear_like);
        linearLike.setOnClickListener(this);
        commentDetailsRelat = (RelativeLayout) findViewById(R.id.comment_details_relat);
        //判断是否攒过
        if (StringUtil.isNotEmpty(did_i_votes) && did_i_votes.equals(BizConstant.IS_FAIL)) {
            commentLikes.setChecked(false);
            commentLikes.setEnabled(false);
            praiseNums.setTextColor(Color.parseColor("#1d1d1d"));
        } else {
            commentLikes.setChecked(true);
            commentLikes.setEnabled(false);
            praiseNums.setTextColor(Color.parseColor("#1d1d1d"));
        }
        if (StringUtil.isNotEmpty(zans)) {
            praiseNums.setText(zans);
        }
        RequestOptions options = new RequestOptions().placeholder(R.mipmap.no_photo_male).error(R.mipmap.no_photo_male);
        Glide.with(this).load(avatarss).apply(options).into(commentHead);
        commentName.setText(nicnames);
        commentHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DynamicCommentDetailsActivity.this, HomePageActivity.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
            }
        });
        //时间转换
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
        Date date = null;
        try {
            date = formatter.parse(isuue_times);
            commentTime.setText(TimeUtil.getTimeFormatText(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (StringUtil.isNotEmpty(contents)) {
            commentContents.setText(contents);
        }

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (islogins) {
                    String comment = editText.getText().toString().trim();
                    if (StringUtil.isNotEmpty(comment)) {
                        if (actionId == EditorInfo.IME_ACTION_SEND) {
                            if (!StringUtil.isEmpty(editText.getText().toString().trim())) {

                                RecordUserBehavior.recordUserBehavior(DynamicCommentDetailsActivity.this, BizConstant.DO_SEARCH);
                                if (StringUtil.isEmpty(ppid)) {
//                                    presenter.NewComment(DynamicCommentDetailsActivity.this, article_id, comment, commentId, ppid);
                                    presenter.NewComment(DynamicCommentDetailsActivity.this, comment, commentId, "", article_id);
                                } else {
                                    presenter.NewComment(DynamicCommentDetailsActivity.this, comment, commentId, ppid, article_id);
                                }
                                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                                        hideSoftInputFromWindow(DynamicCommentDetailsActivity.this.getCurrentFocus().
                                                getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                editText.setHint("写评论");
                                editText.setText("");

//                        adapter.notifyDataSetChanged();
                            } else {
                                ToastUtil.showToast(DynamicCommentDetailsActivity.this, "请输入内容！");
                            }
                            return true;
                        }

                    } else {
                        ToastUtils.showToast(getApplicationContext(), "输入内容不能为空！");
                    }

                } else {
                    ToastUtils.showToast(getApplicationContext(), "请先登录！");
                }
                return false;
            }
        });
        commentContents.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mCurPopupWindow = showTipPopupWindow(commentContents, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.report:
                                mCurPopupWindow.dismiss();
                                ToastUtils.showToast(getApplicationContext(), "举报成功");
                                break;
                            case R.id.cory_eomment:
                                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                // 将文本内容放到系统剪贴板里。
                                cm.setText(contents);
                                mCurPopupWindow.dismiss();
                                ToastUtils.showToast(getApplicationContext(), "复制成功");
                                break;
                        }
                    }
                });
                return false;
            }
        });
        send.setOnClickListener(this);
    }


    /**
     * 评论列表
     *
     * @param articleCommentList
     */
    @Override
    public void showDynamicComment(final List<DynamicDetailsBean.DataBean> articleCommentList) {
        if (articleCommentList != null && articleCommentList.size() > 0) {
            commentsExcerpt = articleCommentList.get(0).commentsExcerpt;
            if (commentsExcerpt != null) {
                if (page == 1) {
                    list.clear();
                }
                for (int i = 0; i < articleCommentList.size(); i++) {
                    list.addAll(articleCommentList.get(i).commentsExcerpt);
                }
                adapter = new DynamicCommentDetailsAdapter(list, this);
                commentDetailsList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                commentDetailsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //显示软键盘
                        InputMethodManager m = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        ppid = list.get(position).comment_id;
                        editText.setHint("回复 " + list.get(position).member.nickname);
                    }
                });

                detailsSpring.setListener(new SpringView.OnFreshListener() {
                    @Override
                    public void onRefresh() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                            }
                        }, 1000);
                    }

                    @Override
                    public void onLoadmore() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (list.size() < 20) {
                                    ToastUtils.showToast(getApplicationContext(), "没有更多了！");
                                    detailsSpring.onFinishFreshAndLoad();
                                } else {
                                    page++;
                                    presenter.mDynamicComment(DynamicCommentDetailsActivity.this, page, article_id, commentId, "", "");
                                    detailsSpring.onFinishFreshAndLoad();
                                }
//                        adapter.notifyDataSetChanged();
                            }
                        }, 1000);
                    }
                });
                adapter.notifyDataSetChanged();

            } else {
                lists.addAll(articleCommentList);
//                ToastUtils.showToast(this, "没有评论");
//                commentDetailsList.addHeaderView(headerView);
                adapters = new DynamicCommentsAdapter(lists, this);
                commentDetailsList.setAdapter(adapters);
                adapters.notifyDataSetChanged();

            }
            commentHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                    intent.putExtra("uid", articleCommentList.get(0).user_id);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                }
            });
        }
    }


    /**
     * 添加评论
     *
     * @param addBankBean
     */
    @Override
    public void showNewComment(AddBankBean addBankBean) {
        if (addBankBean.status == 1) {
            int content_nums = Integer.parseInt(content_num) + 1;
            content_num = content_nums + "";
        }
        presenter.mDynamicComment(this, page, article_id, commentId, "", "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.back:
                Intent intent = new Intent();
                intent.putExtra("zan_num", zans);
                intent.putExtra("did_i_vote", did_i_votes);
                intent.putExtra("content_num", content_num);
                setResult(RESULT_OK, intent);
                finish();
                overridePendingTransition(0, R.anim.push_buttom_out);
                break;
            case R.id.linear_like:
                if (islogins) {
                    if (did_i_votes.equals(BizConstant.IS_FAIL)) {
                        presenter.LikeMomentComment(getApplicationContext(), commentId);
                    } else {
                        ToastUtils.showToast(getApplicationContext(), "已经点过赞了哦");
                    }
                } else {
                    ToastUtils.showToast(getApplicationContext(), "请先登录");
                }
                break;
        }
    }

    @Override
    public void showPictures(PictursBean pictursBean) {

    }

    @Override
    public void showSendNewMoment(SendNewMomentBean sendNewMomentBean) {

    }

    @Override
    public void showConment(List<NewContentBean.DataBean> newList) {

    }

    @Override
    public void showFeedBack(SettingBean settingBean) {

    }


    @Override
    public void showDynamicShare(DynamicShareBean dynamicShareBean) {

    }


    @Override
    public void showDeleteMoment(SettingBean settingBean) {

    }

    @Override
    public void showAttention(AttentionBean attentionBean) {

    }

    /**
     * 一键关注
     *
     * @param attentionBean
     */
    @Override
    public void showAttentions(AttentionBean attentionBean) {

    }

    @Override
    public void showUnAttention(AttentionBean attentionBean) {

    }

    @Override
    public void showBlockUser(RefotPasswordBean refotPasswordBean) {

    }

    @Override
    public void showLikeMoment(AttentionBean attentionBean) {

    }

    /**
     * 屏蔽用户列表
     *
     * @param shielduserList
     */
    @Override
    public void showBlockUserList(List<ShieldUserBean.DataBean> shielduserList) {

    }

    /**
     * 屏蔽列表判断有没有数据
     *
     * @param flag
     */
    @Override
    public void showUserList(boolean flag) {

    }

    /**
     * 没有数据
     */
    @Override
    public void noLoadMore() {

    }

    /**
     * 动态评论点赞
     *
     * @param attentionBean
     */
    @Override
    public void showLikeMomentComment(AttentionBean attentionBean) {
        if (attentionBean.status == 1) {
            commentLikes.setChecked(true);
            commentLikes.setEnabled(false);
            praiseNums.setTextColor(Color.parseColor("#1d1d1d"));
            int likeNum = Integer.parseInt(zans) + 1;
            praiseNums.setText(likeNum + "");
            did_i_votes = BizConstant.IS_SUC;
            zans = likeNum + "";
        } else {
            ToastUtil.showToast(getApplicationContext(), attentionBean.msg);
        }
    }

    /**
     * 删除动态评论
     *
     * @param attentionBean
     */
    @Override
    public void showDeleteComment(AttentionBean attentionBean) {

    }

    /**
     * 推荐关注
     *
     * @param issueList
     */
    @Override
    public void showBusincDate(List<BusinessCircleBean.DataBean> issueList) {

    }

    /**
     * 顶部推荐关注列表
     *
     * @param issueList
     */
    @Override
    public void showAttentionDynamic(List<AtentionDynamicHeadBean.DataBean.ListBean> issueList) {

    }

    /**
     * 商机圈我的消息提醒
     *
     * @param attentionBean
     */
    @Override
    public void showGetMyNotificationCount(AttentionBean attentionBean) {

    }

    @Override
    public void showGetLikeList(List<LikeListBean.DataBean> likeList) {

    }

    /**
     * 动态详情接口
     *
     * @param mommentList
     */
    @Override
    public void showMomentDetail(DynamicDetailsesBean mommentList) {

    }

    /**
     * 举报
     * @param attentionBean
     */
    @Override
    public void showReport(AttentionBean attentionBean) {

    }


    @Override
    public void onLayoutChange(View v, int left, int top, int right,
                               int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;
        //old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值
        //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
            //隐藏软键盘
            ppid = "";
            editText.setHint("写评论");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        activityRootView.addOnLayoutChangeListener(this);
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
//        contentView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (v.getId()){
//                    case R.id.
//                }
//                popupWindow.dismiss();
//                onClickListener.onClick(v);
//            }
//        });

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
     * 系统返回键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent();
            intent.putExtra("zan_num", zans);
            intent.putExtra("did_i_vote", did_i_votes);
            intent.putExtra("content_num", content_num);
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(0, R.anim.push_buttom_out);
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

}