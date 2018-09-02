package com.zwonline.top28.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.zwonline.top28.R;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.RecordUserBehavior;
import com.zwonline.top28.utils.SharedPreferencesUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 描述：启动页
 *
 * @author YSG
 * @date 2018/1/12
 */
public class StartupActivity extends AppCompatActivity {
    private int count = 2;
    private Timer timer;
    private SharedPreferencesUtils sp;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            textView.setText("跳过" + count);
            if (count == 0) {
//                textView.setText("跳过");
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
            }
        }
    };

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setBackgroundResource(R.mipmap.startup);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
        RecordUserBehavior.recordUserBehavior(this.getApplicationContext(), BizConstant.OPEN_APP);
        sp = SharedPreferencesUtils.getUtil();
        sp.insertKey(getApplicationContext(), "isfer", true);
        initView();

    }

    private void initView() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                count--;
                handler.sendEmptyMessage(0);
            }
        }, 1000, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }


}
