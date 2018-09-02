package com.zwonline.top28.fragment;


import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.zwonline.top28.R;
import com.zwonline.top28.adapter.VideoListAdapter;
import com.zwonline.top28.base.BaseFragment;
import com.zwonline.top28.base.BasesFragment;
import com.zwonline.top28.bean.HotExamineBean;
import com.zwonline.top28.bean.VideoBean;
import com.zwonline.top28.presenter.HotExaminePresenter;
import com.zwonline.top28.view.IHotExamine;

import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import pl.droidsonroids.gif.GifImageView;

/**
 * @author YSG
 * @desc视频的列表
 * @date ${Date}
 */
public class VideoFragmentSon extends BasesFragment<IHotExamine, HotExaminePresenter> implements IHotExamine {

    private int page = 1;
    private String cate_id;
    private List<VideoBean.DataBean> hotList;
    private VideoListAdapter adapter;
    private AbsListView.OnScrollListener onScrollListener;
    private int firstVisible;//当前第一个可见的item
    private int visibleCount;//当前可见的item个数
    private JCVideoPlayerStandard currPlayer;
    private SpringView videoSpring;
    private ListView videoList;
//    private TextView video_time;
    private GifImageView videoGif;
    private View inflate;
    private TextView video_time1;

    @Override
    protected void init(View view) {
//        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        StatusBarUtil.setColor(getActivity(), getResources().getColor(R.color.black), 0);
        videoSpring = (SpringView) view.findViewById(R.id.video_spring);
        videoList = (ListView) view.findViewById(R.id.video_list);


        videoGif = (GifImageView) view.findViewById(R.id.video_gif);
        if (getArguments() != null) {
            cate_id = getArguments().getString("cate_id");
            videoSpring.setType(SpringView.Type.FOLLOW);
            videoSpring.setHeader(new DefaultHeader(getActivity()));
            videoSpring.setFooter(new DefaultFooter(getActivity()));
        }
        if (cate_id.equals("300")) {
            presenter.mVideoRecomment(getActivity(),String.valueOf(page));
        } else {
            presenter.mVideo(getActivity(),String.valueOf(page), cate_id);
        }
        hotList = new ArrayList<>();
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//        videoRecy.setLayoutManager(linearLayoutManager);
        adapter = new VideoListAdapter(hotList, getActivity());
        videoList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    protected HotExaminePresenter setPresenter() {
        return new HotExaminePresenter(this);
    }

    @Override
    protected int setLayouId() {
        return R.layout.videofragmentson;
    }

    //动态获取Fragment
    public static VideoFragmentSon getInstance(String cate_id) {
        VideoFragmentSon video = new VideoFragmentSon();
        Bundle bundle = new Bundle();
        bundle.putString("cate_id", cate_id);
        video.setArguments(bundle);
        return video;
    }

    @Override
    public void showHotExamineData(List<HotExamineBean.DataBean> hotExamineList) {

    }

    @Override
    public void showVideo(List<VideoBean.DataBean> videolist) {
        for (int i = 0; i <videolist.size() ; i++) {
//            video_time.setText("11111");
        }
        videoSpring.setVisibility(View.VISIBLE);
        videoGif.setVisibility(View.GONE);
        if (page == 1) {
            hotList.clear();
        }
        hotList.addAll(videolist);
        adapter.notifyDataSetChanged();
        setOnItem(hotList);
    }

    //设置条目点击事件
    public void setOnItem(final List<VideoBean.DataBean> hotList) {
        videoSpring.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        if (cate_id.equals("300")) {
                            presenter.mVideoRecomment(getActivity(),String.valueOf(page));
                        } else {
                            presenter.mVideo(getActivity(),String.valueOf(page), cate_id);
                        }
                        JCVideoPlayer.releaseAllVideos();
                        videoSpring.onFinishFreshAndLoad();
                    }
                }, 1000);
            }

            @Override
            public void onLoadmore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page++;
                        if (cate_id.equals("300")) {
                            presenter.mVideoRecomment(getActivity(),String.valueOf(page));
                        } else {
                            presenter.mVideo(getActivity(),String.valueOf(page), cate_id);
                        }
                        videoSpring.onFinishFreshAndLoad();
                    }
                }, 1000);
            }
        });
    }


    @Override
    public void showErro() {
        if (getActivity() == null) {
            return;
        }
        if (page == 1) {
            hotList.clear();
            adapter.notifyDataSetChanged();
        } else {
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onStop() {
        super.onStop();
        JCVideoPlayer.releaseAllVideos();
    }

    /**
     * 滑动监听
     */
    private void initListener() {
        onScrollListener = new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        break;

                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        //滑动停止自动播放视频
                        autoPlayVideo(view);
//                        video_time.setVisibility(View.VISIBLE);
                        break;

                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisible == firstVisibleItem) {
                    return;
                }

                firstVisible = firstVisibleItem;
                visibleCount = visibleItemCount;
            }
        };

        videoList.setOnScrollListener(onScrollListener);
    }

    /**
     * 滑动停止自动播放视频
     */
    private void autoPlayVideo(AbsListView view) {

        for (int i = 0; i < visibleCount; i++) {
            if (view != null && view.getChildAt(i) != null && view.getChildAt(i).findViewById(R.id.videocontroller) != null) {
                currPlayer = (JCVideoPlayerStandard) view.getChildAt(i).findViewById(R.id.videocontroller);
//                video_time1 = (TextView) view.getChildAt(i).findViewById(R.id.video_time);
                Rect rect = new Rect();
                //获取当前view 的 位置
                currPlayer.getLocalVisibleRect(rect);
                int videoheight = currPlayer.getHeight();
                if (rect.top == 0 && rect.bottom == videoheight) {
                    if (currPlayer.currentState == JCVideoPlayer.CURRENT_STATE_NORMAL
                            || currPlayer.currentState == JCVideoPlayer.CURRENT_STATE_ERROR) {
                        currPlayer.startButton.performClick();
                    }
                    return;
                }
            }
        }
        //释放其他视频资源
        JCVideoPlayer.releaseAllVideos();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // load data here：fragment可见时执行加载数据或者进度条等
        } else {
            // fragment is no longer visible：不可见时不执行操作
            if (currPlayer != null) {
                currPlayer.startButton.performClick();
//                video_time.setVisibility(View.VISIBLE);
            }

        }
        //释放其他视频资源
        JCVideoPlayer.releaseAllVideos();
    }

}
