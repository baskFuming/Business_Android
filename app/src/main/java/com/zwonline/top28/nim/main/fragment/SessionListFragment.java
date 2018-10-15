package com.zwonline.top28.nim.main.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.session.SessionCustomization;
import com.netease.nim.uikit.business.contact.selector.activity.ContactSelectActivity;
import com.netease.nim.uikit.business.recent.RecentContactsCallback;
import com.netease.nim.uikit.business.recent.RecentContactsFragment;
import com.netease.nim.uikit.business.recent.adapter.RecentContactAdapter;
import com.netease.nim.uikit.business.session.ImageViewPluls;
import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.netease.nim.uikit.common.fragment.TabFragment;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.ClientType;
import com.netease.nimlib.sdk.auth.OnlineClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.zwonline.top28.R;
import com.zwonline.top28.activity.AddFriendsActivity;
import com.zwonline.top28.activity.CompanyActivity;
import com.zwonline.top28.activity.HomePageActivity;
import com.zwonline.top28.activity.YunYingGuanActivity;
import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.service.ApiService;
import com.zwonline.top28.base.BaseMainActivity;
import com.zwonline.top28.bean.BannerAdBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.nim.config.preference.Preferences;
import com.zwonline.top28.nim.login.LoginActivity;
import com.zwonline.top28.nim.login.LogoutHelper;
import com.zwonline.top28.nim.main.AnnouncementActivity;
import com.zwonline.top28.nim.main.activity.MultiportActivity;
import com.zwonline.top28.nim.main.reminder.ReminderManager;
import com.zwonline.top28.nim.session.SessionHelper;
import com.zwonline.top28.nim.session.extension.GuessAttachment;
import com.zwonline.top28.nim.session.extension.RTSAttachment;
import com.zwonline.top28.nim.session.extension.RedPacketAttachment;
import com.zwonline.top28.nim.session.extension.RedPacketOpenedAttachment;
import com.zwonline.top28.nim.session.extension.SnapChatAttachment;
import com.zwonline.top28.nim.session.extension.StickerAttachment;
import com.zwonline.top28.nim.yangfen.YangFenAction;
import com.zwonline.top28.nim.yangfen.YangFenAttachment;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.SignUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.badge.BadgeViews;
import com.zwonline.top28.utils.popwindow.EmptyPopwindow;

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
 * Created by zhoujianghua on 2015/8/17.
 */
public class SessionListFragment extends TabFragment {

    private View notifyBar;
    private static final int REQUEST_CODE_ADVANCED = 2;
    private TextView notifyBarText;
    // 同时在线的其他端的信息
    private List<OnlineClient> onlineClients;
    private View multiportBar;
    private RecentContactsFragment fragment;
    private LinearLayout build_group_chat;
    private static SharedPreferencesUtils sp;
    private String accid;
    private LinearLayout add_friend;
    private LinearLayout infoScan;
    private LinearLayout notice;
    private BadgeViews badgeView;
    private ImageView noticeImg;
    private String has_permission;
    private EmptyPopwindow mPopwindow;
    private ImageViewPluls advertisings;
    private RelativeLayout adLayout;
    private TextView adTv;
    private List<RecentContact> recentContactList;
    private RecentContactAdapter recentContactAdapter;

    private static final String PAGE_NAME_KEY = "PAGE_NAME_KEY";
    private String jump_path;
    private String is_jump_off;
    private String is_webview;
    private String project_id;

    public static SessionListFragment getInstance(String pageName) {
        Bundle args = new Bundle();
        args.putString(PAGE_NAME_KEY, pageName);
        SessionListFragment pageFragment = new SessionListFragment();
        pageFragment.setArguments(args);
        return pageFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        onCurrent();
        View view = inflater.inflate(R.layout.session_list, null);
        initView(view);
        sp = SharedPreferencesUtils.getUtil();
        try {
            BannerAd(getActivity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        badgeView = new BadgeViews(getActivity());
        badgeView.setTargetView(noticeImg);
        recentContactList = new ArrayList<>();
        accid = (String) sp.getKey(getActivity(), "account", "");
        has_permission = (String) sp.getKey(getActivity(), "has_permission", "");
        addRecentContactsFragment();
        return view;
    }

    private void initView(View view) {
        build_group_chat = view.findViewById(R.id.build_group_chat);
        add_friend = (LinearLayout) view.findViewById(R.id.add_friend);
        notice = (LinearLayout) view.findViewById(R.id.notice);
        noticeImg = (ImageView) view.findViewById(R.id.notice_img);
        infoScan = (LinearLayout) view.findViewById(R.id.info_scan);


        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AnnouncementActivity.class));
            }
        });
        infoScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), CaptureActivity.class), 0);
            }
        });
        build_group_chat.setOnClickListener(new View.OnClickListener() {//创建群
            @Override
            public void onClick(View v) {
                ArrayList<String> memberAccounts = new ArrayList<>();
                memberAccounts.add(accid);
                ContactSelectActivity.Option option = TeamHelper.getCreateContactSelectOption(memberAccounts, 50);
                NimUIKit.startContactSelector(getActivity(), option, REQUEST_CODE_ADVANCED);// 创建群
            }
        });
        add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddFriendsActivity.class));
            }
        });
        notifyBar = view.findViewById(R.id.status_notify_bar);
        notifyBarText = view.findViewById(R.id.status_desc_label);
        notifyBar.setVisibility(View.GONE);

        multiportBar = view.findViewById(R.id.multiport_notify_bar);
        multiportBar.setVisibility(View.GONE);
        multiportBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiportActivity.startActivity(getActivity(), onlineClients);
            }
        });
    }

    public SessionListFragment() {
        this.setContainerId(R.layout.session_list);
    }


    @Override
    public void onDestroy() {
        registerObservers(false);
        super.onDestroy();
    }


    private void registerObservers(boolean register) {
        NIMClient.getService(AuthServiceObserver.class).observeOtherClients(clientsObserver, register);
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, register);
    }

    /**
     * 用户状态变化
     */
    Observer<StatusCode> userStatusObserver = new Observer<StatusCode>() {

        @Override
        public void onEvent(StatusCode code) {
            if (code.wontAutoLogin()) {
                kickOut(code);
            } else {
                if (code == StatusCode.NET_BROKEN) {
                    notifyBar.setVisibility(View.VISIBLE);
                    notifyBarText.setText(R.string.net_broken);
                } else if (code == StatusCode.UNLOGIN) {
                    notifyBar.setVisibility(View.VISIBLE);
                    notifyBarText.setText(R.string.nim_status_unlogin);
                } else if (code == StatusCode.CONNECTING) {
                    notifyBar.setVisibility(View.VISIBLE);
                    notifyBarText.setText(R.string.nim_status_connecting);
                } else if (code == StatusCode.LOGINING) {
                    notifyBar.setVisibility(View.VISIBLE);
                    notifyBarText.setText(R.string.nim_status_logining);
                } else {
                    notifyBar.setVisibility(View.GONE);
                }
            }
        }
    };

    Observer<List<OnlineClient>> clientsObserver = new Observer<List<OnlineClient>>() {
        @Override
        public void onEvent(List<OnlineClient> onlineClients) {
            SessionListFragment.this.onlineClients = onlineClients;
            if (onlineClients == null || onlineClients.size() == 0) {
                multiportBar.setVisibility(View.GONE);
            } else {
                multiportBar.setVisibility(View.VISIBLE);
                TextView status = (TextView) multiportBar.findViewById(R.id.multiport_desc_label);
                OnlineClient client = onlineClients.get(0);
                switch (client.getClientType()) {
                    case ClientType.Windows:
                    case ClientType.MAC:
                        status.setText(getString(R.string.multiport_logging) + getString(R.string.computer_version));
                        break;
                    case ClientType.Web:
                        status.setText(getString(R.string.multiport_logging) + getString(R.string.web_version));
                        break;
                    case ClientType.iOS:
                    case ClientType.Android:
                        status.setText(getString(R.string.multiport_logging) + getString(R.string.mobile_version));
                        break;
                    default:
                        multiportBar.setVisibility(View.GONE);
                        break;
                }
            }
        }
    };

    private void kickOut(StatusCode code) {
        Preferences.saveUserToken("");

        if (code == StatusCode.PWD_ERROR) {
            LogUtil.e("Auth", "user password error");
            Toast.makeText(getActivity(), R.string.login_failed, Toast.LENGTH_SHORT).show();
        } else {
            LogUtil.i("Auth", "Kicked!");
        }
        onLogout();
    }

    // 注销
    private void onLogout() {
        // 清理缓存&注销监听&清除状态
        LogoutHelper.logout();

        LoginActivity.start(getActivity(), true);
        getActivity().finish();
    }

    // 将最近联系人列表fragment动态集成进来。 开发者也可以使用在xml中配置的方式静态集成。
    private void addRecentContactsFragment() {
        fragment = new RecentContactsFragment();
        fragment.setContainerId(R.id.messages_fragment);

        final BaseMainActivity activity = (BaseMainActivity) getActivity();

        // 如果是activity从堆栈恢复，FM中已经存在恢复而来的fragment，此时会使用恢复来的，而new出来这个会被丢弃掉
        fragment = (RecentContactsFragment) activity.addFragment(fragment);

        // 功能项定制


        fragment.setCallback(new RecentContactsCallback() {
            @Override
            public void onRecentContactsLoaded() {
                // 最近联系人列表加载完毕
            }

            @Override
            public void onUnreadCountChange(int unreadCount) {
                ReminderManager.getInstance().updateSessionUnreadNum(unreadCount);
            }

            @Override
            public void txCallBack(int pos, RecentContact recent) {
                if (recent.getSessionType() == SessionTypeEnum.P2P) {
                    Intent intent = new Intent(getActivity(), HomePageActivity.class);
                    intent.putExtra("uid", recent.getContactId());
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                }

            }

            @Override
            public void yunYingGun(RelativeLayout linearLayout, ImageViewPluls imageView,TextView textView) {
//
                advertisings = imageView;
                adLayout = linearLayout;
                adTv=textView;
            }

            /**
             * 顶部广告点击
             */
            @Override
            public void advertising() {
                if (StringUtil.isNotEmpty(is_webview) && is_webview.endsWith(BizConstant.IS_SUC)) {
                    if (StringUtil.isNotEmpty(is_jump_off) && is_jump_off.equals(BizConstant.IS_FAIL)) {
                        Intent intent = new Intent(getActivity(), YunYingGuanActivity.class);
                        if (StringUtil.isNotEmpty(jump_path)) {
                            intent.putExtra("jump_path", jump_path);
                        }
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                    } else {
                        Uri uri = Uri.parse(jump_path);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(getActivity(), CompanyActivity.class);
                    intent.putExtra("pid", project_id);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                }


            }

            @Override
            public void onItemClick(RecentContact recent) {
                SessionCustomization sessionCustomization = new SessionCustomization();
                ArrayList<BaseAction> actions = new ArrayList<>();
                if (StringUtil.isEmpty(has_permission) || has_permission.equals(BizConstant.ENTERPRISE_tRUE)) {
                    actions.remove(new YangFenAction());
                } else if (has_permission.equals(BizConstant.ALREADY_FAVORITE)) {
                    actions.add(new YangFenAction());
                }
                sessionCustomization.actions = actions;

                // 回调函数，以供打开会话窗口时传入定制化参数，或者做其他动作
                switch (recent.getSessionType()) {
                    case P2P:
                        NimUIKit.startChatting(getContext(), recent.getContactId(), SessionTypeEnum.P2P, sessionCustomization, null);
                        SessionHelper.startP2PSession(getActivity(), recent.getContactId());
                        break;
                    case Team:
                        //红包权限
                        NimUIKit.startChatting(getContext(), recent.getContactId(), SessionTypeEnum.Team, sessionCustomization, null);
                        SessionHelper.startTeamSession(getActivity(), recent.getContactId());
                        break;
                    default:
                        break;
                }
            }

            //
            @Override
            public void onItemLongClick(RecentContact recent) {
            }

            @Override
            public String getDigestOfAttachment(RecentContact recentContact, MsgAttachment
                    attachment) {
                // 设置自定义消息的摘要消息，展示在最近联系人列表的消息缩略栏上
                // 当然，你也可以自定义一些内建消息的缩略语，例如图片，语音，音视频会话等，自定义的缩略语会被优先使用。
                if (attachment instanceof GuessAttachment) {
                    GuessAttachment guess = (GuessAttachment) attachment;
                    return guess.getValue().getDesc();
                } else if (attachment instanceof RTSAttachment) {
                    return "[白板]";
                } else if (attachment instanceof StickerAttachment) {
                    return "[贴图]";
                } else if (attachment instanceof SnapChatAttachment) {
                    return "[阅后即焚]";
                } else if (attachment instanceof RedPacketAttachment) {
                    return "[鞅分红包]";
                } else if (attachment instanceof YangFenAttachment) {
                    return "[鞅分红包]";

                } else if (attachment instanceof RedPacketOpenedAttachment) {
                    return ((RedPacketOpenedAttachment) attachment).getDesc(recentContact.getSessionType(), recentContact.getContactId());
                }
                return null;
            }

            @Override
            public String getDigestOfTipMsg(RecentContact recent) {

                String msgId = recent.getRecentMessageId();
                List<String> uuids = new ArrayList<>(1);
                uuids.add(msgId);
                List<IMMessage> msgs = NIMClient.getService(MsgService.class).queryMessageListByUuidBlock(uuids);
                if (msgs != null && !msgs.isEmpty()) {
                    IMMessage msg = msgs.get(0);
                    Map<String, Object> content = msg.getRemoteExtension();
                    if (content != null && !content.isEmpty()) {
                        return (String) content.get("content");
                    }
                }


                return null;
            }

            @Override
            public void getEmptyMsg(View V, List<RecentContact> items, RecentContactAdapter adapter) {
                mPopwindow = new EmptyPopwindow(getActivity(), itemsOnClick);
                mPopwindow.showAtLocation(V,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                recentContactList.clear();
                recentContactList.addAll(items);
                recentContactAdapter = adapter;
            }

        });
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            if (Build.VERSION.SDK_INT >= 23) {
                String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
                ActivityCompat.requestPermissions((Activity) getActivity(), mPermissionList, 123);
            }
            mPopwindow.setOutsideTouchable(true);
            mPopwindow.dismiss();
            mPopwindow.backgroundAlpha((Activity) getActivity(), 1f);
            switch (v.getId()) {
                case R.id.chat://清空消息
                    if (recentContactList != null && recentContactList.size() != 0) {
                        for (RecentContact r : recentContactList) {
                            NIMClient.getService(MsgService.class).clearUnreadCount(r.getContactId(), r.getSessionType());
                        }
                        fragment.notifyDataSetChanged();
                    }
                    break;
                case R.id.cancel_attention://清空列表
                    if (recentContactList != null && recentContactList.size() != 0) {
                        for (int i = 0; i < recentContactList.size(); i++) {
                            NIMClient.getService(MsgService.class).deleteRecentContact(recentContactList.get(i));
                            NIMClient.getService(MsgService.class).clearChattingHistory(recentContactList.get(i).getContactId(), recentContactList.get(i).getSessionType());
                            NIMClient.getService(MsgService.class).clearUnreadCount(recentContactList.get(i).getContactId(), recentContactList.get(i).getSessionType());
                        }
                        for (RecentContact r : recentContactList) {
                            NIMClient.getService(MsgService.class).clearUnreadCount(r.getContactId(), r.getSessionType());
                        }
                        fragment.clearList();
                        fragment.notifyDataSetChanged();
                    }
                    break;
                default:
                    break;
            }
        }

    };


    //消息顶部的网络请求
    public void BannerAd(final Context context) throws IOException {

        long timestamp = new Date().getTime() / 1000;//获取时间戳
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("token", token);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<BannerAdBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .bannerAd(String.valueOf(timestamp), token, sign);
        flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<BannerAdBean>() {
                    @Override
                    public void onNext(BannerAdBean attentionBean) {
                        if (attentionBean.status == 1) {
                            String is_show = attentionBean.data.is_show;
                            if (StringUtil.isNotEmpty(is_show)&&is_show.equals(BizConstant.IS_FAIL)){
                                adLayout.setVisibility(View.GONE);

                            }else {
                                adLayout.setVisibility(View.VISIBLE);
                            }
                            RequestOptions requestOption = new RequestOptions().placeholder(R.color.backgroud_zanwei).error(R.color.backgroud_zanwei);
                            Glide.with(context).load(attentionBean.data.images).apply(requestOption).listener(new RequestListener<Drawable>() {

                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    adTv.setVisibility(View.VISIBLE);
                                    return false;
                                }
                            }).into(advertisings);
                            jump_path = attentionBean.data.jump_path;
                            is_jump_off = attentionBean.data.is_jump_off;
                            is_webview = attentionBean.data.is_webview;
                            project_id = attentionBean.data.project_id;
                        }
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