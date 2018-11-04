package com.zwonline.top28.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.ClipboardManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zwonline.top28.R;
import com.zwonline.top28.activity.DynamicDetailsActivity;
import com.zwonline.top28.activity.HomeDetailsActivity;
import com.zwonline.top28.activity.InformationNoticeActivity;
import com.zwonline.top28.activity.WithoutCodeLoginActivity;
import com.zwonline.top28.adapter.MyDynamicAdapter;
import com.zwonline.top28.base.BasesFragment;
import com.zwonline.top28.bean.AddBankBean;
import com.zwonline.top28.bean.AtentionDynamicHeadBean;
import com.zwonline.top28.bean.AttentionBean;
import com.zwonline.top28.bean.BusinessCircleBean;
import com.zwonline.top28.bean.BusinessCoinBean;
import com.zwonline.top28.bean.DynamicDetailsBean;
import com.zwonline.top28.bean.DynamicDetailsesBean;
import com.zwonline.top28.bean.DynamicShareBean;
import com.zwonline.top28.bean.GiftBean;
import com.zwonline.top28.bean.GiftSumBean;
import com.zwonline.top28.bean.LikeListBean;
import com.zwonline.top28.bean.NewContentBean;
import com.zwonline.top28.bean.PictursBean;
import com.zwonline.top28.bean.RefotPasswordBean;
import com.zwonline.top28.bean.RewardListBean;
import com.zwonline.top28.bean.SendNewMomentBean;
import com.zwonline.top28.bean.SettingBean;
import com.zwonline.top28.bean.ShieldUserBean;
import com.zwonline.top28.bean.message.MessageFollow;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.SendFriendCirclePresenter;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.utils.popwindow.DynamicFunctionPopwindow;
import com.zwonline.top28.view.ISendFriendCircleActivity;
import com.zwonline.top28.wxapi.RewritePopwindow;
import com.zwonline.top28.wxapi.ShareUtilses;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 我的动态
 */
public class MyDynamicFragment extends BasesFragment<ISendFriendCircleActivity, SendFriendCirclePresenter> implements ISendFriendCircleActivity {

    private XRecyclerView newcontentRecy;
    private int page = 1;
    private MyDynamicAdapter adapter;
    private RewritePopwindow mPopwindow;
    private String share_url;
    private String share_icon;
    private String share_description;
    private String share_title;
    private String moment_id;
    private List<NewContentBean.DataBean> newContentList;
    private int positions;
    private int attentionPosition;
    private SharedPreferencesUtils sp;
    private boolean islogins;
    private String uid;
    private DynamicFunctionPopwindow dynamicFunctionPopwindow;
    private int functionPosition;
    private int refreshTime = 0;
    private int times = 0;
    private int likePosition;
    private String userId;
    private int intentPositions;
    private RelativeLayout noDynamic;
    private TextView daVRecommend;
    private RelativeLayout infoLayout;
    private TextView infoNum;
    private String newContnets;
    private String nickname;
    private String myComment;

    //置顶功能
    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    /**
     * 我的动态
     *
     * @param view
     */
    @Override
    protected void init(View view) {
//        StatusBarUtil.setColor(getActivity(), getResources().getColor(R.color.white), 0);
//        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        sp = SharedPreferencesUtils.getUtil();
        EventBus.getDefault().register(this);
        islogins = (boolean) sp.getKey(getActivity(), "islogin", false);
        userId = (String) sp.getKey(getActivity(), "uid", "");
        nickname = (String) sp.getKey(getActivity(), "nickname", "");
        newContentList = new ArrayList<>();
        initView(view);
        if (sp != null && islogins) {
            presenter.MomentLists(getActivity(), page, userId, "", "");
            presenter.GetMyNotificationCount(getActivity());
        } else {
            Intent intent = new Intent(getActivity(), WithoutCodeLoginActivity.class);
            intent.putExtra("login_type", BizConstant.TYPE_TWO);
            startActivity(intent);
            getActivity().finish();
            getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
        }

        recyclerViewData();
    }


    private void initView(View view) {
        newcontentRecy = (XRecyclerView) view.findViewById(R.id.newcontent_recy);
        noDynamic = view.findViewById(R.id.no_dynamic);
        TextView attention_v = view.findViewById(R.id.attention_v);
        TextView send_dyna = view.findViewById(R.id.send_dyna);
        send_dyna.setVisibility(View.VISIBLE);
        attention_v.setVisibility(View.GONE);
        daVRecommend = view.findViewById(R.id.da_v_recommend);//大Vtuijian
        daVRecommend.setVisibility(View.GONE);
    }

    private static final String PAGE_NAME_KEY = "PAGE_NAME_KEY";

    public static MyDynamicFragment getInstance(String pageName) {
        Bundle args = new Bundle();
        args.putString(PAGE_NAME_KEY, pageName);
        MyDynamicFragment pageFragment = new MyDynamicFragment();
        pageFragment.setArguments(args);
        return pageFragment;
    }

    /**
     * xRecyclerview配置
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void recyclerViewData() {
        newcontentRecy.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        newcontentRecy.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        newcontentRecy.setArrowImageView(R.drawable.iconfont_downgrey);
        newcontentRecy.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        newcontentRecy.getDefaultFootView().setLoadingHint(getString(R.string.loading));
        newcontentRecy.getDefaultFootView().setNoMoreHint(getString(R.string.load_end));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        newcontentRecy.setLayoutManager(linearLayoutManager);
        adapter = new MyDynamicAdapter(newContentList, getActivity());
        setHeader(newcontentRecy);
        newcontentRecy.setAdapter(adapter);
        newcontentRecy.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (getScrollY()>(scrollY+oldScrollY)){
                    floatingActionButton.setVisibility(View.VISIBLE);
                }else {
                    floatingActionButton.setVisibility(View.GONE);
                }
            }
        });

        //点击跳转
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newcontentRecy.setFocusable(true);
                newcontentRecy.smoothScrollToPosition(0);
            }
        });

    }

    private void setHeader(XRecyclerView view) {
        //渲染header布局
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.mydynamic_header, null);
        infoLayout = header.findViewById(R.id.info_layout);
        infoNum = header.findViewById(R.id.info_num);
        adapter.setHeaderView(header);
    }

    @Override
    protected SendFriendCirclePresenter setPresenter() {
        return new SendFriendCirclePresenter(this);
    }

    @Override
    protected int setLayouId() {
        return R.layout.newcontent_fragment;
    }

    //上传多张图片
    @Override
    public void showPictures(PictursBean pictursBean) {

    }

    //发布动态
    @Override
    public void showSendNewMoment(SendNewMomentBean sendNewMomentBean) {

    }

    //动态列表
    @Override
    public void showConment(final List<NewContentBean.DataBean> newList) {
        if (page == 1) {
            newContentList.clear();
        }
        newContentList.addAll(newList);
        if (page == 1 && newContentList.size() == 0) {
            newcontentRecy.setVisibility(View.GONE);
            noDynamic.setVisibility(View.VISIBLE);
        } else {
            newcontentRecy.setVisibility(View.VISIBLE);
            noDynamic.setVisibility(View.GONE);
        }
//        if (page!=1&&newContentList.size()==0){
//            ToastUtils.showToast(getActivity(),"没有更多");
//        }
        adapter.notifyDataSetChanged();
//        infoLayout.setVisibility(View.GONE);
        loadMore();//上拉刷新下拉加载
        adapter.setOnClickItemListener(new MyDynamicAdapter.OnClickItemListener() {

            @Override
            public void setOnItemClick(View view, int position) {
                if (AntiShake.check(view.getId())) {    //判断是否多次点击
                    return;
                }
                intentPositions = position - 2;
                Intent intent = new Intent(getActivity(), DynamicDetailsActivity.class);
                intent.putExtra("moment_id", newContentList.get(intentPositions).moment_id);
                intent.putExtra("author_id", newContentList.get(intentPositions).user_id);
                intent.putExtra("avatars", newContentList.get(intentPositions).author.avatars);
                intent.putExtra("nickname", newContentList.get(intentPositions).author.nickname);
                intent.putExtra("add_date", newContentList.get(intentPositions).add_time);
                intent.putExtra("like_count", newContentList.get(intentPositions).like_count);
                intent.putExtra("comment_count", newContentList.get(intentPositions).comment_count);
                intent.putExtra("type", newContentList.get(intentPositions).type);
                intent.putExtra("did_i_follow", newContentList.get(intentPositions).did_i_follow);
                intent.putExtra("did_i_like", newContentList.get(intentPositions).did_i_like);
                //判断type类型
                if (newContentList.get(intentPositions).type.equals(BizConstant.ALREADY_FAVORITE)) {
                    if (StringUtil.isNotEmpty(newContentList.get(intentPositions).content)) {//判断动态是否有内容
                        intent.putExtra("content", newContentList.get(intentPositions).content);
                    }
                } else {
                    intent.putExtra("target_description", newContentList.get(intentPositions).extend_content.target_description);
                    intent.putExtra("target_id", newContentList.get(intentPositions).extend_content.target_id);
                    intent.putExtra("target_image", newContentList.get(intentPositions).extend_content.target_image);
                    intent.putExtra("target_title", newContentList.get(intentPositions).extend_content.target_title);
                }

                if (newContentList.get(intentPositions).images_arr != null) {
                    String image[] = new String[newContentList.get(intentPositions).images_arr.size()];
                    String images[] = new String[newContentList.get(intentPositions).images_arr.size()];
                    for (int i = 0; i < newContentList.get(intentPositions).images_arr.size(); i++) {
                        image[i] = newContentList.get(intentPositions).images_arr.get(i).original;
                        images[i] = newContentList.get(intentPositions).images_arr.get(i).thumb;
                    }
                    if (newContentList.get(intentPositions).images_arr.size() == 1) {
                        intent.putExtra("hight", newContentList.get(intentPositions).images_arr.get(0).original_size.height);
                        intent.putExtra("width", newContentList.get(intentPositions).images_arr.get(0).original_size.width);
                    }
                    intent.putExtra("orinal_imageUrls", image);
                    intent.putExtra("imageUrls", images);

                }
                intent.putExtra("isComment", BizConstant.MY);
                startActivityForResult(intent, 10);
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
            }
        });
        //分享动态
        adapter.shareSetOnclick(new MyDynamicAdapter.ShareInterface() {
            @Override
            public void onclick(View view, int position) {
                moment_id = newContentList.get(position).moment_id;
                presenter.mDynamicShare(getActivity(), newContentList.get(position).moment_id);
                mPopwindow = new RewritePopwindow(getActivity(), itemsOnClick);
                mPopwindow.showAtLocation(view,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
        //更多功能
        adapter.funtcionSetOnclick(new MyDynamicAdapter.FunctionInterface() {
            @Override
            public void onclick(View view, int position, TextView attention) {
                functionPosition = position;
                dynamicFunctionPopwindow = new DynamicFunctionPopwindow(getActivity(), itemsOnClicks, functionPosition);
                dynamicFunctionPopwindow.showAtLocation(view,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                String did_i_follow = newContentList.get(functionPosition).did_i_follow;
                View dynamicView = dynamicFunctionPopwindow.getContentView();
                Button isAttention = dynamicView.findViewById(R.id.isattention);
                if (StringUtil.isNotEmpty(did_i_follow) && did_i_follow.equals(BizConstant.ENTERPRISE_tRUE)) {
                    isAttention.setText("关注");
                } else {
                    isAttention.setText("取消关注");
                }
            }
        });
        //刪除动态
        adapter.deleteSetOnclick(new MyDynamicAdapter.DeleteInterface() {
            @Override
            public void onclick(View view, int position) {
                positions = position;
                showNormalDialogs(position);
            }
        });
        //文章详情
        adapter.articleSetOnclick(new MyDynamicAdapter.ArticleInterface() {
            @Override
            public void onclick(View view, int position) {
                Intent intent = new Intent(getActivity(), HomeDetailsActivity.class);
                intent.putExtra("id", newContentList.get(position).extend_content.target_id);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
            }
        });
        //关注
        adapter.attentionSetOnclick(new MyDynamicAdapter.AttentionInterface() {
            @Override
            public void onclick(View view, int position, TextView attention) {
                String user_id = newContentList.get(position).user_id;
                attentionPosition = position;
                if (islogins) {
                    if (StringUtil.isNotEmpty(uid) && !uid.equals(user_id)) {
                        presenter.mAttention(getActivity(), "follow", user_id, BizConstant.ALREADY_FAVORITE);
                    } else {
                        ToastUtils.showToast(getActivity(), "自己不能关注自己");
                    }
                } else {
                    ToastUtils.showToast(getActivity(), "请先登录");
                }

            }
        });
        //点赞
        adapter.likeMomentSetOnclick(new MyDynamicAdapter.LikeMomentInterface() {


            @Override
            public void onclick(View view, int position, CheckBox choose_like, TextView like_num) {
                String did_i_like = newContentList.get(position).did_i_like;
                if (StringUtil.isNotEmpty(did_i_like) && did_i_like.equals(BizConstant.IS_FAIL)) {
                    presenter.LikeMoment(getActivity(), newContentList.get(position).moment_id);
                    likePosition = position;
                    newContentList.get(likePosition).did_i_like = "1";
                    int likeCount = Integer.parseInt(newContentList.get(likePosition).like_count);
                    newContentList.get(likePosition).like_count = String.valueOf(likeCount + 1);
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showToast(getActivity(), "您已经赞过了哦");
                }
            }
        });
    }

    /**
     * 点击关注消失刷新列表
     *
     * @param position
     */
    private void attentionData(int position) {
        String user_id = newContentList.get(position).user_id;
        for (int i = 0; i < newContentList.size(); i++) {
            if (user_id.equals(newContentList.get(i).user_id)) {
                newContentList.get(i).did_i_follow = "1";
            }
        }
        adapter.notifyDataSetChanged();
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
        }
    }


    /**
     * 动态新评论
     *
     * @param addBankBean
     */
    @Override
    public void showNewComment(AddBankBean addBankBean) {

    }

    /**
     * 删除动态
     *
     * @param settingBean
     */
    @Override
    public void showDeleteMoment(SettingBean settingBean) {
        if (settingBean.status == 1) {
            newContentList.remove(positions);
            adapter.notifyDataSetChanged();
            if (newContentList.size() == 0) {
                newcontentRecy.setVisibility(View.GONE);
                noDynamic.setVisibility(View.VISIBLE);
            } else {
                newcontentRecy.setVisibility(View.VISIBLE);
                noDynamic.setVisibility(View.GONE);
            }
        } else {
            ToastUtils.showToast(getActivity(), settingBean.msg);
        }

    }

    /**
     * 关注
     *
     * @param attentionBean
     */
    @Override
    public void showAttention(AttentionBean attentionBean) {
        ToastUtils.showToast(getActivity(), attentionBean.msg);
        attentionData(attentionPosition);
    }

    /**
     * 一键关注
     *
     * @param attentionBean
     */
    @Override
    public void showAttentions(AttentionBean attentionBean) {

    }

    /**
     * 点击关注消失刷新列表
     *
     * @param position
     */
    private void unAttentionData(int position) {
        String user_id = newContentList.get(position).user_id;
        for (int i = 0; i < newContentList.size(); i++) {
            if (user_id.equals(newContentList.get(i).user_id)) {
                newContentList.get(i).did_i_follow = "0";
            }
        }
        adapter.notifyDataSetChanged();
    }


    /**
     * 取消关注
     *
     * @param attentionBean
     */
    @Override
    public void showUnAttention(AttentionBean attentionBean) {
        ToastUtils.showToast(getActivity(), attentionBean.msg);
        String did_i_follow = newContentList.get(functionPosition).did_i_follow;
        if (did_i_follow.equals(BizConstant.ENTERPRISE_tRUE)) {
            attentionData(functionPosition);
        } else {
            String user_id = newContentList.get(functionPosition).user_id;
            for (int i = 0; i < newContentList.size(); i++) {
                if (user_id.equals(newContentList.get(i).user_id)) {
                    newContentList.get(i).did_i_follow = "0";
//                        ToastUtils.showToast(getActivity(), newContentList.get(i) + "");
                }
            }
        }

        adapter.notifyDataSetChanged();
    }

    //屏蔽
    @Override
    public void showBlockUser(RefotPasswordBean settingBean) {

        if (settingBean.status == 1) {

            String user_id = newContentList.get(functionPosition).user_id;
            //循环删除列表
            for (int i = 0; i < newContentList.size(); i++) {
                if (user_id.equals(newContentList.get(i).user_id)) {
                    newContentList.remove(i);
                    i = i - 1;
                }
            }
            adapter.notifyDataSetChanged();
        } else {
            ToastUtils.showToast(getActivity(), settingBean.msg);
        }
    }

    /**
     * 点赞
     *
     * @param attentionBean
     */
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
        MessageFollow messageFollow = new MessageFollow();
        if (attentionBean.status == 1) {
            messageFollow.notifyCount = attentionBean.data.unread_count;
            EventBus.getDefault().post(messageFollow);
            if (StringUtil.isNotEmpty(attentionBean.data.unread_count) && attentionBean.data.unread_count.equals(BizConstant.IS_FAIL)) {
                infoLayout.setVisibility(View.GONE);
            } else {
                infoNum.setText(attentionBean.data.unread_count + "条新消息");
                infoLayout.setVisibility(View.VISIBLE);
            }
        }
        infoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), InformationNoticeActivity.class));
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
            }
        });
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
     *
     * @param attentionBean
     */
    @Override
    public void showReport(AttentionBean attentionBean) {

    }

    /**
     * 礼物数量的接口
     *
     * @param giftSumBean
     */
    @Override
    public void showGiftSummary(GiftSumBean giftSumBean) {

    }

    /**
     * 礼物
     *
     * @param giftBean
     */
    @Override
    public void showGift(List<GiftBean.DataBean> giftBean) {

    }
    /**
     * 打赏接口
     *
     * @param attentionBean
     */
    @Override
    public void showSendGifts(AttentionBean attentionBean) {

    }

    /**
     * 打赏列表
     *
     * @param rewardLists
     */
    @Override
    public void showGiftList(List<RewardListBean.DataBean.ListBean> rewardLists) {

    }

    /**
     * 商机币余额
     *
     * @param businessCoinBean
     */
    @Override
    public void showBalanceLog(BusinessCoinBean businessCoinBean) {

    }

    @Override
    public void showFeedBack(SettingBean settingBean) {

    }

    @Override
    public void showDynamicComment(List<DynamicDetailsBean.DataBean> dataBeanList) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10) {
            if (resultCode == 20) {
                String like_counts = data.getStringExtra("like_count");
                String comment_counts = data.getStringExtra("comment_count");
                String did_i_follows = data.getStringExtra("did_i_follow");
                String did_i_likes = data.getStringExtra("did_i_like");
                String gift_counts = data.getStringExtra("gift_count");
                if (StringUtil.isNotEmpty(gift_counts)) {
                    newContentList.get(intentPositions).gift_count = gift_counts;
                }
                if (StringUtil.isNotEmpty(like_counts)) {
                    newContentList.get(intentPositions).like_count = like_counts;
                }
                if (StringUtil.isNotEmpty(comment_counts)) {
                    newContentList.get(intentPositions).comment_count = comment_counts;
                }
                if (StringUtil.isNotEmpty(did_i_follows) && did_i_follows.equals(BizConstant.IS_SUC)) {
                    attentionData(intentPositions);
                } else {
                    unAttentionData(intentPositions);
                }
                if (StringUtil.isNotEmpty(did_i_likes)) {
                    newContentList.get(intentPositions).did_i_like = did_i_likes;
                }
                adapter.notifyDataSetChanged();
            }
//            if (resultCode == 100) {
//                //发表完动态刷新
//                if (StringUtil.isNotEmpty(userId)) {
//                    presenter.MomentList(getActivity(), page, userId, "", "");
//                }
//            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageFollow event) {
//        NewContentBean.DataBean.CommentsExcerptBean bean = new NewContentBean.DataBean.CommentsExcerptBean();
//        bean.content = event.comment;
//        bean.user_id = uid;
//        bean.nickname = nickname;
//        ToastUtils.showToast(getActivity(),event.newContnet+"");
        myComment = event.myComment;
        newContnets = event.newContnet;
//        newContentList.get(intentPositions).comments_excerpt.add(bean);

//        adapter.notifyDataSetChanged();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (newContentList.size() == 0) {
            presenter.MomentLists(getActivity(), page, userId, "", "");
        }
        if (StringUtil.isNotEmpty(newContnets) && newContnets.equals(BizConstant.IS_SUC)) {
            presenter.MomentList(getActivity(), page, userId, "", "");
        }
        if (StringUtil.isNotEmpty(myComment)) {
            NewContentBean.DataBean.CommentsExcerptBean bean = new NewContentBean.DataBean.CommentsExcerptBean();
            bean.content = myComment;
            bean.user_id = uid;
            bean.nickname = nickname;
            if (newContentList.get(intentPositions).comments_excerpt != null) {
                newContentList.get(intentPositions).comments_excerpt.add(bean);
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.GetMyNotificationCount(getActivity());
//     if (newContentList.size() == 0) {
//            presenter.MomentLists(getActivity(), page, userId, "");
//        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    /**
     * 上拉刷新下拉加载
     */
    public void loadMore() {
        newcontentRecy.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshTime++;
                times = 0;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page = 1;
                        presenter.GetMyNotificationCount(getActivity());
                        presenter.MomentLists(getActivity(), page, userId, "", "");
                        if (newcontentRecy != null)
                            newcontentRecy.refreshComplete();
                    }

                }, 1000);            //refresh data here
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page++;
                        presenter.GetMyNotificationCount(getActivity());
                        presenter.MomentLists(getActivity(), page, userId, "", "");
                        if (newcontentRecy != null) {
                            newcontentRecy.loadMoreComplete();
                        }
                    }
                }, 1000);
                times++;
            }
        });

    }


    /**
     * 分享的弹窗
     */
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            if (Build.VERSION.SDK_INT >= 23) {
                String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
                ActivityCompat.requestPermissions(getActivity(), mPermissionList, 123);
            }
            mPopwindow.dismiss();
            mPopwindow.backgroundAlpha(getActivity(), 1f);
            switch (v.getId()) {
                case R.id.weixinghaoyou:
                    ShareUtilses.shareWebs(getActivity(), share_url, share_title
                            , share_description, share_icon, R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN, moment_id
                    );
                    break;
                case R.id.pengyouquan:
                    ShareUtilses.shareWebs(getActivity(), share_url, share_title
                            , share_description, share_icon, R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN_CIRCLE, moment_id
                    );
                    break;
                case R.id.qqhaoyou:
                    ShareUtilses.shareWebs(getActivity(), share_url, share_title
                            , share_description, share_icon, R.mipmap.ic_launcher, SHARE_MEDIA.QQ, moment_id
                    );
                    break;
                case R.id.qqkongjian:
                    ShareUtilses.shareWebs(getActivity(), share_url, share_title
                            , share_description, share_icon, R.mipmap.ic_launcher, SHARE_MEDIA.QZONE, moment_id
                    );
                    break;
                case R.id.copyurl:
                    ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将文本内容放到系统剪贴板里。
                    cm.setText(share_title + share_url);
                    ToastUtils.showToast(getActivity(), "复制成功");
                    break;
                default:
                    break;
            }
        }

    };


    /**
     * 删除的弹窗
     *
     * @param position
     */
    private void showNormalDialogs(int position) {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(getActivity());
        normalDialog.setMessage("确认删除该条动态吗？");
        normalDialog.setPositiveButton(getString(R.string.delete_contract_clause),
                new DialogInterface.OnClickListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (StringUtil.isNotEmpty(newContentList.get(positions).moment_id)) {
                            presenter.DeleteMoment(getActivity(), newContentList.get(positions).moment_id);//删除动态
                        }
                    }
                });
        normalDialog.setNegativeButton(getString(R.string.common_cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        // 显示
        normalDialog.show();
    }

    /**
     * 更多更能设置
     */
    private View.OnClickListener itemsOnClicks = new View.OnClickListener() {
        public void onClick(View v) {
            if (Build.VERSION.SDK_INT >= 23) {
                String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
                ActivityCompat.requestPermissions(getActivity(), mPermissionList, 123);
            }
            dynamicFunctionPopwindow.setOutsideTouchable(true);
            dynamicFunctionPopwindow.dismiss();
            dynamicFunctionPopwindow.backgroundAlpha(getActivity(), 1f);
            switch (v.getId()) {
                case R.id.isattention://关注，取消关注
                    //销毁弹出框
                    dynamicFunctionPopwindow.dismiss();
                    dynamicFunctionPopwindow.backgroundAlpha(getActivity(), 1f);
                    if (islogins) {
                        String did_i_follow = newContentList.get(functionPosition).did_i_follow;
                        String user_id = newContentList.get(functionPosition).user_id;
                        if (StringUtil.isNotEmpty(did_i_follow) && did_i_follow.equals(BizConstant.ENTERPRISE_tRUE)) {
                            //判断是关注还是取消关注
                            if (StringUtil.isNotEmpty(uid) && !uid.equals(user_id)) {
                                presenter.mUnAttention(getActivity(), "follow", user_id);
                            } else {
                                ToastUtils.showToast(getActivity(), "自己不能关注自己");
                            }
                        } else {
                            presenter.mUnAttention(getActivity(), "un_follow", user_id);
                        }
                    } else {
                        ToastUtils.showToast(getActivity(), "请先登录");
                    }

                    break;
                case R.id.shield://屏蔽
                    if (islogins) {
                        //销毁弹出框
                        dynamicFunctionPopwindow.dismiss();
                        dynamicFunctionPopwindow.backgroundAlpha(getActivity(), 1f);
                        presenter.BlockUser(getActivity(), BizConstant.PINGBI, newContentList.get(functionPosition).user_id);
                    } else {
                        ToastUtils.showToast(getActivity(), "请先登录");
                    }


                    break;
                case R.id.report://举报
                    if (islogins) {
                        //销毁弹出框
                        dynamicFunctionPopwindow.dismiss();
                        dynamicFunctionPopwindow.backgroundAlpha(getActivity(), 1f);
                        ToastUtils.showToast(getActivity(), "举报成功");
                    } else {
                        ToastUtils.showToast(getActivity(), "请先登录");
                    }


                    break;
                default:
                    break;
            }
        }

    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
    /*
              getScrollY 该方法用于测算ListView滑动的距离
            */
    public int getScrollY() {
        View c = newcontentRecy.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = newcontentRecy.getVerticalScrollbarPosition();
        int top = c.getTop();
        return -top + firstVisiblePosition * c.getHeight();
    }


}
