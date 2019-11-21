package com.zwonline.top28.base;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.CookieManager;

import com.jaeger.library.StatusBarUtil;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.umeng.analytics.MobclickAgent;
import com.zwonline.top28.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 1.类的用途
 * 2.@authorDell
 * 3.@date2017/12/6 13:40
 * BaseActivity基类
 */

public abstract class BaseActivity<V, T extends BasePresenter<V>> extends AppCompatActivity {

    public T presenter;
    private Unbinder bind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (null != savedInstanceState)
            savedInstanceState = null;
        super.onCreate(savedInstanceState);
        setContentView(setLayoutId());
//        SysApplication.getInstance().addActivity(this);
        bind = ButterKnife.bind(this);
//        EventBus.getDefault().register(this);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (presenter != null) {
            presenter.attachView((V) this);
        }
        presenter = getPresenter();
        init();

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);//友盟统计
        Resources resource = getResources();
        Configuration configuration = resource.getConfiguration();
        configuration.fontScale = 1.0f;//设置字体的缩放比例
        resource.updateConfiguration(configuration, resource.getDisplayMetrics());
    }

    protected abstract void init();

    protected abstract T getPresenter();

    protected abstract int setLayoutId();

    @Override
    protected void onDestroy() {

        super.onDestroy();
        if (presenter != null) {
            presenter.detachView();
            bind.unbind();
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    /**
     * 将cookie同步到WebView
     *
     * @param url          WebView要加载的url
     * @param cookieString 要同步的cookie
     * @return true 同步cookie成功，false同步cookie失败
     * @Author JPH
     */
    protected void synCookies(String url, String cookieString) {
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();//移除
        cookieManager.removeAllCookie();  //移除所有
        cookieManager.setCookie(url, cookieString);//cookies是在HttpClient中获得的cookie 如果没有特殊需求，这里只需要将session id以"key=value"形式作为cookie即可
        CookieSyncManager.getInstance().sync();
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
