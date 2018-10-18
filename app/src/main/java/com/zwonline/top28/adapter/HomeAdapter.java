package com.zwonline.top28.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zwonline.top28.R;
import com.zwonline.top28.bean.HomeClassBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.utils.ImageViewPlu;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by YU on 2017/12/8.
 * 首页的适配器
 */

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<HomeClassBean.DataBean> homelist;
    private Context context;
    public static final int TYPE_ONE = 0;
    public static final int TYPE_TWO = 1;
    public static final int TYPE_THREE = 2;

    public HomeAdapter(List<HomeClassBean.DataBean> homelist, Context context) {
        this.homelist = homelist;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_ONE) {
            view = LayoutInflater.from(context).inflate(R.layout.home_item_one, parent, false);
//            view = View.inflate(context, R.layout.home_item_one, null);
            final ViewHolderTypeOne one = new ViewHolderTypeOne(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickItemListener.setOnItemClick(one.getPosition(), v);
                }
            });

            return one;
        } else if (viewType == TYPE_TWO) {
            view = LayoutInflater.from(context).inflate(R.layout.home_item_two, parent, false);
//            view = View.inflate(context, R.layout.home_item_two, parent);
            final ViewHolderTypeTwo two = new ViewHolderTypeTwo(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickItemListener.setOnItemClick(two.getPosition(), v);
                }
            });
            return two;
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.home_item_three, parent, false);
//            view = View.inflate(context, R.layout.home_item_two, parent);
            final ViewHolderTypeThree three = new ViewHolderTypeThree(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickItemListener.setOnItemClick(three.getPosition(), v);
                }
            });
            return three;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        RequestOptions requestOption = new RequestOptions().placeholder(R.mipmap.gray_logo).error(R.mipmap.gray_logo);
        RequestOptions requestOptions = new RequestOptions().placeholder(R.mipmap.gray_logo3).error(R.mipmap.gray_logo3);
        if (type == TYPE_ONE) {
            try {
                String time = homelist.get(position).showtime;
                String datetime = getDateToString(Long.parseLong(time) * 1000, "yyyy-MM-dd HH:m:s");
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
                Date date = formatter.parse(datetime);
                ViewHolderTypeOne viewHolderTypeOne = (ViewHolderTypeOne) holder;
                viewHolderTypeOne.title.setText(homelist.get(position).title);
                viewHolderTypeOne.cdatetime.setText(TimeUtil.getTimeFormatText(date));
                viewHolderTypeOne.articleImg1.setScaleType(ImageView.ScaleType.FIT_XY);
                viewHolderTypeOne.articleImg2.setScaleType(ImageView.ScaleType.FIT_XY);
                viewHolderTypeOne.articleImg3.setScaleType(ImageView.ScaleType.FIT_XY);
                if (homelist.get(position).is_top.equals("1")) {
                    viewHolderTypeOne.top.setVisibility(View.VISIBLE);

                } else if (homelist.get(position).is_top.equals("0")) {
                    viewHolderTypeOne.top.setVisibility(View.GONE);
                }
                viewHolderTypeOne.look_count.setText(homelist.get(position).view + context.getString(R.string.favorite_read));
                if (homelist.get(position).show_nickname.equals(BizConstant.ENTERPRISE_tRUE)) {
                    viewHolderTypeOne.type.setText(homelist.get(position).cate_name);
                } else if (homelist.get(position).show_nickname.equals(BizConstant.ALREADY_FAVORITE)) {
                    viewHolderTypeOne.type.setText(homelist.get(position).nickname);
                }
                if (homelist.get(position).is_hot.equals(BizConstant.ENTERPRISE_tRUE)) {
                    viewHolderTypeOne.home_fire.setVisibility(View.GONE);
                } else if (homelist.get(position).is_hot.equals(BizConstant.ALREADY_FAVORITE)) {
                    viewHolderTypeOne.home_fire.setVisibility(View.VISIBLE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
//            System.out.println(size+"a");
//            if (size1 == 1) {
//                Glide.with(context).load(lists.get(0)).into(((ViewHolderTypeOne) holder).articleImg1);
//            } else if (size1 == 2) {
//
//                Glide.with(context).load(lists.get(0)).into(((ViewHolderTypeOne) holder).articleImg1);
//                Glide.with(context).load(lists.get(1)).into(((ViewHolderTypeOne) holder).articleImg2);
//            } else if (size1 == 3) {
            final String path1 = homelist.get(position).ArticleImg.get(0).path;
            final String path2 = homelist.get(position).ArticleImg.get(1).path;
            final String path3 = homelist.get(position).ArticleImg.get(2).path;
            Glide.with(context).load(path1).apply(requestOption).into(((ViewHolderTypeOne) holder).articleImg1);
            Glide.with(context).load(path2).apply(requestOption).into(((ViewHolderTypeOne) holder).articleImg2);
            Glide.with(context).load(path3).apply(requestOption).into(((ViewHolderTypeOne) holder).articleImg3);
//            }


        } else if (type == TYPE_TWO) {
            try {
                String time = homelist.get(position).showtime;
                String datetime = getDateToString(Long.parseLong(time) * 1000, "yyyy-MM-dd HH:m:s");
                ViewHolderTypeTwo viewHolderTypeTwo = (ViewHolderTypeTwo) holder;
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
                Date date = formatter.parse(datetime);
                viewHolderTypeTwo.cdatetime.setText(TimeUtil.getTimeFormatText(date));
                viewHolderTypeTwo.title.setText(homelist.get(position).title);
                viewHolderTypeTwo.image.setScaleType(ImageView.ScaleType.FIT_XY);
                final String path = homelist.get(position).ArticleImg.get(0).path;
                if (homelist.get(position).is_top.equals("1")) {
                    viewHolderTypeTwo.top.setVisibility(View.VISIBLE);

                } else {
                    viewHolderTypeTwo.top.setVisibility(View.GONE);
                }
                Glide.with(context).load(path).apply(requestOption).into(((ViewHolderTypeTwo) holder).image);
                viewHolderTypeTwo.look_count.setText(homelist.get(position).view + context.getString(R.string.favorite_read));
//                viewHolderTypeTwo.type.setText(homelist.get(position).cate_name);
                if (homelist.get(position).show_nickname.equals(BizConstant.ENTERPRISE_tRUE)) {
                    viewHolderTypeTwo.type.setText(homelist.get(position).cate_name);
                } else if (homelist.get(position).show_nickname.equals(BizConstant.ALREADY_FAVORITE)) {
                    viewHolderTypeTwo.type.setText(homelist.get(position).nickname);
                }
                if (homelist.get(position).is_hot.equals(BizConstant.ENTERPRISE_tRUE)) {
                    viewHolderTypeTwo.home_fire.setVisibility(View.GONE);
                } else if (homelist.get(position).is_hot.equals(BizConstant.ALREADY_FAVORITE)) {
                    viewHolderTypeTwo.home_fire.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (type == TYPE_THREE) {
            try {
                ViewHolderTypeThree viewHolderTypeThree = (ViewHolderTypeThree) holder;
                viewHolderTypeThree.title.setText(homelist.get(position).title);
                viewHolderTypeThree.look_count.setText(homelist.get(position).sub_title);
                viewHolderTypeThree.image.setScaleType(ImageView.ScaleType.FIT_XY);
                final String path = homelist.get(position).images;
                String add_time = homelist.get(position).add_time;
//                String is_ad = homelist.get(position).is_ad;
//                ToastUtils.showToast(context,"is_ad=="+is_ad);
                Glide.with(context).load(path).apply(requestOptions).into(((ViewHolderTypeThree) holder).image);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
                Date date = formatter.parse(add_time);
//                ToastUtils.showToast(context,add_time);
                viewHolderTypeThree.cdatetime.setText(TimeUtil.getTimeFormatText(date));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return homelist != null ? homelist.size() : 0;
    }

    class ViewHolderTypeOne extends RecyclerView.ViewHolder {
        private ImageViewPlu articleImg1, articleImg2, articleImg3;
        private ImageView home_fire;
        private TextView title, cdatetime, look_count, reply_count, type, top;

        public ViewHolderTypeOne(View itemView) {
            super(itemView);
            articleImg1 = (ImageViewPlu) itemView.findViewById(R.id.articleImg1);
            articleImg2 = (ImageViewPlu) itemView.findViewById(R.id.articleImg2);
            articleImg3 = (ImageViewPlu) itemView.findViewById(R.id.articleImg3);
            home_fire = (ImageView) itemView.findViewById(R.id.home_fire);
            title = (TextView) itemView.findViewById(R.id.title);
            cdatetime = (TextView) itemView.findViewById(R.id.cdatetime);
            top = (TextView) itemView.findViewById(R.id.top);
            look_count = (TextView) itemView.findViewById(R.id.look_count);
            type = (TextView) itemView.findViewById(R.id.type);
        }

    }

    class ViewHolderTypeTwo extends RecyclerView.ViewHolder {
        private ImageViewPlu image;
        private ImageView home_fire;
        private TextView title, cdatetime, look_count, type, top;

        public ViewHolderTypeTwo(View itemView) {
            super(itemView);
            home_fire = (ImageView) itemView.findViewById(R.id.home_fire);
            image = (ImageViewPlu) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            cdatetime = (TextView) itemView.findViewById(R.id.cdatetime);
            top = (TextView) itemView.findViewById(R.id.top);
            look_count = (TextView) itemView.findViewById(R.id.look_count);
            type = (TextView) itemView.findViewById(R.id.type);
        }

    }

    class ViewHolderTypeThree extends RecyclerView.ViewHolder {
        private ImageViewPlu image;
        private TextView title, cdatetime, type, look_count;

        public ViewHolderTypeThree(View itemView) {
            super(itemView);
            image = (ImageViewPlu) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            cdatetime = (TextView) itemView.findViewById(R.id.cdatetime);
            look_count = (TextView) itemView.findViewById(R.id.look_count);
            type = (TextView) itemView.findViewById(R.id.type);
        }

    }

    @Override
    public int getItemViewType(int position) {
        List<HomeClassBean.DataBean> articleImg = homelist;
        String is_ad = articleImg.get(position).is_ad;
        if (StringUtil.isNotEmpty(articleImg.get(position).is_ad) && articleImg.get(position).is_ad.equals("0")) {
            if (articleImg.get(position).ArticleImg.size() == 3) {
                return TYPE_ONE;
            } else if (articleImg.get(position).ArticleImg.size() == 1) {
                return TYPE_TWO;
            }
        } else if (StringUtil.isNotEmpty(articleImg.get(position).is_ad) && articleImg.get(position).is_ad.equals("1")) {
            return TYPE_THREE;
        }
        return TYPE_THREE;
    }

    public OnClickItemListener onClickItemListener;

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }

    public interface OnClickItemListener {
        void setOnItemClick(int position, View view);
    }

    public static String getDateToString(long milSecond, String pattern) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }
}