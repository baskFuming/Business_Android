package com.zwonline.top28.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.contrarywind.timer.MessageHandler;
import com.zwonline.top28.R;
import com.zwonline.top28.adapter.GuidePageAdapter;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 引导页
 * type值1引导页2查看玩法
 * imageIdArray引导页
 * imageIdArrays查看玩法
 * isLast是true最后一张图片false不是最后一张图片
 */
public class GuideActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private ViewPager vp;
    private int[] imageIdArray;//图片资源的数组
    private List<View> viewList;//图片资源的集合
    private ViewGroup vg;//放置圆点
    private int[] imageIdArrays;//查看玩法
    //实例化原点View
    private ImageView iv_point;
    private ImageView[] ivPointArray;

    //最后一页的按钮
    private Button ib_start;
    private int len;
    private boolean isLast = false;//判断是否是最后一页
    private ImageView imageView;
    private int positons;
    private LinearLayout.LayoutParams params;
    private int[] imageArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_guide);
        imageArray = getIntent().getIntArrayExtra("imageArray");

        ib_start = (Button) findViewById(R.id.guide_ib_start);
        ib_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否是最后一页，如果是最后一页点击finish()界面，不是最后一页点击切换图片
                if (isLast) {
                        finish();
                   overridePendingTransition(R.anim.activity_left_in,R.anim.activity_right_out);
                } else {
                    //点击切换图片
                    vp.setCurrentItem(positons + 1, true);
                }
            }
        });

        //加载ViewPager
        initViewPager();
        //加载底部圆点
//        initPoint();
    }

    /**
     * 加载底部圆点
     */
    private void initPoint() {
        //这里实例化LinearLayout
        vg = (ViewGroup) findViewById(R.id.guide_ll_point);
        //根据ViewPager的item数量实例化数组
        ivPointArray = new ImageView[viewList.size()];
        //循环新建底部圆点ImageView，将生成的ImageView保存到数组中
        int size = viewList.size();
        for (int i = 0; i < size; i++) {
            iv_point = new ImageView(this);
            iv_point.setLayoutParams(new ViewGroup.LayoutParams(20, 20));
            iv_point.setPadding(30, 0, 30, 0);//left,top,right,bottom
            ivPointArray[i] = iv_point;
            //第一个页面需要设置为选中状态，这里采用两张不同的图片
//            if (i == 0) {
//                iv_point.setBackgroundResource(R.mipmap.full_holo);
//            } else {
//                iv_point.setBackgroundResource(R.mipmap.empty_holo);
//            }
            //将数组中的ImageView加入到ViewGroup
            vg.addView(ivPointArray[i]);
        }
    }

    /**
     * 加载图片ViewPager
     */
    private void initViewPager() {
        vp = (ViewPager) findViewById(R.id.guide_vp);
        //第一次下载刚进来
        viewList = new ArrayList<>();
        //获取一个Layout参数，设置为全屏
        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        //循环创建View并加入到集合中
        len = imageArray.length;
        for (int i = 0; i < len; i++) {
            //new ImageView并设置全屏和图片资源
            imageView = new ImageView(this);
            imageView.setLayoutParams(params);
            imageView.setBackgroundResource(imageArray[i]);
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
        //循环设置当前页的标记图
        int length = imageArray.length;
        this.positons = position;
//        for (int i = 0; i < length; i++) {
//            ivPointArray[position].setBackgroundResource(R.mipmap.guide1);
//            if (position != i) {
//                ivPointArray[i].setBackgroundResource(R.mipmap.guide1);
//            }
//        }

        //判断是否是最后一页，若是则显示按钮
        guideType(imageArray, position);
//        if (StringUtil.isNotEmpty(type) && type.equals(BizConstant.RECOMMEND)) {
//            guideType(imageIdArrays, position);
//        } else {
//            guideType(imageIdArray, position);
//        }

    }

    /**
     * 判断引导页类别
     *
     * @param imageArray
     * @param position
     */
    public void guideType(int[] imageArray, int position) {
        if (position == imageArray.length - 1) {
            ib_start.setText("朕已阅");
            ib_start.setTextColor(Color.parseColor("#FFFFFF"));
            ib_start.setBackgroundResource(R.drawable.btn_red_shape);
            isLast = true;
        } else {
            ib_start.setText("下一步");
            ib_start.setTextColor(Color.parseColor("#FF2B2B"));
            ib_start.setBackgroundResource(R.drawable.reword__shape);
            isLast = false;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


}
