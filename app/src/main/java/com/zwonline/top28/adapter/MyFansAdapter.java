package com.zwonline.top28.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zwonline.top28.R;
import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.service.ApiService;
import com.zwonline.top28.api.service.PayService;
import com.zwonline.top28.bean.AttentionBean;
import com.zwonline.top28.bean.MyFansBean;
import com.zwonline.top28.bean.message.MessageFollow;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.SignUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;

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
 * 描述：粉丝的适配器
 *
 * @author YSG
 * @date 2017/12/22
 */
public class MyFansAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MyFansBean.DataBean> list;
    private Context context;
    private SharedPreferencesUtils sp;
    private MessageFollow messageFollow;
    private int currentNum;

    public MyFansAdapter(List<MyFansBean.DataBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.myfans_item, parent, false);
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
        System.out.print("list====" + list.size());
        RequestOptions options = new RequestOptions().error(R.mipmap.no_photo_male).placeholder(R.mipmap.no_photo_male);
        if (!StringUtil.isEmpty(list.get(position).avatars)) {
            Glide.with(context).load(list.get(position).avatars).into(myViewHolder.avatars);
        } else {
            myViewHolder.avatars.setImageResource(R.mipmap.no_photo_male);
        }
        myViewHolder.nickname.setText(list.get(position).nickname);
        myViewHolder.signature.setText(list.get(position).signature);
        if (list.get(position).did_i_follow.equals("0") && !list.get(position).did_i_follow.equals("")) {
            myViewHolder.guanzhu.setText(R.string.common_btn_add_focus);
            myViewHolder.guanzhu.setBackgroundResource(R.drawable.guanzhu_shape);
            myViewHolder.guanzhu.setTextColor(Color.parseColor("#FF2B2B"));
        } else {
            myViewHolder.guanzhu.setText(R.string.common_followed);
            myViewHolder.guanzhu.setBackgroundResource(R.drawable.quxiaoguanzhu_shpae);
            myViewHolder.guanzhu.setTextColor(Color.parseColor("#DDDDDD"));
        }
        //点击关注
        myViewHolder.guanzhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {


                    SharedPreferencesUtils sp = SharedPreferencesUtils.getUtil();
                    String token = (String) sp.getKey(context, "dialog", "");
                    if (myViewHolder.guanzhu.getText().toString().trim().equals(context.getString(R.string.common_btn_add_focus))) {
                        mAnttent(token, position);
                    } else {
                        long timestamp = new Date().getTime() / 1000;//获取时间戳
//                        myViewHolder.guanzhu.setText(R.string.common_btn_add_focus);
                        currentNum = Integer.parseInt((String) sp.getKey(context, "follow", "0")) - 1;
                        messageFollow.followNum = currentNum + "";
                        sp.insertKey(context, "follow", messageFollow.followNum);
                        EventBus.getDefault().post(messageFollow);

//                        myViewHolder.guanzhu.setBackgroundResource(R.drawable.guanzhu_shape);
//                        myViewHolder.guanzhu.setTextColor(Color.parseColor("#FF2B2B"));
                        Map<String, String> map = new HashMap<>();
                        map.put("timestamp", String.valueOf(timestamp));
                        map.put("type", "un_follow");
                        map.put("token", token);
                        map.put("uid", list.get(position).uid);
                        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
                        Flowable<AttentionBean> flowable = ApiRetrofit.getInstance()
                                .getClientApi(PayService.class, Api.url)
                                .iAttention(token, String.valueOf(timestamp), sign, "un_follow", list.get(position).uid);
                        flowable.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeWith(new DisposableSubscriber<AttentionBean>() {
                                    @Override
                                    public void onNext(AttentionBean attentionBean) {
                                        list.get(position).did_i_follow = BizConstant.IS_FAIL;
                                        ToastUtils.showToast(context, attentionBean.msg);
                                        notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onError(Throwable t) {

                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nickname, signature, no;
        ImageView avatars;
        TextView guanzhu;

        public MyViewHolder(View itemView) {
            super(itemView);
            nickname = (TextView) itemView.findViewById(R.id.nickname);
            signature = (TextView) itemView.findViewById(R.id.signature);
            avatars = (ImageView) itemView.findViewById(R.id.avatars);
            no = (TextView) itemView.findViewById(R.id.no);
            guanzhu = (TextView) itemView.findViewById(R.id.consult);
        }
    }

    public OnClickItemListener onClickItemListener;

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }

    public interface OnClickItemListener {
        void setOnItemClick(int position, View view);
    }

    //关注的请求
    public void mAnttent(String token, final int position) throws IOException {
        messageFollow.followNum = currentNum + "";
        long timestamp = new Date().getTime() / 1000;
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
                        list.get(position).did_i_follow = BizConstant.IS_SUC;
                        ToastUtils.showToast(context, attentionBean.msg);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }



}
