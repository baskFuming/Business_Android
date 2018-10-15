package com.zwonline.top28.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.api.NimUIKit;
import com.zwonline.top28.R;
import com.zwonline.top28.activity.WithoutCodeLoginActivity;
import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.service.PayService;
import com.zwonline.top28.bean.AmountPointsBean;
import com.zwonline.top28.bean.BusinessListBean;
import com.zwonline.top28.bean.ExamineChatBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.tip.toast.ToastUtil;
import com.zwonline.top28.utils.ImageViewPlus;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.SignUtils;
import com.zwonline.top28.utils.StringUtil;

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
 * Created by YU on 2017/12/12.
 * 商机的适配器
 */

public class BusinessAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<BusinessListBean.DataBean> list;
    private Context context;
    private SharedPreferencesUtils sp;
    private boolean islogins;
    private String cost_point;
    private String chatted;
    private String token;

    public BusinessAdapter(List<BusinessListBean.DataBean> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.business_item, parent, false);
        final BusinessViewHolder holder = new BusinessViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItemListener.setOnItemClick(holder.getPosition(),v);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        sp = SharedPreferencesUtils.getUtil();
        islogins = (boolean) sp.getKey(context, "islogin", false);
        BusinessViewHolder examineViewHolder = (BusinessViewHolder) holder;
        examineViewHolder.title.setText(list.get(position).title);
        Glide.with(context).load(list.get(position).logo).into(examineViewHolder.image);
        examineViewHolder.sign.setText(list.get(position).sign);
        final String uid = list.get(position).uid;
        final String pid = list.get(position).id;
        examineViewHolder.cate_name.setText(list.get(position).cate_name);

        token = (String) sp.getKey(context, "dialog", "");

        examineViewHolder.consult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (islogins) {
                    try {

                        mExamineChat(uid,pid, token);//检查是否和某人聊过天
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // 打开单聊界面
//                    MessageInfo.startP2PSession(context, uid);
                } else {
                    Intent intent=new Intent(context, WithoutCodeLoginActivity.class);
                    intent.putExtra("login_type", BizConstant.BUSINESS_LOGIN);
                    context.startActivity(intent);
                    Toast.makeText(context, R.string.user_not_login, Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class BusinessViewHolder extends RecyclerView.ViewHolder {
        TextView title, sign, cate_name;
        ImageViewPlus image;
        Button consult;

        public BusinessViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            image = (ImageViewPlus) itemView.findViewById(R.id.image);
            consult = (Button) itemView.findViewById(R.id.consult);
            sign = (TextView) itemView.findViewById(R.id.busin_centent);
            cate_name = (TextView) itemView.findViewById(R.id.cate_name);
        }
    }

    public OnClickItemListener onClickItemListener;

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }

    public interface OnClickItemListener {
        void setOnItemClick(int position,View view);
    }

    //Dialog弹窗
    private void showNormalDialog(final String uid, final String pid) {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(context);
        normalDialog.setMessage(context.getString(R.string.consume_integral) + cost_point + context.getString(R.string.coin_bole_coin));
        normalDialog.setPositiveButton(R.string.chat,
                new DialogInterface.OnClickListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mOnLineChat(uid, pid);
                        // 打开单聊界面
                        NimUIKit.startP2PSession(context, uid);
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

    public void mExamineChat(final String uid, final String pid, String token) throws IOException {
        try {
//获取时间戳
           long timestamp = new Date().getTime() / 1000;
            Map<String, String> map = new HashMap<>();
            map.put("timestamp", String.valueOf(timestamp));
            map.put("token", token);
            map.put("uid", uid);
            String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
            Flowable<ExamineChatBean> flowable = ApiRetrofit.getInstance()
                    .getClientApi(PayService.class, Api.url)
                    .iExamineChat(String.valueOf(timestamp), token, uid, sign);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<ExamineChatBean>() {
                        @Override
                        public void onNext(ExamineChatBean attentionBean) {
                            chatted = attentionBean.data.chatted;
                            cost_point = attentionBean.data.cost_point;
                        }

                        @Override
                        public void onError(Throwable t) {

                        }

                        @Override
                        public void onComplete() {
                            if (!StringUtil.isEmpty(chatted)&&chatted.equals("1")) {
                                mOnLineChat(uid, pid);
                                // 打开单聊界面
                                NimUIKit.startP2PSession(context, uid);
                            } else if (!StringUtil.isEmpty(chatted)&&chatted.equals("0")) {
                                showNormalDialog(uid,pid);
                            }else {
                                NimUIKit.startP2PSession(context, uid);
                                ToastUtil.showToast(context,"chatted="+chatted);
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //在线聊天
    public void mOnLineChat(String uid, String pid) {
        try {
            long timestamp = new Date().getTime() / 1000;
            Map<String, String> map = new HashMap<>();
            map.put("timestamp", String.valueOf(timestamp));
            map.put("token", token);
            map.put("uid", uid);
            map.put("project_id",pid);
            String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
            Flowable<AmountPointsBean> flowable = ApiRetrofit.getInstance()
                    .getClientApi(PayService.class, Api.url)
                    .iOnLineChats(String.valueOf(timestamp), token, uid, pid, sign);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<AmountPointsBean>() {
                        @Override
                        public void onNext(AmountPointsBean attentionBean) {
                        }

                        @Override
                        public void onError(Throwable t) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}