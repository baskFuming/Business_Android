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
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.widget.SpringView;
import com.zwonline.top28.R;
import com.zwonline.top28.adapter.CommentDetailsAdapter;
import com.zwonline.top28.adapter.CommentsAdapter;
import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.service.PayService;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.AddCommentBean;
import com.zwonline.top28.bean.ArticleCommentBean;
import com.zwonline.top28.bean.AttentionBean;
import com.zwonline.top28.bean.BusinessCoinBean;
import com.zwonline.top28.bean.GiftBean;
import com.zwonline.top28.bean.GiftSumBean;
import com.zwonline.top28.bean.HomeDetailsBean;
import com.zwonline.top28.bean.PersonageInfoBean;
import com.zwonline.top28.bean.RewardListBean;
import com.zwonline.top28.bean.ShareDataBean;
import com.zwonline.top28.bean.ZanBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.HomeDetailsPresenter;
import com.zwonline.top28.presenter.RecordUserBehavior;
import com.zwonline.top28.tip.toast.ToastUtil;
import com.zwonline.top28.utils.ImageViewPlus;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.SignUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.view.IHomeDetails;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * description：评论详情
 * author:YSG
 * data:2018/5/22
 */
public class CommentDetailsActivity extends BaseActivity<IHomeDetails, HomeDetailsPresenter> implements
        IHomeDetails, View.OnClickListener, View.OnLayoutChangeListener {

    private ListView commentDetailsList;
    private EditText editText;
    private Button send;
    private RelativeLayout back;
    private String uid;
    private String commentId;
    private ImageViewPlus commentHead;
    private List<ArticleCommentBean.DataBean.CommentsExcerptBean> list;
    private List<ArticleCommentBean.DataBean> lists;
    private RelativeLayout activityRootView;
    private TextView commentName;
    private TextView commentTime;
    private CheckBox commentLikes;
    private TextView praiseNums;
    private TextView commentContents;
    private RelativeLayout commentDians;
    private CommentDetailsAdapter adapter;
    private int page = 1;
    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;
    private String article_id;
    private String ppid;
    private String replyname;
    private CommentsAdapter adapters;
    private List<ArticleCommentBean.DataBean.CommentsExcerptBean> commentsExcerpt;
    private String zans;
    private String did_i_votes;
    private String nicnames;
    private String isuue_times;
    private String contents;
    private String avatarss;
    private SharedPreferencesUtils sp;
    private String ownAvatar;
    private String ownName;
    private boolean islogins;
    private String token;
    private PopupWindow mCurPopupWindow;
    private SpringView detailsSpring;

    @Override
    protected void init() {
        initView();
        list = new ArrayList<>();
        lists = new ArrayList<>();
        sp = SharedPreferencesUtils.getUtil();
        islogins = (boolean) sp.getKey(this, "islogin", false);
        ownAvatar = (String) sp.getKey(this, "avatar", "");
        token = (String) sp.getKey(this, "dialog", "");
        ownName = (String) sp.getKey(this, "nickname", "");
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        commentId = intent.getStringExtra("comment_id");
        article_id = intent.getStringExtra("article_id");
        zans = intent.getStringExtra("zan");
        did_i_votes = intent.getStringExtra("did_i_vote");
        nicnames = intent.getStringExtra("nicname");
        isuue_times = intent.getStringExtra("isuue_time");
        avatarss = intent.getStringExtra("avatarss");
        contents = intent.getStringExtra("content");
        presenter.mArticleComment(this, "", commentId, "", "", page);
//        headerView = getLayoutInflater().inflate(R.layout.comment_details_head, null);
        initHeadView();
        detailsSpring.setType(SpringView.Type.FOLLOW);
        detailsSpring.setFooter(new DefaultFooter(this));
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (islogins) {
                    String comment = editText.getText().toString().trim();
                    if (StringUtil.isNotEmpty(comment)) {
                        if (actionId == EditorInfo.IME_ACTION_SEND) {
                            if (!StringUtil.isEmpty(editText.getText().toString().trim())) {

                                RecordUserBehavior.recordUserBehavior(CommentDetailsActivity.this, BizConstant.DO_SEARCH);
                                if (StringUtil.isEmpty(ppid)) {
                                    presenter.mReplyComment(CommentDetailsActivity.this, article_id, comment, commentId, ppid);
                                } else {
                                    presenter.mReplyComment(CommentDetailsActivity.this, article_id, comment, commentId, ppid);
                                }
                                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                                        hideSoftInputFromWindow(CommentDetailsActivity.this.getCurrentFocus().
                                                getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                editText.setHint("写评论");
                                editText.setText("");

//                        adapter.notifyDataSetChanged();
                            } else {
                                ToastUtil.showToast(CommentDetailsActivity.this, "请输入内容！");
                            }
                            return true;
                        }

                    } else {
                        ToastUtils.showToast(CommentDetailsActivity.this, "输入内容不能为空！");
                    }

                } else {
                    ToastUtils.showToast(CommentDetailsActivity.this, "请先登录！");
                }
                return false;
            }
        });
    }


    @Override
    protected HomeDetailsPresenter getPresenter() {
        return new HomeDetailsPresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_comment_details;
    }

    private void initView() {
        commentDetailsList = (ListView) findViewById(R.id.comment_details_list);
        editText = (EditText) findViewById(R.id.editText);
        send = (Button) findViewById(R.id.send);
        activityRootView = (RelativeLayout) findViewById(R.id.comment_details_relat);
        send.setOnClickListener(this);
        back = (RelativeLayout) findViewById(R.id.back);
        back.setOnClickListener(this);
        detailsSpring = (SpringView) findViewById(R.id.details_spring);
    }

    private void initHeadView() {
        commentHead = (ImageViewPlus) findViewById(R.id.comment_head);
        commentName = (TextView) findViewById(R.id.comment_name);
        commentTime = (TextView) findViewById(R.id.comment_time);
        commentLikes = (CheckBox) findViewById(R.id.comment_likes);
        praiseNums = (TextView) findViewById(R.id.praise_nums);
        commentContents = (TextView) findViewById(R.id.comment_contents);
        commentDians = (RelativeLayout) findViewById(R.id.comment_dians);
        RequestOptions options = new RequestOptions().placeholder(R.mipmap.no_photo_male).error(R.mipmap.no_photo_male);
        Glide.with(this).load(avatarss).apply(options).into(commentHead);
        commentName.setText(nicnames);
        commentTime.setText(isuue_times);

        commentContents.setText(contents);

        commentLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//点赞
                if (islogins) {//判断是否登录
                    try {
                        int zanNum = Integer.parseInt(zans) + 1;
                        praiseNums.setText(zanNum + "");
                        praiseNums.setTextColor(Color.parseColor("#FF2B2B"));
                        commentLikes.setEnabled(false);
                        dianZan(commentId, token);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    commentLikes.setChecked(false);
                    ToastUtils.showToast(CommentDetailsActivity.this, "请先登录！");
                }
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
                                ToastUtils.showToast(CommentDetailsActivity.this, "举报成功");
                                break;
                            case R.id.cory_eomment:
                                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                // 将文本内容放到系统剪贴板里。
                                cm.setText(contents);
                                mCurPopupWindow.dismiss();
                                ToastUtils.showToast(CommentDetailsActivity.this, "复制成功");
                                break;
                        }
                    }
                });
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send:

                break;
            case R.id.back:
                finish();
                overridePendingTransition(0, R.anim.push_buttom_out);
                break;
        }
    }

    /**
     * 评论的列表
     *
     * @param articleCommentList
     */
    @Override
    public void showArticleComment(final List<ArticleCommentBean.DataBean> articleCommentList) {
        praiseNums.setText(articleCommentList.get(0).zan);
        //判断是否点赞过
        if (StringUtil.isNotEmpty(articleCommentList.get(0).did_i_vote) && articleCommentList.get(0).did_i_vote.equals(BizConstant.ALREADY_FAVORITE)) {
            commentLikes.setChecked(true);
            commentLikes.setEnabled(false);
            praiseNums.setTextColor(Color.parseColor("#FF2B2B"));

        } else {
            commentLikes.setChecked(false);
            commentLikes.setEnabled(true);
            praiseNums.setTextColor(Color.parseColor("#C2C2C2"));
        }
        if (articleCommentList != null && articleCommentList.size() > 0) {
            commentsExcerpt = articleCommentList.get(0).commentsExcerpt;
            if (commentsExcerpt != null) {
                if (page == 1) {
                    list.clear();
                }
                for (int i = 0; i < articleCommentList.size(); i++) {
                    list.addAll(articleCommentList.get(i).commentsExcerpt);
                }
//                commentDetailsList.addHeaderView(headerView);
                adapter = new CommentDetailsAdapter(list, this);
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
                                    presenter.mArticleComment(CommentDetailsActivity.this, "", commentId, "", "", page);
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
                adapters = new CommentsAdapter(lists, this);
                commentDetailsList.setAdapter(adapters);
                adapters.notifyDataSetChanged();

            }
            commentHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                    intent.putExtra("uid", articleCommentList.get(0).uid);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                }
            });
        }

    }

    @Override
    public void onError(AddCommentBean articleCommentBean) {

    }

    @Override
    public void showGiftSummary(GiftSumBean giftSumBean) {

    }

    @Override
    public void showGift(GiftBean giftBean) {

    }

    @Override
    public void showSendGifts(AttentionBean attentionBean) {

    }

    @Override
    public void showGiftList(List<RewardListBean.DataBean.ListBean> rewardLists) {

    }

    /**
     * 商机币余额
     *
     * @param rewardListBean
     */
    @Override
    public void showBocBanlance(BusinessCoinBean rewardListBean) {

    }


    @Override
    public void showHomeDetails(HomeDetailsBean homeDetails) {

    }

    @Override
    public void showShareData(ShareDataBean.DataBean shareList) {

    }

    @Override
    public void commentSuccess() {
        presenter.mArticleComment(this, "", commentId, "", "", page);
    }

    @Override
    public void onErro() {

    }

    @Override
    public void showCompany(PersonageInfoBean companyBean) {

    }

    @Override
    public void initFavorite() {

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
            finish();
            overridePendingTransition(0, R.anim.push_buttom_out);
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

}
