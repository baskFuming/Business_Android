package com.zwonline.top28.activity;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zwonline.top28.R;
import com.zwonline.top28.api.Api;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.AntiShake;

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
public class TransmitActivity extends BaseActivity {
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

    @Override
    protected void init() {
        sp = SharedPreferencesUtils.getUtil();
        token = (String) sp.getKey(this, "dialog", "");

//    transmitWeb.setWebViewClient(new WebViewClient());//禁止跳系统浏览器
//    CookieManager.getInstance().setCookie(url,cookieString);
//    transmitWeb.loadUrl(url);
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
                if (!StringUtil.isEmpty(s) ){
                     if (s.contains("https")||s.contains("http")||s.contains("www")){
                         Intent intent = new Intent(TransmitActivity.this, EditActivity.class);
                         intent.putExtra("url", editWeb.getText().toString().trim());
                         startActivity(intent);
                         editWeb.setText("");
                         overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                         finish();
                     }else {
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


}
