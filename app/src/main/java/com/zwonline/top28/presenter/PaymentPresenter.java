package com.zwonline.top28.presenter;

import android.content.Context;

import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.AmountPointsBean;
import com.zwonline.top28.bean.OrderInfoBean;
import com.zwonline.top28.bean.PrepayPayBean;
import com.zwonline.top28.model.PaymentModel;
import com.zwonline.top28.view.IPaymentActivity;

import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by sdh on 2018/3/12.
 * 确认收款p层
 */

public class PaymentPresenter extends BasePresenter<IPaymentActivity>{

    private PaymentModel paymentModel;
    private IPaymentActivity iPaymentActivity;

    public PaymentPresenter(IPaymentActivity iPaymentActivity){
        this.iPaymentActivity=iPaymentActivity;
        paymentModel=new PaymentModel();
    }


    /**
     * 获取支付宝orderStr
     * @param context
     * @param orderId
     */
    public void getPayOrderInfoByOrderId (Context context, String orderId) {
        try {
            paymentModel.getOrderInfoById(context, orderId).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<PrepayPayBean>() {
                        @Override
                        public void onNext(PrepayPayBean prepayPayBean) {
                            iPaymentActivity.getOrderInfoByOrderId(prepayPayBean.data);
                        }

                        @Override
                        public void onError(Throwable t) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 根据ID获取支付详情
     * @param context
     * @param orderId
     */
    public void getPayOrderInfoByOrder(Context context, String orderId) {
        try {
            paymentModel.getOrderInfo(context, orderId).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<OrderInfoBean>() {
                        @Override
                        public void onNext(OrderInfoBean prepayPayBean) {
                            iPaymentActivity.showOrderInfo(prepayPayBean);
                        }

                        @Override
                        public void onError(Throwable t) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取订单支付状态接口
     * @param context
     * @param orderId
     */
    public void getOrderPayStatus (Context context, String orderId) {
        try {
            paymentModel.GetOrderPayStatus(context, orderId).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<AmountPointsBean>() {
                        @Override
                        public void onNext(AmountPointsBean  amountPointsBean) {
                            iPaymentActivity.showGetOrderPayStatus(amountPointsBean);
                        }

                        @Override
                        public void onError(Throwable t) {
                            iPaymentActivity.stopPollingOrder();
                        }

                        @Override
                        public void onComplete() {
                            iPaymentActivity.pollingOrderPayStatus();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取订单支付状态接口
     * @param context
     * @param orderId
     */
//    public void getOrderPayStatus3 (final Context context,final String orderId) {
//         /*
//         * 步骤1：采用interval（）延迟发送
//         * 注：此处主要展示无限次轮询，若要实现有限次轮询，仅需将interval（）改成intervalRange（）即可
//         **/
//
//        Observable observable = Observable.intervalRange(1, 3, 3, 2, TimeUnit.SECONDS)
//                .doOnNext(new Consumer<Long>() {
//                    @Override
//                    public void accept(Long integer)  {
//                        Log.d("test", "第 " + integer + " 次轮询" );
//                        // b. 创建 网络请求接口 的实例
//                        // c. 采用Observable<...>形式 对 网络请求 进行封装
//                        Observable<AmountPointsBean> observable = paymentModel.GetOrderPayStatus(context, orderId);
//                        // d. 通过线程切换发送网络请求
//                        observable.subscribeOn(Schedulers.io())               // 切换到IO线程进行网络请求
//                                .observeOn(AndroidSchedulers.mainThread())  // 切换回到主线程 处理请求结果
//                                .subscribe(new Observer<AmountPointsBean>() {
//                                    @Override
//                                    public void onSubscribe(Disposable d) {
//
//                                    }
//
//                                    @Override
//                                    public void onNext(AmountPointsBean amountPointsBean) {
//                                        // e.接收服务器返回的数据
//                                        iPaymentActivity.showGetOrderPayStatus(amountPointsBean);
//                                        Log.i("test", "返回的数据" + amountPointsBean.data);
//                                    }
//
//                                    @Override
//                                    public void onError(Throwable e) {
//                                        Log.d("test", "请求失败");
//                                    }
//
//                                    @Override
//                                    public void onComplete() {
//
//                                    }
//                                });
//
//                    }
//                });
//
//    }

    // 设置变量 = 模拟轮询服务器次数
    private int i = 0 ;
//
//    public void getOrderPayStatus2(Context context, String orderId) throws Exception{
//
//        // 步骤3：采用Observable<...>形式 对 网络请求 进行封装
//        Observable<AmountPointsBean> observable = paymentModel.GetOrderPayStatus(context, orderId);
//
//        // 步骤4：发送网络请求 & 通过repeatWhen（）进行轮询
//        observable.repeatWhen(new Function<Observable<Object>, ObservableSource<?>>() {
//            @Override
//            // 在Function函数中，必须对输入的 Observable<Object>进行处理，此处使用flatMap操作符接收上游的数据
//            public ObservableSource<?> apply(@NonNull Observable<Object> objectObservable) throws Exception {
//                // 将原始 Observable 停止发送事件的标识（Complete（） /  Error（））转换成1个 Object 类型数据传递给1个新被观察者（Observable）
//                // 以此决定是否重新订阅 & 发送原来的 Observable，即轮询
//                // 此处有2种情况：
//                // 1. 若返回1个Complete（） /  Error（）事件，则不重新订阅 & 发送原来的 Observable，即轮询结束
//                // 2. 若返回其余事件，则重新订阅 & 发送原来的 Observable，即继续轮询
//                return objectObservable.flatMap(new Function<Object, ObservableSource<?>>() {
//                    @Override
//                    public ObservableSource<?> apply(@NonNull Object throwable) throws Exception {
//                        // 加入判断条件：当轮询次数 = 5次后，就停止轮询
//                        if (i > 3) {
//                            // 此处选择发送onError事件以结束轮询，因为可触发下游观察者的onError（）方法回调
//                            return Observable.error(new Throwable("轮询结束"));
//                        }
//                        // 若轮询次数＜4次，则发送1Next事件以继续轮询
//                        // 注：此处加入了delay操作符，作用 = 延迟一段时间发送（此处设置 = 2s），以实现轮询间间隔设置
//                        return Observable.just(1).delay(2000, TimeUnit.MILLISECONDS);
//                    }
//                });
//            }
//        }).subscribeOn(Schedulers.io())               // 切换到IO线程进行网络请求
//                .observeOn(AndroidSchedulers.mainThread())  // 切换回到主线程 处理请求结果
//                .subscribe(new Observer<AmountPointsBean>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                    }
//
//                    @Override
//                    public void onNext(AmountPointsBean amountPointsBean) {
//                        // e.接收服务器返回的数据
//                        iPaymentActivity.showGetOrderPayStatus(amountPointsBean);
//                        Log.d("test",  amountPointsBean.data);
//                        i++;
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        // 获取轮询结束信息
//                        Log.d("test",  e.toString());
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//    }

}
