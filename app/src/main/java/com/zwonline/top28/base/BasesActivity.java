package com.zwonline.top28.base;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;

import com.jaeger.library.StatusBarUtil;
import com.netease.nim.uikit.common.fragment.TFragment;
import com.netease.nim.uikit.common.util.sys.ReflectionUtil;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.umeng.analytics.MobclickAgent;
import com.zwonline.top28.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 1.类的用途
 * 2.@authorDell
 * 3.@date2017/12/6 13:40
 * BaseActivity基类
 */

public abstract class BasesActivity<V, T extends BasePresenter<V>> extends AppCompatActivity {
    private static Handler handler;
    public T presenter;
    private Unbinder bind;
    private boolean destroyed = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (null != savedInstanceState)
            savedInstanceState = null;
        super.onCreate(savedInstanceState);
        setContentView(setLayoutId());
//        SysApplication.getInstance().addActivity(this);
        bind = ButterKnife.bind(this);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.black), 0);
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
        destroyed = true;
        if (presenter != null) {
            presenter.detachView();
            bind.unbind();
            //EventBus.getDefault().unregister(this);
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

    private void invokeFragmentManagerNoteStateNotSaved() {
        FragmentManager fm = getSupportFragmentManager();
        ReflectionUtil.invokeMethod(fm, "noteStateNotSaved", null);
    }

    protected void switchFragmentContent(TFragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(fragment.getContainerId(), fragment);
        try {
            transaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected boolean isCompatible(int apiLevel) {
        return Build.VERSION.SDK_INT >= apiLevel;
    }

    protected <T extends View> T findView(int resId) {
        return (T) (findViewById(resId));

    }
    @Override
    public void onBackPressed() {
        invokeFragmentManagerNoteStateNotSaved();
        super.onBackPressed();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onNavigateUpClicked();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onNavigateUpClicked() {
        onBackPressed();
    }

    protected final Handler getHandler() {
        if (handler == null) {
            handler = new Handler(getMainLooper());
        }
        return handler;
    }
}
