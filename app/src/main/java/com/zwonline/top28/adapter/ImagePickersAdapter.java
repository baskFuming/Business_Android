package com.zwonline.top28.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.zwonline.top28.R;
import com.zwonline.top28.activity.FeedBackActivity;
import com.zwonline.top28.activity.SendFriendActivity;
import com.zwonline.top28.utils.ImageViewPlu;
import com.zwonline.top28.utils.ImageViewPlus;

import java.util.ArrayList;
import java.util.List;

/**
 * 微信图片选择的Adapter
 */
public class ImagePickersAdapter extends RecyclerView.Adapter<ImagePickersAdapter.SelectedPicViewHolder> {
    private int maxImgCount;
    private Context mContext;
    private List<ImageItem> mData;
    private LayoutInflater mInflater;
    private OnRecyclerViewItemClickListener listener;
    private boolean isAdded;   //是否额外添加了最后一个图片

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    public void setImages(List<ImageItem> data) {
        mData = new ArrayList<>(data);
        if (getItemCount() < maxImgCount) {
            mData.add(new ImageItem());
            isAdded = true;
        } else {
            isAdded = false;
        }
        notifyDataSetChanged();
    }

    public List<ImageItem> getImages() {
        //由于图片未选满时，最后一张显示添加图片，因此这个方法返回真正的已选图片
        if (isAdded) return new ArrayList<>(mData.subList(0, mData.size() - 1));
        else return mData;
    }

    public ImagePickersAdapter(Context mContext, List<ImageItem> data, int maxImgCount) {
        this.mContext = mContext;
        this.maxImgCount = maxImgCount;
        this.mInflater = LayoutInflater.from(mContext);
        setImages(data);
    }

    @Override
    public SelectedPicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SelectedPicViewHolder(mInflater.inflate(R.layout.list_item_images, parent, false));
    }

    @Override
    public void onBindViewHolder(SelectedPicViewHolder holder, int position) {

        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class SelectedPicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageViewPlu iv_img;
        private int clickPosition;
        private ImageView close;

        public SelectedPicViewHolder(View itemView) {
            super(itemView);
            iv_img = (ImageViewPlu) itemView.findViewById(R.id.iv_img);
            close = (ImageView) itemView.findViewById(R.id.close);
        }

        public void bind(final int position) {
            if (mData.get(position).equals(mData.size()-1)){
                close.setVisibility(View.GONE);
            }
            //设置条目的点击事件
            itemView.setOnClickListener(this);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mData.size() > 1) {
                        mData.remove(position);
                        notifyDataSetChanged();
                    }

                }
            });
            //根据条目位置设置图片s
            ImageItem item = mData.get(position);
            if (isAdded && position == getItemCount() - 1) {
                iv_img.setImageResource(R.drawable.selector_image_adds);
                clickPosition = FeedBackActivity.IMAGE_ITEM_ADD;
            } else {
                ImagePicker.getInstance().getImageLoader().displayImage((Activity) mContext, item.path, iv_img, 0, 0);
                clickPosition = position;
            }
        }

        @Override
        public void onClick(View v) {
            if (listener != null) listener.onItemClick(v, clickPosition);
        }
    }
}