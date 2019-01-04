package com.zwonline.top28.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zwonline.top28.R;
import com.zwonline.top28.wave.PinyinComparator;
import com.zwonline.top28.wave.PinyinUtils;
import com.zwonline.top28.wave.SortAdapter;
import com.zwonline.top28.wave.SortModel;
import com.zwonline.top28.wave.TitleItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *  圈子分类
 */
public class All_CirclerFragment extends Fragment{
    private View  rootView;
    private RecyclerView recyclerView;
    private SortAdapter mAdapter;
    private LinearLayoutManager manager;
    private List<SortModel> mDateList;
    private TitleItemDecoration mDecoration;
    /**
     * 根据拼音来排列RecyclerView里面的数据类
     */
    private PinyinComparator mComparator;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.all_fragmet_type,container,false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv);
        initView();
        return rootView;
    }
    private void initView() {
        mComparator = new PinyinComparator();
        mDateList = filledData(getResources().getStringArray(R.array.date));
        // 根据a-z进行排序源数据
        Collections.sort(mDateList, mComparator);
        //RecyclerView设置manager
        manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        mAdapter = new SortAdapter(getActivity(), mDateList);
        recyclerView.setAdapter(mAdapter);
        mDecoration = new TitleItemDecoration(getActivity(), mDateList);
        //如果add两个，那么按照先后顺序，依次渲染。
        recyclerView.addItemDecoration(mDecoration);
//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
    }
    /**
     * 为RecyclerView填充数据
     *
     * @param date
     * @return
     */
    private List<SortModel> filledData(String[] date) {
        List<SortModel> mSortList = new ArrayList<>();

        for (int i = 0; i < date.length; i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(date[i]);
            //汉字转换成拼音
            String pinyin = PinyinUtils.getPingYin(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setLetters(sortString.toUpperCase());
            } else {
                sortModel.setLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

}
