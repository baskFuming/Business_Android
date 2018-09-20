package com.zwonline.top28.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;
import com.zwonline.top28.R;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.module.Constants;
import com.zwonline.top28.presenter.RecordUserBehavior;
import com.zwonline.top28.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * 描述：启动页
 *
 * @author YSG
 * @date 2018/1/12
 */
public class StartupActivity extends AppCompatActivity  {
    private int count = 2;
    private Timer timer;
    private SharedPreferencesUtils sp;
    private SplashAD splashAD;
    private static final int Permissions_GALLERY_KEY = 1;
    private static final int Permissions_CAMERA_KEY = 2;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            textView.setText("跳过" + count);
            if (count == 0) {
             intentData();

            }
        }
    };

    private TextView textView;
    private RelativeLayout main;
    private String mPermissions[] = {Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setBackgroundResource(R.mipmap.startup);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);


        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        sp = SharedPreferencesUtils.getUtil();
        sp.insertKey(getApplicationContext(), "isfer", true);
        if (Build.VERSION.SDK_INT >= 23) {
            setPermissions(Permissions_CAMERA_KEY);
        } else {
            initView();
        }

    }

    private void initView() {
        main = (RelativeLayout) findViewById(R.id.main);
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


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Permissions_CAMERA_KEY) {
            if (grantResults.length > 0) { //安全写法，如果小于0，肯定会出错了
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        intentData();
                    } else {
                        intentData();
                    }
                }
            }
        }

    }


    public void setPermissions(int mPermissions_KEY) {
        /*
        要添加List原因是想判断数组里如果有个别已经授权的权限，就不需要再添加到List中。添加到List中的权限后续将转成数组去申请权限
         */
        List<String> permissionsList = new ArrayList<>();
        //判断系统版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int i = 0; i < mPermissions.length; i++) {
                //判断一个权限是否已经允许授权，如果没有授权就会将单个未授权的权限添加到List里面
                if (ContextCompat.checkSelfPermission(this.getApplicationContext(), mPermissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    permissionsList.add(mPermissions[i]);
                }
            }
            //判断List不是空的，如果有内容就运行获取权限
            if (!permissionsList.isEmpty()) {
                String[] permissions = permissionsList.toArray(new String[permissionsList.size()]);
                for (int j = 0; j < permissions.length; j++) {
                }
                //执行授权的代码。此处执行后会弹窗授权
                ActivityCompat.requestPermissions(this, permissions, mPermissions_KEY);
            } else { //如果是空的说明全部权限都已经授权了，就不授权了,直接执行进入相机或者图库
                initView();
            }
        } else {
            initView();
        }
    }
    public void intentData(){
        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
        intent.putExtra("pos_id", Constants.SplashPosID);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
    }
}
