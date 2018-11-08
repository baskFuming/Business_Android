package com.zwonline.top28.nim.shangjibi;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.netease.nim.uikit.business.chatroom.adapter.ChatRoomMsgAdapter;
import com.netease.nim.uikit.business.session.module.ModuleProxy;
import com.netease.nim.uikit.business.session.module.list.MsgAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.zwonline.top28.R;
import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.service.PayService;
import com.zwonline.top28.api.subscriber.BaseDisposableSubscriber;
import com.zwonline.top28.api.subscriber.BaseDisposableSubscribers;
import com.zwonline.top28.bean.GetHongBaoBean;
import com.zwonline.top28.bean.HongBaoLeftCountBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.nim.yangfen.MsgViewHolder;
import com.zwonline.top28.nim.yangfen.SnatchYangFenActivity;
import com.zwonline.top28.nim.yangfen.YangFenPopuWindow;
import com.zwonline.top28.tip.toast.ToastUtil;
import com.zwonline.top28.utils.ImageViewPlus;
import com.zwonline.top28.utils.MyYAnimation;
import com.zwonline.top28.utils.SignUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.click.AntiShake;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 点击打开商机币红包
 */
public class SJBViewHolderLink extends MsgViewHolder {

    private String redpackUserToken;
    private TextView send_title;
    private ImageView openRedPack;

    public SJBViewHolderLink(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    private RelativeLayout sendView, revView;
    private TextView sendContentText, revContentText;    // 红包描述
    private TextView sendTitleText, revTitleText;    // 红包名称
    private YangFenPopuWindow yangFenPopuWindow;
    private int totalPackageCount;
    private int hasGetPackageCount;
    private MyYAnimation myYAnimation;

    @Override
    protected int getContentResId() {
        return R.layout.red_packet_item;
    }

    @Override
    protected void inflateContentView() {
        sendContentText = findViewById(R.id.tv_bri_mess_send);
        sendTitleText = findViewById(R.id.tv_bri_name_send);
        sendView = findViewById(R.id.bri_send);
        revContentText = findViewById(R.id.tv_bri_mess_rev);
        revTitleText = findViewById(R.id.tv_bri_name_rev);
        revView = findViewById(R.id.bri_rev);
        revView.setBackgroundResource(R.mipmap.message_sjb_bg);
        sendView.setBackgroundResource(R.mipmap.message_sjb_bg);
    }

    @Override
    protected void bindContentView() {
        SJBAttachment attachment = (SJBAttachment) message.getAttachment();

        if (!isReceivedMessage()) {// 消息方向，自己发送的
            sendView.setVisibility(View.VISIBLE);
            revView.setVisibility(View.GONE);
            sendContentText.setText(attachment.getTitle());
            sendTitleText.setText("商机币红包");
        } else {
            sendView.setVisibility(View.GONE);
            revView.setVisibility(View.VISIBLE);
            revContentText.setText(attachment.getTitle());
            revTitleText.setText("商机币红包");
        }
    }

    @Override
    protected int leftBackground() {
        return R.color.transparent;
    }

    @Override
    protected int rightBackground() {
        return R.color.transparent;
    }

    @Override
    protected void onItemClick() {
        if (AntiShake.check(view.getId())) {    //判断是否多次点击
            return;
        }
        SJBAttachment attachment = (SJBAttachment) message.getAttachment();
        redpackUserToken = attachment.getRedpackUserToken();
        BaseMultiItemFetchLoadAdapter adapter = getAdapter();
        ModuleProxy proxy = null;
        if (adapter instanceof MsgAdapter) {
            proxy = ((MsgAdapter) adapter).getContainer().proxy;
        } else if (adapter instanceof ChatRoomMsgAdapter) {
            proxy = ((ChatRoomMsgAdapter) adapter).getContainer().proxy;
        }
        hongBaoCount(attachment.getRedPacketId());
    }


    private View.OnClickListener listener = new View.OnClickListener() {


        @Override
        public void onClick(View v) {
            if (AntiShake.check(v.getId())) {    //判断是否多次点击
                return;
            }
            switch (v.getId()) {
                case R.id.open_redpack:
                    myYAnimation = new MyYAnimation();
                    myYAnimation.setRepeatCount(Animation.INFINITE); //旋转的次数（无数次）
                    openRedPack.startAnimation(myYAnimation);
                    SJBAttachment attachment = (SJBAttachment) message.getAttachment();
                    getHongBao(attachment.getRedPacketId());
                    break;
                case R.id.look_details:
                    SJBAttachment attachments = (SJBAttachment) message.getAttachment();
                    Intent intent = new Intent(context.getApplicationContext(), SnatchYangFenActivity.class);
                    intent.putExtra("hongbao_id", attachments.getRedPacketId() + "");
                    intent.putExtra("send_name", attachments.getRedpackUserName());
                    intent.putExtra("title", attachments.getTitle());
                    intent.putExtra("send_avatars", attachments.getRedpackUserHeader());
                    intent.putExtra("redpackType", attachments.getRedpackType() + "");
                    intent.putExtra("packge_type", BizConstant.NEW);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    yangFenPopuWindow.dismiss();
                    yangFenPopuWindow.backgroundAlpha((Activity) context, 1f);
                    break;
            }
        }
    };


    /**
     * 查看红包剩余量接口
     *
     * @param hongbao_id
     */
    public void hongBaoCount(String hongbao_id) {
        try {
            long timestamp = new Date().getTime() / 1000;//获取时间戳
            String token = (String) sp.getKey(context, "dialog", "");
            Map<String, String> map = new HashMap<>();
            map.put("timestamp", String.valueOf(timestamp));
            map.put("hongbao_id", hongbao_id);
            map.put("token", token);
            SignUtils.removeNullValue(map);
            String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
            Flowable<HongBaoLeftCountBean> flowable = ApiRetrofit.getInstance()
                    .getClientApi(PayService.class, Api.url)
                    .getBocHongbaoLeftCount(String.valueOf(timestamp), token, hongbao_id, sign);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<HongBaoLeftCountBean>(context) {


                        @Override
                        protected void onBaseNext(HongBaoLeftCountBean hongBaoLeftCountBean) {
                            SJBAttachment attachment = (SJBAttachment) message.getAttachment();
                            // 拆红包
                            if (hongBaoLeftCountBean.status == 1) {
//                                sendView.setBackgroundResource(R.mipmap.message_gift2_bg);
//                                revView.setBackgroundResource(R.mipmap.message_gift2_bg);
                                hasGetPackageCount = hongBaoLeftCountBean.data.hasGetPackageCount;
                                totalPackageCount = hongBaoLeftCountBean.data.totalPackageCount;
                                String getAmount = hongBaoLeftCountBean.data.getAmount;
                                //判断是否已抢过红包
                                if (StringUtil.isNotEmpty(getAmount) && getAmount.equals(BizConstant.ENTERPRISE_tRUE)) {
                                    yangFenPopuWindow = new YangFenPopuWindow((Activity) context, listener);
                                    yangFenPopuWindow.showAtLocation(view, Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                                    View yangFenView = yangFenPopuWindow.getContentView();//send_head
                                    openRedPack = (ImageView) yangFenView.findViewById(R.id.open_redpack);
                                    ImageViewPlus send_head = (ImageViewPlus) yangFenView.findViewById(R.id.send_head);
                                    TextView slow_no_more = (TextView) yangFenView.findViewById(R.id.slow_no_more);
                                    slow_no_more.setText("发了一个商机币红包");
                                    TextView look_details = (TextView) yangFenView.findViewById(R.id.look_details);
                                    send_title = (TextView) yangFenView.findViewById(R.id.send_title);
                                    TextView userName = (TextView) yangFenView.findViewById(R.id.user_name);
                                    if (hongBaoLeftCountBean.data.expireFlag == 0) {
                                        openRedPack.setVisibility(View.VISIBLE);
                                        if (hasGetPackageCount < totalPackageCount) {
                                            openRedPack.setVisibility(View.VISIBLE);
                                        } else {
                                            openRedPack.setVisibility(View.GONE);
//                                            revView.setBackgroundResource(R.mipmap.message_gift2_bg);
                                            send_title.setText("手慢了，红包抢完了");

                                        }
                                    } else if (hongBaoLeftCountBean.data.expireFlag == 1) {
                                        openRedPack.setVisibility(View.GONE);
                                        send_title.setText("红包已过期");
                                    }


                                    userName.setText(attachment.getRedpackUserName());
                                    RequestOptions options = new RequestOptions().placeholder(R.mipmap.no_photo_male).error(R.mipmap.no_photo_male);
                                    Glide.with(context).load(attachment.getRedpackUserHeader()).apply(options).into(send_head);
                                } else {
                                    Intent intent = new Intent(context.getApplicationContext(), SnatchYangFenActivity.class);
                                    intent.putExtra("hongbao_id", attachment.getRedPacketId() + "");
                                    intent.putExtra("send_name", attachment.getRedpackUserName());
                                    intent.putExtra("title", attachment.getTitle());
                                    intent.putExtra("send_avatars", attachment.getRedpackUserHeader());
                                    intent.putExtra("redpackType", attachment.getRedpackType() + "");
                                    intent.putExtra("packge_type", BizConstant.NEW);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
                                }

                            } else {
                                ToastUtil.showToast(context.getApplicationContext(), hongBaoLeftCountBean.msg);
                            }


                        }

                        @Override
                        protected String getTitleMsg() {
                            return null;
                        }

                        @Override
                        protected boolean isNeedProgressDialog() {
                            return true;
                        }

                        @Override
                        protected void onBaseComplete() {

                        }


                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 抢红包
     *
     * @param hongbao_id
     */
    public void getHongBao(String hongbao_id) {
        try {
            long timestamp = new Date().getTime() / 1000;//获取时间戳
            String token = (String) sp.getKey(context, "dialog", "");
            Map<String, String> map = new HashMap<>();
            map.put("timestamp", String.valueOf(timestamp));
            map.put("hongbao_id", hongbao_id);
            map.put("token", token);
            SignUtils.removeNullValue(map);
            String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
            Flowable<GetHongBaoBean> flowable = ApiRetrofit.getInstance()
                    .getClientApi(PayService.class, Api.url)
                    .getBocHongbao(String.valueOf(timestamp), token, hongbao_id, sign);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscribers<GetHongBaoBean>(context) {

                        @Override
                        protected void onBaseNext(GetHongBaoBean getHongBaoBean) {
                            // 拆红包
                            if (getHongBaoBean.status == 1) {

                                SJBAttachment attachment = (SJBAttachment) message.getAttachment();
                                Intent intent = new Intent(context.getApplicationContext(), SnatchYangFenActivity.class);
                                intent.putExtra("get_amount", getHongBaoBean.data.get_amount + "");
                                intent.putExtra("hongbao_id", attachment.getRedPacketId() + "");
                                intent.putExtra("send_name", attachment.getRedpackUserName());
                                intent.putExtra("title", attachment.getTitle());
                                intent.putExtra("send_avatars", attachment.getRedpackUserHeader());
                                intent.putExtra("redpackType", attachment.getRedpackType() + "");
                                intent.putExtra("packge_type", BizConstant.NEW);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                                openRedPack.clearAnimation();
                                yangFenPopuWindow.dismiss();
                                yangFenPopuWindow.backgroundAlpha((Activity) context, 1f);
                            } else {
                                ToastUtil.showToast(context.getApplicationContext(), getHongBaoBean.msg);
                            }


                        }

                        @Override
                        protected String getTitleMsg() {
                            return null;
                        }

                        @Override
                        public void onError(Throwable t) {
                            openRedPack.clearAnimation();
                            openRedPack.setVisibility(View.GONE);
                            send_title.setText("手慢了，红包抢完了");
                        }

                        @Override
                        protected boolean isNeedProgressDialog() {

                            return false;
                        }

                        @Override
                        protected void onBaseComplete() {

                        }


                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
