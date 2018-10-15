package com.zwonline.top28.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zwonline.top28.R;
import com.zwonline.top28.activity.HomePageActivity;
import com.zwonline.top28.bean.BusinessCircleBean;
import com.zwonline.top28.bean.message.MessageFollow;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.utils.ImageViewPlus;
import com.zwonline.top28.utils.SharedPreferencesUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.netease.nim.uikit.common.util.sys.ScreenUtil.dip2px;

/**
 * 遍历数据   index :  position
 * <p>
 * 按照接口返回的数据uid是唯一的，判断uid遍历选择相同的数据
 */
public class BusinessProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //解决复用问题
    private SparseBooleanArray mCheckStates = new SparseBooleanArray();
    private DisplayMetrics dm;
    private List<BusinessCircleBean.DataBean.ListBean> list;
    private Context context;
    private boolean isAttention = true;
    private String uid;
    private SharedPreferencesUtils sp;
    private MessageFollow messageFollow;
    private int currentNum;
    private int fuction;
    private View view;
    private AttentionInterface attentionInterface;

    public BusinessProductAdapter(List<BusinessCircleBean.DataBean.ListBean> list, Context context) {
        this.list = list;
        this.context = context;
        dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
    }

    //数据刷新
    public void changeData(List<BusinessCircleBean.DataBean.ListBean> dlist) {
        if (list == null) {
            this.list = dlist;
        } else {
            this.list.addAll(dlist);
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.busin_product_item, parent, false);
        //动态设置ImageView的宽高，根据自己每行item数量计算
//         dm.widthPixels-dip2px(20)即屏幕宽度-左右10dp+10dp=20dp再转换为px的宽度，最后/3得到每个item的宽高
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams((dm.widthPixels - dip2px(90)) / 3, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        RequestOptions requestOptions = new RequestOptions().placeholder(R.mipmap.no_photo_male).error(R.mipmap.no_photo_male).centerCrop();
        if (position == list.size()) {
            //用户头像
            Glide.with(context).load(R.mipmap.friends_more)
                    .into(((MyViewHolder) holder).imgUser);
            ((MyViewHolder) holder).tename.setText("查看更多");
            ((MyViewHolder) holder).tenameAdd.setVisibility(View.GONE);
            ((MyViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickItemListener != null) {
                        onClickItemListener.setOnItemClick(v, list.size());
                    }
                }
            });
        } else {
            //获取Tag值
            ((MyViewHolder) holder).tenameAdd.setTag(position);
            final BusinessCircleBean.DataBean.ListBean businessCircle = list.get(position);
            //用户头像
            Glide.with(context).load(businessCircle.avatars)
                    .apply(requestOptions)
                    .into(((MyViewHolder) holder).imgUser);
            ((MyViewHolder) holder).imgUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, HomePageActivity.class);
                    intent.putExtra("uid",list.get(position).uid);
                    context.startActivity(intent);

                }
            });
            ((MyViewHolder) holder).tename.setText(businessCircle.nickname);
            if(list.get(position).identity_type.equals(BizConstant.IS_FAIL)){
                ((MyViewHolder) holder).daV.setVisibility(View.GONE);
            }else {
                ((MyViewHolder) holder).daV.setVisibility(View.VISIBLE);
            }
            final String uid = list.get(position).uid;
            fuction = position;
            /**
             * 这里对数据进行处理，根据当前的did_i_follow=0:1判断是否关注
             * 遍历循环数据拿到posotion下标以及相应的,得到当前的选择的关注人数
             */
            //判断关注返回1，未关注返回0......
            if (businessCircle.did_i_follow.equals(BizConstant.IS_FAIL)) {
                ((MyViewHolder) holder).tenameAdd.setText("+");
                ((MyViewHolder) holder).tenameAdd.setTextColor(Color.parseColor("#ff2d2d2d"));
                ((MyViewHolder) holder).tenameAdd.setBackgroundResource(R.drawable.back_action);
            } else if (businessCircle.did_i_follow.equals(BizConstant.IS_SUC)) {
                ((MyViewHolder) holder).tenameAdd.setText("√");
                ((MyViewHolder) holder).tenameAdd.setTextColor(Color.parseColor("#FFFFFF"));
                ((MyViewHolder) holder).tenameAdd.setBackgroundResource(R.drawable.btn_ganzhu_red);
            }
            //判断是否选中重新赋值---避免复选的状况
            ((MyViewHolder) holder).tenameAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    attentionInterface.onclick(v, position, list.get(position).did_i_follow, ((MyViewHolder) holder).tenameAdd);
                }
            });
            ((MyViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickItemListener != null) {
                        onClickItemListener.setOnItemClick(v, position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() + 1 : 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.busi_user)
        ImageViewPlus imgUser;
        @BindView(R.id.busin_nkname)
        TextView tename;
        @BindView(R.id.busi_add)
        TextView tenameAdd;
        @BindView(R.id.addli)
        LinearLayout linearLayout;
        @BindView(R.id.da_v)
        ImageView daV;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //循环数组长度------ 一键全选
    public void allFollow() {
        for (int i = 0; i < mCheckStates.size(); i++) {
            mCheckStates.put(i, true);
        }
    }

    //循环数组长度------- 一键全部取消
    public void cancelFollow() {
        for (int i = 0; i < mCheckStates.size(); i++) {
            mCheckStates.put(i, false);
        }
    }

    public OnClickItemListener onClickItemListener;

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }

    public interface OnClickItemListener {
        void setOnItemClick(View view, int position);
    }


    /**
     * `
     * 按钮点击事件需要的方法
     */
    public void attentionSetOnclick(AttentionInterface attentionInterface) {
        this.attentionInterface = attentionInterface;
    }

    /**
     * 按钮点击事件对应的接口
     */
    public interface AttentionInterface {
        public void onclick(View view, int position, String did_follow, TextView textView);
    }

}

