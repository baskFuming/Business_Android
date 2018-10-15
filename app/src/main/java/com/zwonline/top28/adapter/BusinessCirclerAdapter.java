package com.zwonline.top28.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zwonline.top28.R;
import com.zwonline.top28.activity.AddGetRelistActivity;
import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.service.ApiService;
import com.zwonline.top28.api.service.PayService;
import com.zwonline.top28.bean.AttentionBean;
import com.zwonline.top28.bean.BusinessCircleBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.SignUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * 推荐关注的适配器
 */
public class BusinessCirclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<BusinessCircleBean.DataBean> dlist;
    private String User_id;
    View views;
    private SharedPreferencesUtils sp;
    private AKeyAttentionSetOnclick aKeyAttentionSetOnclick;
    private boolean islogins;
    private String uid;

    public BusinessCirclerAdapter(Context context, List<BusinessCircleBean.DataBean> dlist) {
        this.context = context;
        this.dlist = dlist;
    }

    //数据刷新
    public void changeData(List<BusinessCircleBean.DataBean> list) {
        if (list == null) {
            dlist = list;
        } else {
            this.dlist.clear();
            dlist.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.busi_circler_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        //  textView.setText(labels[i]);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        sp = SharedPreferencesUtils.getUtil();
        islogins = (boolean) sp.getKey(context, "islogin", false);
        uid = (String) sp.getKey(context, "uid", "");
        final BusinessCircleBean.DataBean bean = dlist.get(position);
        User_id = bean.list.get(position).uid;
        ((MyViewHolder) holder).te_circler_name.setText(bean.title);
        //加载子控件数据
        final BusinessProductAdapter productAdapter = new BusinessProductAdapter(bean.list, context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        ((MyViewHolder) holder).xy_circler_item_xcy.setLayoutManager(linearLayoutManager);
        ((MyViewHolder) holder).xy_circler_item_xcy.setAdapter(productAdapter);
        ((MyViewHolder) holder).xy_circler_item_xcy.setVisibility(View.VISIBLE);
        ((MyViewHolder) holder).xy_circler_item_xcy.setVisibility(View.VISIBLE);

        /**
         * 查看更多
         */
        productAdapter.setOnClickItemListener(new BusinessProductAdapter.OnClickItemListener() {
            @Override
            public void setOnItemClick(View view, int positions) {
                if (positions == bean.list.size()) {
                    Intent intent = new Intent(context, AddGetRelistActivity.class);
                    intent.putExtra("item_id", bean.item_id + "");
                    intent.putExtra("title", bean.title + "");
                    context.startActivity(intent);
                }

            }
        });
        //点击处理这里可以随机修改
        ((MyViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickItemListener != null) {
                    onClickItemListener.setOnItemClick(v, position);
                }
            }
        });


        /**
         * 一键关注
         */
        ((MyViewHolder) holder).te_busi_attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                aKeyAttentionSetOnclick.onclick(v, position);
            }
        });
        /**
         * 关注、取消关注
         */
        productAdapter.attentionSetOnclick(new BusinessProductAdapter.AttentionInterface() {
            @Override
            public void onclick(View view, int positions, String did_follow, TextView textView) {
                if (islogins) {
                    if (StringUtil.isNotEmpty(did_follow) && did_follow.equals(BizConstant.IS_FAIL)) {

//                        //重新赋1,添加状态
                        for (int i = 0; i < dlist.size(); i++) {
                            for (int j = 0; j < dlist.get(i).list.size(); j++) {
                                if (bean.list.get(positions).uid.equals(dlist.get(i).list.get(j).uid)) {
                                    dlist.get(i).list.get(j).did_i_follow = BizConstant.IS_SUC;
                                }
                            }
                        }
                        try {
                            attention(context, "follow", bean.list.get(positions).uid, BizConstant.ALREADY_FAVORITE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (StringUtil.isNotEmpty(did_follow) && did_follow.equals(BizConstant.IS_SUC)) {
                        for (int i = 0; i < dlist.size(); i++) {
                            for (int j = 0; j < dlist.get(i).list.size(); j++) {
                                if (bean.list.get(positions).uid.equals(dlist.get(i).list.get(j).uid)) {
                                    dlist.get(i).list.get(j).did_i_follow = BizConstant.IS_FAIL;
                                }
                            }
                        }

                        try {
                            UnAttention(context, "un_follow", bean.list.get(positions).uid);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    notifyDataSetChanged();
                } else {
                    ToastUtils.showToast(context, "请先登录");
                }

            }
        });
    }

    private void attentionBtn(int position) {

    }

    @Override
    public int getItemCount() {
        return dlist != null ? dlist.size() : 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.circler_name)
        TextView te_circler_name;
        //一键关注未处理
        @BindView(R.id.busi_attention)
        TextView te_busi_attention;
        @BindView(R.id.circler_item_xcy)
        RecyclerView xy_circler_item_xcy;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public OnClickItemListener onClickItemListener;

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }

    public interface OnClickItemListener {
        void setOnItemClick(View view, int position);
    }


    //关注
    public Flowable<AttentionBean> attention(final Context context, String type, String uid, String allow_be_call) throws IOException {
        long timestamp = new Date().getTime() / 1000;//获取时间戳
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("type", type);
        map.put("token", token);
        map.put("uid", uid);
        map.put("allow_be_call", allow_be_call);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<AttentionBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iAttention(token, String.valueOf(timestamp), sign, type, uid, allow_be_call);
        flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<AttentionBean>() {
                    @Override
                    public void onNext(AttentionBean attentionBean) {
                        if (attentionBean.status == 1) {
                            ToastUtils.showToast(context, attentionBean.msg);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return flowable;
    }

    //取消关注的接口
    public Flowable<AttentionBean> UnAttention(final Context context, String type, String uid) throws IOException {
        long timestamp = new Date().getTime() / 1000;//获取时间戳
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("type", type);
        map.put("token", token);
        map.put("uid", uid);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<AttentionBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(PayService.class, Api.url)
                .iAttention(token, String.valueOf(timestamp), sign, type, uid);
        flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<AttentionBean>() {
                    @Override
                    public void onNext(AttentionBean attentionBean) {
                        ToastUtils.showToast(context, attentionBean.msg);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return flowable;
    }

    /**
     * `
     * 按钮点击事件需要的方法
     */
    public void aKeyAttentionSetOnclick(AKeyAttentionSetOnclick aKeyAttentionSetOnclick) {
        this.aKeyAttentionSetOnclick = aKeyAttentionSetOnclick;
    }

    /**
     * 按钮点击事件对应的接口
     */
    public interface AKeyAttentionSetOnclick {
        public void onclick(View view, int position);
    }

}
