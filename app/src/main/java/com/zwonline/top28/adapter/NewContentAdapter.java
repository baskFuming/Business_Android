package com.zwonline.top28.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.zwonline.top28.R;
import com.zwonline.top28.activity.HomePageActivity;
import com.zwonline.top28.activity.PhotoBrowserActivity;
import com.zwonline.top28.bean.NewContentBean;
import com.zwonline.top28.bean.PhotoInfos;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.tip.toast.ToastUtil;
import com.zwonline.top28.utils.AutoTextView;
import com.zwonline.top28.utils.ImageViewPlu;
import com.zwonline.top28.utils.ImageViewPlus;
import com.zwonline.top28.utils.MultiImageView;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.TimeUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.TextClick;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.forward.androids.utils.ViewUtil;

/**
 * 商机圈动态列表适配器
 */
public class NewContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<NewContentBean.DataBean> list;
    private Context context;
    //分享点击接口
    private ShareInterface shareInterface;
    private DeleteInterface deleteInterface;
    private ArticleInterface articleInterface;
    private AttentionInterface attentionInterface;
    private SharedPreferencesUtils sp;
    private FunctionInterface functionInterface;
    private LikeMomentInterface likeMomentInterface;
    private boolean islogins;
    public String yuMing="thumb";
    public NewContentAdapter(List<NewContentBean.DataBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.newcontent_item, null);
        final MyViewHolder myViewHolder = new MyViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItemListener.setOnItemClick(v, myViewHolder.getPosition());
            }
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        sp = SharedPreferencesUtils.getUtil();
        String uid = (String) sp.getKey(context, "uid", "");
        islogins = (boolean) sp.getKey(context, "islogin", false);
        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.username.setText(list.get(position).author.nickname);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
        Date date = null;
        try {
            date = formatter.parse(list.get(position).add_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        myViewHolder.times.setText(TimeUtil.getTimeFormatText(date));

        if (StringUtil.isNotEmpty(uid) && uid.equals(list.get(position).user_id)) {
            myViewHolder.delete.setVisibility(View.VISIBLE);
            myViewHolder.attention_linear.setVisibility(View.GONE);
        } else {
            myViewHolder.attention_linear.setVisibility(View.VISIBLE);
            myViewHolder.delete.setVisibility(View.GONE);
        }

        //用户头像
        RequestOptions requestOptions = new RequestOptions().placeholder(R.mipmap.no_photo_male).error(R.mipmap.no_photo_male);
        Glide.with(context).load(list.get(position).author.avatars).apply(requestOptions).into(myViewHolder.userhead);
        //判断大V显隐
        if (list.get(position).author.identity_type.equals(BizConstant.IS_FAIL)) {
            myViewHolder.daV.setVisibility(View.GONE);
        } else {
            myViewHolder.daV.setVisibility(View.VISIBLE);
        }
        //判断是否有图片
        if (list.get(position).images_arr == null) {
            myViewHolder.imag_linear.setVisibility(View.GONE);
        }
        //判断是一张图片的时候展示
        if (list.get(position).images_arr != null && list.get(position).images_arr.size() == 1) {
            myViewHolder.imag_linear.setVisibility(View.VISIBLE);
            myViewHolder.imag_relative.setVisibility(View.VISIBLE);
            myViewHolder.multiImageView.setVisibility(View.GONE);
            int width = Integer.parseInt(list.get(position).images_arr.get(0).original_size.width);
            int height = Integer.parseInt(list.get(position).images_arr.get(0).original_size.height);
            String thumb = list.get(position).images_arr.get(0).thumb;
            if (StringUtil.isNotEmpty(thumb)&&thumb.contains(yuMing)){
                thumb=list.get(position).images_arr.get(0).original;
            }else {
                thumb = list.get(position).images_arr.get(0).thumb;
            }
            RequestOptions requestOption = new RequestOptions().placeholder(R.color.backgroud_zanwei).error(R.color.backgroud_zanwei);
            if (width < height) {
                myViewHolder.dynamic_imag_h.setVisibility(View.VISIBLE);
                myViewHolder.dynamic_imag_z.setVisibility(View.GONE);
                myViewHolder.dynamic_imag_w.setVisibility(View.GONE);
                myViewHolder.dynamic_imag_h.setScaleType(ImageView.ScaleType.MATRIX);

                Glide.with(context).load(thumb).apply(requestOption).into(myViewHolder.dynamic_imag_h);
            } else if (width > height) {
                myViewHolder.dynamic_imag_h.setVisibility(View.GONE);
                myViewHolder.dynamic_imag_z.setVisibility(View.GONE);
                myViewHolder.dynamic_imag_w.setVisibility(View.VISIBLE);
                myViewHolder.dynamic_imag_h.setScaleType(ImageView.ScaleType.MATRIX);
                Glide.with(context).load(thumb).apply(requestOption).into(myViewHolder.dynamic_imag_w);
            } else {
                myViewHolder.dynamic_imag_h.setVisibility(View.GONE);
                myViewHolder.dynamic_imag_z.setVisibility(View.VISIBLE);
                myViewHolder.dynamic_imag_w.setVisibility(View.GONE);
                myViewHolder.dynamic_imag_h.setScaleType(ImageView.ScaleType.MATRIX);
                Glide.with(context).load(thumb).apply(requestOption).into(myViewHolder.dynamic_imag_z);
            }
            //单张图片点击放大
            myViewHolder.imag_relative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String image[] = new String[list.get(position).images_arr.size()];
                    if (list.get(position).images_arr != null) {
                        image[0] = list.get(position).images_arr.get(0).original;
                        Intent intent = new Intent(context, PhotoBrowserActivity.class);
                        intent.putExtra("imageUrls", image);
                        intent.putExtra("curImg", list.get(position).images_arr.get(0).original);
                        context.startActivity(intent);
                    } else {

                    }
                }
            });

        }
        //判断多张图片的时候展示
        if (list.get(position).images_arr != null && list.get(position).images_arr.size() >= 2) {
            myViewHolder.imag_linear.setVisibility(View.VISIBLE);
            myViewHolder.imag_relative.setVisibility(View.GONE);
            myViewHolder.multiImageView.setVisibility(View.VISIBLE);
            List<PhotoInfos> images = new ArrayList<>();
            for (int i = 0; i < list.get(position).images_arr.size(); i++) {
                PhotoInfos bean = new PhotoInfos();
                bean.url = list.get(position).images_arr.get(i).thumb;
                images.add(bean);
            }
            myViewHolder.multiImageView.setList(images);
        }
        //多张图片展示的时候点击图片
        myViewHolder.multiImageView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int positions) {
                // To do something or 查看大图.
                String image[] = new String[list.get(position).images_arr.size()];
                if (list.get(position).images_arr != null) {
                    for (int i = 0; i < list.get(position).images_arr.size(); i++) {
                        image[i] = list.get(position).images_arr.get(i).original;
                    }
                    Intent intent = new Intent(context, PhotoBrowserActivity.class);
                    intent.putExtra("imageUrls", image);
                    intent.putExtra("curImg", list.get(position).images_arr.get(positions).original);
                    context.startActivity(intent);
                } else {

                }

            }
        });
        //判断有没有评论    展示评论通过评论数量来判断评论显示隐藏
        if (list.get(position).comments_excerpt == null) {
            myViewHolder.linear_child_comments.setVisibility(View.GONE);
        } else {
            if (list.get(position).comments_excerpt.size() > 0) {
                myViewHolder.linear_child_comments.setVisibility(View.VISIBLE);
                if (list.get(position).comments_excerpt.size() == 1) {
                    myViewHolder.comment_user1.setText(list.get(position).comments_excerpt.get(0).nickname + ":" + list.get(position).comments_excerpt.get(0).content);
                    myViewHolder.comment_user2.setVisibility(View.GONE);
                    myViewHolder.look_more_comment.setVisibility(View.GONE);
                    SpannableStringBuilder spannable = new SpannableStringBuilder(list.get(position).comments_excerpt.get(0).nickname);
                    spannable.append(":");
                    spannable.append(list.get(position).comments_excerpt.get(0).content);

                    if (list.get(position).user_id.equals(uid)) {
                        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#228FFE")), 0, list.get(position).comments_excerpt.get(0).nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    } else {
                        spannable.setSpan(new TextClick(context, list.get(position).comments_excerpt.get(0).user_id), 0, list.get(position).comments_excerpt.get(0).nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    }
                    myViewHolder.comment_user1.setMovementMethod(LinkMovementMethod.getInstance());
                    myViewHolder.comment_user1.setText(spannable);
                } else if (list.get(position).comments_excerpt.size() == 2) {
                    SpannableStringBuilder spannable1 = new SpannableStringBuilder(list.get(position).comments_excerpt.get(0).nickname);
                    SpannableStringBuilder spannable2 = new SpannableStringBuilder(list.get(position).comments_excerpt.get(1).nickname);
                    spannable1.append(":");
                    spannable1.append(list.get(position).comments_excerpt.get(0).content);
                    spannable2.append(":");
                    spannable2.append(list.get(position).comments_excerpt.get(1).content);
                    if (list.get(position).user_id.equals(uid)) {
                        spannable1.setSpan(new ForegroundColorSpan(Color.parseColor("#228FFE")), 0, list.get(position).comments_excerpt.get(0).nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        spannable2.setSpan(new ForegroundColorSpan(Color.parseColor("#228FFE")), 0, list.get(position).comments_excerpt.get(1).nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    } else {
                        spannable1.setSpan(new TextClick(context, list.get(position).comments_excerpt.get(0).user_id), 0, list.get(position).comments_excerpt.get(0).nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        spannable2.setSpan(new TextClick(context, list.get(position).comments_excerpt.get(1).user_id), 0, list.get(position).comments_excerpt.get(1).nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

                    }
                    myViewHolder.comment_user1.setMovementMethod(LinkMovementMethod.getInstance());
                    myViewHolder.comment_user2.setMovementMethod(LinkMovementMethod.getInstance());
                    myViewHolder.comment_user1.setText(spannable1);
                    myViewHolder.comment_user2.setText(spannable2);
                    myViewHolder.comment_user2.setVisibility(View.VISIBLE);
                    myViewHolder.look_more_comment.setVisibility(View.GONE);
//                    myViewHolder.comment_user1.setText(list.get(position).comments_excerpt.get(0).nickname + ":" + list.get(position).comments_excerpt.get(0).content);
//                    myViewHolder.comment_user2.setText(list.get(position).comments_excerpt.get(1).nickname + ":" + list.get(position).comments_excerpt.get(1).content);
                    myViewHolder.look_more_comment.setVisibility(View.GONE);
                }
                int commentCount = Integer.parseInt(list.get(position).comment_count);
                if (commentCount > 2) {
                    SpannableStringBuilder spannable1 = new SpannableStringBuilder(list.get(position).comments_excerpt.get(0).nickname);
                    SpannableStringBuilder spannable2 = new SpannableStringBuilder(list.get(position).comments_excerpt.get(1).nickname);
                    spannable1.append(":");
                    spannable1.append(list.get(position).comments_excerpt.get(0).content);
                    spannable2.append(":");
                    spannable2.append(list.get(position).comments_excerpt.get(1).content);
                    if (list.get(position).user_id.equals(uid)) {
                        spannable1.setSpan(new ForegroundColorSpan(Color.parseColor("#228FFE")), 0, list.get(position).comments_excerpt.get(0).nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        spannable2.setSpan(new ForegroundColorSpan(Color.parseColor("#228FFE")), 0, list.get(position).comments_excerpt.get(1).nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    } else {
                        spannable1.setSpan(new TextClick(context, list.get(position).comments_excerpt.get(0).user_id), 0, list.get(position).comments_excerpt.get(0).nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        spannable2.setSpan(new TextClick(context, list.get(position).comments_excerpt.get(1).user_id), 0, list.get(position).comments_excerpt.get(1).nickname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

                    }
                    myViewHolder.comment_user1.setMovementMethod(LinkMovementMethod.getInstance());
                    myViewHolder.comment_user2.setMovementMethod(LinkMovementMethod.getInstance());
                    myViewHolder.comment_user1.setText(spannable1);
                    myViewHolder.comment_user2.setText(spannable2);
                    myViewHolder.comment_user2.setVisibility(View.VISIBLE);
                    myViewHolder.look_more_comment.setVisibility(View.VISIBLE);
//                    myViewHolder.comment_user1.setText(list.get(position).comments_excerpt.get(0).nickname + ":" + list.get(position).comments_excerpt.get(0).content);
//                    myViewHolder.comment_user2.setText(list.get(position).comments_excerpt.get(1).nickname + ":" + list.get(position).comments_excerpt.get(1).content);
                    myViewHolder.look_more_comment.setText("查看全部" + list.get(position).comment_count + "条评论");
                }
            }
        }
        if (StringUtil.isNotEmpty(list.get(position).type) && list.get(position).type.equals(BizConstant.ALREADY_FAVORITE)) {
            myViewHolder.article_linear.setVisibility(View.GONE);
            //判断是否有内容
            if (StringUtil.isNotEmpty(list.get(position).content)) {
                myViewHolder.dynamic_conment.setVisibility(View.VISIBLE);
                myViewHolder.dynamic_conment.setMaxLines(3);
                myViewHolder.dynamic_conment.setText(list.get(position).content);
            } else {
                myViewHolder.dynamic_conment.setVisibility(View.GONE);
            }
        } else if (StringUtil.isNotEmpty(list.get(position).type) && list.get(position).type.equals(BizConstant.ALIPAY_METHOD)) {
            myViewHolder.dynamic_conment.setVisibility(View.GONE);
            myViewHolder.article_linear.setVisibility(View.VISIBLE);
            myViewHolder.article_title.setText(list.get(position).extend_content.target_title);
            myViewHolder.article_desc.setText(list.get(position).extend_content.target_description);
            RequestOptions requestOption = new RequestOptions().placeholder(R.mipmap.gray_logo).error(R.mipmap.gray_logo);
            Glide.with(context).load(list.get(position).extend_content.target_image).apply(requestOption).into(myViewHolder.article_img);
        }
        myViewHolder.comment_num.setText(list.get(position).comment_count);
        myViewHolder.like_num.setText(list.get(position).like_count);
        //点击分享
        myViewHolder.linear_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareInterface.onclick(v, position);
            }
        });
        myViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteInterface.onclick(v, position);
            }
        });
        myViewHolder.article_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                articleInterface.onclick(v, position);
            }
        });
        if (list.get(position).did_i_follow.equals(BizConstant.ENTERPRISE_tRUE)) {
            myViewHolder.attention.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.attention.setVisibility(View.GONE);
        }
        //关注
        myViewHolder.attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attentionInterface.onclick(v, position, myViewHolder.attention);
            }
        });
        myViewHolder.userhead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomePageActivity.class);
                intent.putExtra("uid", list.get(position).user_id);
                context.startActivity(intent);
            }
        });
        //更多功能
        myViewHolder.more_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                functionInterface.onclick(v, position, myViewHolder.attention);
            }
        });
        myViewHolder.linear_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeMomentInterface.onclick(v, position, myViewHolder.choose_like, myViewHolder.like_num);
            }
        });
        myViewHolder.choose_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeMomentInterface.onclick(v, position, myViewHolder.choose_like, myViewHolder.like_num);
            }
        });
        String did_i_like = list.get(position).did_i_like;
        if (StringUtil.isNotEmpty(did_i_like) && did_i_like.equals(BizConstant.IS_FAIL)) {

            myViewHolder.choose_like.setChecked(false);
            myViewHolder.choose_like.setEnabled(true);
            myViewHolder.like_num.setTextColor(Color.parseColor("#1d1d1d"));
        } else {
            myViewHolder.choose_like.setChecked(true);
            myViewHolder.choose_like.setEnabled(false);
            myViewHolder.like_num.setTextColor(Color.parseColor("#ff2b2b"));
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageViewPlus userhead;
        TextView username, times, comment_user1, comment_user2, look_more_comment, comment_num, like_num, delete, article_title, article_desc;
        ImageView dynamic_imag_h, dynamic_imag_w, dynamic_imag_z, daV;
        TextView attention;
        LinearLayout imag_linear, linear_share, linear_comment, linear_like, attention_linear, article_linear;
        MultiImageView multiImageView;
        RelativeLayout imag_relative, more_setting, linear_child_comments;
        AutoTextView dynamic_conment;
        ImageViewPlu article_img;
        CheckBox choose_like;

        public MyViewHolder(View itemView) {
            super(itemView);
            userhead = itemView.findViewById(R.id.userhead);
            multiImageView = itemView.findViewById(R.id.multi_image);
            username = itemView.findViewById(R.id.username);
            times = itemView.findViewById(R.id.time);
            dynamic_conment = itemView.findViewById(R.id.dynamic_conment);
            comment_user1 = itemView.findViewById(R.id.comment_user1);
            comment_user2 = itemView.findViewById(R.id.comment_user2);
            look_more_comment = itemView.findViewById(R.id.look_more_comment);
            dynamic_imag_h = itemView.findViewById(R.id.dynamic_imag_h);
            dynamic_imag_w = itemView.findViewById(R.id.dynamic_imag_w);
            dynamic_imag_z = itemView.findViewById(R.id.dynamic_imag_z);
            attention = itemView.findViewById(R.id.attention);
            linear_child_comments = itemView.findViewById(R.id.linear_child_comments);
            imag_linear = itemView.findViewById(R.id.imag_linear);
            imag_relative = itemView.findViewById(R.id.imag_relative);
            comment_num = itemView.findViewById(R.id.comment_num);
            like_num = itemView.findViewById(R.id.like_num);
            linear_share = itemView.findViewById(R.id.linear_share);
            linear_comment = itemView.findViewById(R.id.linear_comment);
            linear_like = itemView.findViewById(R.id.linear_like);
            attention_linear = itemView.findViewById(R.id.attention_linear);
            delete = itemView.findViewById(R.id.delete);
            article_title = itemView.findViewById(R.id.article_title);
            article_desc = itemView.findViewById(R.id.article_desc);
            article_img = itemView.findViewById(R.id.article_img);
            article_linear = itemView.findViewById(R.id.article_linear);
            more_setting = itemView.findViewById(R.id.more_setting);
            choose_like = itemView.findViewById(R.id.choose_like);
            daV = itemView.findViewById(R.id.da_v);

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
     * 按钮点击事件需要的方法
     */
    public void shareSetOnclick(ShareInterface shareInterface) {
        this.shareInterface = shareInterface;
    }

    /**
     * 按钮点击事件对应的接口
     */
    public interface ShareInterface {
        public void onclick(View view, int position);
    }

    /**
     * 按钮点击事件需要的方法
     */
    public void deleteSetOnclick(DeleteInterface deleteInterface) {
        this.deleteInterface = deleteInterface;
    }

    /**
     * 按钮点击事件对应的接口
     */
    public interface DeleteInterface {
        public void onclick(View view, int position);
    }

    /**
     * 按钮点击事件需要的方法
     */
    public void articleSetOnclick(ArticleInterface articleInterface) {
        this.articleInterface = articleInterface;
    }

    /**
     * 按钮点击事件对应的接口
     */
    public interface ArticleInterface {
        public void onclick(View view, int position);
    }

    /**
     * 按钮点击事件需要的方法
     */
    public void attentionSetOnclick(AttentionInterface attentionInterface) {
        this.attentionInterface = attentionInterface;
    }

    /**
     * 按钮点击事件对应的接口
     */
    public interface AttentionInterface {
        public void onclick(View view, int position, TextView attention);
    }

    /**
     * 按钮点击事件需要的方法
     */
    public void funtcionSetOnclick(FunctionInterface functionInterface) {
        this.functionInterface = functionInterface;
    }

    /**
     * 按钮点击事件对应的接口
     */
    public interface FunctionInterface {
        public void onclick(View view, int position, TextView attention);
    }

    /**
     * `
     * 按钮点击事件需要的方法
     */
    public void likeMomentSetOnclick(LikeMomentInterface likeMomentInterface) {
        this.likeMomentInterface = likeMomentInterface;
    }

    /**
     * 按钮点击事件对应的接口
     */
    public interface LikeMomentInterface {
        public void onclick(View view, int position, CheckBox choose_like, TextView like_num);
    }

    /**
     * 去除特殊字符或将所有中文标号替换为英文标号
     *
     * @param str
     * @return
     */
    public static String stringFilter(String str) {
        str = str.replaceAll("【", "[").replaceAll("】", "]")
                .replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
}
