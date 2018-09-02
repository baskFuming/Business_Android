package com.zwonline.top28.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.xys.libzxing.zxing.encoding.EncodingUtils;
import com.zwonline.top28.R;
import com.zwonline.top28.adapter.AddFriendAdapter;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.AddFriendBean;
import com.zwonline.top28.bean.MyQRCodeBean;
import com.zwonline.top28.bean.RecommendTeamsBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.nim.contact.activity.UserProfileActivity;
import com.zwonline.top28.nim.session.SessionHelper;
import com.zwonline.top28.presenter.AddFriendPresenter;
import com.zwonline.top28.tip.toast.ToastUtil;
import com.zwonline.top28.utils.ImageViewPlus;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.popwindow.MyQrCodePopwindow;
import com.zwonline.top28.view.IAddFriendActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * 添加好友
 */
public class AddFriendsActivity extends BaseActivity<IAddFriendActivity, AddFriendPresenter> implements IAddFriendActivity {


    private RelativeLayout forRecordBack;
    private RelativeLayout addFriendRelat;
    private EditText addFriendEt;
    private LinearLayout addSaosao;
    private LinearLayout meQrcode;
    private LinearLayout functionLinear;
    private XRecyclerView addFriendXrecy;
    private AddFriendAdapter addFriendAdapter;
    private MyQrCodePopwindow myQrCodePopwindow;
    private SharedPreferencesUtils sp;
    private String nickname;
    private String avatar;
    private String uid;
    private String sign;
    private List<MyQRCodeBean> qrlist;
    private MyQRCodeBean bean;

    @Override
    protected void init() {

        bean = new MyQRCodeBean();
        initView();
    }

    /**
     * 查找控件
     */
    private void initView() {
        sp = SharedPreferencesUtils.getUtil();
        nickname = (String) sp.getKey(this, "nickname", "");
        avatar = (String) sp.getKey(this, "avatar", "");
        uid = (String) sp.getKey(this, "uid", "");
        sign = (String) sp.getKey(this, "sign", "");
        forRecordBack = (RelativeLayout) findViewById(R.id.for_record_back);
        addFriendRelat = (RelativeLayout) findViewById(R.id.add_friend_relat);
        addFriendEt = (EditText) findViewById(R.id.add_friend_et);
        addSaosao = (LinearLayout) findViewById(R.id.add_saosao);
        meQrcode = (LinearLayout) findViewById(R.id.me_qrcode);
        functionLinear = (LinearLayout) findViewById(R.id.function_linear);
        addFriendXrecy = (XRecyclerView) findViewById(R.id.add_friend_xrecy);

        addFriendEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!StringUtil.isEmpty(addFriendEt.getText().toString().trim())) {
                        functionLinear.setVisibility(View.GONE);
                        addFriendXrecy.setVisibility(View.VISIBLE);
                        presenter.mAddFriend(getApplicationContext(), addFriendEt.getText().toString().trim());
//                        addFriendEt.setCursorVisible(false);//光标隐藏
                        overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                    } else {
                        ToastUtil.showToast(getApplicationContext(), "请输入内容！");
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected AddFriendPresenter getPresenter() {
        return new AddFriendPresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_add_friends;
    }

    /**
     * 展示搜索好友的数据
     *
     * @param addFriendList
     */
    @Override
    public void showAddFriend(final List<AddFriendBean.DataBean> addFriendList) {
//        functionLinear.setVisibility(View.GONE);
//        addFriendXrecy.setVisibility(View.VISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        addFriendXrecy.setLayoutManager(linearLayoutManager);
        addFriendAdapter = new AddFriendAdapter(addFriendList, this);
        addFriendXrecy.setAdapter(addFriendAdapter);
        addFriendAdapter.notifyDataSetChanged();
        addFriendAdapter.setOnClickItemListener(new AddFriendAdapter.OnClickItemListener() {
            @Override
            public void setOnItemClick(View view, int position) {
                Intent intent = new Intent(AddFriendsActivity.this, UserProfileActivity.class);
                intent.putExtra("account", addFriendList.get(position - 1).account);
                intent.putExtra("nickname", addFriendList.get(position - 1).nickname);
                intent.putExtra("avatars", addFriendList.get(position - 1).avatars);
                intent.putExtra("signature", addFriendList.get(position - 1).signature);
                startActivity(intent);
//                UserProfileActivity.start(AddFriendsActivity.this, addFriendList.get(position-1).account);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
            }
        });
    }

    /**
     * 没有搜索到好友
     *
     * @param flag
     */
    @Override
    public void noFriend(boolean flag) {

    }

    @Override
    public void showRecommendTeams(List<RecommendTeamsBean.DataBean> recommendList) {

    }

    @OnClick({R.id.for_record_back, R.id.add_saosao, R.id.me_qrcode, R.id.function_linear, R.id.search_group})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.for_record_back:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.add_saosao:
                startActivityForResult(new Intent(AddFriendsActivity.this, CaptureActivity.class), 0);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.me_qrcode://二维码创建
                myQrCodePopwindow = new MyQrCodePopwindow(AddFriendsActivity.this);
                myQrCodePopwindow.showAtLocation(AddFriendsActivity.this.findViewById(R.id.friend_relat), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                View contentView = myQrCodePopwindow.getContentView();
                ImageViewPlus myHead = contentView.findViewById(R.id.my_head);
                TextView myName = contentView.findViewById(R.id.my_name);

                TextView mySign = contentView.findViewById(R.id.my_sign);
                if (StringUtil.isNotEmpty(nickname)) {
                    myName.setText(nickname);
                }
                if (StringUtil.isNotEmpty(sign)) {
                    mySign.setText(sign);
                }
                RequestOptions options = new RequestOptions().placeholder(R.mipmap.no_photo_male).error(R.mipmap.no_photo_male);
                Glide.with(getApplicationContext()).load(avatar).apply(options).into(myHead);
                ImageView myQrCode = contentView.findViewById(R.id.my_qrcode);
                // 位图
                try {
                    qrlist = new ArrayList<>();
                    bean.setQr_Type(BizConstant.TYPE_ONE);
                    bean.setQr_Code(uid);
                    qrlist.add(bean);
                    Gson gson = new Gson();
                    String s = gson.toJson(qrlist);
//                    ToastUtils.showToast(AddFriendsActivity.this, s.substring(1, s.length() - 1));
                    /**
                     * 参数：1.文本 2 3.二维码的宽高 4.二维码中间的那个logo
                     */
                    Bitmap bitmap = EncodingUtils.createQRCode(s.substring(1, s.length() - 1), 1000, 1000,
                            null);
                    // 设置图片
                    myQrCode.setImageBitmap(bitmap);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;

            case R.id.search_group:
                startActivity(new Intent(AddFriendsActivity.this, SearchGroupActivity.class));
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            try {
                String result = data.getExtras().getString("result");
                JSONObject jobj = new JSONObject(result.toString());
                String qrType = jobj.getString("qr_Type");
                String qrCode = jobj.getString("qr_Code");
                //扫一扫加好友，加群回调
                if (StringUtil.isNotEmpty(qrType) && qrType.equals(BizConstant.ALREADY_FAVORITE)) {
                    SessionHelper.query(getApplicationContext(), qrCode);
                } else if (StringUtil.isNotEmpty(qrType) && qrType.equals(BizConstant.ALIPAY_METHOD)) {
                    SessionHelper.queryTeamById(getApplicationContext(), qrCode);
                } else {
                    ToastUtils.showToast(getApplicationContext(), result);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    /**
     * popwindow弹窗
     */
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {//关闭
                case R.id.close:
                    myQrCodePopwindow.dismiss();
                    myQrCodePopwindow.backgroundAlpha(AddFriendsActivity.this, 1f);
                    break;
            }
        }
    };

}
