package com.zwonline.top28.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zwonline.top28.R;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.badge.InfoBadgeView;

import java.util.List;

/**
 * 消息模块fragmentpage适配器，添加角标
 */
public class InfoFragmentPageAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private List<Fragment> mFragmentList;
    private List<String> mPageTitleList;
    private List<Integer> mBadgeCountList;

    public InfoFragmentPageAdapter(Context context,
                                   FragmentManager fm,
                                   List<Fragment> fragmentList,
                                   List<String> pageTitleList,
                                   List<Integer> badgeCountList) {
        super(fm);
        this.mContext = context;
        this.mFragmentList = fragmentList;
        this.mPageTitleList = pageTitleList;
        this.mBadgeCountList = badgeCountList;
    }

    public InfoFragmentPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mPageTitleList.get(position);
    }

    public View getTabItemView(int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tab_layout_item, null);
        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mPageTitleList.get(position));

        View target = view.findViewById(R.id.badgeview_target);

        InfoBadgeView badgeView = new InfoBadgeView(mContext);
        badgeView.setTargetView(target);
        badgeView.setBadgeMargin(0, 6, 10, 0);
        badgeView.setTextSize(10);
        badgeView.setText(formatBadgeNumber(mBadgeCountList.get(position)));

        return view;
    }

    public static String formatBadgeNumber(int value) {
        if (value <= 0) {
            return null;
        }

        if (value < 100) {
            // equivalent to String#valueOf(int);
            return Integer.toString(value);
        }

        // my own policy
        return "99+";
    }
}
