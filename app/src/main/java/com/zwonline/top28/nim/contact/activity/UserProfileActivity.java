package com.zwonline.top28.nim.contact.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.SimpleCallback;
import com.netease.nim.uikit.api.model.contact.ContactChangedObserver;
import com.netease.nim.uikit.api.model.session.SessionCustomization;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialog;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.netease.nim.uikit.common.ui.dialog.EasyEditDialog;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.common.ui.widget.SwitchButton;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.FriendServiceObserve;
import com.netease.nimlib.sdk.friend.constant.FriendFieldEnum;
import com.netease.nimlib.sdk.friend.constant.VerifyType;
import com.netease.nimlib.sdk.friend.model.AddFriendData;
import com.netease.nimlib.sdk.friend.model.MuteListChangedNotify;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.uinfo.constant.GenderEnum;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.zwonline.top28.R;
import com.zwonline.top28.activity.CommentDetailsActivity;
import com.zwonline.top28.activity.HomePageActivity;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.nim.DemoCache;
import com.zwonline.top28.nim.contact.constant.UserConstant;
import com.zwonline.top28.nim.contact.helper.UserUpdateHelper;
import com.zwonline.top28.nim.main.model.Extras;
import com.zwonline.top28.nim.session.SessionHelper;
import com.zwonline.top28.nim.shangjibi.SJBAction;
import com.zwonline.top28.nim.yangfen.YangFenAction;
import com.zwonline.top28.presenter.RecordUserBehavior;
import com.zwonline.top28.tip.toast.ToastUtil;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户资料页面
 * Created by huangjun on 2015/8/11.
 */
public class UserProfileActivity extends UI {

    private static final String TAG = UserProfileActivity.class.getSimpleName();

    private final boolean FLAG_ADD_FRIEND_DIRECTLY = true; // 是否直接加为好友开关，false为需要好友申请
    private final String KEY_BLACK_LIST = "black_list";
    private final String KEY_MSG_NOTICE = "msg_notice";
    private static final String EXTRA_KEY = "EXTRA_KEY";
    private static final String EXTRA_DATA = "EXTRA_DATA";
    public static final int REQUEST_CODE = 1000;
    private String account;

    // 基本信息
    private HeadImageView headImageView;
    private TextView nameText;
    private ImageView genderImage;
    private TextView accountText;
    private TextView birthdayText;
    private TextView mobileText;
    private TextView emailText;
    private TextView signatureText;
    private RelativeLayout birthdayLayout;
    private RelativeLayout phoneLayout;
    private RelativeLayout emailLayout;
    private RelativeLayout signatureLayout;
    private RelativeLayout aliasLayout;
    private TextView nickText;
    private TextView source;

    // 开关
    private ViewGroup toggleLayout;
    private Button addFriendBtn;
    private Button removeFriendBtn;
    private Button chatBtn;
    private SwitchButton blackSwitch;
    private SwitchButton noticeSwitch;
    private Map<String, Boolean> toggleStateMap;
    private String signature;//签名
    private String nickname;
    private int key;
    private String data;
    private Map<Integer, UserInfoFieldEnum> fieldMap;
    private EditText remarksEt;
    private SharedPreferencesUtils sp;
    private String has_permission;
    private String has_boc_permission;

    public static void start(Context context, String account) {
        Intent intent = new Intent();
        intent.setClass(context, UserProfileActivity.class);
        intent.putExtra(Extras.EXTRA_ACCOUNT, account);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static final void startActivity(Context context, int key, String data) {
        Intent intent = new Intent();
        intent.setClass(context, UserProfileEditItemActivity.class);
        intent.putExtra(EXTRA_KEY, key);
        intent.putExtra(EXTRA_DATA, data);
        ((Activity) context).startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_activity);
        parseIntent();
        if (key == UserConstant.KEY_NICKNAME || key == UserConstant.KEY_PHONE || key == UserConstant.KEY_EMAIL
                || key == UserConstant.KEY_SIGNATURE || key == UserConstant.KEY_ALIAS) {
            setContentView(R.layout.user_profile_edittext_layout);
        }
        ToolBarOptions options = new NimToolBarOptions();
        options.titleId = R.string.user_profile;
        setToolBar(R.id.toolbar, options);
        Intent intent = getIntent();
        nickname = intent.getStringExtra("nickname");
        signature = intent.getStringExtra("signature");
        account = intent.getStringExtra(Extras.EXTRA_ACCOUNT);
        sp = SharedPreferencesUtils.getUtil();
        has_boc_permission = (String) sp.getKey(getApplicationContext(), "has_boc_permission", "");
        has_permission = (String) sp.getKey(getApplicationContext(), "has_permission", "");
        initActionbar();

        findViews();
        registerObserver(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateUserInfo();
        updateToggleView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerObserver(false);
    }

    private void registerObserver(boolean register) {
        NimUIKit.getContactChangedObservable().registerObserver(friendDataChangedObserver, register);
        NIMClient.getService(FriendServiceObserve.class).observeMuteListChangedNotify(muteListChangedNotifyObserver, register);
    }

    Observer<MuteListChangedNotify> muteListChangedNotifyObserver = new Observer<MuteListChangedNotify>() {
        @Override
        public void onEvent(MuteListChangedNotify notify) {
            setToggleBtn(noticeSwitch, !notify.isMute());
        }
    };

    ContactChangedObserver friendDataChangedObserver = new ContactChangedObserver() {
        @Override
        public void onAddedOrUpdatedFriends(List<String> account) {
            updateUserOperatorView();
        }

        @Override
        public void onDeletedFriends(List<String> account) {
            updateUserOperatorView();
        }

        @Override
        public void onAddUserToBlackList(List<String> account) {
            updateUserOperatorView();
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> account) {
            updateUserOperatorView();
        }
    };

    private void findViews() {

        headImageView = findView(R.id.user_head_image);
        nameText = findView(R.id.user_name);
        genderImage = findView(R.id.gender_img);
        accountText = findView(R.id.user_account);
        toggleLayout = findView(R.id.toggle_layout);
        addFriendBtn = findView(R.id.add_buddy);
        chatBtn = findView(R.id.begin_chat);
        removeFriendBtn = findView(R.id.remove_buddy);
        birthdayLayout = findView(R.id.birthday);
        nickText = findView(R.id.user_nick);
        birthdayText = (TextView) birthdayLayout.findViewById(R.id.value);
        phoneLayout = findView(R.id.phone);
        mobileText = (TextView) phoneLayout.findViewById(R.id.value);
        emailLayout = findView(R.id.email);
        emailText = (TextView) emailLayout.findViewById(R.id.value);
        signatureLayout = findView(R.id.signature);
        source = signatureLayout.findViewById(R.id.source);//来源
        signatureText = (TextView) signatureLayout.findViewById(R.id.value);
        aliasLayout = findView(R.id.alias);
//        ((TextView) birthdayLayout.findViewById(R.id.attribute)).setText(R.string.birthday);
//        ((TextView) phoneLayout.findViewById(R.id.attribute)).setText(R.string.phone);
        ((TextView) emailLayout.findViewById(R.id.attribute)).setText(R.string.email);
//        ((TextView) signatureLayout.findViewById(R.id.attribute)).setText(R.string.signature);
        ((TextView) aliasLayout.findViewById(R.id.attribute)).setText(R.string.alias);
        ((TextView) signatureLayout.findViewById(R.id.attribute)).setText("来源");
        ((TextView) emailLayout.findViewById(R.id.attribute)).setText("查看个人主页");
        emailLayout.findViewById(R.id.arrow_right).setVisibility(View.VISIBLE);
        remarksEt = aliasLayout.findViewById(R.id.remarks_et);//输入备注名
        addFriendBtn.setOnClickListener(onClickListener);
        source.setText("个人名片");
        source.setVisibility(View.VISIBLE);
        chatBtn.setOnClickListener(onClickListener);
        removeFriendBtn.setOnClickListener(onClickListener);
        emailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, HomePageActivity.class);
                intent.putExtra("uid", account);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
            }
        });
        //修改备注
        remarksEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String comment = remarksEt.getText().toString().trim();
                if (StringUtil.isNotEmpty(comment)) {
                    if (actionId == EditorInfo.IME_ACTION_SEND) {
                        if (!StringUtil.isEmpty(remarksEt.getText().toString().trim())) {
                            update(remarksEt.getText().toString().trim());


                            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                                    hideSoftInputFromWindow(UserProfileActivity.this.getCurrentFocus().
                                            getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                        } else {
                            ToastUtil.showToast(UserProfileActivity.this, "请输入内容！");
                        }
                        return true;
                    }

                } else {
                    ToastUtils.showToast(UserProfileActivity.this, "输入内容不能为空！");
                }


                return false;
            }
        });
    }

    private void initActionbar() {
        TextView toolbarView = findView(R.id.action_bar_right_clickable_textview);
//        if (!DemoCache.getAccount().equals(account)) {
//            toolbarView.setVisibility(View.GONE);
//            return;
//        } else {
//            toolbarView.setVisibility(View.VISIBLE);
//        }
        toolbarView.setText(R.string.edit);
        toolbarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfileSettingActivity.start(UserProfileActivity.this, account);
            }
        });
    }

    private void addToggleBtn(boolean black, boolean notice) {
        blackSwitch = addToggleItemView(KEY_BLACK_LIST, R.string.black_list, black);
        noticeSwitch = addToggleItemView(KEY_MSG_NOTICE, R.string.msg_notice, notice);
    }

    private void setToggleBtn(SwitchButton btn, boolean isChecked) {
        btn.setCheck(isChecked);
    }

    private void updateUserInfo() {
        if (NimUIKit.getUserInfoProvider().getUserInfo(account) != null) {
            updateUserInfoView();
            return;
        }

        NimUIKit.getUserInfoProvider().getUserInfoAsync(account, new SimpleCallback<NimUserInfo>() {

            @Override
            public void onResult(boolean success, NimUserInfo result, int code) {
                updateUserInfoView();
            }
        });
    }

    private void updateUserInfoView() {
//        accountText.setText("帐号：" + account);
        accountText.setText(signature);
        headImageView.loadBuddyAvatar(account);

//        if (DemoCache.getAccount().equals(account)) {
//            nameText.setText(UserInfoHelper.getUserName(account));
        nameText.setText(nickname);
//        }

        final NimUserInfo userInfo = (NimUserInfo) NimUIKit.getUserInfoProvider().getUserInfo(account);
        if (userInfo == null) {
            LogUtil.e(TAG, "userInfo is null when updateUserInfoView");
            return;
        }

        if (userInfo.getGenderEnum() == GenderEnum.MALE) {
            genderImage.setVisibility(View.VISIBLE);
            genderImage.setBackgroundResource(R.drawable.nim_male);
        } else if (userInfo.getGenderEnum() == GenderEnum.FEMALE) {
            genderImage.setVisibility(View.VISIBLE);
            genderImage.setBackgroundResource(R.drawable.nim_female);
        } else {
            genderImage.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(userInfo.getBirthday())) {
            birthdayLayout.setVisibility(View.VISIBLE);
            birthdayText.setText(userInfo.getBirthday());
        } else {
            birthdayLayout.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(userInfo.getMobile())) {
            phoneLayout.setVisibility(View.GONE);
            mobileText.setText(userInfo.getMobile());
        } else {
            phoneLayout.setVisibility(View.GONE);
        }

//        if (!TextUtils.isEmpty(userInfo.getEmail())) {
//            emailLayout.setVisibility(View.VISIBLE);
//            emailText.setText("查看个人主页");
//        } else {
//            emailLayout.setVisibility(View.GONE);
//        }

//        if (!TextUtils.isEmpty(userInfo.getSignature())) {
//            signatureLayout.setVisibility(View.VISIBLE);
//            signatureText.setText(userInfo.getSignature());
//        } else {
//            signatureLayout.setVisibility(View.GONE);
//        }

    }

    private void updateUserOperatorView() {

        if (NIMClient.getService(FriendService.class).isMyFriend(account)) {
            removeFriendBtn.setVisibility(View.VISIBLE);
            addFriendBtn.setVisibility(View.GONE);
            chatBtn.setVisibility(View.VISIBLE);
            updateAlias(true);
        } else {
            addFriendBtn.setVisibility(View.VISIBLE);
            removeFriendBtn.setVisibility(View.GONE);
            chatBtn.setVisibility(View.GONE);
            updateAlias(false);
        }
    }

    private void updateToggleView() {
//        if (DemoCache.getAccount() != null && !DemoCache.getAccount().equals(account)) {
//            boolean black = NIMClient.getService(FriendService.class).isInBlackList(account);
//            boolean notice = NIMClient.getService(FriendService.class).isNeedMessageNotify(account);
//            if (blackSwitch == null || noticeSwitch == null) {
//                addToggleBtn(black, notice);
//            } else {
//                setToggleBtn(blackSwitch, black);
//                setToggleBtn(noticeSwitch, notice);
//            }
//            Log.i(TAG, "black=" + black + ", notice=" + notice);
        updateUserOperatorView();
//        }
    }

    private SwitchButton addToggleItemView(String key, int titleResId, boolean initState) {
        ViewGroup vp = (ViewGroup) getLayoutInflater().inflate(R.layout.nim_user_profile_toggle_item, null);
        ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.isetting_item_height));
        vp.setLayoutParams(vlp);

        TextView titleText = ((TextView) vp.findViewById(R.id.user_profile_title));
        titleText.setText(titleResId);

        SwitchButton switchButton = (SwitchButton) vp.findViewById(R.id.user_profile_toggle);
        switchButton.setCheck(initState);
        switchButton.setOnChangedListener(onChangedListener);
        switchButton.setTag(key);

        toggleLayout.addView(vp);

        if (toggleStateMap == null) {
            toggleStateMap = new HashMap<>();
        }
        toggleStateMap.put(key, initState);
        return switchButton;
    }

    private void updateAlias(boolean isFriend) {
        if (isFriend) {
            aliasLayout.setVisibility(View.VISIBLE);
            remarksEt.setVisibility(View.VISIBLE);
            String alias = NimUIKit.getContactProvider().getAlias(account);
            String name = UserInfoHelper.getUserName(account);
            nameText.setVisibility(View.VISIBLE);
            nameText.setText(name);

            if (!TextUtils.isEmpty(alias)) {
                nickText.setVisibility(View.VISIBLE);
                remarksEt.setText(alias);
//                nickText.setText("昵称：" + name);
            } else {
                remarksEt.setHint("请输入备注名");
            }
        } else {
            aliasLayout.setVisibility(View.GONE);
            remarksEt.setVisibility(View.GONE);
            aliasLayout.findViewById(R.id.arrow_right).setVisibility(View.GONE);
            nickText.setVisibility(View.GONE);
            nameText.setText(UserInfoHelper.getUserName(account));
        }
    }

    private SwitchButton.OnChangedListener onChangedListener = new SwitchButton.OnChangedListener() {
        @Override
        public void OnChanged(View v, final boolean checkState) {
            final String key = (String) v.getTag();
            if (!NetworkUtil.isNetAvailable(UserProfileActivity.this)) {
                Toast.makeText(UserProfileActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                if (key.equals(KEY_BLACK_LIST)) {
                    blackSwitch.setCheck(!checkState);
                } else if (key.equals(KEY_MSG_NOTICE)) {
                    noticeSwitch.setCheck(!checkState);
                }
                return;
            }

            updateStateMap(checkState, key);

            if (key.equals(KEY_BLACK_LIST)) {
                if (checkState) {
                    NIMClient.getService(FriendService.class).addToBlackList(account).setCallback(new RequestCallback<Void>() {
                        @Override
                        public void onSuccess(Void param) {
                            Toast.makeText(UserProfileActivity.this, "加入黑名单成功", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailed(int code) {
                            if (code == 408) {
                                Toast.makeText(UserProfileActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(UserProfileActivity.this, "on failed：" + code, Toast.LENGTH_SHORT).show();
                            }
                            updateStateMap(!checkState, key);
                            blackSwitch.setCheck(!checkState);
                        }

                        @Override
                        public void onException(Throwable exception) {

                        }
                    });
                } else {
                    NIMClient.getService(FriendService.class).removeFromBlackList(account).setCallback(new RequestCallback<Void>() {
                        @Override
                        public void onSuccess(Void param) {
                            Toast.makeText(UserProfileActivity.this, "移除黑名单成功", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailed(int code) {
                            if (code == 408) {
                                Toast.makeText(UserProfileActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(UserProfileActivity.this, "on failed:" + code, Toast.LENGTH_SHORT).show();
                            }
                            updateStateMap(!checkState, key);
                            blackSwitch.setCheck(!checkState);
                        }

                        @Override
                        public void onException(Throwable exception) {

                        }
                    });
                }
            } else if (key.equals(KEY_MSG_NOTICE)) {
                NIMClient.getService(FriendService.class).setMessageNotify(account, checkState).setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        if (checkState) {
                            Toast.makeText(UserProfileActivity.this, "开启消息提醒成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UserProfileActivity.this, "关闭消息提醒成功", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailed(int code) {
                        if (code == 408) {
                            Toast.makeText(UserProfileActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UserProfileActivity.this, "on failed:" + code, Toast.LENGTH_SHORT).show();
                        }
                        updateStateMap(!checkState, key);
                        noticeSwitch.setCheck(!checkState);
                    }

                    @Override
                    public void onException(Throwable exception) {

                    }
                });
            }
        }
    };

    private void updateStateMap(boolean checkState, String key) {
        if (toggleStateMap.containsKey(key)) {
            toggleStateMap.put(key, checkState);  // update state
            Log.i(TAG, "toggle " + key + "to " + checkState);
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == addFriendBtn) {
//                if (FLAG_ADD_FRIEND_DIRECTLY) {
//                    doAddFriend(null, false);  // 直接加为好友
//                } else {
                onAddFriendByVerify(); // 发起好友验证请求
//                }
            } else if (v == removeFriendBtn) {
                onRemoveFriend();
            } else if (v == chatBtn) {
                onChat();
            }
        }
    };

    /**
     * 通过验证方式添加好友
     */
    private void onAddFriendByVerify() {
        final EasyEditDialog requestDialog = new EasyEditDialog(this);
        requestDialog.setEditTextMaxLength(32);
        requestDialog.setTitle(getString(R.string.add_friend_verify_tip));
        requestDialog.addNegativeButtonListener(R.string.cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDialog.dismiss();
            }
        });
        requestDialog.addPositiveButtonListener(R.string.send, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDialog.dismiss();
                String msg = requestDialog.getEditMessage();
                doAddFriend(msg, false);
            }
        });
        requestDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
        requestDialog.show();
    }

    private void doAddFriend(String msg, boolean addDirectly) {
        if (!NetworkUtil.isNetAvailable(this)) {
            Toast.makeText(UserProfileActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.isEmpty(account) && account.equals(DemoCache.getAccount())) {
            Toast.makeText(UserProfileActivity.this, "不能加自己为好友", Toast.LENGTH_SHORT).show();
            return;
        }
        final VerifyType verifyType = addDirectly ? VerifyType.DIRECT_ADD : VerifyType.VERIFY_REQUEST;
        DialogMaker.showProgressDialog(this, "", true);
        NIMClient.getService(FriendService.class).addFriend(new AddFriendData(account, verifyType, msg))
                .setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        DialogMaker.dismissProgressDialog();
                        updateUserOperatorView();
                        if (VerifyType.DIRECT_ADD == verifyType) {
                            Toast.makeText(UserProfileActivity.this, "添加好友成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UserProfileActivity.this, "添加好友请求发送成功", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailed(int code) {
                        DialogMaker.dismissProgressDialog();
                        if (code == 408) {
                            Toast.makeText(UserProfileActivity.this, R.string.network_is_not_available, Toast
                                    .LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UserProfileActivity.this, "自己不能添加自己", Toast
                                    .LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onException(Throwable exception) {
                        DialogMaker.dismissProgressDialog();
                    }
                });

        Log.i(TAG, "onAddFriendByVerify");
    }

    private void onRemoveFriend() {
        Log.i(TAG, "onRemoveFriend");
        if (!NetworkUtil.isNetAvailable(this)) {
            Toast.makeText(UserProfileActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
            return;
        }
        EasyAlertDialog dialog = EasyAlertDialogHelper.createOkCancelDiolag(this, getString(R.string.remove_friend),
                getString(R.string.remove_friend_tip), true,
                new EasyAlertDialogHelper.OnDialogActionListener() {

                    @Override
                    public void doCancelAction() {

                    }

                    @Override
                    public void doOkAction() {
                        DialogMaker.showProgressDialog(UserProfileActivity.this, "", true);
                        NIMClient.getService(FriendService.class).deleteFriend(account).setCallback(new RequestCallback<Void>() {
                            @Override
                            public void onSuccess(Void param) {
                                DialogMaker.dismissProgressDialog();
                                Toast.makeText(UserProfileActivity.this, R.string.remove_friend_success, Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onFailed(int code) {
                                DialogMaker.dismissProgressDialog();
                                if (code == 408) {
                                    Toast.makeText(UserProfileActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(UserProfileActivity.this, "on failed:" + code, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onException(Throwable exception) {
                                DialogMaker.dismissProgressDialog();
                            }
                        });
                    }
                });
        if (!isFinishing() && !isDestroyedCompatible()) {
            dialog.show();
        }
    }

    /**
     * 聊天
     */
    private void onChat() {
        //红包权限
        SessionCustomization sessionCustomization = new SessionCustomization();
        ArrayList<BaseAction> actions = new ArrayList<>();
        if (StringUtil.isEmpty(has_permission) || has_permission.equals(BizConstant.ENTERPRISE_tRUE)) {
            actions.remove(new YangFenAction());
        } else if (has_permission.equals(BizConstant.ALREADY_FAVORITE)) {
            actions.add(new YangFenAction());
        }
        //商机币红包权限
        if (StringUtil.isNotEmpty(has_permission) && has_boc_permission.equals(BizConstant.TYPE_ONE)) {
            actions.add(new SJBAction());
        } else {
            actions.remove(new SJBAction());
        }
        sessionCustomization.actions = actions;
        NimUIKit.startChatting(getApplicationContext(), account, SessionTypeEnum.P2P, sessionCustomization, null);
        //单聊
        SessionHelper.startP2PSession(this, account);
    }

    private void update(Serializable content) {
        RequestCallbackWrapper callback = new RequestCallbackWrapper() {
            @Override
            public void onResult(int code, Object result, Throwable exception) {
                DialogMaker.dismissProgressDialog();
                if (code == ResponseCode.RES_SUCCESS) {
                    onUpdateCompleted();
                } else if (code == ResponseCode.RES_ETIMEOUT) {
                    Toast.makeText(UserProfileActivity.this, R.string.user_info_update_failed, Toast.LENGTH_SHORT).show();
                }
            }
        };

        DialogMaker.showProgressDialog(this, null, true);
        Map<FriendFieldEnum, Object> map = new HashMap<>();
        map.put(FriendFieldEnum.ALIAS, content);
        NIMClient.getService(FriendService.class).updateFriendFields(account, map).setCallback(callback);

    }

    private void onUpdateCompleted() {
        showKeyboard(false);
        Toast.makeText(UserProfileActivity.this, R.string.user_info_update_success, Toast.LENGTH_SHORT).show();
//        finish();
    }

    private void parseIntent() {
        key = getIntent().getIntExtra(EXTRA_KEY, -1);
        data = getIntent().getStringExtra(EXTRA_DATA);
    }

    private class MyDatePickerDialog extends DatePickerDialog {
        private int maxYear = 2015;
        private int minYear = 1900;
        private int currYear;
        private int currMonthOfYear;
        private int currDayOfMonth;

        public MyDatePickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
            super(context, callBack, year, monthOfYear, dayOfMonth);
            currYear = year;
            currMonthOfYear = monthOfYear;
            currDayOfMonth = dayOfMonth;
        }

        @Override
        public void onDateChanged(DatePicker view, int year, int month, int day) {
            if (year >= minYear && year <= maxYear) {
                currYear = year;
                currMonthOfYear = month;
                currDayOfMonth = day;
            } else {
                if (currYear > maxYear) {
                    currYear = maxYear;
                } else if (currYear < minYear) {
                    currYear = minYear;
                }
                updateDate(currYear, currMonthOfYear, currDayOfMonth);
            }
        }

        public void setMaxYear(int year) {
            maxYear = year;
        }

        public void setMinYear(int year) {
            minYear = year;
        }

        public void setTitle(CharSequence title) {
            super.setTitle("生 日");
        }
    }
}
