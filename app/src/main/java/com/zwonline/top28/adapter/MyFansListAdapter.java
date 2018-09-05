package com.zwonline.top28.adapter;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.netease.nim.uikit.api.NimUIKit;
import com.zwonline.top28.R;
import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.ApiService;
import com.zwonline.top28.api.PayService;
import com.zwonline.top28.bean.AmountPointsBean;
import com.zwonline.top28.bean.AttentionBean;
import com.zwonline.top28.bean.ExamineChatBean;
import com.zwonline.top28.bean.MyFansBean;
import com.zwonline.top28.bean.message.MessageFollow;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.utils.MyYAnimation;
import com.zwonline.top28.utils.popwindow.AttentionPopwindow;
import com.zwonline.top28.utils.ImageViewPlus;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.SignUtils;
import com.zwonline.top28.utils.StringUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * 描述：我的粉丝的适配器
 *
 * @author YSG
 * @date 2018/3/7
 */
public class MyFansListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MyFansBean.DataBean> list;
    public Context context;
    private SharedPreferencesUtils sp;
    private MessageFollow messageFollow;
    private int currentNum;
    private AttentionPopwindow mPopwindow;
    private String uid;
    private String attention;
    private int positions;
    private String costPoint;
    private String fansAttentions = "1";
    private String isFollow;
    private Button attentiones;
    private AttentionMomentInterface attentionMomentInterface;

    public MyFansListAdapter(List<MyFansBean.DataBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.myfanss_item, parent, false);
        final MyViewHolder holder = new MyViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItemListener.setOnItemClick(holder.getPosition(), v);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        sp = SharedPreferencesUtils.getUtil();
        messageFollow = new MessageFollow();
        RequestOptions options = new RequestOptions().error(R.mipmap.no_photo_male).placeholder(R.mipmap.no_photo_male);
        Glide.with(context).load(list.get(position).avatars).apply(options).into(myViewHolder.avatars);
        myViewHolder.nickname.setText(list.get(position).nickname);
        myViewHolder.signature.setText(list.get(position).signature);
        String did_i_follow = list.get(position).did_i_follow;
        if (did_i_follow.equals(BizConstant.IS_FAIL)) {
            myViewHolder.consult.setText(R.string.common_btn_add_focus);
            myViewHolder.consult.setTextColor(Color.parseColor("#FF2B2B"));
            myViewHolder.consult.setBackgroundResource(R.drawable.guanzhu_shape);
        } else {
            myViewHolder.consult.setText(R.string.common_followed);
            myViewHolder.consult.setTextColor(Color.parseColor("#DDDDDD"));
            myViewHolder.consult.setBackgroundResource(R.drawable.quxiaoguanzhu_shpae);
        }
        myViewHolder.consult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attentionMomentInterface.onclick(v, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nickname, signature, consult;
        ImageViewPlus avatars;

        public MyViewHolder(View itemView) {
            super(itemView);
            nickname = (TextView) itemView.findViewById(R.id.nickname);
            signature = (TextView) itemView.findViewById(R.id.signature);
            avatars = (ImageViewPlus) itemView.findViewById(R.id.avatars);
            consult = (TextView) itemView.findViewById(R.id.attentions);
        }
    }

    public OnClickItemListener onClickItemListener;

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }

    public interface OnClickItemListener {
        void setOnItemClick(int position, View view);
    }

    public void attentionMomentSetOnclick(AttentionMomentInterface attentionMomentInterface) {
        this.attentionMomentInterface = attentionMomentInterface;
    }

    /**
     * 按钮点击事件对应的接口
     */
    public interface AttentionMomentInterface {
        public void onclick(View view, int position);
    }

    private MyViewHolder myViewHolder;
    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {


        public void onClick(View v) {
            if (Build.VERSION.SDK_INT >= 23) {
                String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
                ActivityCompat.requestPermissions((Activity) context, mPermissionList, 123);
            }
            mPopwindow.setOutsideTouchable(true);
            mPopwindow.dismiss();
            mPopwindow.backgroundAlpha((Activity) context, 1f);
            switch (v.getId()) {
                case R.id.call:

                    break;
                case R.id.chat:
//                    NimUIKit.startP2PSession(context, uid);
                    try {
                        long timestamp = new Date().getTime() / 1000;//获取时间戳
                        SharedPreferencesUtils sp = SharedPreferencesUtils.getUtil();
                        String token = (String) sp.getKey(context, "dialog", "");
                        //currentNum = Integer.parseInt((String) sp.getKey(context, "follow", "0")) + 1;
                        Map<String, String> map = new HashMap<>();
                        map.put("timestamp", String.valueOf(timestamp));
                        map.put("token", token);
                        map.put("uid", list.get(positions).uid);
                        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);

                        Flowable<ExamineChatBean> flowable = ApiRetrofit.getInstance()
                                .getClientApi(PayService.class, Api.url)
                                .iExamineChat(String.valueOf(timestamp), token, list.get(positions).uid, sign);
                        flowable.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeWith(new DisposableSubscriber<ExamineChatBean>() {

                                    private String chatted;

                                    @Override
                                    public void onNext(ExamineChatBean attentionChatBean) {
                                        chatted = attentionChatBean.data.chatted;
                                        costPoint = attentionChatBean.data.cost_point;
                                        if (chatted.equals("1")) {
                                            onLineChat();
                                            // 打开单聊界面
                                            NimUIKit.startP2PSession(context, list.get(positions).uid);
                                        } else if (chatted.equals("0")) {
                                            showNormalDialog();
                                        }
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
                    break;
                case R.id.cancel_attention:
                    try {
                        long timestamp = new Date().getTime() / 1000;//获取时间戳
                        SharedPreferencesUtils sp = SharedPreferencesUtils.getUtil();
                        String token = (String) sp.getKey(context, "dialog", "");
                        String unFollow = attentiones.getText().toString();
                        if (StringUtil.isNotEmpty(unFollow) && unFollow.equals(R.string.common_btn_add_focus)) {
                            showNormalDialogFollow(String.valueOf(timestamp), token, attentiones, positions);
                            notifyDataSetChanged();
                        } else if (StringUtil.isNotEmpty(unFollow) && unFollow.equals(R.string.cancel_attention)) {
                            //currentNum = Integer.parseInt((String) sp.getKey(context, "follow", "0")) + 1;
                            Map<String, String> map = new HashMap<>();
                            map.put("timestamp", String.valueOf(timestamp));
                            map.put("type", "un_follow");
                            map.put("token", token);
                            map.put("uid", list.get(positions).uid);
                            String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
                            Flowable<AttentionBean> flowable = ApiRetrofit.getInstance()
                                    .getClientApi(PayService.class, Api.url)
                                    .iAttention(token, String.valueOf(timestamp), sign, "un_follow", list.get(positions).uid);
                            flowable.subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeWith(new DisposableSubscriber<AttentionBean>() {
                                        @Override
                                        public void onNext(AttentionBean attentionBean) {

                                        }

                                        @Override
                                        public void onError(Throwable t) {

                                        }

                                        @Override
                                        public void onComplete() {

                                        }
                                    });
                            notifyDataSetChanged();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }

    };

    //请求在线聊天
    public void onLineChat() {
        try {
            long timestamp = new Date().getTime() / 1000;//获取时间戳
            SharedPreferencesUtils sp = SharedPreferencesUtils.getUtil();
            String token = (String) sp.getKey(context, "dialog", "");
            //currentNum = Integer.parseInt((String) sp.getKey(context, "follow", "0")) + 1;
            Map<String, String> map = new HashMap<>();
            map.put("timestamp", String.valueOf(timestamp));
            map.put("token", token);
            map.put("uid", list.get(positions).uid);
            String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);

            Flowable<AmountPointsBean> flowable = ApiRetrofit.getInstance()
                    .getClientApi(PayService.class, Api.url)
                    .iOnLineChated(String.valueOf(timestamp), token, list.get(positions).uid, sign);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<AmountPointsBean>() {

                        @Override
                        public void onNext(AmountPointsBean attentionChatBean) {

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

    //Dialog弹窗
    private void showNormalDialog() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(context);
        normalDialog.setMessage(context.getString(R.string.consume_integral) + costPoint + context.getString(R.string.coin_bole_coin));
        normalDialog.setPositiveButton(R.string.chat,
                new DialogInterface.OnClickListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onLineChat();
                        // 打开单聊界面
                        NimUIKit.startP2PSession(context, list.get(positions).uid);
                    }
                });
        normalDialog.setNegativeButton(R.string.common_cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();
    }


    //关注的请求
    public void mAnttent(String timestamp, String token, int position) throws IOException {
        messageFollow.followNum = currentNum + "";
        sp.insertKey(context, "follow", messageFollow.followNum);
        EventBus.getDefault().post(messageFollow);
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("type", "follow");
        map.put("token", token);
        map.put("uid", list.get(position).uid);
        map.put("allow_be_call", BizConstant.ALREADY_FAVORITE);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<AttentionBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iAttention(token, String.valueOf(timestamp), sign, "follow", list.get(position).uid, BizConstant.ALREADY_FAVORITE);
        flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<AttentionBean>() {
                    @Override
                    public void onNext(AttentionBean attentionBean) {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    //关注是否愿意接听电话
    private void showNormalDialogFollow(final String timestamp, final String token, final Button consult, final int position) {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(context);
        normalDialog.setMessage(R.string.is_willing_answer_calls);
        normalDialog.setPositiveButton(R.string.willing,
                new DialogInterface.OnClickListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            mAnttent(timestamp, token, position);
                            consult.setText(R.string.cancel_attention);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        normalDialog.setNegativeButton(R.string.unwillingness,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            mAnttent(timestamp, token, position);
                            consult.setText(R.string.cancel_attention);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        // 显示
        normalDialog.show();
    }
}
