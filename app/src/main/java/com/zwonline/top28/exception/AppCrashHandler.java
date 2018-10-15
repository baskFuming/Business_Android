package com.zwonline.top28.exception;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.zwonline.top28.utils.LogUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sdh on 2018/3/10.
 * 实际开发中为了防止程序异常奔溃，而使得开发人员不知道奔溃原因，
 * 且影响用户体验：所以我们应该在app中统一处理异常，
 * 拦截异常信息，上报服务器
 */

public class AppCrashHandler implements Thread.UncaughtExceptionHandler{
    public static final String TAG = "CrashHandler";
    //异常文件存储路径
    private static final String PATH = Environment.getExternalStorageDirectory().getPath() + "/myExceptionCatch/log/";
    //异常文件存储名字
    private static final String FILE_NAME = "catch";
    //异常文件存储后缀
    private static final String FLIE_NAME_SUFFIX = ".log";
    //CrashHandler实例
    private static volatile AppCrashHandler instance;
    //系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    //程序的Context对象
    private Context mContext;
    //创建警告对话框
    private Dialog dialog;
    /**
     * 无参构造
     */
    public AppCrashHandler() {

    }
    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static AppCrashHandler getInstance() {
        if (instance == null)
            synchronized (AppCrashHandler.class) {
                if (instance == null) {
                    instance = new AppCrashHandler();
                }
            }
        return instance;
    }

    /**
     * 初始化
     */
    public void init(Context context) {
        mContext = context.getApplicationContext();
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 程序崩溃会被该方法捕获到   在此添加逻辑是自己处理还是交给系统异常处理
     * handleException是自己处理异常的方法
     */
    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        if (!handleException(throwable) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, throwable);
        } else {
            LogUtils.d("flag","----------------获取到异常了");
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                Log.e(TAG, "error : ", e);
//            }
//            //退出程序
//            android.os.Process.killProcess(android.os.Process.myPid());
//            System.exit(1);
        }
    }

    /**
     * 添加自己处理异常的方法    在这里收集收集的设备信息   保存异常文件
     */
    private boolean handleException(final Throwable ex) {
        if (ex == null || mContext == null) {
            return false;
        }

        ex.printStackTrace();  //异常打印

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                try {
                    //先获取手机的基本信息  将异常信息一起写入文件   上传服务器
                    savcExceptionToSDCard(ex);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_SHORT).show();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    Log.e(TAG, "error : " + e);
                }
                //退出程序
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                Looper.loop();
            }
        }).start();
        return true;
    }

    /**
     *  出现异常写到内存卡上   然后读文件上传到服务器
     */
    private void savcExceptionToSDCard(final Throwable ex) throws IOException {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            LogUtils.w("TAG", "sdcard unmounted,skip save exception");
        }
        File dir = new File(PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        long current = System.currentTimeMillis();
        final String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new
                Date(current));
        File file = new File(PATH + FILE_NAME + FLIE_NAME_SUFFIX);
        try {
            //写入时间
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            pw.println(time);
            //写入手机信息
            savePhoneInfo(pw, ex);
        } catch (Exception e) {

        }
        //上传服务器
        //uploadToServer(file);
    }

    /**
     *   写入手机基本信息   异常机型   异常原因等
     */
    private void savePhoneInfo(PrintWriter pw, Throwable ex) throws PackageManager.NameNotFoundException {
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        pw.print("App version: ");
        pw.print(pi.versionName);
        pw.print('_');
        pw.println(pi.versionCode);

        //Android版本号
        pw.print("OS Version: ");
        pw.print(Build.VERSION.RELEASE);
        pw.print(" _ sdk: ");
        pw.println(Build.VERSION.SDK_INT);

        //手机制造商
        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);

        //手机型号
        pw.print("Model: ");
        pw.println(Build.MODEL);

        //CPU架构
        pw.print("CPU ABI : ");
        pw.println(Build.CPU_ABI);
        pw.println();
        //异常信息
        ex.printStackTrace(pw);
        pw.close();
    }
    /**
     * 读取文件转换成字符串
     * 参数一 输入文件file
     *
     * @return
     */
    public String readFromFile(File file) {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            StringBuilder stringBuilder = new StringBuilder();
            String content;
            while ((content = bufferedReader.readLine()) != null) {
                stringBuilder.append(content);
            }
            return stringBuilder.toString();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void uploadToServer(File file) {
//        String ec=readFromFile(file);
//        OkhttpUtils.upEcInfo(ec,"服务器的url");
    }
}
