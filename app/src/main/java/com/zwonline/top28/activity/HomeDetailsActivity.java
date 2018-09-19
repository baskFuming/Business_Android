package com.zwonline.top28.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.widget.SpringView;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zwonline.top28.R;
import com.zwonline.top28.adapter.ArticleCommentAdapter;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.AddCommentBean;
import com.zwonline.top28.bean.ArticleCommentBean;
import com.zwonline.top28.bean.HomeDetailsBean;
import com.zwonline.top28.bean.PersonageInfoBean;
import com.zwonline.top28.bean.ShareDataBean;
import com.zwonline.top28.bean.message.MessageFollow;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.HomeDetailsPresenter;
import com.zwonline.top28.presenter.RecordUserBehavior;
import com.zwonline.top28.utils.ImageViewPlus;
import com.zwonline.top28.utils.LanguageUitils;
import com.zwonline.top28.utils.MyListView;
import com.zwonline.top28.utils.ObservableScrollView;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.IHomeDetails;
import com.zwonline.top28.web.MJavascriptInterface;
import com.zwonline.top28.wxapi.RewritePopwindow;
import com.zwonline.top28.wxapi.ShareUtilses;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.droidsonroids.gif.GifImageView;


/**
 * 首页详情页
 */
@SuppressLint("SetJavaScriptEnabled")
public class HomeDetailsActivity extends BaseActivity<IHomeDetails, HomeDetailsPresenter> implements
        IHomeDetails, View.OnLayoutChangeListener {

    private String sID;
    private int id;
    private HomeDetailsBean bean;
    //Activity最外层的Layout视图
    private View activityRootView;
    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;
    private String ititle;
    private MyListView commentListview;
    private RewritePopwindow mPopwindow;
    private String uid;
    private SharedPreferencesUtils sp;
    private MessageFollow messageFollow;
    private int currentNum;
    private boolean islogins;
    public String article_img;
    public String article_desc;
    public String article_url;
    private String token;
    private RelativeLayout back;
    private RelativeLayout linearhead;
    private RelativeLayout rootLayout;
    private RelativeLayout relativeHome;
    private TextView title;
    private TextView mtitle;
    private TextView username;
    private TextView companyName;
    private TextView time;
    private ImageViewPlus userhead;
    private LinearLayout linear;
    private LinearLayout linearImage;
    private LinearLayout noComment;
    private Button attention;
    private Button send;
    private WebView contentWeb;
    private EditText editText;
    private ImageView comment;
    private ImageView shared;
    //    private ImageView collect;
    private ImageView imageViewShare;
    private GifImageView detailsGif;
    private String ititle1;
    private CheckBox collect;
    private ObservableScrollView scrollView;
    private Context context;
    private String pid;
    private int recLen = 20;
    Timer timer = new Timer();
    private boolean scroll_bottom = false;
    private int imageHeight = 200; //设置渐变高度，一般为导航图片高度，自己控制
    private List<ArticleCommentBean.DataBean> list;
    private View headerView;
    private ArticleCommentAdapter adapter;
    private int page = 1;
    private SpringView commentSpring;
    private int reply;
    private int count = 0;
    private String userUid;
    private View footView;
    private String[] imageUrls = StringUtil.returnImageUrlsFromHtml();
    private int status;
    private TextView badgeviewTv;

    public void initActivityWindow() {
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.black), 0);
        ButterKnife.bind(this);
        activityRootView = findViewById(R.id.root_layout);
        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right,
                               int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        //old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值
        //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
            send.setVisibility(View.VISIBLE);
            linearImage.setVisibility(View.GONE);
//            Toast.makeText(HomeDetailsActivity.this, "监听到软键盘弹起...", Toast.LENGTH_SHORT).show();
        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
            //隐藏软键盘
            send.setVisibility(View.GONE);
            linearImage.setVisibility(View.VISIBLE);
            pid = "";
//            Toast.makeText(HomeDetailsActivity.this, "监听到软件盘关闭...", Toast.LENGTH_SHORT).show();
        }


    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //添加layout大小发生改变监听器
        activityRootView.addOnLayoutChangeListener(this);
        if (StringUtil.isNotEmpty(sID)) {
            presenter.mArticleComment(this, sID, "", "", "", page);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void init() {
        try {
            list = new ArrayList<>();
            context = HomeDetailsActivity.this;
            initActivityWindow();
            initData();//查找控件
            sp = SharedPreferencesUtils.getUtil();
            islogins = (boolean) sp.getKey(this, "islogin", false);
            token = (String) sp.getKey(HomeDetailsActivity.this, "dialog", "");
            userUid = (String) sp.getKey(this, "uid", "");
            messageFollow = new MessageFollow();

            Intent intent = getIntent();
            sID = intent.getStringExtra("id");
            id = Integer.parseInt(sID);

            commentSpring.setType(SpringView.Type.FOLLOW);
            commentSpring.setFooter(new DefaultFooter(this));
            if (islogins) {
                presenter.mHomeDetail(this, id);
            } else {
                presenter.mHomeDetails(this, id);
            }
            presenter.mShareData(this, sID);//分享文章
//            scrollView.setOnTouchListener(new TouchListenerImpl());
            presenter.mArticleComment(this, String.valueOf(id), "", "", "", page);
//            presenter.mArticleComment(this,id,"","","",page);
            adapter = new ArticleCommentAdapter(list, this);
            headerView = getLayoutInflater().inflate(R.layout.home_details_header, null);
            footView = getLayoutInflater().inflate(R.layout.foot_view, null);
            initListView();
            commentListview.addHeaderView(headerView, null, false);
            commentListview.addFooterView(footView);
            commentListview.setAdapter(adapter);
            webSettingInit();
            StringUtil.textBold(title);
            StringUtil.textBold(mtitle);//中文字体加粗
//            adapter.notifyDataSetChanged();
//            ScrollVieListener();

        } catch (Exception e) {
            e.printStackTrace();
        }
        //timer.schedule(task, 1000, 1000);       // timeTask
        //软键盘修改发送按钮点击
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String editTextContent = editText.getText().toString().trim();
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    RecordUserBehavior.recordUserBehavior(context, BizConstant.SUBMIT_COMMENT);
                    if (islogins) {
                        try {
                            if (StringUtil.isNotEmpty(editTextContent)) {
                                presenter.mReplyComment(HomeDetailsActivity.this, String.valueOf(id), editTextContent, "", "");
                                editText.setText("");
                                contentWeb.setWebViewClient(new WebViewClient() {
                                    @Override
                                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                        view.loadUrl(url);
                                        return false;
                                    }
                                });

                            } else {
                                ToastUtils.showToast(HomeDetailsActivity.this, "输入内容不能为空！");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        ToastUtils.showToast(HomeDetailsActivity.this, "请先登录！");
                    }


                }
                return false;
            }
        });
    }


    private void initListView() {
        mtitle = (TextView) headerView.findViewById(R.id.mtitle);
        username = (TextView) headerView.findViewById(R.id.username);
        companyName = (TextView) headerView.findViewById(R.id.company_name);
        time = (TextView) headerView.findViewById(R.id.time);
        userhead = (ImageViewPlus) headerView.findViewById(R.id.userhead);
        linear = (LinearLayout) headerView.findViewById(R.id.linear);
        noComment = (LinearLayout) footView.findViewById(R.id.no_comment);
        attention = (Button) headerView.findViewById(R.id.attention);
        contentWeb = (WebView) headerView.findViewById(R.id.content_web);
        relativeHome = (RelativeLayout) headerView.findViewById(R.id.relative_home);

    }

    //查找控件
    private void initData() {
        back = (RelativeLayout) findViewById(R.id.back);
        linearhead = (RelativeLayout) findViewById(R.id.linearhead);
//        scrollView = (ObservableScrollView) findViewById(R.id.Observable_ScrollView);
//        rootLayout = (RelativeLayout) findViewById(R.id.root_layout);
        linearImage = (LinearLayout) findViewById(R.id.linear_image);
        title = (TextView) findViewById(R.id.title);
        send = (Button) findViewById(R.id.send);
//        commentWeb = (WebView) findViewById(R.id.comment_web);
        editText = (EditText) findViewById(R.id.editText);
        editText.addTextChangedListener(textWatcher);
        comment = (ImageView) findViewById(R.id.comment);
        shared = (ImageView) findViewById(R.id.shared);
//        collect = (ImageView) findViewById(R.id.collect);
        collect = (CheckBox) findViewById(R.id.collect);
        collect.setOnCheckedChangeListener(collectOnCheckedClickListener);//GifImageView
        imageViewShare = (ImageView) findViewById(R.id.imageViewShare);
        detailsGif = (GifImageView) findViewById(R.id.details_gif);
        commentListview = (MyListView) findViewById(R.id.comment_listview);
        badgeviewTv = (TextView) findViewById(R.id.badgeview_tv);
        commentSpring = (SpringView) findViewById(R.id.comment_spring);
//        badgeView = new BadgeView(this);
//        badgeView.setTargetView(comment);
//        badgeView.setBadgeMargin(0, 0, 0, 0);
//        badgeView.setTextSize(8);
    }

    /**
     * 初始化收藏
     */
    @Override
    public void initFavorite() {
        if (bean.data == null) {
            return;
        }
        String isFavorite = bean.data.is_favorite;
        if (!StringUtil.isEmpty(isFavorite) && BizConstant.ALREADY_FAVORITE.equals(isFavorite)) {
            collect.setChecked(true);

        } else {
            collect.setChecked(false);
        }
    }

    /**
     * 评论
     *
     * @param articleCommentList
     */
    @Override
    public void showArticleComment(final List<ArticleCommentBean.DataBean> articleCommentList) {
//        list.clear();\if
        if (page == 1) {
            list.clear();
        }
        list.addAll(articleCommentList);
        adapter.notifyDataSetChanged();
        commentListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (reply > 0) {
                    int positions = position - 1;
                    Intent intent = new Intent(HomeDetailsActivity.this, CommentDetailsActivity.class);
                    intent.putExtra("uid", list.get(positions).uid);
                    intent.putExtra("article_id", sID);
                    intent.putExtra("comment_id", list.get(positions).comment_id);
                    intent.putExtra("nicname", list.get(positions).member.nickname);
                    intent.putExtra("isuue_time", list.get(positions).ctime);
                    intent.putExtra("content", list.get(positions).content);
                    intent.putExtra("zan", list.get(positions).zan);
                    intent.putExtra("avatarss", list.get(positions).member.avatars);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_bottom_in, R.anim.activity_top_out);
                }

            }
        });

        commentSpring.setListener(new SpringView.OnFreshListener() {
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
                        page++;
                        presenter.mArticleComment(HomeDetailsActivity.this, String.valueOf(id), "", "", "", page);
                        commentSpring.onFinishFreshAndLoad();
//                        adapter.notifyDataSetChanged();
                    }
                }, 1000);
            }
        });
        adapter.notifyDataSetChanged();
    }

    /**
     * 评论失败
     *
     * @param articleCommentBean
     */
    @Override
    public void onError(AddCommentBean articleCommentBean) {
        status = articleCommentBean.status;
        if (status == 1) {
            reply++;
//                            comment.setNotificationNumber(reply);
//            badgeView.setBadgeCount(reply);
            badgeviewTv.setVisibility(View.VISIBLE);
            badgeviewTv.setText(reply + "");
            //点击发送让软键盘隐藏
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(HomeDetailsActivity.this.getCurrentFocus().
                            getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } else {
            ToastUtils.showToast(getApplicationContext(), articleCommentBean.msg);
        }
    }

    //收藏 取消 选中关注文章
    CompoundButton.OnCheckedChangeListener collectOnCheckedClickListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            RecordUserBehavior.recordUserBehavior(context, BizConstant.SHARED_ARTICLE);
            if (!islogins) {
                startActivity(new Intent(HomeDetailsActivity.this, WithoutCodeLoginActivity.class));
                Toast.makeText(HomeDetailsActivity.this, R.string.user_not_login, Toast.LENGTH_SHORT).show();
                return;
            }
            if (isChecked) {
                presenter.mCollect(HomeDetailsActivity.this, String.valueOf(id), BizConstant.ALREADY_FAVORITE);
            } else {
                presenter.mCollect(HomeDetailsActivity.this, String.valueOf(id), BizConstant.NO_FAVORITE);
            }
        }
    };

    @Override
    protected HomeDetailsPresenter getPresenter() {
        return new HomeDetailsPresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_home_details;
    }

    //展示数据
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void showHomeDetails(final HomeDetailsBean homeDetails) {

        if (bean == null) {
            bean = new HomeDetailsBean();
            this.bean = homeDetails;
        }

        if (Integer.parseInt(homeDetails.data.vo.reply) == 0) {
            noComment.setVisibility(View.VISIBLE);
//            footView.setVisibility(View.VISIBLE);
        } else if (Integer.parseInt(homeDetails.data.vo.reply) > 0) {
            noComment.setVisibility(View.GONE);
//            footView.setVisibility(View.GONE);
        }
        reply = Integer.parseInt(homeDetails.data.vo.reply);
        WindowManager windowManager = getWindowManager();
//        badgeView.setBadgeCount(reply);
        if (reply > 0) {
            badgeviewTv.setVisibility(View.VISIBLE);
            badgeviewTv.setText(reply + "");
        } else {
            badgeviewTv.setVisibility(View.GONE);
        }
        String showtime = homeDetails.data.vo.showtime;
        time.setText(getDateToString(Long.parseLong(showtime) * 1000, "yyyy-MM-dd"));
        uid = homeDetails.data.members.uid;
        String is_attention = homeDetails.data.is_attention;
//        System.out.print("is_attention==" + is_attention);
        username.setText(homeDetails.data.members_info.nickname);
        mtitle.setText(homeDetails.data.vo.title);

        title.setText(homeDetails.data.vo.title);
        ititle1 = homeDetails.data.vo.title;
        if (homeDetails.data.members_info.sex.equals(BizConstant.SEX)) {
            RequestOptions options = new RequestOptions().placeholder(R.mipmap.no_photo_male).error(R.mipmap.no_photo_male);
            Glide.with(this).load(homeDetails.data.members.avatars).apply(options).into(userhead);
        } else {
            RequestOptions options = new RequestOptions().placeholder(R.mipmap.no_photo_female).error(R.mipmap.no_photo_female);
            Glide.with(this).load(homeDetails.data.members.avatars).apply(options).into(userhead);
        }
        userhead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AntiShake.check(v.getId())) {    //判断是否多次点击
                    return;
                }
                Intent intent = new Intent(HomeDetailsActivity.this, HomePageActivity.class);
                intent.putExtra("uid", homeDetails.data.members.uid);
                intent.putExtra("sex", homeDetails.data.members_info.sex);
                intent.putExtra("is_attention", homeDetails.data.is_attention);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
//                }
            }
        });
        if (homeDetails.data.is_attention.equals("0")) {
            attention.setText(R.string.common_btn_add_focus);
            attention.setBackgroundResource(R.drawable.btn_ganzhu_red);
            attention.setTextColor(Color.parseColor("#FDFDFD"));
        } else if (homeDetails.data.is_attention.equals("1")) {
            attention.setText(R.string.common_followed);
            attention.setBackgroundResource(R.drawable.btn_noguanzhu_gray);
            attention.setTextColor(Color.parseColor("#FDFDFD"));
        }

        setComPany(homeDetails);
//        settingWebview();
//        webSettingInit();
    }

    /**
     * 设置企业信息
     *
     * @param homeDetails
     */
    private void setComPany(final HomeDetailsBean homeDetails) {
        try {
            if (homeDetails.data.company.equals("") && homeDetails.data.company == null) {
                companyName.setVisibility(View.GONE);
            } else {
                companyName.setVisibility(View.VISIBLE);
                companyName.setText("[" + homeDetails.data.company.enterprise_name + "]");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        companyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AntiShake.check(v.getId())) {    //判断是否多次点击
                    return;
                }
                if (TextUtils.isEmpty(token)) {
                    startActivity(new Intent(HomeDetailsActivity.this, LoginActivity.class));
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                } else {
                    Intent intent = new Intent(HomeDetailsActivity.this, CompanyActivity.class);
                    intent.putExtra("uid", homeDetails.data.company.uid);
                    intent.putExtra("sex", homeDetails.data.members_info.sex);
                    intent.putExtra("pid", homeDetails.data.company.id);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                }
            }
        });
    }


    @Override
    public void onErro() {
        Toast.makeText(this, R.string.data_loading, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showCompany(PersonageInfoBean companyBean) {

    }

    //WebVIew配置
    public void settingWebview() {
//        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        contentWeb.setWebViewClient(new WebViewClient());
        WebSettings setting = contentWeb.getSettings();
        setting.setUseWideViewPort(true);
        setting.setLoadWithOverviewMode(true);
        //让缩放显示的最小值为起始
        setting.setJavaScriptEnabled(true);//支持JS
        setting.setBlockNetworkImage(false);//解决图片不显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        setting.setJavaScriptCanOpenWindowsAutomatically(true);


        contentWeb.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                super.onReceivedSslError(view, handler, error);
                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                contentWeb.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        contentWeb.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }


        });

//
    }


    //    //点击事件
    @OnClick({R.id.back, R.id.imageViewShare, R.id.send, R.id.collect, R.id.comment, R.id.shared})
    public void onViewClicked(View view) {
        if (AntiShake.check(view.getId())) {    //判断是否多次点击
            return;
        }
        switch (view.getId()) {

            case R.id.back:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.shared:
                RecordUserBehavior.recordUserBehavior(context, BizConstant.CLICK_SHARE_ICON);
                mPopwindow = new RewritePopwindow(HomeDetailsActivity.this, itemsOnClick);
                mPopwindow.showAtLocation(view,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.comment:
                count++;
                if (count % 2 == 1) {
//                    adapter.notifyDataSetChanged();
                    commentListview.setSelection(1);
                } else if (count % 2 == 0) {
                    commentListview.setSelection(0);
                }
                break;
            case R.id.imageViewShare:
                mPopwindow = new RewritePopwindow(HomeDetailsActivity.this, itemsOnClick);
                mPopwindow.showAtLocation(view,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.send:
                RecordUserBehavior.recordUserBehavior(context, BizConstant.SUBMIT_COMMENT);
                if (islogins) {
                    try {
                        String editTextContent = editText.getText().toString().trim();
                        if (StringUtil.isNotEmpty(editTextContent)) {

                            presenter.mReplyComment(HomeDetailsActivity.this, String.valueOf(id), editTextContent, "", "");
                            editText.setText("");
                            contentWeb.setWebViewClient(new WebViewClient() {
                                @Override
                                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                    view.loadUrl(url);
                                    return false;
                                }
                            });
//                            reply++;
////                            comment.setNotificationNumber(reply);
//                            badgeView.setBadgeCount(reply);
//                            //点击发送让软键盘隐藏
//                            InputMethodManager imm = (InputMethodManager)
//                                    getSystemService(Context.INPUT_METHOD_SERVICE);
//                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        } else {
                            ToastUtils.showToast(this, "输入内容不能为空！");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ToastUtils.showToast(this, "请先登录！");
                }

                break;
//

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    //分享文章
    @Override
    public void showShareData(ShareDataBean.DataBean shareList) {
        article_img = shareList.article_img;
        article_desc = shareList.article_desc;
        article_url = shareList.article_url;
    }


    public class Defaultcontent {
        public String url = article_url;
        public String text = article_desc;
        public String title = ititle1;
        public String imageurl = article_img;
//        public static String videourl = article_url;
    }

    public static String getDateToString(long milSecond, String pattern) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

    }

    @Override
    public void commentSuccess() {
        pid = "";
        presenter.mArticleComment(this, String.valueOf(id), "", "", "", page);
        noComment.setVisibility(View.GONE);
//        footView.setVisibility(View.GONE);
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            if (Build.VERSION.SDK_INT >= 23) {
                String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
                ActivityCompat.requestPermissions(HomeDetailsActivity.this, mPermissionList, 123);
            }
            mPopwindow.dismiss();
            mPopwindow.backgroundAlpha(HomeDetailsActivity.this, 1f);
            switch (v.getId()) {
                case R.id.weixinghaoyou:
                    ShareUtilses.shareWeb(HomeDetailsActivity.this, article_url, ititle1
                            , article_desc, article_img, R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN, sID
                    );
                    break;
                case R.id.pengyouquan:
                    ShareUtilses.shareWeb(HomeDetailsActivity.this, article_url, ititle1
                            , article_desc, article_img, R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN_CIRCLE, sID
                    );
                    break;
                case R.id.qqhaoyou:
                    ShareUtilses.shareWeb(HomeDetailsActivity.this, article_url, ititle1
                            , article_desc, article_img, R.mipmap.ic_launcher, SHARE_MEDIA.QQ, sID
                    );
                    break;
                case R.id.qqkongjian:
                    ShareUtilses.shareWeb(HomeDetailsActivity.this, article_url, ititle1
                            , article_desc, article_img, R.mipmap.ic_launcher, SHARE_MEDIA.QZONE, sID
                    );
                    break;
                case R.id.copyurl:
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将文本内容放到系统剪贴板里。
                    cm.setText(ititle1 + article_url);
                    ToastUtils.showToast(HomeDetailsActivity.this, "复制成功");
                    break;
                default:
                    break;
            }
        }

    };

    //Dialog弹窗
//    private void showNormalDialogs() {
//        /* @setIcon 设置对话框图标
//         * @setTitle 设置对话框标题
//         * @setMessage 设置对话框消息提示
//         * setXXX方法返回Dialog对象，因此可以链式设置属性
//         */
//        final AlertDialog.Builder normalDialog =
//                new AlertDialog.Builder(HomeDetailsActivity.this);
//        normalDialog.setMessage(getString(R.string.is_willing_answer_calls));
//        normalDialog.setPositiveButton(getString(R.string.willing),
//                new DialogInterface.OnClickListener() {
//                    @SuppressLint("MissingPermission")
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        presenter.mAttention(HomeDetailsActivity.this, "follow", String.valueOf(uid), BizConstant.ALREADY_FAVORITE);
//
//                    }
//                });
//        normalDialog.setNegativeButton(getString(R.string.unwillingness),
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        presenter.mAttention(HomeDetailsActivity.this, "follow", String.valueOf(uid), BizConstant.NO_FAVORITE);
//                    }
//                });
//        // 显示
//        normalDialog.show();
//    }


    /**
     * 滑动监听隐藏title
     */
    public void ScrollVieListener() {
        commentListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState > 0 && scrollState <= imageHeight) {
                    float scale = (float) scrollState / imageHeight;
                    float alpha = (255 * scale);
//                    title.setVisibility(View.GONE);
                } else {
//                    title.setVisibility(View.VISIBLE);
                }
//                title.setVisibility(View.GONE);
            }


            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
//                    Log.d("ListView", "##### 滚动到顶部 #####");
//                    title.setVisibility(View.GONE);
                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
//                    Log.d("ListView", "##### 滚动到底部 ######");
//                    title.setVisibility(View.VISIBLE);
                }
            }
        });
        //搜索框在布局最上面
//        linearhead.bringToFront();
//        //滑动监听
//        commentListview.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
//            @Override
//            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
//                if (y <= 0) {
////                        line.setBackgroundColor(Color.argb((int) 0, 111, 29, 26));//AGB由相关工具获得，或者美工提供
//                } else if (y > 0 && y <= imageHeight) {
//                    float scale = (float) y / imageHeight;
//                    float alpha = (255 * scale);
//                    title.setVisibility(View.GONE);
//                } else {
//                    title.setVisibility(View.VISIBLE);
//                }
//            }
//        });

    }

    /**
     * 定时器20秒
     */
//    TimerTask task = new TimerTask() {
//        @Override
//        public void run() {
//
//            runOnUiThread(new Runnable() {      // UI thread
//                @Override
//                public void run() {
//                    recLen--;
//                    if (recLen <= 0 && scroll_bottom) {
//                        timer.cancel();
////                        ToastUtils.showToast(HomeDetailsActivity.this, "20");
//                    }
//                }
//            });
//        }
//    };
    @Override
    protected void onDestroy() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(getApplicationContext()).clearDiskCache();//清理磁盘缓存需要在子线程中执行
            }
        }).start();
        Glide.get(this).clearMemory();//清理内存缓存可以在UI主线程中进行
        super.onDestroy();
//        if (time != null) {
//            timer.cancel();
//        }
        contentWeb.stopLoading();
        contentWeb.removeAllViews();
        contentWeb.destroy();
        contentWeb = null;
    }


    //webview配置
    @SuppressLint("JavascriptInterface")
    private void webSettingInit() {
        WebSettings settings = contentWeb.getSettings();
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        settings.setUserAgentString("app28/");
        settings.setSupportZoom(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setBlockNetworkImage(false);//解决图片不显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //设置缓存
        settings.setDomStorageEnabled(true);//设置适应Html5的一些方法
        //请求头
        Map<String, String> headMap = new HashMap<>();
        headMap.put("Accept-Language", LanguageUitils.getCurCountryLan());
        contentWeb.loadUrl("https://toutiao.28.com/Index/article_app/id/" + id + ".html");
        contentWeb.addJavascriptInterface(new MJavascriptInterface(this), "imagelistener");
        contentWeb.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }


            @Override
            public void onPageFinished(final WebView view, String url) {
                super.onPageFinished(view, url);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        //execute the task
                        addImageClickListener(view);//待网页加载完全后设置图片点击的监听方法
                    }
                }, 500);


            }
        });

//        contentWeb.setWebViewClient(new MyWebViewClient());
        contentWeb.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);


            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    detailsGif.setVisibility(View.GONE);
                }
            }

            //设置响应js 的Confirm()函数
            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(HomeDetailsActivity.this);
                b.setTitle("Confirm");
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.cancel();
                    }
                });
                b.create().show();
                return true;
            }

        });


        attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (islogins) {
                    RecordUserBehavior.recordUserBehavior(HomeDetailsActivity.this, BizConstant.CLICK_FOLLOW);
                    if (!uid.equals(userUid)) {
                        if (attention.getText().toString().trim().equals(getString(R.string.common_btn_add_focus))) {
                            presenter.mAttention(HomeDetailsActivity.this, "follow", String.valueOf(uid), BizConstant.ALREADY_FAVORITE);
                            attention.setText(R.string.common_followed);
                            currentNum = Integer.parseInt((String) sp.getKey(HomeDetailsActivity.this, "follow", "0")) + 1;
                            messageFollow.followNum = currentNum + "";
                            sp.insertKey(HomeDetailsActivity.this, "follow", messageFollow.followNum);
                            EventBus.getDefault().post(messageFollow);
                            attention.setBackgroundResource(R.drawable.btn_noguanzhu_gray);
                            attention.setTextColor(Color.parseColor("#FDFDFD"));
                        } else {
                            presenter.mUnAttention(HomeDetailsActivity.this, "un_follow", String.valueOf(uid));
                            attention.setText(R.string.common_btn_add_focus);
                            currentNum = Integer.parseInt((String) sp.getKey(HomeDetailsActivity.this, "follow", "0")) - 1;
                            messageFollow.followNum = currentNum + "";
                            sp.insertKey(HomeDetailsActivity.this, "follow", messageFollow.followNum);
                            EventBus.getDefault().post(messageFollow);
                            attention.setBackgroundResource(R.drawable.btn_ganzhu_red);
                            attention.setTextColor(Color.parseColor("#FDFDFD"));
                        }
                    } else {
                        ToastUtils.showToast(HomeDetailsActivity.this, "自己不能关注自己！");
                    }

                } else {
                    Toast.makeText(HomeDetailsActivity.this, R.string.user_not_login, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomeDetailsActivity.this, WithoutCodeLoginActivity.class);
//                    intent.putExtra()
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                }

            }
        });
    }

    /**
     * 设置图片点击事件
     *
     * @param webView
     */
    private void addImageClickListener(WebView webView) {
        //"cc_detail_blog_img"这个ClassName和前端对应的，前端那边不能修改
        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                " var array=new Array(); " + " for(var j=0;j<objs.length;j++){ " + "array[j]=objs[j].src;" + " }  " +
                "for(var i=0;i<objs.length;i++)  " +
                "{" +
                "  objs[i].onclick=function()  " +
                "  {  " +
                "    window.imagelistener.openImage(this.src,array);  " +
                "  }  " +
                "}" +
                "})()");
    }


    /**
     * 监听输入框的输入状态
     */
    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            String pointsEditT = editText.getText().toString();
            if (pointsEditT.length() > 0) {
                send.setTextColor(Color.parseColor("#007AFF"));
            } else {
                send.setTextColor(Color.parseColor("#AAAAAA"));
            }

        }

        @Override
        public void afterTextChanged(Editable s) {
            String pointsEditT = editText.getText().toString();
            if (pointsEditT.length() > 0) {
                send.setTextColor(Color.parseColor("#007AFF"));
            } else {
                send.setTextColor(Color.parseColor("#AAAAAA"));
            }

        }

    };

}
