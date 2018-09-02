package com.zwonline.top28.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.netease.nim.uikit.api.NimUIKit;
import com.zwonline.top28.R;
import com.zwonline.top28.adapter.CompanyAdapter;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.AmountPointsBean;
import com.zwonline.top28.bean.CompanyBean;
import com.zwonline.top28.bean.ExamineChatBean;
import com.zwonline.top28.bean.MyIssueBean;
import com.zwonline.top28.bean.message.MessageFollow;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.CompanyPresenter;
import com.zwonline.top28.utils.ImageViewPlus;
import com.zwonline.top28.utils.ScrollLinearLayoutManager;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.ICompanyActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.OnClick;

/**
 * 描述：企业主页
 *
 * @author YSG
 * @date 2018/1/17
 */
public class CompanyActivity extends BaseActivity<ICompanyActivity, CompanyPresenter> implements ICompanyActivity {
    private String uid;
    //    private String uids;
    private String token;
    private CompanyAdapter adapter;
    private String nickname;
    private String tel;
    private String did_attention;
    private MessageFollow messageFollow;
    private int currentNum;
    private SharedPreferencesUtils sp;
    private RelativeLayout back;
    private TextView title;
    private RelativeLayout relative;
    private TextView tvGuanzhu;
    private TextView tvGuanzhuNum;
    private TextView tvFensiNum;
    private TextView tvFensi;
    private TextView articleNum;
    private TextView article;
    private TextView companyName;
    private LinearLayout imageLinear;
    private RecyclerView recyclerview;
    private TextView attention;
    private RelativeLayout phone;
    private ImageViewPlus imageHea;
    private TextView name;
    private TextView tvTitle;
    private RelativeLayout user;
    private String chatted;//是否聊过天
    private String costPoint;//消耗积分数量
    private String pid;//项目id
    private String kid;
    private String login="1";
    private boolean islogins;
    @Override
    protected void init() {
        initData();//查找控件
        sp = SharedPreferencesUtils.getUtil();
        islogins = (boolean) sp.getKey(this, "islogin", false);
        token = (String) sp.getKey(this, "doalog", "");
//        ToastUtils.showToast(this, "token==" + token);
        messageFollow = new MessageFollow();
        Intent intent = getIntent();
        pid = intent.getStringExtra("pid");
//        uid = intent.getStringExtra("uid");
        System.out.print("uid===xxx" + uid);
        presenter.mCompany(this, pid);

    }

    //查找控件
    private void initData() {
        back = (RelativeLayout) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        relative = (RelativeLayout) findViewById(R.id.relative);
        tvGuanzhu = (TextView) findViewById(R.id.tv_guanzhu);
        tvGuanzhuNum = (TextView) findViewById(R.id.tv_guanzhu_num);
        tvFensiNum = (TextView) findViewById(R.id.tv_fensi_num);
        tvFensi = (TextView) findViewById(R.id.tv_fensi);
        articleNum = (TextView) findViewById(R.id.article_num);
        article = (TextView) findViewById(R.id.article);
        companyName = (TextView) findViewById(R.id.company_name);
        imageLinear = (LinearLayout) findViewById(R.id.image_linear);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        attention = (TextView) findViewById(R.id.attention);
        phone = (RelativeLayout) findViewById(R.id.phone);
        imageHea = (ImageViewPlus) findViewById(R.id.image_hea);
        name = (TextView) findViewById(R.id.name);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        user = (RelativeLayout) findViewById(R.id.user);

    }

    @Override
    protected CompanyPresenter getPresenter() {
        return new CompanyPresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_company;
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.mCompany(this, pid);
        presenter.mExamineChat(this, uid);//检查是否聊过天
    }

    @Override
    public void showCompany(CompanyBean companyBean) {

        nickname = companyBean.data.post_page.get(0).title;
        uid = companyBean.data.kefu_info.uid;
        presenter.mCompany_Article(this, uid, BizConstant.PAGE);
        presenter.mExamineChat(this, uid);//检查是否聊过天
        title.setText(nickname);
        companyName.setText(nickname);
        did_attention = companyBean.data.did_i_follow;
        tel = companyBean.data.contact_tel;
        tvFensiNum.setText(companyBean.data.fans_count);
        tvGuanzhuNum.setText(companyBean.data.follow_count);
        articleNum.setText(companyBean.data.article_count);
        imageLinear.removeAllViews();//清理所有view
        kid = companyBean.data.kefu_info.uid;
        RequestOptions options = new RequestOptions().error(R.mipmap.no_photo_male).placeholder(R.mipmap.no_photo_male);
        Glide.with(getApplicationContext()).load(companyBean.data.kefu_info.avatars).apply(options).into(imageHea);
        name.setText(companyBean.data.kefu_info.nickname);
        tvTitle.setText(companyBean.data.post_page.get(0).title);
        //动态添加imageview
        for (int i = 0; i < companyBean.data.post_page.get(0).img_path.size(); i++) {
            System.out.print("imagelinear===" + companyBean.data.post_page.get(0).img_path.get(i));
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));  //设置图片宽高
            Glide.with(this).load(companyBean.data.post_page.get(0).img_path.get(i)).into(imageView); //图片资源
            imageLinear.addView(imageView); //动态添加图片
        }
        if (TextUtils.isEmpty(did_attention)) {
            Toast.makeText(this, R.string.get_fail_tip, Toast.LENGTH_SHORT).show();
        } else {

            if (!StringUtil.isEmpty(did_attention) && did_attention.equals("0")) {
                attention.setText(R.string.common_btn_add_focus);
//            guanzhu.setBackgroundResource(R.drawable.rectangle_shape_red);
//            guanzhu.setTextColor(Color.parseColor("#FF0000"));
            } else if (!StringUtil.isEmpty(did_attention) && did_attention.equals("1")) {
                attention.setText(R.string.common_followed);
//            guanzhu.setBackgroundResource(R.drawable.rectangle_shape_gay);
//            guanzhu.setTextColor(Color.parseColor("#DDDDDD"));
            }
        }
    }

    @Override
    public void showColmpanyArticle(final List<MyIssueBean.DataBean> articleList) {
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        ScrollLinearLayoutManager scrollLinearLayoutManager = new ScrollLinearLayoutManager(CompanyActivity.this);
        scrollLinearLayoutManager.setScrollEnabled(false);
        recyclerview.setLayoutManager(scrollLinearLayoutManager);
        adapter = new CompanyAdapter(articleList, this);
        recyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnClickItemListener(new CompanyAdapter.OnClickItemListener() {
            @Override
            public void setOnItemClick(int position) {

                Intent intent = new Intent(CompanyActivity.this, HomeDetailsActivity.class);
                intent.putExtra("id", articleList.get(position).id + "");
                intent.putExtra("title", articleList.get(position).title);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
            }
        });
    }

    @Override
    public void showErro() {
//        if (this == null) {
//            return;
//        }
        Toast.makeText(CompanyActivity.this, R.string.data_loading, Toast.LENGTH_SHORT).show();
    }


    @OnClick({R.id.back, R.id.tv_guanzhu, R.id.tv_fensi, R.id.article, R.id.attention, R.id.phone, R.id.user})
    public void onViewClicked(View view) {
        if (AntiShake.check(view.getId())) {    //判断是否多次点击
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.tv_guanzhu:
                Intent intent_guan = new Intent(CompanyActivity.this, MyAttentionActivity.class);
                intent_guan.putExtra("uid", uid);
                intent_guan.putExtra("attention", getString(R.string.myis_followed, nickname));
                startActivity(intent_guan);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.tv_fensi:
                Intent intent_fans = new Intent(CompanyActivity.this, MyFansActivity.class);
                intent_fans.putExtra("uid", uid);
                intent_fans.putExtra("fans", getString(R.string.myis_fans, nickname));
                startActivity(intent_fans);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.article:
                Intent intent_article = new Intent(CompanyActivity.this, MyIssueActivity.class);
                intent_article.putExtra("uid", uid);
                intent_article.putExtra("issue", getString(R.string.myis_article, nickname));
                startActivity(intent_article);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.attention:
                if (islogins) {
                    if (!StringUtil.isEmpty(attention.getText().toString()) && attention.getText().toString().equals(getString(R.string.common_btn_add_focus))) {
//                    presenter.mAttention(this, "follow", uid);
                        showNormalDialogFollow();
                        attention.setText(R.string.common_followed);
                        currentNum = Integer.parseInt((String) sp.getKey(CompanyActivity.this, "follow", "0")) + 1;
                        messageFollow.followNum = currentNum + "";
                        sp.insertKey(CompanyActivity.this, "follow", messageFollow.followNum);
                        EventBus.getDefault().post(messageFollow);
                    } else {
                        presenter.mAttention(this, "un_follow", pid,"");
                        attention.setText(R.string.common_btn_add_focus);
                        currentNum = Integer.parseInt((String) sp.getKey(CompanyActivity.this, "follow", "0")) - 1;
                        messageFollow.followNum = currentNum + "";
                        sp.insertKey(CompanyActivity.this, "follow", messageFollow.followNum);
                        EventBus.getDefault().post(messageFollow);
                    }
                } else {
                    Toast.makeText(CompanyActivity.this, R.string.user_not_login, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CompanyActivity.this, WithoutCodeLoginActivity.class));
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                }

                break;
            case R.id.phone:
                showNormalDialog();
                break;
            case R.id.user:
                if (islogins) {
                    if (!StringUtil.isEmpty(chatted) && chatted.equals(BizConstant.ATTENTION_CHATTTE)) {
                        presenter.mOnLineChat(this, uid, pid, kid);
                        // 打开单聊界面
                        NimUIKit.startP2PSession(this, uid);
                        overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                    } else if (!StringUtil.isEmpty(chatted) && chatted.equals(BizConstant.NO_ATTENTION_CHATTTE)) {
                        showNormalDialogs();
                    }
                } else {
                    Toast.makeText(CompanyActivity.this, R.string.user_not_login, Toast.LENGTH_SHORT).show();
                    Intent imItent = new Intent(CompanyActivity.this, WithoutCodeLoginActivity.class);
                    imItent.putExtra("login",login);
                    startActivity(imItent);
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                }

                break;
        }
    }

    //检查是否聊过天
    @Override
    public void showExamineChat(ExamineChatBean.DataBean examineList) {
        chatted = examineList.chatted;
        costPoint = examineList.cost_point;
    }

    //在线聊天
    @Override
    public void showOnLineChat(AmountPointsBean amountPointsBean) {

    }

    //呼叫电话
    private void showNormalDialog() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(CompanyActivity.this);
        normalDialog.setMessage(tel);
        normalDialog.setPositiveButton(R.string.mycenter_call,
                new DialogInterface.OnClickListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intentPhone = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
                        startActivity(intentPhone);
                    }
                });
        normalDialog.setNegativeButton(R.string.common_cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();
    }

    //Dialog弹窗聊天
    private void showNormalDialogs() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(CompanyActivity.this);
        normalDialog.setMessage(getString(R.string.consume_integral) + costPoint + getString(R.string.coin_bole_coin));
        normalDialog.setPositiveButton(R.string.chat,
                new DialogInterface.OnClickListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.mOnLineChat(CompanyActivity.this, uid, pid, kid);
                        // 打开单聊界面
                        NimUIKit.startP2PSession(CompanyActivity.this, uid);
                        overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                    }
                });
        normalDialog.setNegativeButton(R.string.common_cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();
    }

    //关注是否愿意接听电话
    private void showNormalDialogFollow() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(CompanyActivity.this);
        normalDialog.setMessage(getString(R.string.is_willing_answer_calls));
        normalDialog.setPositiveButton(getString(R.string.willing),
                new DialogInterface.OnClickListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.mAttention(CompanyActivity.this, "follow", pid, BizConstant.ALREADY_FAVORITE);

                    }
                });
        normalDialog.setNegativeButton(getString(R.string.unwillingness),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.mAttention(CompanyActivity.this, "follow", String.valueOf(pid), BizConstant.NO_FAVORITE);
                    }
                });
        // 显示
        normalDialog.show();
    }
}
