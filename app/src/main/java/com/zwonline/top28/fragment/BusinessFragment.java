package com.zwonline.top28.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.zwonline.top28.R;
import com.zwonline.top28.activity.BusinessActivity;
import com.zwonline.top28.activity.BusinessSearchActivity;
import com.zwonline.top28.activity.CompanyActivity;
import com.zwonline.top28.activity.HomeDetailsActivity;
import com.zwonline.top28.activity.HomeSearchActivity;
import com.zwonline.top28.activity.PasswordLoginActivity;
import com.zwonline.top28.activity.WithoutCodeLoginActivity;
import com.zwonline.top28.adapter.BusinessAdapter;
import com.zwonline.top28.adapter.JZHOAdapter;
import com.zwonline.top28.adapter.RecommendAdapter;
import com.zwonline.top28.base.BasesFragment;
import com.zwonline.top28.bean.BannerBean;
import com.zwonline.top28.bean.BusinessClassifyBean;
import com.zwonline.top28.bean.BusinessListBean;
import com.zwonline.top28.bean.JZHOBean;
import com.zwonline.top28.bean.RecommendBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.BusinessClassPresenter;
import com.zwonline.top28.presenter.RecordUserBehavior;
import com.zwonline.top28.tip.toast.ToastUtil;
import com.zwonline.top28.utils.BannerImage;
import com.zwonline.top28.utils.NavigationBar;
import com.zwonline.top28.utils.ObservableScrollView;
import com.zwonline.top28.utils.ScrollLinearLayoutManager;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.IBusinessClassFra;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;


/**
 * 1.商机的页面
 * 2.@authorDell
 * 3.@date2017/12/6 14:27
 */

public class BusinessFragment extends BasesFragment<IBusinessClassFra, BusinessClassPresenter> implements IBusinessClassFra {
    private List<BusinessListBean.DataBean> hlist;
    private SharedPreferencesUtils sp;
    private String token;
    private boolean islogins;
    private Button btnSearch;
    private EditText etHome;
    private Banner businessBanner;
    private RecyclerView youlike;
    private RecyclerView hotRecy;
    private ListView jzhoList;
    private LinearLayout hairdressing;
    private LinearLayout education;
    private LinearLayout feature;
    private LinearLayout homeBuild;
    private LinearLayout present;
    private LinearLayout environmental;
    private LinearLayout clothing;
    private LinearLayout food;
    private BusinessAdapter adapter;
    private ObservableScrollView scrollView;
    private int imageHeight = 260; //设置渐变高度，一般为导航图片高度，自己控制
    private RelativeLayout line;
    private static final String STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height";
    private TextView tv_bottom;

    @Override
    protected void init(final View view) {
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);//设置状态栏字体为白色
        StatusBarUtil.setColor(getActivity(), getResources().getColor(R.color.black), 0);
//        NavigationBar.Statedata(getActivity());
        initData(view);
//        NavigationBar.transparencyBar(getActivity());
        presenter.mBusinessList();//猜你喜欢
        presenter.mBanner(getActivity());//轮播图
        presenter.mRecommend();//推荐项目
        presenter.mJZHO();//创业资讯
        etHome.setCursorVisible(false);//光标出现
        businessBanner.setImageLoader(new BannerImage());
        //设置banner动画效果
        businessBanner.setBannerAnimation(Transformer.DepthPage);
        //设置banner样式
        businessBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        sp = SharedPreferencesUtils.getUtil();
        islogins = (boolean) sp.getKey(getActivity(), "islogin", false);
        token = (String) sp.getKey(getActivity(), "dialog", "");
        etHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etHome.setCursorVisible(true);//光标出现
                etHome.requestFocus();//获取焦点
            }
        });
        //软件盘上添加搜索点击事件
        etHome.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!StringUtil.isEmpty(etHome.getText().toString().trim())) {
                        RecordUserBehavior.recordUserBehavior(getActivity(), BizConstant.DO_SEARCH);

                        Intent intent = new Intent(getActivity(), BusinessSearchActivity.class);
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
        //搜索框在布局最上面
        line.bringToFront();
        //滑动监听
        scrollView.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                if (y <= 0) {
                    line.setBackgroundColor(Color.argb((int) 0, 111, 29, 26));//AGB由相关工具获得，或者美工提供
                } else if (y > 0 && y <= imageHeight) {
                    float scale = (float) y / imageHeight;
                    float alpha = (255 * scale);
                    tv_bottom.setVisibility(View.GONE);
//                    getWindowStateHeight(getActivity());
//                    NavigationBar.Statedata(getActivity());
                    // 只是layout背景透明
                    line.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                    etHome.setBackgroundResource(R.drawable.business_serch_withe_shape);
//                    getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);//设置状态栏字体为白色
                } else {
//                    NavigationBar.transparencyBar(getActivity());
//                    getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);//设置状态栏字体为白色
                    StatusBarUtil.setColor(getActivity(), getResources().getColor(R.color.black), 0);
                    line.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
                    tv_bottom.setVisibility(View.VISIBLE);
                    etHome.setBackgroundResource(R.drawable.business_serch_shape);
                }
            }
        });

    }


    private void initData(View view) {
        btnSearch = (Button) view.findViewById(R.id.btn_search);
        etHome = (EditText) view.findViewById(R.id.et_home);
        businessBanner = (Banner) view.findViewById(R.id.business_banner);
        youlike = (RecyclerView) view.findViewById(R.id.youlike);
        hotRecy = (RecyclerView) view.findViewById(R.id.hot_recy);
        jzhoList = (ListView) view.findViewById(R.id.jzho_list);
        hairdressing = (LinearLayout) view.findViewById(R.id.hairdressing);
        education = (LinearLayout) view.findViewById(R.id.education);
        feature = (LinearLayout) view.findViewById(R.id.feature);
        homeBuild = (LinearLayout) view.findViewById(R.id.home_build);
        present = (LinearLayout) view.findViewById(R.id.present);
        environmental = (LinearLayout) view.findViewById(R.id.environmental);
        clothing = (LinearLayout) view.findViewById(R.id.clothing);
        food = (LinearLayout) view.findViewById(R.id.food);
        scrollView = (ObservableScrollView) view.findViewById(R.id.scrollView);
        line = (RelativeLayout) view.findViewById(R.id.line);
        tv_bottom = (TextView) view.findViewById(R.id.tv_bottom);
    }

    @Override
    protected BusinessClassPresenter setPresenter() {
        return new BusinessClassPresenter(getActivity(), this);
    }


    @Override
    protected int setLayouId() {
        return R.layout.businessfragment;
    }


    @Override
    public void showBusinessClassFra(List<BusinessClassifyBean.DataBean> classList) {

    }

    @Override
    public void showSearch(List<BusinessListBean.DataBean> list) {

    }

    //轮播图
    @Override
    public void showBanner(final List<BannerBean.DataBean> bannerList) {
        List<String> image = new ArrayList<>();
        for (int i = 0; i < bannerList.size(); i++) {
            image.add(bannerList.get(i).img_src);
        }
        View view;

        businessBanner.setImages(image);
        businessBanner.start();
        businessBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (islogins) {
                    String type = bannerList.get(position).type;
                    if (TextUtils.isEmpty(type)) {
                        Toast.makeText(getActivity(), R.string.get_content_fail_tip, Toast.LENGTH_SHORT).show();
                    } else {
                        if (!StringUtil.isEmpty(type) && type.equals("project")) {
                            Intent intent = new Intent(getActivity(), CompanyActivity.class);
                            intent.putExtra("uid", bannerList.get(position).target);
                            intent.putExtra("pid", bannerList.get(position).target);
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                        } else if (!StringUtil.isEmpty(type) && type.equals("article")) {
                            Intent intent = new Intent(getActivity(), HomeDetailsActivity.class);
                            intent.putExtra("id", bannerList.get(position).target + "");
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                        } else {
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.user_not_login, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), PasswordLoginActivity.class));
                    getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                }

            }
        });

    }


    //推荐项目
    @Override
    public void showRecommend(final List<RecommendBean.DataBean> recommendList) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        hotRecy.setLayoutManager(linearLayoutManager);
        RecommendAdapter hotAdapter = new RecommendAdapter(recommendList, getActivity());
        hotRecy.setAdapter(hotAdapter);
        hotAdapter.notifyDataSetChanged();
        hotAdapter.setOnClickItemListener(new RecommendAdapter.OnClickItemListener() {
            @Override
            public void setOnItemClick(int position, View view) {
                if (AntiShake.check(view.getId())) {    //判断是否多次点击
                    return;
                }

                Intent intent = new Intent(getActivity(), CompanyActivity.class);
                intent.putExtra("enterprise_name", recommendList.get(position).cate_name);
                intent.putExtra("uid", recommendList.get(position).uid);
                intent.putExtra("pid", hlist.get(position).id);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);


            }
        });
    }

    //猜你喜欢
    @Override
    public void showData(List<BusinessListBean.DataBean> list) {
        if (hlist == null) {
            hlist = new ArrayList<>();
        }
        for (int i = 0; i < list.size(); i++) {
            hlist.add(list.get(i));
        }
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        ScrollLinearLayoutManager scrollLinearLayoutManager = new ScrollLinearLayoutManager(getActivity());
        scrollLinearLayoutManager.setScrollEnabled(false);
        youlike.setLayoutManager(scrollLinearLayoutManager);
        adapter = new BusinessAdapter(hlist, getActivity());
        youlike.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnClickItemListener(new BusinessAdapter.OnClickItemListener() {
            @Override
            public void setOnItemClick(int position, View view) {
                if (AntiShake.check(view.getId())) {    //判断是否多次点击
                    return;
                }

                Intent intent = new Intent(getActivity(), CompanyActivity.class);
                intent.putExtra("uid", hlist.get(position).uid);
                intent.putExtra("pid", hlist.get(position).id);
                intent.putExtra("enterprise_name", hlist.get(position).cate_name);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
            }
        });

    }

    //创业资讯
    @Override
    public void showJZHO(final List<JZHOBean.DataBean> JZHOlist) {
        JZHOAdapter jzhoAdapter = new JZHOAdapter(JZHOlist, getActivity());
        jzhoList.setAdapter(jzhoAdapter);
        jzhoAdapter.notifyDataSetChanged();
        jzhoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (AntiShake.check(view.getId())) {    //判断是否多次点击
                    return;
                }
//                if (TextUtils.isEmpty(token)) {
//                    Toast.makeText(getActivity(), R.string.user_not_login, Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(getActivity(), WithoutCodeLoginActivity.class);
//                    startActivity(intent);
//                    getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
//                } else {
                Intent intent = new Intent(getActivity(), HomeDetailsActivity.class);
                intent.putExtra("id", JZHOlist.get(position).article_id);
                intent.putExtra("token", token);
                intent.putExtra("title", JZHOlist.get(position).title);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);

//                }
            }
        });
    }

    @Override
    public void showErro() {
//        Toast.makeText(getActivity(), "获取数据失败！", Toast.LENGTH_SHORT).show();
    }


    @OnClick({R.id.btn_search, R.id.food, R.id.clothing, R.id.environmental, R.id.present, R.id.home_build, R.id.education, R.id.hairdressing, R.id.feature})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_search:
                if (!StringUtil.isEmpty(etHome.getText().toString().trim())) {
                    Intent intent = new Intent(getActivity(), BusinessSearchActivity.class);
                    intent.putExtra("title", etHome.getText().toString().trim());
                    startActivity(intent);
                    etHome.setText("");
                    etHome.setCursorVisible(false);//光标隐藏
                    getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                } else {
                    ToastUtil.showToast(getActivity(), "请输入内容！");
                }
                break;
            case R.id.food:
                Intent food_intent = new Intent(getActivity(), BusinessActivity.class);
                food_intent.putExtra("id", 0);
                startActivity(food_intent);
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.clothing:
                Intent clothing_intent = new Intent(getActivity(), BusinessActivity.class);
                clothing_intent.putExtra("id", 1);
                startActivity(clothing_intent);
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.environmental:
                Intent en_intent = new Intent(getActivity(), BusinessActivity.class);
                en_intent.putExtra("id", 2);
                startActivity(en_intent);
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.present:
                Intent present_intent = new Intent(getActivity(), BusinessActivity.class);
                present_intent.putExtra("id", 0);
                startActivity(present_intent);
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.home_build:
                Intent build_intent = new Intent(getActivity(), BusinessActivity.class);
                build_intent.putExtra("id", 3);
                startActivity(build_intent);
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.education:
                Intent education_intent = new Intent(getActivity(), BusinessActivity.class);
                education_intent.putExtra("id", 4);
                startActivity(education_intent);
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.hairdressing:
                Intent haird_intent = new Intent(getActivity(), BusinessActivity.class);
                haird_intent.putExtra("id", 5);
                startActivity(haird_intent);
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.feature:
                Intent feature_intent = new Intent(getActivity(), BusinessActivity.class);
                feature_intent.putExtra("id", 6);
                startActivity(feature_intent);
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;

        }
    }


    public static int getWindowStateHeight(Context context) {
        int statusBarHeight1 = -1;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {

            statusBarHeight1 = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight1;
    }


}
