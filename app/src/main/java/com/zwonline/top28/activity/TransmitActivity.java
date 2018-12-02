package com.zwonline.top28.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zwonline.top28.R;
import com.zwonline.top28.adapter.GuidePageAdapter;
import com.zwonline.top28.api.Api;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.AntiShake;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述：转载文章
 *
 * @author YSG
 * @date 2018/1/3
 */
public class TransmitActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    @BindView(R.id.back)
    RelativeLayout back;
    @BindView(R.id.fans)
    TextView fans;
    @BindView(R.id.edit_web)
    EditText editWeb;
    @BindView(R.id.empty)
    Button empty;
    @BindView(R.id.compile)
    Button compile;
    @BindView(R.id.transmit_web)
    WebView transmitWeb;
    private SharedPreferencesUtils sp;
    private String token;
    private String cookieString = "PHPSESSID=" + token + "; path=/";
    private String url = Api.baseUrl() + "/Members/add_url.html";
    private String mHtmlText;
    private WebView webView;
    private ViewPager vp;
    private int[] imageIdArray;//图片资源的数组
    private List<View> viewList;//图片资源的集合
    private ViewGroup vg;//放置圆点

    //最后一页的按钮
    private Button ib_start;

    //实例化原点View
    private ImageView iv_point;
    private ImageView[] ivPointArray;
    private RelativeLayout transmit_guide;
    private LinearLayout transmit_linear;
    private SharedPreferences transmit;
    private boolean isfristTtansmit;
    private boolean isLast = false;
    private int positions;

    @Override
    protected void init() {
        transmit = getSharedPreferences("startup", 0);
        //这个文件里面的布尔常量名，和它的初始状态，状态为是，则触发下面的方法
        isfristTtansmit = transmit.getBoolean("isfristTtansmit", true);
        transmit_guide = (RelativeLayout) findViewById(R.id.transmit_guide);
        ib_start = (Button) findViewById(R.id.guide_ib_start);
        transmit_linear = (LinearLayout) findViewById(R.id.transmit_linear);
        isFrist();
        ib_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLast) {
                    transmit_linear.setVisibility(View.VISIBLE);
                    transmit_guide.setVisibility(View.GONE);
                } else {
                    vp.setCurrentItem(positions + 1, true);
                }

            }
        });
        sp = SharedPreferencesUtils.getUtil();
        token = (String) sp.getKey(this, "dialog", "");
        //加载ViewPager
        initViewPager();
//    transmitWeb.setWebViewClient(new WebViewClient());//禁止跳系统浏览器
//    CookieManager.getInstance().setCookie(url,cookieString);
//    transmitWeb.loadUrl(url);
    }

    /**
     * 判断是不是第一次下载app，如果是第一次下载显示引导页，不是直接显示转载文章页
     */
    private void isFrist() {
        if (isfristTtansmit) {
            SharedPreferences.Editor edit = transmit.edit();//创建状态储存文件
            edit.putBoolean("isfristTtansmit", false);//将参数put，改变其状态
            edit.commit();//保证文件的创建和编辑
            transmit_linear.setVisibility(View.GONE);
            ib_start.setVisibility(View.VISIBLE);
            transmit_guide.setVisibility(View.VISIBLE);
        } else {
            ib_start.setVisibility(View.GONE);
            transmit_linear.setVisibility(View.VISIBLE);
            transmit_guide.setVisibility(View.GONE);
        }
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_transmit;
    }


    @OnClick({R.id.back, R.id.empty, R.id.compile})
    public void onViewClicked(View view) {
        if (AntiShake.check(view.getId())) {    //判断是否多次点击
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.empty://清空
                editWeb.setText("");
                break;
            case R.id.compile://编辑
                String s = editWeb.getText().toString();
                if (!StringUtil.isEmpty(s)) {
                    if (s.contains("https") || s.contains("http") || s.contains("www")) {
                        Intent intent = new Intent(TransmitActivity.this, EditActivity.class);
                        intent.putExtra("url", editWeb.getText().toString().trim());
                        startActivity(intent);
                        editWeb.setText("");
                        overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                        finish();
                    } else {
                        ToastUtils.showToast(TransmitActivity.this, "请输入正确的链接！");
                    }

                } else {
                    ToastUtils.showToast(TransmitActivity.this, getString(R.string.article_not_empty));
                }
                break;
        }
    }


    /**
     * 逻辑处理
     *
     * @author linzewu
     */

    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void getSource(String html) {
            Pattern pattern = Pattern.compile("<p.*?>(.*?)</p>");
            Matcher matcher = pattern.matcher(html);
            StringBuffer sb = new StringBuffer();
            while (matcher.find()) {
                sb.append(matcher.group(1));
            }
            mHtmlText = sb.toString();
            System.out.print("mHtmlText==" + mHtmlText);
            Toast.makeText(TransmitActivity.this, sb.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    class Handler {
        public void show(String data) {
            new AlertDialog.Builder(TransmitActivity.this).setMessage(data).create().show();
        }

    }

    /**
     * 加载图片ViewPager
     */
    private void initViewPager() {
        vp = (ViewPager) findViewById(R.id.guide_vp);
        //实例化图片资源
        imageIdArray = new int[]{R.mipmap.page_22, R.mipmap.page_23, R.mipmap.page_24};
        viewList = new ArrayList<>();
        //获取一个Layout参数，设置为全屏
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        //循环创建View并加入到集合中
        int len = imageIdArray.length;
        for (int i = 0; i < len; i++) {
            //new ImageView并设置全屏和图片资源
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(params);
            imageView.setBackgroundResource(imageIdArray[i]);

            //将ImageView加入到集合中
            viewList.add(imageView);
        }

        //View集合初始化好后，设置Adapter
        vp.setAdapter(new GuidePageAdapter(viewList));
        //设置滑动监听
        vp.setOnPageChangeListener(this);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * 滑动后的监听
     *
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        this.positions = position;
        //循环设置当前页的标记图
        int length = imageIdArray.length;
//        for (int i = 0; i < length; i++) {
//            ivPointArray[position].setBackgroundResource(R.mipmap.guide1);
//            if (position != i) {
//                ivPointArray[i].setBackgroundResource(R.mipmap.guide1);
//            }
//        }

        //判断是否是最后一页，若是则显示按钮
        if (position == imageIdArray.length - 1) {
            ib_start.setText("朕已阅");
            ib_start.setTextColor(Color.parseColor("#FFFFFF"));
            ib_start.setBackgroundResource(R.drawable.btn_red_shape);
            isLast = true;
        } else {
            ib_start.setText("下一页");
            ib_start.setTextColor(Color.parseColor("#FF2B2B"));
            ib_start.setBackgroundResource(R.drawable.reword__shape);
            isLast = false;
        }
    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
