package com.zwonline.top28.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.zwonline.top28.R;
import com.zwonline.top28.activity.AdvertisingActivity;
import com.zwonline.top28.activity.HomeSearchActivity;
import com.zwonline.top28.activity.MainActivity;
import com.zwonline.top28.base.BasesFragment;
import com.zwonline.top28.bean.HomeBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.HomeClassPresenter;
import com.zwonline.top28.presenter.RecordUserBehavior;
import com.zwonline.top28.tip.toast.ToastUtil;
import com.zwonline.top28.utils.LanguageUitils;
import com.zwonline.top28.utils.NetUtils;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.utils.popwindow.CustomPopuWindow;
import com.zwonline.top28.utils.popwindow.YangFenUnclaimedWindow;
import com.zwonline.top28.view.IHomeClassFrag;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.liaoinstan.springview.utils.DensityUtil.dip2px;

/**
 * 1.类的用途
 * 2.@authorDell
 * 3.@date2017/12/6 14:27
 */

public class HomeFragment extends BasesFragment<IHomeClassFrag, HomeClassPresenter> implements IHomeClassFrag {


    @BindView(R.id.et_home)
    EditText etHome;
    @BindView(R.id.btn_search)
    Button btnSearch;
    @BindView(R.id.header_layout)
    LinearLayout headerLayout;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    Unbinder unbinder;
    private SharedPreferencesUtils sp;
    private boolean isfer;
    private ImageView hotBusiness;
    private RelativeLayout hot_business_relat;
    private LinearLayout home_linear;
    private YangFenUnclaimedWindow yangFenUnclaimedWindow;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    protected void init(View view) {
        StatusBarUtil.setColor(getActivity(), getResources().getColor(R.color.black), 0);
        sp = SharedPreferencesUtils.getUtil();
        hotBusiness = (ImageView) view.findViewById(R.id.hot_business);
        hot_business_relat = (RelativeLayout) view.findViewById(R.id.hot_business_relat);
        home_linear = (LinearLayout) view.findViewById(R.id.home_linear);

        date(view);
        if (NetUtils.isConnected(getActivity())) {
            presenter.mHomeClass(getActivity());
        }
        etHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etHome.requestFocus();//获取焦点
                etHome.setCursorVisible(true);//光标出现
                RecordUserBehavior.recordUserBehavior(getActivity(), BizConstant.CLICK_SEARCH_BAR);
            }
        });
        etHome.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!StringUtil.isEmpty(etHome.getText().toString().trim())) {
                        RecordUserBehavior.recordUserBehavior(getActivity(), BizConstant.DO_SEARCH);

                        Intent intent = new Intent(getActivity(), HomeSearchActivity.class);
                        intent.putExtra("title", etHome.getText().toString().trim());
                        startActivity(intent);
                        etHome.setText("");
                        etHome.setCursorVisible(false);//光标隐藏
                        getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                    } else {
                        ToastUtil.showToast(getActivity(), "请输入内容！");
                    }
                    return true;
                }
                return false;
            }
        });
        //触摸监听
        touchListener();

    }

    private int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected HomeClassPresenter setPresenter() {
        return new HomeClassPresenter(this, getActivity());
    }

    @Override
    protected int setLayouId() {
        return R.layout.homefragment;
    }

    @Override
    public void showHomeClass(List<HomeBean.DataBean> classList) {
        for (int i = 0; i < classList.size(); i++) {
            tablayout.newTab().setText(classList.get(i).cate_name);
        }

        tablayout.newTab().setText(R.string.center_recommend);
        if (classList.size() > 5) {

            tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        } else {
            tablayout.setTabMode(TabLayout.MODE_FIXED);
        }

        MyFragmentAdapter myFragmentAdapter = new MyFragmentAdapter(getChildFragmentManager(), classList);
        viewpager.setAdapter(myFragmentAdapter);
        viewpager.setOffscreenPageLimit(1);
        viewpager.setCurrentItem(1);
        StringUtil.reflex(tablayout);
        tablayout.setTabsFromPagerAdapter(myFragmentAdapter);
        tablayout.setupWithViewPager(viewpager);
    }

    @Override
    public void showHomesClass(HomeBean homeBean) {
//        sp.insertKey(getActivity(), "dialog", homeBean.dialog);
    }

    //这里用于处理Tablayout的
//    @Override
//    public void onStart() {
//        super.onStart();
//        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
//        tablayout.post(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    //拿到tabLayout的mTabStrip属性
//                    LinearLayout mTabStrip = (LinearLayout) tablayout.getChildAt(0);
//
//                    int dp10 = dip2px(tablayout.getContext(), 70);
//
//                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
//                        View tabView = mTabStrip.getChildAt(i);
//
//                        //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
//                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
//                        mTextViewField.setAccessible(true);
//
//                        TextView mTextView = (TextView) mTextViewField.get(tabView);
//
//                        tabView.setPadding(0, 0, 0, 0);
//
//                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
//                        int width = 0;
//                        width = mTextView.getWidth();
//                        if (width == 0) {
//                            mTextView.measure(0, 0);
//                            width = mTextView.getMeasuredWidth();
//                        }
//
//                        //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
//                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
//                        params.width = width;
//                        params.leftMargin = dp10;
//                        params.rightMargin = dp10;
//                        tabView.setLayoutParams(params);
//
//                        tabView.invalidate();
//                    }
//
//                } catch (NoSuchFieldException e) {
//                    e.printStackTrace();
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

    @Override
    public void showErro() {
        Toast.makeText(getActivity(), R.string.get_content_fail_tip, Toast.LENGTH_SHORT).show();
    }


    @OnClick({R.id.btn_search, R.id.hot_business})
    public void onViewClicked(View view) {
        if (AntiShake.check(view.getId())) {    //判断是否多次点击
            return;
        }
        switch (view.getId()) {
            case R.id.btn_search://搜索
                if (!StringUtil.isEmpty(etHome.getText().toString().trim())) {
                    RecordUserBehavior.recordUserBehavior(getActivity(), BizConstant.DO_SEARCH);
                    Intent intent = new Intent(getActivity(), HomeSearchActivity.class);
                    intent.putExtra("title", etHome.getText().toString().trim());
                    startActivity(intent);
                    etHome.setText("");
                    etHome.setCursorVisible(false);//光标隐藏
                    getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                } else {
                    ToastUtil.showToast(getActivity(), "请输入内容！");
                }
                break;
            case R.id.hot_business://热商机
                Intent intent = new Intent(getActivity(), AdvertisingActivity.class);
                intent.putExtra("jump_path", BizConstant.HOT_BUSINESS);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;

        }
    }

    //适配Fragment
    class MyFragmentAdapter extends FragmentPagerAdapter {
        private List<HomeBean.DataBean> hlist;

        public MyFragmentAdapter(FragmentManager fm, List<HomeBean.DataBean> list) {
            super(fm);
            if (hlist == null) {
                hlist = new ArrayList<>();
            }
            hlist.addAll(list);
            HomeBean.DataBean bean = new HomeBean.DataBean("300", getString(R.string.center_recommend));

            hlist.add(1, bean);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object); //避免多出销毁Fragment
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return hlist.get(position).cate_name;
        }

        @Override
        public Fragment getItem(int position) {
            return HomeClass.getInstance(hlist.get(position));
        }

        @Override
        public int getCount() {
            return hlist.size();
        }
    }


    //判断应用第一次启动
    private void date(View view) {
        isfer = (boolean) sp.getKey(getActivity(), "isfer", false);
        if (isfer) {
            hot_business_relat.setVisibility(View.VISIBLE);
        } else {
            //第二次进入跳转
            hot_business_relat.setVisibility(View.GONE);
        }
    }


    //触摸监听方法
    private void touchListener() {
        //触摸监听
        MainActivity.MyTouchListener myTouchListener = new MainActivity.MyTouchListener() {
            @Override
            public void onTouchEvent(MotionEvent event) {
                // 处理手势事件
                hot_business_relat.setVisibility(View.GONE);
            }
        };

        // 将myTouchListener注册到分发列表
        ((MainActivity) this.getActivity()).registerMyTouchListener(myTouchListener);
    }

}
