package com.zwonline.top28.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.zwonline.top28.R;

import cn.forward.androids.base.BaseActivity;

public class DataAnalysisActivity extends BaseActivity {
    private RelativeLayout reback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_analysis);
        reback = (RelativeLayout)findViewById(R.id.backnotice);
        reback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
