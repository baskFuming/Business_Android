package com.zwonline.top28.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zwonline.top28.R;
import com.zwonline.top28.bean.VideoBean;

import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * @author YSG
 * @desc视频的适配器
 * @date ${Date}
 */
public class VideoListAdapter extends BaseAdapter {
    private List<VideoBean.DataBean> list;
    private Context context;
    private int count=0;
    public VideoListAdapter(List<VideoBean.DataBean> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return list != null ? list.size() : 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.video_item, null);
            holder.video_time = (TextView) convertView.findViewById(R.id.video_time);
            holder.look = (TextView) convertView.findViewById(R.id.browse);
            holder.videocontroller = (JCVideoPlayerStandard) convertView.findViewById(R.id.videocontroller);
            holder.video_linear = (LinearLayout) convertView.findViewById(R.id.video_linear);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.video_time.setText(list.get(position).duration);
//        holder.video_time.setVisibility(View.VISIBLE);
        holder.look.setText(list.get(position).view);//SCREEN_LAYOUT_NORMAL
        holder.videocontroller.setUp(list.get(position).url, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, list.get(position).title,list.get(position).duration);
        Glide.with(context).load(list.get(position).cover_url).into(holder.videocontroller.thumbImageView);
        if (holder.videocontroller.titleTextView.getVisibility()==View.GONE){
            holder.video_time.setVisibility(View.VISIBLE);
        }else {
            holder.video_time.setVisibility(View.GONE);
        }


        return convertView;
    }

    class ViewHolder {
        TextView video_time, look;
        ImageView start_stop, background;
        JCVideoPlayerStandard videocontroller;
        LinearLayout video_linear;
    }


}
