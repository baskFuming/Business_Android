package com.zwonline.top28.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.zwonline.top28.R;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.LanchScreenBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.LanchScreenPresenter;
import com.zwonline.top28.tip.toast.ToastUtil;
import com.zwonline.top28.utils.CountDownView;
import com.zwonline.top28.utils.CountDownViewUtils;
import com.zwonline.top28.utils.NetUtils;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.view.ILanchScreenActivity;
import com.zwonline.top28.web.BaseWebViewActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 开屏广告Activity
 */
public class SplashViewActivity extends BaseActivity<ILanchScreenActivity, LanchScreenPresenter> implements ILanchScreenActivity {
    private ImageView imageView;
    private ImageView imageviewStart;
    private CountDownView countDownView;
    private RelativeLayout MainRe;
    private RelativeLayout reimage;
    private ImageView imageLoag;
    private TextView skipView;
    private String img_url;
    private String jump_url;
    private int jump_out;
    private List<LanchScreenBean.DataBean> lanchList;
    private SharedPreferencesUtils sp;
    public boolean canJump = false;
    private SharedPreferences startup;
    private boolean isfrist;
    private CountDownViewUtils countDownViewUtils;
    private boolean isAD;//定义是否跳过链接

    @Override
    protected void init() {
        full(true);//判断是否全屏
        presenter.lanchAD(this);
        lanchList = new ArrayList<>();
        sp = SharedPreferencesUtils.getUtil();
        sp.insertKey(getApplicationContext(), "isfer", true);
        startup = getSharedPreferences("startup", 0);
        //这个文件里面的布尔常量名，和它的初始状态，状态为是，则触发下面的方法
        isfrist = startup.getBoolean("isfrist", true);
        initView();
        countView();
    }

    //加载控件
    private void initView() {
        countDownViewUtils = (CountDownViewUtils) findViewById(R.id.cdv_time);
        skipView = (TextView) findViewById(R.id.skip_view);
        imageLoag = (ImageView) findViewById(R.id.app_logo);
        reimage = (RelativeLayout) findViewById(R.id.relative);
        imageviewStart = (ImageView) findViewById(R.id.start);
        imageView = (ImageView) findViewById(R.id.img_background);
        countDownView = (CountDownView) findViewById(R.id.count_down_view);
        MainRe = (RelativeLayout) findViewById(R.id.main_bg);
    }

    //倒计时
    private void countView() {
        countDownView.setCountDownTimerListener(new CountDownView.CountDownTimerListener() {
            @Override
            public void onStartCount() {//开始
            }

            @Override
            public void onChangeCount(int second) {//改变
            }

            @Override
            public void onFinishCount() {//结束   TODO 倒计时五秒结束方法
                if (!isAD) {
                    Intent intent = new Intent(SplashViewActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                } else {
                    return;
                }
            }
        });
        //点击跳过按钮     TODO 点击跳过处理
        countDownView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownView.cancel();
                Intent intent = new Intent(SplashViewActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
            }
        });
        //启动倒计时
        countDownView.start(true);
        //点击广告后处理
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击广告后停止计时
                countDownView.cancel();
                isAD = true;
                if (StringUtil.isNotEmpty(jump_url) && jump_out == 0) {
                    Intent cardIntent = new Intent(SplashViewActivity.this, BaseWebViewActivity.class);
                    cardIntent.putExtra("weburl", jump_url);
                    cardIntent.putExtra("eventId", BizConstant.TYPE_ONE);
                    startActivity(cardIntent);
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                    finish();
                } else if (StringUtil.isNotEmpty(jump_url) && jump_out == 1) {
                    //调到手机自带浏览器
                    Uri uri = Uri.parse(jump_url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        //网址正确 跳转成功
                        startActivity(intent);
                        overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                        finish();
                    } else {
                        //网址不正确 跳转失败 提示错误
                        ToastUtil.showToast(SplashViewActivity.this, "网页链接信息错误");
                    }
                }
            }
        });
    }

    @Override
    protected LanchScreenPresenter getPresenter() {
        return new LanchScreenPresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.lanchsp;
    }

    @Override
    public void onErro() {

    }

    @Override
    public void successLanch(LanchScreenBean lanchScreenBean) {
        if (lanchScreenBean.status == 1) {
            lanchList.add(lanchScreenBean.data);
            img_url = lanchScreenBean.data.img_url;
            jump_url = lanchScreenBean.data.jump_url;
            jump_out = lanchScreenBean.data.jump_out;
            imageviewStart.setVisibility(View.GONE);
            countDownView.setVisibility(View.GONE);
            if (NetUtils.isConnected(this)) {
                //RequestOptions options= new RequestOptions();
                //增加图片加载完成监听
                Glide.with(this)
                        .load(img_url)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                AlphaAnimation alpha = new AlphaAnimation(0.0f, 1.0f);//渐变
                                alpha.setDuration(500);
                                MainRe.startAnimation(alpha);
                                countDownView.setVisibility(View.VISIBLE);
                                reimage.setVisibility(View.VISIBLE);
                                return false;
                            }
                        })
                        .into(imageView);
            } else {
                reimage.setVisibility(View.VISIBLE);
            }
        }
    }

    //全屏显示
    private void full(boolean enable) {
        WindowManager.LayoutParams p = this.getWindow().getAttributes();
        if (enable) {
            p.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;//|=：或等于，取其一
        } else {
            p.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);//&=：与等于，取其二同时满足 取反
        }
        getWindow().setAttributes(p);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownView != null) {
            countDownView = null;
        }
    }

    /**
     * 开屏页一定要禁止用户对返回按钮的控制，否则将可能导致用户手动退出了App而广告无法正常曝光和计费
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
