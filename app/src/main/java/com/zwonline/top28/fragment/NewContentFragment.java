package com.zwonline.top28.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zwonline.top28.R;
import com.zwonline.top28.activity.DynamicDetailsActivity;
import com.zwonline.top28.activity.HomeDetailsActivity;
import com.zwonline.top28.activity.MainActivity;
import com.zwonline.top28.adapter.NewContentAdapter;
import com.zwonline.top28.base.BasesFragment;
import com.zwonline.top28.bean.AddBankBean;
import com.zwonline.top28.bean.AtentionDynamicHeadBean;
import com.zwonline.top28.bean.AttentionBean;
import com.zwonline.top28.bean.BusinessCircleBean;
import com.zwonline.top28.bean.DynamicDetailsBean;
import com.zwonline.top28.bean.DynamicDetailsesBean;
import com.zwonline.top28.bean.DynamicShareBean;
import com.zwonline.top28.bean.ExamineChatBean;
import com.zwonline.top28.bean.LikeListBean;
import com.zwonline.top28.bean.NewContentBean;
import com.zwonline.top28.bean.PictursBean;
import com.zwonline.top28.bean.RefotPasswordBean;
import com.zwonline.top28.bean.SendNewMomentBean;
import com.zwonline.top28.bean.SettingBean;
import com.zwonline.top28.bean.ShieldUserBean;
import com.zwonline.top28.bean.message.MessageFollow;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.nim.main.NotifyDetailsActivity;
import com.zwonline.top28.nim.main.fragment.SessionListFragment;
import com.zwonline.top28.presenter.SendFriendCirclePresenter;
import com.zwonline.top28.tip.toast.ToastUtil;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.SignUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.utils.popwindow.AttentionPopwindow;
import com.zwonline.top28.utils.popwindow.DynamicFunctionPopwindow;
import com.zwonline.top28.view.ISendFriendCircleActivity;
import com.zwonline.top28.wxapi.RewritePopwindow;
import com.zwonline.top28.wxapi.ShareUtilses;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 最新的商机圈动态
 */
public class NewContentFragment extends BasesFragment<ISendFriendCircleActivity, SendFriendCirclePresenter> implements ISendFriendCircleActivity {

    private XRecyclerView newcontentRecy;
    private int page = 1;
    private NewContentAdapter adapter;
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
    private int intentPositions;
    private String nickname;
    private String newContnets;
    private String newComment;
    //    private List<String> comment_list = new ArrayList<>();

    @Override
    protected void init(View view) {
//        StatusBarUtil.setColor(getActivity(), getResources().getColor(R.color.black), 0);
        sp = SharedPreferencesUtils.getUtil();
        EventBus.getDefault().register(this);
        islogins = (boolean) sp.getKey(getActivity(), "islogin", false);
        uid = (String) sp.getKey(getActivity(), "uid", "");
        nickname = (String) sp.getKey(getActivity(), "nickname", "");
        newContentList = new ArrayList<>();
        newcontentRecy = (XRecyclerView) view.findViewById(R.id.newcontent_recy);
        presenter.MomentLists(getActivity(), page, "", "", "");
        presenter.GetMyNotificationCount(getActivity());
        recyclerViewData();
    }

    private static final String PAGE_NAME_KEY = "PAGE_NAME_KEY";

    public static NewContentFragment getInstance(String pageName) {
        Bundle args = new Bundle();
        args.putString(PAGE_NAME_KEY, pageName);
        NewContentFragment pageFragment = new NewContentFragment();
        pageFragment.setArguments(args);
        return pageFragment;
    }

    /**
     * xRecyclerview配置
     */
    private void recyclerViewData() {
        newcontentRecy.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        newcontentRecy.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        newcontentRecy.setArrowImageView(R.drawable.iconfont_downgrey);
        newcontentRecy.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        newcontentRecy.getDefaultFootView().setLoadingHint(getString(R.string.loading));
        newcontentRecy.getDefaultFootView().setNoMoreHint(getString(R.string.load_end));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        newcontentRecy.setLayoutManager(linearLayoutManager);
        adapter = new NewContentAdapter(newContentList, getActivity());
        newcontentRecy.setAdapter(adapter);

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

        adapter.notifyDataSetChanged();
        loadMore();//上拉刷新下拉加载
        adapter.setOnClickItemListener(new NewContentAdapter.OnClickItemListener() {

            @Override
            public void setOnItemClick(View view, int position) {
                if (AntiShake.check(view.getId())) {    //判断是否多次点击
                    return;
                }

                intentPositions = position - 1;
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
                intent.putExtra("isComment", BizConstant.NEW);
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

                startActivityForResult(intent, 10);
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
            }
        });
        //分享动态
        adapter.shareSetOnclick(new NewContentAdapter.ShareInterface() {
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
        adapter.funtcionSetOnclick(new NewContentAdapter.FunctionInterface() {
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
        adapter.deleteSetOnclick(new NewContentAdapter.DeleteInterface() {
            @Override
            public void onclick(View view, int position) {
                positions = position;
                showNormalDialogs(position);
            }
        });
        //文章详情
        adapter.articleSetOnclick(new NewContentAdapter.ArticleInterface() {
            @Override
            public void onclick(View view, int position) {
                Intent intent = new Intent(getActivity(), HomeDetailsActivity.class);
                intent.putExtra("id", newContentList.get(position).extend_content.target_id);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
            }
        });
        //关注
        adapter.attentionSetOnclick(new NewContentAdapter.AttentionInterface() {
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
        adapter.likeMomentSetOnclick(new NewContentAdapter.LikeMomentInterface() {


            @Override
            public void onclick(View view, int position, CheckBox choose_like, TextView like_num) {
                String did_i_like = newContentList.get(position).did_i_like;
                if (StringUtil.isNotEmpty(did_i_like) && did_i_like.equals(BizConstant.IS_FAIL)) {
                    presenter.LikeMoment(getActivity(), newContentList.get(position).moment_id);
                    likePosition = position;
                    newContentList.get(likePosition).did_i_like = "1";
                    int likeCount = Integer.parseInt(newContentList.get(likePosition).like_count);
                    newContentList.get(likePosition).like_count = String.valueOf(likeCount + 1);
                    ToastUtils.showToast(getActivity(), "点赞成功");
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
//            String user_id = newContentList.get(functionPosition).user_id;
//            for (int i = 0; i < newContentList.size(); i++) {
//                if (user_id.equals(newContentList.get(i).user_id)) {
//                    newContentList.get(i).did_i_follow = "0";
////                        ToastUtils.showToast(getActivity(), newContentList.get(i) + "");
//                }
//            }
            unAttentionData(functionPosition);
        }

        adapter.notifyDataSetChanged();
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
        if (attentionBean.status == 1) {

        }else {
            ToastUtils.showToast(getActivity(), attentionBean.msg);
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

        }
    }

    /**
     * 点赞列表
     *
     * @param likeList
     */
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
                String did_i_likes = data.getStringExtra("did_i_like");//comment_lists

//                comment_list = data.getStringArrayListExtra("comment_list");
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
//                if (comment_list != null && comment_list.size() > 0) {
//                    NewContentBean.DataBean.CommentsExcerptBean bean = new NewContentBean.DataBean.CommentsExcerptBean();
//                    for (int i = 0; i < comment_list.size(); i++) {
//                        bean.content = comment_list.get(i);
//                        bean.user_id = uid;
//                        bean.nickname = nickname;
//                        newContentList.get(intentPositions).comments_excerpt.add(bean);
//                    }
//                }
                adapter.notifyDataSetChanged();
            }
//            if (resultCode == 100) {
//                ToastUtils.showToast(getActivity(),"1");
//                //发表完动态刷新
////                presenter.MomentList(getActivity(), 1, "", "", "");
//            }
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageFollow event) {
        newContnets = event.newContnet;
        newComment = event.newComment;

    }

    @Override
    public void onResume() {
        super.onResume();
        if (StringUtil.isNotEmpty(newContnets) && newContnets.equals(BizConstant.IS_SUC)) {
            presenter.MomentList(getActivity(), 1, "", "", "");
        }
        if (StringUtil.isNotEmpty(newComment)) {
            NewContentBean.DataBean.CommentsExcerptBean bean = new NewContentBean.DataBean.CommentsExcerptBean();
            bean.content = newComment;
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
                        presenter.MomentLists(getActivity(), page, "", "", "");
                        presenter.GetMyNotificationCount(getActivity());
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
                        presenter.MomentLists(getActivity(), page, "", "", "");
                        presenter.GetMyNotificationCount(getActivity());
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
                        presenter.Report(getActivity(), BizConstant.DYNAMIC, BizConstant.TYPE_ONE, newContentList.get(functionPosition).moment_id);
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


}
