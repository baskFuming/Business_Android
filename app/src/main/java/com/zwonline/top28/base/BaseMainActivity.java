package com.zwonline.top28.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.netease.nim.uikit.business.recent.RecentContactsFragment;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.fragment.TFragment;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.common.util.sys.ReflectionUtil;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.umeng.analytics.MobclickAgent;
import com.zwonline.top28.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseMainActivity<V, T extends BasePresenter<V>> extends AppCompatActivity {

    private boolean destroyed = false;

    private static Handler handler;

    private Toolbar toolbar;

    private TextView textViewTitle;


    public T presenter;
    private Unbinder bind;

    @Override
    protected void onStart() {
        super.onStart();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutId());
//        SysApplication.getInstance().addActivity(this);
        bind = ButterKnife.bind(this);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (presenter != null) {
            presenter.attachView((V) this);
        }
        presenter = getPresenter();
        init();
        new RecentContactsFragment();
        LogUtil.ui("activity: " + getClass().getSimpleName() + " onCreate()");
    }


    protected abstract void init();

    protected abstract T getPresenter();

    protected abstract int setLayoutId();

    @Override
    public void onBackPressed() {
        invokeFragmentManagerNoteStateNotSaved();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LogUtil.ui("activity: " + getClass().getSimpleName() + " onDestroy()");
        destroyed = true;
        if (presenter != null) {
            presenter.detachView();
            bind.unbind();
            EventBus.getDefault().unregister(this);
        }
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

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
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

    public void setToolBar(int toolBarId, ToolBarOptions options) {
        toolbar = (Toolbar) findViewById(toolBarId);

        if (options.titleId != 0) {
            toolbar.setTitle(options.titleId);
        }
        if (!TextUtils.isEmpty(options.titleString)) {
            //  toolbar.setTitle(options.titleString);


        }
        if (options.logoId != 0) {
            toolbar.setLogo(options.logoId);
        }
        setSupportActionBar(toolbar);

        if (options.isNeedNavigate) {
            toolbar.setNavigationIcon(options.navigateId);
            toolbar.setContentInsetStartWithNavigation(0);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNavigateUpClicked();
                }
            });
        }
    }

//    public void setToolBar(int toolbarId, int titleId, int logoId) {
//        toolbar = (Toolbar) findViewById(toolbarId);
//        toolbar.setTitle(titleId);
//        toolbar.setLogo(logoId);
//        setSupportActionBar(toolbar);
//    }

    public Toolbar getToolBar() {
        return toolbar;
    }

    public int getToolBarHeight() {
        if (toolbar != null) {
            return toolbar.getHeight();
        }

        return 0;
    }

    public void onNavigateUpClicked() {
        onBackPressed();
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        if (toolbar != null) {
            toolbar.setTitle(title);
        }
    }

    public void setSubTitle(String subTitle) {
        if (toolbar != null) {
            toolbar.setSubtitle(subTitle);
        }
    }

    protected final Handler getHandler() {
        if (handler == null) {
            handler = new Handler(getMainLooper());
        }
        return handler;
    }

    protected void showKeyboard(boolean isShow) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isShow) {
            if (getCurrentFocus() == null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            } else {
                imm.showSoftInput(getCurrentFocus(), 0);
            }
        } else {
            if (getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 延时弹出键盘
     *
     * @param focus 键盘的焦点项
     */
    protected void showKeyboardDelayed(View focus) {
        final View viewToFocus = focus;
        if (focus != null) {
            focus.requestFocus();
        }

        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (viewToFocus == null || viewToFocus.isFocused()) {
                    showKeyboard(true);
                }
            }
        }, 200);
    }


    public boolean isDestroyedCompatible() {
        if (Build.VERSION.SDK_INT >= 17) {
            return isDestroyedCompatible17();
        } else {
            return destroyed || super.isFinishing();
        }
    }

    @TargetApi(17)
    private boolean isDestroyedCompatible17() {
        return super.isDestroyed();
    }

    /**
     * fragment management
     */
    public TFragment addFragment(TFragment fragment) {
        List<TFragment> fragments = new ArrayList<TFragment>(1);
        fragments.add(fragment);

        List<TFragment> fragments2 = addFragments(fragments);
        return fragments2.get(0);
    }

    public List<TFragment> addFragments(List<TFragment> fragments) {
        List<TFragment> fragments2 = new ArrayList<TFragment>(fragments.size());

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        boolean commit = false;
        for (int i = 0; i < fragments.size(); i++) {
            // install
            TFragment fragment = fragments.get(i);
            int id = fragment.getContainerId();

            // exists
            TFragment fragment2 = (TFragment) fm.findFragmentById(id);

            if (fragment2 == null) {
                fragment2 = fragment;
                transaction.add(id, fragment);
                commit = true;
            }

            fragments2.add(i, fragment2);
        }

        if (commit) {
            try {
                transaction.commitAllowingStateLoss();
            } catch (Exception e) {

            }
        }

        return fragments2;
    }

    public TFragment switchContent(TFragment fragment) {
        return switchContent(fragment, false);
    }

    protected TFragment switchContent(TFragment fragment, boolean needAddToBackStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(fragment.getContainerId(), fragment);
        if (needAddToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        try {
            fragmentTransaction.commitAllowingStateLoss();
        } catch (Exception e) {

        }

        return fragment;
    }

    protected boolean displayHomeAsUpEnabled() {
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:
                return onMenuKeyDown();

            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    protected boolean onMenuKeyDown() {
        return false;
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

