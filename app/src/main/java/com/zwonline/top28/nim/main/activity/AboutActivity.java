package com.zwonline.top28.nim.main.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.zwonline.top28.R;
import com.zwonline.top28.constants.BizConstant;

public class AboutActivity extends UI {

    private TextView versionGit;
    private TextView versionDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_layout);

        ToolBarOptions options = new NimToolBarOptions();
        setToolBar(R.id.toolbar, options);

        findViews();
        initViewData();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void findViews() {
        versionGit = (TextView) findViewById(R.id.version_detail_git);
        versionDate = (TextView) findViewById(R.id.version_detail_date);
    }

    private void initViewData() {
        versionGit.setText("Git Version: " + BizConstant.GIT_REVISION);
        versionDate.setText("Build Date:" + BizConstant.BUILD_DATE);
    }
}
