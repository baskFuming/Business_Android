package com.zwonline.top28.nim.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zwonline.top28.R;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.AnnouncementBean;
import com.zwonline.top28.bean.NotifyDetailsBean;
import com.zwonline.top28.presenter.AnnouncementPresenter;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.view.IAnnouncementActivity;

import java.util.List;

import butterknife.OnClick;

public class NotifyDetailsActivity extends BaseActivity<IAnnouncementActivity, AnnouncementPresenter> implements IAnnouncementActivity {

    private RelativeLayout back;
    private TextView title;
    private TextView detailsTime;
    private TextView contentDetails;
    private View notifyToolbar;
    private String noticeId;
    private TextView notifyTitle;

    @Override
    protected void init() {
        initView();
        presenter.mNotifyDetails(getApplicationContext(), noticeId);
    }

    @Override
    protected AnnouncementPresenter getPresenter() {
        return new AnnouncementPresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_notify_details;
    }

    /**
     * 初始化数据
     */
    private void initView() {
        noticeId = getIntent().getStringExtra("noticeId");
        back = (RelativeLayout) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        notifyTitle = (TextView) findViewById(R.id.notify_title);
        title.setText("通告详情");
        detailsTime = (TextView) findViewById(R.id.details_time);
        contentDetails = (TextView) findViewById(R.id.content_details);
        notifyToolbar = findViewById(R.id.notify_toolbar);

    }

    /**
     * 展示数据赋值
     * @param notifyDetailsBean
     */
    @Override
    public void showNotifyDetails(NotifyDetailsBean notifyDetailsBean) {
        if (StringUtil.isNotEmpty(notifyDetailsBean.data.title)){
            notifyTitle.setText(notifyDetailsBean.data.title);
        }
        if (StringUtil.isNotEmpty(notifyDetailsBean.data.date)){
            detailsTime.setText(notifyDetailsBean.data.date);
        }
        if (StringUtil.isNotEmpty(notifyDetailsBean.data.content)){
            contentDetails.setText(notifyDetailsBean.data.content);
        }
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
        overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
    }


    @Override
    public void showAnnouncement(List<AnnouncementBean.DataBean> list) {

    }

    @Override
    public void noAnnouncement(boolean flag) {

    }

    @Override
    public void noLoadMore() {

    }


}
