package com.zwonline.top28.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialog;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.netease.nim.uikit.common.ui.dialog.EasyEditDialog;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.constant.VerifyType;
import com.netease.nimlib.sdk.friend.model.AddFriendData;
import com.zwonline.top28.R;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.nim.DemoCache;
import com.zwonline.top28.utils.ImageViewPlus;
import com.zwonline.top28.utils.StringUtil;

public class FriendProfileActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout back;
    private TextView friendTitle;
    private ImageViewPlus friendHead;
    private TextView userName;
    private TextView friendSign;
    private EditText remarksEt;
    private TextView source;
    private Button sendMessage;
    private Button deleteFriend;
    private String uid;
    private String nickname;
    private String avatars;
    private String signature;

    @Override
    protected void init() {
        Intent intent = getIntent();
        uid = intent.getStringExtra("account");
        nickname = intent.getStringExtra("nickname");
        avatars = intent.getStringExtra("avatars");
        signature = intent.getStringExtra("signature");
        initView();
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_friend_profile;
    }

    private void initView() {
        back = (RelativeLayout) findViewById(R.id.back);
        friendTitle = (TextView) findViewById(R.id.friend_title);
        friendHead = (ImageViewPlus) findViewById(R.id.friend_head);
        userName = (TextView) findViewById(R.id.user_name);
        friendSign = (TextView) findViewById(R.id.friend_sign);
        remarksEt = (EditText) findViewById(R.id.remarks_et);
        source = (TextView) findViewById(R.id.source);
        sendMessage = (Button) findViewById(R.id.send_message);
        deleteFriend = (Button) findViewById(R.id.delete_friend);
        if (StringUtil.isNotEmpty(nickname)){
            userName.setText(nickname);
        }
        if (StringUtil.isNotEmpty(signature)){
            friendSign.setText(signature);
        }
        if (StringUtil.isNotEmpty(avatars)){
            RequestOptions options=new RequestOptions().placeholder(R.mipmap.no_photo_male).error(R.mipmap.no_photo_male);
            Glide.with(getApplicationContext()).load(avatars).apply(options).into(friendHead);
        }
        sendMessage.setOnClickListener(this);
        deleteFriend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_message:
                onAddFriendByVerify(); // 发起好友验证请求

                break;
            case R.id.delete_friend:
                onRemoveFriend();
                break;
        }
    }

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
            Toast.makeText(FriendProfileActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.isEmpty(uid) && uid.equals(DemoCache.getAccount())) {
            Toast.makeText(FriendProfileActivity.this, "不能加自己为好友", Toast.LENGTH_SHORT).show();
            return;
        }
        final VerifyType verifyType = addDirectly ? VerifyType.DIRECT_ADD : VerifyType.VERIFY_REQUEST;
        DialogMaker.showProgressDialog(this, "", true);
        NIMClient.getService(FriendService.class).addFriend(new AddFriendData(uid, verifyType, msg))
                .setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        DialogMaker.dismissProgressDialog();
                        updateUserOperatorView();
                        if (VerifyType.DIRECT_ADD == verifyType) {
                            Toast.makeText(FriendProfileActivity.this, "添加好友成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(FriendProfileActivity.this, "添加好友请求发送成功", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailed(int code) {
                        DialogMaker.dismissProgressDialog();
                        if (code == 408) {
                            Toast.makeText(FriendProfileActivity.this, R.string.network_is_not_available, Toast
                                    .LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(FriendProfileActivity.this, "on failed:" + code, Toast
                                    .LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onException(Throwable exception) {
                        DialogMaker.dismissProgressDialog();
                    }
                });

    }



    private void updateUserOperatorView() {

        if (NIMClient.getService(FriendService.class).isMyFriend(uid)) {
            sendMessage.setText("发消息");
            deleteFriend.setVisibility(View.VISIBLE);
//            updateAlias(true);
        } else {
            sendMessage.setText("添加好友");
            deleteFriend.setVisibility(View.GONE);
//            updateAlias(false);
        }
    }


    private void onRemoveFriend() {
        if (!NetworkUtil.isNetAvailable(this)) {
            Toast.makeText(FriendProfileActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
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
                        DialogMaker.showProgressDialog(FriendProfileActivity.this, "", true);
                        NIMClient.getService(FriendService.class).deleteFriend(uid).setCallback(new RequestCallback<Void>() {
                            @Override
                            public void onSuccess(Void param) {
                                DialogMaker.dismissProgressDialog();
                                Toast.makeText(FriendProfileActivity.this, R.string.remove_friend_success, Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onFailed(int code) {
                                DialogMaker.dismissProgressDialog();
                                if (code == 408) {
                                    Toast.makeText(FriendProfileActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(FriendProfileActivity.this, "on failed:" + code, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onException(Throwable exception) {
                                DialogMaker.dismissProgressDialog();
                            }
                        });
                    }
                });
        if (!isFinishing()) {
            dialog.show();
        }
    }


}
