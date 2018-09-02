package com.zwonline.top28.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtil {
    private static ThreadUtil instance = null;
    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2 + 1);
    private ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();

    private ThreadUtil() {
    }

    public static ThreadUtil getInstance() {
        Class var0 = ThreadUtil.class;
        synchronized(ThreadUtil.class) {
            if (instance == null) {
                instance = new ThreadUtil();
            }
        }

        return instance;
    }

    public ExecutorService getSingleThreadPool() {
        return this.singleThreadPool;
    }

    public void singleExecute(Runnable var1) {
        if (var1 != null) {
            this.singleThreadPool.execute(var1);
        }

    }

    public void runMainThread(Runnable var1) {
        (new Handler(Looper.getMainLooper())).post(var1);
    }

    public void execute(Runnable var1) {
        this.fixedThreadPool.execute(var1);
    }

    public void shutdown() {
        this.fixedThreadPool.shutdown();
    }

    public void shutdownNow() {
        this.fixedThreadPool.shutdownNow();
    }
}
