package com.zwonline.top28.nim.main.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.zwonline.top28.nim.common.ui.viewpager.SlidingTabPagerAdapter;
import com.zwonline.top28.nim.main.fragment.MainTabFragment;
import com.zwonline.top28.nim.main.model.MainTab;

public class MainTabPagerAdapter extends SlidingTabPagerAdapter {

    @Override
    public int getCacheCount() {
        return MainTab.values().length;
    }

    public MainTabPagerAdapter(FragmentManager fm, Context context, ViewPager pager) {
        super(fm, MainTab.values().length, context.getApplicationContext(), pager);

        for (MainTab tab : MainTab.values()) {
            try {
                MainTabFragment fragment = null;

//                List<Fragment> fs = fm.getFragments();
//                if (fs != null) {
//                    for (Fragment f : fs) {
//                        if (f.getClass() == tab.clazz) {
//                            fragment = (MainTabFragment) f;
//                            break;
//                        }
//                    }
//                }
//
//                if (fragment == null) {
//                    fragment = tab.clazz.newInstance();
//                }

                fragment.setState(this);
//                fragment.attachTabData(tab);

                fragments[tab.tabIndex] = fragment;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getCount() {
        return MainTab.values().length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        MainTab tab = MainTab.fromTabIndex(position);

        int resId = tab != null ? tab.resId : 0;

        return resId != 0 ? context.getText(resId) : "";
    }

}