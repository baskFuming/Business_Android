package com.zwonline.top28.presenter;

import android.content.Context;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.QrCodeBean;
import com.zwonline.top28.model.QrCodeModel;
import com.zwonline.top28.view.IQrCode;

import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * @author YSG
 * @desc生成二维码的p层
 * @date ${Date}
 */
public class QrCodePresenter extends BasePresenter<IQrCode> {
    private QrCodeModel qrCodeModel;
    private IQrCode iQrCode;
    public QrCodePresenter(IQrCode iQrCode){
        this.iQrCode=iQrCode;
        qrCodeModel=new QrCodeModel();
    }
    public  void mQrCode(Context context,Double amount, String projectId,String contractId){
        try {
            qrCodeModel.Qrcode(context,amount, projectId,contractId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<QrCodeBean>() {
                        @Override
                        public void onNext(QrCodeBean qrCodeBean) {
                            iQrCode.showQrCode(qrCodeBean);
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
}
