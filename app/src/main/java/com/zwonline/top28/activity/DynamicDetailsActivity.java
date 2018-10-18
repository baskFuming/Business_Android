package com.zwonline.top28.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zwonline.top28.R;
import com.zwonline.top28.adapter.DynamicDetailsComentAdapter;
import com.zwonline.top28.adapter.LikeListAdapter;
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
import com.zwonline.top28.bean.PhotoInfos;
import com.zwonline.top28.bean.PictursBean;
import com.zwonline.top28.bean.RefotPasswordBean;
import com.zwonline.top28.bean.SendNewMomentBean;
import com.zwonline.top28.bean.SettingBean;
import com.zwonline.top28.bean.ShieldUserBean;
import com.zwonline.top28.bean.message.MessageFollow;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.SendFriendCirclePresenter;
import com.zwonline.top28.tip.toast.ToastUtil;
import com.zwonline.top28.utils.ImageViewPlu;
import com.zwonline.top28.utils.ImageViewPlus;
import com.zwonline.top28.utils.MultiImageView;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.TimeUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.utils.popwindow.DynamicCommentPopuWindow;
import com.zwonline.top28.view.ISendFriendCircleActivity;
import com.zwonline.top28.wxapi.RewritePopwindow;
import com.zwonline.top28.wxapi.ShareUtilses;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 商机圈动态详情
 */
public class DynamicDetailsActivity extends BaseActivity<ISendFriendCircleActivity, SendFriendCirclePresenter> implements ISendFriendCircleActivity {

    private RelativeLayout back;
    private TextView title;
    private XRecyclerView dynamicdetailsList;
    private List<DynamicDetailsBean.DataBean> dynamicList;
    private String author_id;
    private String avatars;
    private String nickname;
    private String add_date;
    private String content;
    private String[] imageUrls;
    private String[] orinal_imageUrls;
    private View headView;
    private DynamicDetailsComentAdapter adapter;
    private int page = 1;
    private LinearLayout linearShare;
    private RewritePopwindow mPopwindow;
    private String share_url;
    private String share_icon;
    private String share_description;
    private String share_title;
    private String moment_id;
    private TextView likeAcount;
    private TextView commentAcount;
    private TextView attention;
    private LinearLayout linearComment;
    private LinearLayout linearLike;
    private DynamicCommentPopuWindow dynamicCommentPopuWindow;
    private EditText commentEt;
    private String type;
    private String articleDesc;
    private String articleID;
    private String articleImage;
    private String articleTitle;
    private String did_i_like;
    private String did_i_follow;
    private SharedPreferencesUtils sp;
    private boolean islogins;
    private String uid;
    private TextView isLike;
    private CheckBox choose_like;
    private String comment_count;
    private String like_count;
    private PopupWindow mCurPopupWindow;
    private int deliteCommentPosition;
    private int commentLikePosition;
    private String hight;
    private String width;
    private TextView tvNum;
    private int positions;
    private String commentContent;
    private ArrayList<String> commentList;
    private XRecyclerView zanRecy;
    private TextView commentUnderline;
    private TextView likeUnderline;
    private List<LikeListBean.DataBean> likeLists;
    private LikeListAdapter likeListAdapter;
    private int refreshTime = 0;
    private int times = 0;
    private String isComment;
    private ImageViewPlus userHead;
    private TextView userName;
    private TextView time;
    private TextView article_title;
    private LinearLayout article_linear;
    private TextView article_desc;
    private ImageViewPlu article_img;
    private TextView dynamicConment;
    private LinearLayout imagLinear;
    private MultiImageView multiImage;
    private ImageView dynamic_imag_w;
    private ImageView dynamic_imag_h;
    private ImageView dynamic_imag_z;
    private RelativeLayout imag_relative;

    @BindView(R.id.appBarLayout)
    AppBarLayout appbarlayout;

    @Subscribe
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void init() {

        sp = SharedPreferencesUtils.getUtil();
        islogins = (boolean) sp.getKey(getApplicationContext(), "islogin", false);
        uid = (String) sp.getKey(getApplicationContext(), "uid", "");
        dynamicList = new ArrayList<>();
        commentList = new ArrayList<>();
        likeLists = new ArrayList<>();
        Intent intent = getIntent();
        moment_id = intent.getStringExtra("moment_id");
        isComment = intent.getStringExtra("isComment");
        initView();
//        if (StringUtil.strIsNum(moment_id)){
            presenter.MomentDetail(this, moment_id);
            presenter.mDynamicComment(this, page, moment_id, "", "", "");
            presenter.mDynamicShare(this, moment_id);
            presenter.GetLikeList(this, moment_id, page);
//        }

//        headView = getLayoutInflater().inflate(R.layout.dynamicdetails_head, null);
        initListView();
        recyclerViewData();
    }

    /**
     * xRecyclerview配置
     */
    private void recyclerViewData() {
        dynamicdetailsList.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        dynamicdetailsList.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        dynamicdetailsList.setArrowImageView(R.drawable.iconfont_downgrey);
        dynamicdetailsList.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        dynamicdetailsList.getDefaultFootView().setLoadingHint(getString(R.string.loading));
        dynamicdetailsList.getDefaultFootView().setNoMoreHint(getString(R.string.load_end));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        dynamicdetailsList.setLayoutManager(linearLayoutManager);
        adapter = new DynamicDetailsComentAdapter(this, dynamicList);
        dynamicdetailsList.setAdapter(adapter);

        //点赞列表配置
        zanRecy.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        zanRecy.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        zanRecy.setArrowImageView(R.drawable.iconfont_downgrey);
        zanRecy.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        zanRecy.getDefaultFootView().setLoadingHint(getString(R.string.loading));
        zanRecy.getDefaultFootView().setNoMoreHint(getString(R.string.load_end));
        LinearLayoutManager linearLayoutManagers = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        zanRecy.setLayoutManager(linearLayoutManagers);
        likeListAdapter = new LikeListAdapter(likeLists, this);
        zanRecy.setAdapter(likeListAdapter);
    }

    /**
     * 控件查找
     */
    private void initListView() {
        userHead = (ImageViewPlus) findViewById(R.id.userhead);
        userName = (TextView) findViewById(R.id.username);
        time = (TextView) findViewById(R.id.time);
        likeAcount = (TextView) findViewById(R.id.like_acount);
        commentAcount = (TextView) findViewById(R.id.comment_acount);
        article_linear = (LinearLayout) findViewById(R.id.article_linear);
        article_title = (TextView) findViewById(R.id.article_title);
        article_desc = (TextView) findViewById(R.id.article_desc);
        commentUnderline = (TextView) findViewById(R.id.comment_underline);
        likeUnderline = (TextView) findViewById(R.id.like_underline);
        article_img = (ImageViewPlu) findViewById(R.id.article_img);
        zanRecy = (XRecyclerView) findViewById(R.id.zan_recy);
        attention = (TextView) findViewById(R.id.attention);
        dynamicConment = (TextView) findViewById(R.id.dynamic_conment);
        imagLinear = (LinearLayout) findViewById(R.id.imag_linear);
        multiImage = (MultiImageView) findViewById(R.id.multi_image);
        dynamic_imag_w = (ImageView) findViewById(R.id.dynamic_imag_w);
        dynamic_imag_h = (ImageView) findViewById(R.id.dynamic_imag_h);
        dynamic_imag_z = (ImageView) findViewById(R.id.dynamic_imag_z);
        imag_relative = (RelativeLayout) findViewById(R.id.imag_relative);

    }

    /**
     * 动态详情接口
     *
     * @param mommentList
     */
    @Override
    public void showMomentDetail(DynamicDetailsesBean mommentList) {
        if (mommentList.status==1){
            commentUnderline.setVisibility(View.VISIBLE);
            author_id = mommentList.data.user_id;
            avatars = mommentList.data.author.avatars;
            nickname = mommentList.data.author.nickname;
            add_date = mommentList.data.add_time;
            content = mommentList.data.content;
            type = mommentList.data.type;
            comment_count = mommentList.data.comment_count;
            like_count = mommentList.data.like_count;
            articleDesc = mommentList.data.extend_content.target_description;
            articleID = mommentList.data.extend_content.target_id;
            articleImage = mommentList.data.extend_content.target_image;
            articleTitle = mommentList.data.extend_content.target_title;
            did_i_follow = mommentList.data.did_i_follow;
            did_i_like = mommentList.data.did_i_like;
            if (mommentList.data.images_arr != null) {
                String image[] = new String[mommentList.data.images_arr.size()];
                String images[] = new String[mommentList.data.images_arr.size()];
                for (int i = 0; i < mommentList.data.images_arr.size(); i++) {
                    image[i] = mommentList.data.images_arr.get(i).original;
                    images[i] = mommentList.data.images_arr.get(i).thumb;
                }
                if (mommentList.data.images_arr.size() == 1) {
                    hight = mommentList.data.images_arr.get(0).original_size.height;
                    width = mommentList.data.images_arr.get(0).original_size.width;
                }
                imageUrls = images;
                orinal_imageUrls = image;
            }
            commentAcount.setText("评论 " + comment_count + "条");
            likeAcount.setText("赞 " + like_count);
            RequestOptions requestOptions = new RequestOptions().placeholder(R.mipmap.no_photo_male).error(R.mipmap.no_photo_male);
            Glide.with(this).load(avatars).apply(requestOptions).into(userHead);
            userName.setText(nickname);


            userHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DynamicDetailsActivity.this, HomePageActivity.class);
                    intent.putExtra("uid", author_id);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                }
            });
            //时间转换
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
            Date date = null;
            try {
                date = formatter.parse(add_date);
                time.setText(TimeUtil.getTimeFormatText(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (StringUtil.isNotEmpty(uid) && StringUtil.isNotEmpty(author_id) && uid.equals(author_id)) {
                attention.setVisibility(View.GONE);
            }
            //判断是否一关注
            if (StringUtil.isNotEmpty(did_i_follow) && did_i_follow.equals(BizConstant.IS_FAIL)) {
                attention.setText(R.string.common_btn_add_focus);
                attention.setBackgroundResource(R.drawable.dynamic_guanzhu_shape);
                attention.setTextColor(Color.parseColor("#FF2B2B"));
            } else if (StringUtil.isNotEmpty(did_i_follow) && did_i_follow.equals(BizConstant.IS_SUC)) {
                attention.setText(R.string.common_followed);
                attention.setBackgroundResource(R.drawable.dynamic_unguanzhu_shape);
                attention.setTextColor(Color.parseColor("#ffffff"));
            }
            //点击关注或取消关注
            attention.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (islogins) {
                        if (StringUtil.isNotEmpty(did_i_follow) && did_i_follow.equals(BizConstant.IS_FAIL)) {

                            if (StringUtil.isNotEmpty(uid) && !uid.equals(author_id)) {
                                presenter.mAttention(getApplicationContext(), "follow", author_id, BizConstant.ALREADY_FAVORITE);
                            } else {
                                ToastUtils.showToast(getApplicationContext(), "自己不能关注自己");
                            }
                        } else {
                            presenter.mUnAttention(getApplicationContext(), "un_follow", author_id);
                        }
                    } else {
                        ToastUtils.showToast(getApplicationContext(), "请先登录");
                    }

                }
            });
            //判断内容类型
            if (type.equals(BizConstant.ALREADY_FAVORITE)) {
                article_linear.setVisibility(View.GONE);
                dynamicConment.setVisibility(View.VISIBLE);
                if (StringUtil.isNotEmpty(content)) {
                    dynamicConment.setText(content);
                } else {
                    dynamicConment.setVisibility(View.GONE);
                }
            } else {
                article_linear.setVisibility(View.VISIBLE);
                dynamicConment.setVisibility(View.GONE);
                article_title.setText(articleTitle);
                article_desc.setText(articleDesc);
                RequestOptions requestOption = new RequestOptions().placeholder(R.mipmap.no_photo_male).error(R.mipmap.no_photo_male);
                Glide.with(this).load(articleImage).apply(requestOption).into(article_img);
                article_linear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), HomeDetailsActivity.class);
                        intent.putExtra("id", articleID);
                        startActivity(intent);
                        overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                    }
                });
            }
            if (imageUrls != null && imageUrls.length > 0) {
                imagLinear.setVisibility(View.VISIBLE);
                if (imageUrls.length == 1) {
                    RequestOptions requestOption = new RequestOptions().placeholder(R.color.backgroud_zanwei).error(R.color.backgroud_zanwei);
                    if (StringUtil.isNotEmpty(hight) && StringUtil.isNotEmpty(width)) {
                        int widths = Integer.parseInt(width);
                        int hights = Integer.parseInt(hight);
                        if (widths < hights) {
                            dynamic_imag_h.setVisibility(View.VISIBLE);
                            dynamic_imag_w.setVisibility(View.GONE);
                            dynamic_imag_z.setVisibility(View.GONE);
                            dynamic_imag_h.setScaleType(ImageView.ScaleType.MATRIX);
                            Glide.with(DynamicDetailsActivity.this).load(imageUrls[0]).apply(requestOption).into(dynamic_imag_h);
                        } else if (widths > hights) {
                            dynamic_imag_h.setVisibility(View.GONE);
                            dynamic_imag_w.setVisibility(View.VISIBLE);
                            dynamic_imag_z.setVisibility(View.GONE);
                            dynamic_imag_h.setScaleType(ImageView.ScaleType.MATRIX);
                            Glide.with(DynamicDetailsActivity.this).load(imageUrls[0]).apply(requestOption).into(dynamic_imag_w);

                        } else if (widths == hights) {
                            dynamic_imag_h.setVisibility(View.GONE);
                            dynamic_imag_w.setVisibility(View.GONE);
                            dynamic_imag_z.setVisibility(View.VISIBLE);
                            dynamic_imag_h.setScaleType(ImageView.ScaleType.MATRIX);
                            Glide.with(DynamicDetailsActivity.this).load(imageUrls[0]).apply(requestOption).into(dynamic_imag_z);
                        }

                    }

                    imag_relative.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), PhotoBrowserActivity.class);
                            intent.putExtra("imageUrls", orinal_imageUrls);
                            intent.putExtra("curImg", orinal_imageUrls[0]);
                            startActivity(intent);
                        }
                    });
                } else if (imageUrls.length > 1) {
                    List<PhotoInfos> images = new ArrayList<>();
                    for (int i = 0; i < imageUrls.length; i++) {
                        PhotoInfos bean = new PhotoInfos();
                        bean.url = imageUrls[i];
                        images.add(bean);
                    }
                    multiImage.setList(images);
                    multiImage.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int positions) {
                            Intent intent = new Intent(getApplicationContext(), PhotoBrowserActivity.class);
                            intent.putExtra("imageUrls", orinal_imageUrls);
                            intent.putExtra("curImg", orinal_imageUrls[positions]);
                            startActivity(intent);
                        }
                    });
                }
            } else {
                imagLinear.setVisibility(View.GONE);
            }
            if (StringUtil.isNotEmpty(did_i_like) && did_i_like.equals(BizConstant.IS_FAIL)) {
                choose_like.setChecked(false);
                choose_like.setEnabled(false);
                isLike.setText("点赞");
                isLike.setTextColor(Color.parseColor("#1d1d1d"));
            } else {
                choose_like.setChecked(true);
                choose_like.setEnabled(false);
                isLike.setText("已赞");
                isLike.setTextColor(Color.parseColor("#ff2b2b"));
            }
        }else {
            ToastUtils.showToast(getApplicationContext(),mommentList.msg);
        }

    }

    /**
     * 举报
     *
     * @param attentionBean
     */
    @Override
    public void showReport(AttentionBean attentionBean) {

    }

    @Override
    protected SendFriendCirclePresenter getPresenter() {
        return new SendFriendCirclePresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_friend_circle_details;
    }

    private void initView() {
        back = (RelativeLayout) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        title.setText("动态详情");
        dynamicdetailsList = (XRecyclerView) findViewById(R.id.comment_xrecy);
        linearShare = (LinearLayout) findViewById(R.id.linear_share);
        linearComment = (LinearLayout) findViewById(R.id.linear_comment);
        linearLike = (LinearLayout) findViewById(R.id.linear_like);
        isLike = (TextView) findViewById(R.id.is_like);
        choose_like = (CheckBox) findViewById(R.id.choose_like);


    }

    /**
     * 评论列表
     *
     * @param dataBeanList
     */
    @Override
    public void showDynamicComment(List<DynamicDetailsBean.DataBean> dataBeanList) {

        if (page == 1) {
            dynamicList.clear();
        }
        dynamicList.addAll(dataBeanList);
        adapter.notifyDataSetChanged();
        adapter.setOnClickItemListener(new DynamicDetailsComentAdapter.OnClickItemListener() {
            @Override
            public void setOnItemClick(View view, int position) {
                positions = position - 1;
                Intent intent = new Intent(getApplicationContext(), DynamicCommentDetailsActivity.class);
                intent.putExtra("uid", dynamicList.get(positions).user_id);
                intent.putExtra("article_id", dynamicList.get(positions).moment_id);
                intent.putExtra("comment_id", dynamicList.get(positions).comment_id);
                intent.putExtra("nicname", dynamicList.get(positions).member.nickname);
                intent.putExtra("isuue_time", dynamicList.get(positions).add_time);
                intent.putExtra("content", dynamicList.get(positions).content);
                intent.putExtra("zan", dynamicList.get(positions).like_count);
                intent.putExtra("content_num", comment_count);
                intent.putExtra("did_i_vote", dynamicList.get(positions).did_i_vote);
                intent.putExtra("avatarss", dynamicList.get(positions).member.avatars);
                startActivityForResult(intent, 100);
                overridePendingTransition(R.anim.activity_bottom_in, R.anim.activity_top_out);
            }
        });
        /**
         * 复制  删除评论
         */
        adapter.commentsContentSetOnclick(new DynamicDetailsComentAdapter.CommentsContentInterface() {
            @Override
            public void onclick(View view, final int position, TextView textView) {
                deliteCommentPosition = position;
                mCurPopupWindow = showTipPopupWindow(textView, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.report:
                                mCurPopupWindow.dismiss();
                                if (islogins) {
                                    if (StringUtil.isNotEmpty(uid) && dynamicList.get(deliteCommentPosition).user_id.equals(uid)) {
                                        presenter.DeleteComment(getApplicationContext(), dynamicList.get(position).comment_id, author_id);
                                    } else {
                                        ToastUtils.showToast(getApplicationContext(), "举报成功");
                                    }
                                } else {
                                    ToastUtils.showToast(getApplicationContext(), "请先登录");
                                }
                                break;
                            case R.id.cory_eomment:
                                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                // 将文本内容放到系统剪贴板里。
                                cm.setText(dynamicList.get(position).content);
                                mCurPopupWindow.dismiss();
                                ToastUtils.showToast(getApplicationContext(), "复制成功");
                                break;
                        }
                    }
                });
            }
        });

        commentLoadMore();
        /**
         * 评论点赞
         */

        adapter.commentLikeSetOnclick(new DynamicDetailsComentAdapter.CommentLikeContentInterface() {
            @Override
            public void onclick(View view, int position, CheckBox checkBox, TextView textView) {
                commentLikePosition = position;
                if (islogins) {
                    if (dynamicList.get(position).did_i_vote.equals(BizConstant.IS_FAIL)) {
                        presenter.LikeMomentComment(getApplicationContext(), dynamicList.get(position).comment_id);
                    } else {
                        ToastUtils.showToast(getApplicationContext(), "已经点过赞了哦");
                    }
                } else {
                    ToastUtils.showToast(getApplicationContext(), "请先登录");
                }

            }
        });


    }

    /**
     * 动态评论上拉刷新下拉加载
     */
    public void commentLoadMore() {
        dynamicdetailsList.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshTime++;
                times = 0;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page = 1;
                        presenter.mDynamicComment(DynamicDetailsActivity.this, page, moment_id, "", "", "");
                        if (dynamicdetailsList != null)
                            dynamicdetailsList.refreshComplete();
                    }

                }, 1000);            //refresh data here
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page++;
                        presenter.mDynamicComment(DynamicDetailsActivity.this, page, moment_id, "", "", "");
                        if (dynamicdetailsList != null) {
                            dynamicdetailsList.loadMoreComplete();
                        }
                    }
                }, 1000);
                times++;
            }
        });

    }

    /**
     * 点赞列表上拉刷新下拉加载
     */
    public void likeLoadMore() {
        zanRecy.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshTime++;
                times = 0;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page = 1;
                        presenter.GetLikeList(getApplicationContext(), moment_id, page);
                        if (zanRecy != null)
                            zanRecy.refreshComplete();
                    }

                }, 1000);            //refresh data here
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page++;
                        presenter.GetLikeList(getApplicationContext(), moment_id, page);
                        if (zanRecy != null) {
                            zanRecy.loadMoreComplete();
                        }
                    }
                }, 1000);
                times++;
            }
        });

    }

    /**
     * 动态分享
     *
     * @param dynamicShareBean
     */
    @Override
    public void showDynamicShare(DynamicShareBean dynamicShareBean) {
        if (dynamicShareBean.status == 1) {
            share_url = dynamicShareBean.data.share_url;
            share_icon = dynamicShareBean.data.share_icon;
            share_description = dynamicShareBean.data.share_description;
            share_title = dynamicShareBean.data.share_title;
        }else {
            ToastUtils.showToast(getApplicationContext(),dynamicShareBean.msg);
        }
    }

    /**
     * 添加评论
     *
     * @param addBankBean
     */
    @Subscribe
    @Override
    public void showNewComment(AddBankBean addBankBean) {
        if (addBankBean.status == 1) {
            commentList.add(commentContent);
            MessageFollow messageFollow = new MessageFollow();
            if (StringUtil.isNotEmpty(isComment) && isComment.equals(BizConstant.NEW)) {
                messageFollow.newComment = commentContent;
            } else if (StringUtil.isNotEmpty(isComment) && isComment.equals(BizConstant.RECOMMEND)) {
                messageFollow.recommendComment = commentContent;
            } else if (StringUtil.isNotEmpty(isComment) && isComment.equals(BizConstant.ATTENTION)) {
                messageFollow.attentionComment = commentContent;
            } else if (StringUtil.isNotEmpty(isComment) && isComment.equals(BizConstant.MY)) {
                messageFollow.myComment = commentContent;
            }

            EventBus.getDefault().post(messageFollow);
            int comment = Integer.parseInt(comment_count) + 1;
            commentAcount.setText("评论 " + comment + "条");
            comment_count = comment + "";
            presenter.mDynamicComment(this, page, moment_id, "", "", "");
        } else {
            ToastUtils.showToast(getApplicationContext(), addBankBean.msg);
        }
    }

    /**
     * 删除动态
     *
     * @param settingBean
     */
    @Override
    public void showDeleteMoment(SettingBean settingBean) {
    }

    /**
     * 关注
     *
     * @param attentionBean
     */
    @Override
    public void showAttention(AttentionBean attentionBean) {
        ToastUtils.showToast(getApplicationContext(), attentionBean.msg);
        if (attentionBean.status == 1) {
            attention.setText(R.string.common_followed);
            attention.setBackgroundResource(R.drawable.dynamic_unguanzhu_shape);
            attention.setTextColor(Color.parseColor("#ffffff"));
            did_i_follow = BizConstant.IS_SUC;
        }


    }

    /**
     * 一键关注
     *
     * @param attentionBean
     */
    @Override
    public void showAttentions(AttentionBean attentionBean) {

    }

    //取消关注
    @Override
    public void showUnAttention(AttentionBean attentionBean) {
        ToastUtils.showToast(getApplicationContext(), attentionBean.msg);
        if (attentionBean.status == 1) {
            attention.setText(R.string.common_btn_add_focus);
            attention.setBackgroundResource(R.drawable.dynamic_guanzhu_shape);
            attention.setTextColor(Color.parseColor("#FF2B2B"));
            did_i_follow = BizConstant.IS_FAIL;
        }
    }

    /**
     * 屏蔽
     *
     * @param settingBean
     */
    @Override
    public void showBlockUser(RefotPasswordBean settingBean) {

    }

    /**
     * 动态点赞
     *
     * @param attentionBean
     */
    @Override
    public void showLikeMoment(AttentionBean attentionBean) {
        if (attentionBean.status == 1) {
            isLike.setText("已赞");
            choose_like.setEnabled(false);
            choose_like.setChecked(true);
            did_i_like = BizConstant.ALREADY_FAVORITE;
            int like = Integer.parseInt(like_count);
            likeAcount.setText("赞 " + (like + 1));
            isLike.setTextColor(Color.parseColor("#ff2b2b"));
            like_count = (like + 1) + "";
            presenter.GetLikeList(this, moment_id, page);
        } else {
            ToastUtil.showToast(getApplicationContext(), attentionBean.msg);
        }
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
            dynamicList.get(commentLikePosition).did_i_vote = BizConstant.IS_SUC;//重置状态
            int like_count = Integer.parseInt(dynamicList.get(commentLikePosition).like_count) + 1;
            dynamicList.get(commentLikePosition).like_count = like_count + "";
            adapter.notifyDataSetChanged();
        } else {
            ToastUtils.showToast(getApplicationContext(), attentionBean.msg);
        }
    }

    /**
     * 删除动态评论
     *
     * @param attentionBean
     */
    @Override
    public void showDeleteComment(AttentionBean attentionBean) {
        if (attentionBean.status == 1) {
            dynamicList.remove(deliteCommentPosition);
            adapter.notifyDataSetChanged();
            int comment = Integer.parseInt(comment_count) - 1;
            commentAcount.setText("评论 " + comment + "条");
            comment_count = comment + "";
        } else {
            ToastUtils.showToast(getApplicationContext(), attentionBean.msg);
        }
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

    /**
     * 点赞列表
     *
     * @param likeList
     */
    @Override
    public void showGetLikeList(List<LikeListBean.DataBean> likeList) {
        if (page == 1) {
            likeLists.clear();
        }
        likeLists.addAll(likeList);
        likeListAdapter.notifyDataSetChanged();
        likeLoadMore();
    }

    /**
     * 上传多张图片
     *
     * @param pictursBean
     */
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

    /**
     * 点击事件
     *
     * @param view
     */
    @OnClick({R.id.back, R.id.linear_share, R.id.linear_like, R.id.linear_comment, R.id.comment_acount_linear, R.id.like_acount_linear, R.id.appBarLayout})
    public void onViewClicked(View view) {
        if (AntiShake.check(view.getId())) {    //判断是否多次点击
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                Intent intent = new Intent();
                intent.putExtra("like_count", like_count);
                intent.putExtra("comment_count", comment_count);
                intent.putExtra("did_i_follow", did_i_follow);
                intent.putExtra("did_i_like", did_i_like);
                if (commentList.size() > 0) {
                    intent.putStringArrayListExtra("comment_list", (ArrayList<String>) commentList);
                }
                //判断type类型
                setResult(20, intent);//返回值调用函数，其中2为resultCode，返回值的标志
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.linear_share:
                presenter.mDynamicShare(getApplicationContext(), moment_id);
                mPopwindow = new RewritePopwindow(DynamicDetailsActivity.this, itemsOnClick);
                mPopwindow.showAtLocation(view,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.linear_like:
                if (sp != null && islogins) {
                    if (StringUtil.isNotEmpty(did_i_like) && did_i_like.equals(BizConstant.IS_FAIL)) {
                        presenter.LikeMoment(getApplicationContext(), moment_id);
                    }
                } else {
                    ToastUtils.showToast(getApplicationContext(), "请先登录");
                }

                break;
            case R.id.linear_comment:
                if (sp != null && islogins) {
                    dynamicCommentPopuWindow = new DynamicCommentPopuWindow(DynamicDetailsActivity.this, listener);
                    dynamicCommentPopuWindow.showAtLocation(view, Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                    View contentView = dynamicCommentPopuWindow.getContentView();
                    commentEt = contentView.findViewById(R.id.comment_et);
                    commentEt.addTextChangedListener(textWatcher);
                    tvNum = contentView.findViewById(R.id.tv_num);
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    ToastUtils.showToast(getApplicationContext(), "请先登录");
                }

                break;
            case R.id.comment_acount_linear:
                likeUnderline.setVisibility(View.GONE);
                commentUnderline.setVisibility(View.VISIBLE);
                dynamicdetailsList.setVisibility(View.VISIBLE);
                zanRecy.setVisibility(View.GONE);
                break;
            case R.id.like_acount_linear:
                commentUnderline.setVisibility(View.GONE);
                likeUnderline.setVisibility(View.VISIBLE);
                dynamicdetailsList.setVisibility(View.GONE);
                zanRecy.setVisibility(View.VISIBLE);
                break;
            case R.id.appBarLayout:
                appbarlayout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        dynamicdetailsList.dispatchTouchEvent(event);
                        zanRecy.dispatchTouchEvent(event);
                        return false;
                    }
                });
                break;
            default:
                break;
        }
    }


    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            if (Build.VERSION.SDK_INT >= 23) {
                String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
                ActivityCompat.requestPermissions(DynamicDetailsActivity.this, mPermissionList, 123);
            }
            mPopwindow.dismiss();
            mPopwindow.backgroundAlpha(DynamicDetailsActivity.this, 1f);
            switch (v.getId()) {
                case R.id.weixinghaoyou:
                    ShareUtilses.shareWebs(DynamicDetailsActivity.this, share_url, share_title
                            , share_description, share_icon, R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN, moment_id
                    );
                    break;
                case R.id.pengyouquan:
                    ShareUtilses.shareWebs(DynamicDetailsActivity.this, share_url, share_title
                            , share_description, share_icon, R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN_CIRCLE, moment_id
                    );
                    break;
                case R.id.qqhaoyou:
                    ShareUtilses.shareWebs(DynamicDetailsActivity.this, share_url, share_title
                            , share_description, share_icon, R.mipmap.ic_launcher, SHARE_MEDIA.QQ, moment_id
                    );
                    break;
                case R.id.qqkongjian:
                    ShareUtilses.shareWebs(DynamicDetailsActivity.this, share_url, share_title
                            , share_description, share_icon, R.mipmap.ic_launcher, SHARE_MEDIA.QZONE, moment_id
                    );
                    break;
                case R.id.copyurl:
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将文本内容放到系统剪贴板里。
                    cm.setText(share_url + "#" + share_title);
                    ToastUtils.showToast(getApplicationContext(), "复制成功");
                    break;
                default:
                    break;
            }
        }

    };

    /**
     * popwindow弹窗
     */
    private View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (Build.VERSION.SDK_INT >= 23) {
                String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
                ActivityCompat.requestPermissions(DynamicDetailsActivity.this, mPermissionList, 123);
            }
            dynamicCommentPopuWindow.dismiss();
            dynamicCommentPopuWindow.backgroundAlpha(DynamicDetailsActivity.this, 1f);
            switch (v.getId()) {
                case R.id.cancel://取消
                    dynamicCommentPopuWindow.dismiss();
                    dynamicCommentPopuWindow.backgroundAlpha(DynamicDetailsActivity.this, 1f);
                    break;
                case R.id.send://发表

                    if (islogins) {
                        commentContent = commentEt.getText().toString().trim();
                        if (StringUtil.isNotEmpty(commentContent)) {
                            presenter.NewComment(DynamicDetailsActivity.this, commentContent, "", "", moment_id);
                            dynamicCommentPopuWindow.dismiss();
                            dynamicCommentPopuWindow.backgroundAlpha(DynamicDetailsActivity.this, 1f);
                        } else {
                            ToastUtils.showToast(getApplicationContext(), "评论内容不能为空！");
                        }
                    } else {
                        ToastUtils.showToast(getApplicationContext(), "请先登录");
                    }


                    break;
            }
        }
    };

    public PopupWindow showTipPopupWindow(final View anchorView, final View.OnClickListener onClickListener) {
        final View contentView = LayoutInflater.from(anchorView.getContext()).inflate(R.layout.pop_top_window, null);
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 创建PopupWindow时候指定高宽时showAsDropDown能够自适应
        // 如果设置为wrap_content,showAsDropDown会认为下面空间一直很充足（我以认为这个Google的bug）
        // 备注如果PopupWindow里面有ListView,ScrollView时，一定要动态设置PopupWindow的大小
        final PopupWindow popupWindow = new PopupWindow(contentView,
                contentView.getMeasuredWidth(), contentView.getMeasuredHeight(), false);
        contentView.findViewById(R.id.cory_eomment).setOnClickListener(onClickListener);
        TextView report = contentView.findViewById(R.id.report);
        if (StringUtil.isNotEmpty(uid) && dynamicList.get(deliteCommentPosition).user_id.equals(uid)) {
            report.setText("删除");
        } else {
            report.setText("举报");
        }

        report.setOnClickListener(onClickListener);
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

    @Override
    protected void onResume() {
        super.onResume();
        presenter.mDynamicComment(this, page, moment_id, "", "", "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
//                intent.putExtra("zan_num", zans);
//                intent.putExtra("did_i_vote", did_i_votes);
//                intent.putExtra("content_num", content_num);
                String zan_nums = data.getStringExtra("zan_num");
                String content_num = data.getStringExtra("content_num");
                String did_i_votes = data.getStringExtra("did_i_vote");
                if (StringUtil.isNotEmpty(zan_nums)) {
                    dynamicList.get(positions).like_count = zan_nums;
                }
                if (StringUtil.isNotEmpty(content_num)) {
                    comment_count = content_num + "";
                    commentAcount.setText("评论 " + content_num + "条");
                }
                if (StringUtil.isNotEmpty(did_i_votes)) {
                    dynamicList.get(positions).did_i_vote = did_i_votes;
                }
                adapter.notifyDataSetChanged();
            }
        }
    }


    /**
     * 监听输入框的输入状态
     */
    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            String pointsEditT = commentEt.getText().toString();

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            String pointsEditT = commentEt.getText().toString();
            if (StringUtil.isNotEmpty(pointsEditT)) {

                tvNum.setText(pointsEditT.length() + "/200");
            } else {
                tvNum.setText("0/200");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            String pointsEditT = commentEt.getText().toString();
            if (StringUtil.isNotEmpty(pointsEditT)) {

                tvNum.setText(pointsEditT.length() + "/200");
            } else {
                tvNum.setText("0/200");
            }
        }

    };


    //系统返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent();
            intent.putExtra("like_count", like_count);
            intent.putExtra("comment_count", comment_count);
            intent.putExtra("did_i_follow", did_i_follow);
            intent.putExtra("did_i_like", did_i_like);
            setResult(20, intent);//返回值调用函数，其中2为resultCode，返回值的标志
            finish();
            overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

}
