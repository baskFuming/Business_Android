package com.netease.nim.uikit.business.recent.adapter;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.recent.RecentContactsCallback;
import com.netease.nim.uikit.business.recent.holder.CommonRecentViewHolder;
import com.netease.nim.uikit.business.recent.holder.TeamRecentViewHolder;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemQuickAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.holder.BaseViewHolder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.team.model.Team;

import java.util.List;

/**
 * Created by huangjun on 2016/12/11.
 */

public class RecentContactAdapter extends BaseMultiItemQuickAdapter<RecentContact, BaseViewHolder> {
    interface ViewType {
        int VIEW_TYPE_COMMON = 1;
        int VIEW_TYPE_TEAM = 2;
    }

    private RecentContactsCallback callback;

    public RecentContactAdapter(RecyclerView recyclerView, List<RecentContact> data) {
        super(recyclerView, data);
        addItemType(ViewType.VIEW_TYPE_COMMON, R.layout.nim_recent_contact_list_item, CommonRecentViewHolder.class);
        addItemType(ViewType.VIEW_TYPE_TEAM, R.layout.nim_recent_contact_list_item, TeamRecentViewHolder.class);


    }

    @Override
    protected void convert(BaseViewHolder baseHolder, RecentContact item, int position, boolean isScrolling) {

        super.convert(baseHolder, item, position, isScrolling);
        baseHolder.addOnClickListener(R.id.img_head);
        //获取当前条目position
        position = baseHolder.getLayoutPosition();
        ImageView group_icon = (ImageView) baseHolder.itemView.findViewById(R.id.group_icon);
        switch (item.getSessionType()) {
            case P2P:
                group_icon.setVisibility(View.GONE);
                break;
            case Team:
                group_icon.setVisibility(View.VISIBLE);
                Team team = NimUIKit.getTeamProvider().getTeamById(item.getContactId());
                if (team != null) {
                    String extServer = team.getExtServer();
                    if (extServer == null || "".equals(extServer)) {
                        group_icon.setImageResource(R.drawable.qun_icon);
                    } else {
                        group_icon.setImageResource(R.drawable.guan_icon);
                    }
                }
                break;
            default:
                break;
        }
    }
    @Override
    protected int getViewType(RecentContact item) {
        return item.getSessionType() == SessionTypeEnum.Team ? ViewType.VIEW_TYPE_TEAM : ViewType.VIEW_TYPE_COMMON;
    }
    @Override
    protected String getItemKey(RecentContact item) {
        StringBuilder sb = new StringBuilder();
        sb.append(item.getSessionType().getValue()).append("_").append(item.getContactId());
        return sb.toString();
    }
    public RecentContactsCallback getCallback() {
        return callback;
    }

    public void setCallback(RecentContactsCallback callback) {
        this.callback = callback;
    }


}
