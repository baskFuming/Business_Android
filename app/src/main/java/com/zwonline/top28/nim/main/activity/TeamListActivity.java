package com.zwonline.top28.nim.main.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.session.SessionCustomization;
import com.netease.nim.uikit.api.model.team.TeamDataChangedObserver;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.business.contact.core.item.AbsContactItem;
import com.netease.nim.uikit.business.contact.core.item.ContactItem;
import com.netease.nim.uikit.business.contact.core.item.ItemTypes;
import com.netease.nim.uikit.business.contact.core.model.ContactDataAdapter;
import com.netease.nim.uikit.business.contact.core.model.ContactGroupStrategy;
import com.netease.nim.uikit.business.contact.core.provider.ContactDataProvider;
import com.netease.nim.uikit.business.contact.core.query.IContactDataProvider;
import com.netease.nim.uikit.business.contact.core.viewholder.ContactHolder;
import com.netease.nim.uikit.business.contact.core.viewholder.LabelHolder;
import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.constant.TeamMemberType;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.zwonline.top28.R;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.nim.session.SessionHelper;
import com.zwonline.top28.nim.shangjibi.SJBAction;
import com.zwonline.top28.nim.yangfen.YangFenAction;
import com.zwonline.top28.utils.ImageViewPlus;
import com.zwonline.top28.utils.LogUtils;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import static com.netease.nimlib.sdk.team.constant.TeamMemberType.Owner;

/**
 * 群列表(通讯录)
 * <p/>
 * Created by huangjun on 2015/4/21.
 */
public class TeamListActivity extends UI implements AdapterView.OnItemClickListener {

    private static final String EXTRA_DATA_ITEM_TYPES = "EXTRA_DATA_ITEM_TYPES";

    private ContactDataAdapter adapter;

    private ListView lvContacts;
    private SharedPreferencesUtils sp;
    private int itemType;
    private String uid;
    //一级列表数据源
    private String[] groups = {"我管理的群", "我加入的群"};
    private ExpandableListView expandableListView;
    private List<Team> allList;
    private List<Team> manageList;
    private List<List<Team>> lists;
    private MyExpandableListView myExpandableListView;
    private String has_permission;
    public static final void start(Context context, int teamItemTypes) {
        Intent intent = new Intent();
        intent.setClass(context, TeamListActivity.class);
        intent.putExtra(EXTRA_DATA_ITEM_TYPES, teamItemTypes);

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allList = new ArrayList<>();
        lists = new ArrayList<>();
        manageList = new ArrayList<>();
        itemType = getIntent().getIntExtra(EXTRA_DATA_ITEM_TYPES, ItemTypes.TEAMS.ADVANCED_TEAM);

        setContentView(R.layout.group_list_activity);
        sp = SharedPreferencesUtils.getUtil();
        uid = (String) sp.getKey(getApplicationContext(), "uid", "");
        has_permission = (String) sp.getKey(getApplicationContext(), "has_permission", "");
        ToolBarOptions options = new NimToolBarOptions();
        options.titleId = itemType == ItemTypes.TEAMS.ADVANCED_TEAM ? R.string.advanced_team : R.string.normal_team;
        setToolBar(R.id.toolbar, options);

        lvContacts = (ListView) findViewById(R.id.group_list);
        expandableListView = (ExpandableListView) findViewById(R.id.group_expan);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("群组");
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
            }
        });
        expandableListView.setDivider(null);
        //#TODO 去掉自带箭头，在一级列表中动态添加
        expandableListView.setGroupIndicator(null);
        getTeamList();//获取群组集合
        lists.add(manageList);
        lists.add(allList);
        myExpandableListView = new MyExpandableListView(this, lists);
        expandableListView.setAdapter(myExpandableListView);
        GroupStrategy groupStrategy = new GroupStrategy();
        IContactDataProvider dataProvider = new ContactDataProvider(itemType);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                SessionCustomization sessionCustomization = new SessionCustomization();

                ArrayList<BaseAction> actions = new ArrayList<>();
                if (StringUtil.isEmpty(has_permission) || has_permission.equals(BizConstant.ENTERPRISE_tRUE)) {
                    actions.remove(new YangFenAction());
                } else if (has_permission.equals(BizConstant.ALREADY_FAVORITE)) {
                    actions.add(new YangFenAction());
                }
                actions.add(new SJBAction());
                sessionCustomization.actions = actions;
                NimUIKit.startChatting(getApplicationContext(), lists.get(groupPosition).get(childPosition).getId(), SessionTypeEnum.Team, sessionCustomization, null);
                SessionHelper.startTeamSession(TeamListActivity.this, lists.get(groupPosition).get(childPosition).getId());
                return true;
            }
        });
        adapter = new ContactDataAdapter(this, groupStrategy, dataProvider) {
            @Override
            protected List<AbsContactItem> onNonDataItems() {
                return null;
            }

            @Override
            protected void onPreReady() {
            }

            @Override
            protected void onPostLoad(boolean empty, String queryText, boolean all) {
            }
        };
        adapter.addViewHolder(ItemTypes.LABEL, LabelHolder.class);
        adapter.addViewHolder(ItemTypes.TEAM, ContactHolder.class);

        lvContacts.setAdapter(adapter);
        lvContacts.setOnItemClickListener(this);
        lvContacts.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                showKeyboard(false);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }
        });

        // load data
        int count = NIMClient.getService(TeamService.class).queryTeamCountByTypeBlock(itemType == ItemTypes.TEAMS
                .ADVANCED_TEAM ? TeamTypeEnum.Advanced : TeamTypeEnum.Normal);
        if (count == 0) {
            if (itemType == ItemTypes.TEAMS.ADVANCED_TEAM) {
                Toast.makeText(TeamListActivity.this, R.string.no_team, Toast.LENGTH_SHORT).show();
            } else if (itemType == ItemTypes.TEAMS.NORMAL_TEAM) {
                Toast.makeText(TeamListActivity.this, R.string.no_normal_team, Toast.LENGTH_SHORT).show();
            }
        }

        adapter.load(true);

        registerTeamUpdateObserver(true);
    }

    /**
     * 获取team集合
     */
    private void getTeamList() {
        NIMClient.getService(TeamService.class).queryTeamListByType(TeamTypeEnum.Advanced).setCallback(new RequestCallback<List<Team>>() {

            @Override
            public void onSuccess(List<Team> teams) {
                allList.addAll(teams);
                for (int i = 0; i < teams.size(); i++) {
                    TeamMember member = NIMClient.getService(TeamService.class).queryTeamMemberBlock(teams.get(i).getId(), uid);
                    if (member.getType() == TeamMemberType.Owner || member.getType() == TeamMemberType.Manager) {
                        manageList.add(teams.get(i));
                    }
                }
                allList.removeAll(manageList);
            }

            @Override
            public void onFailed(int i) {

            }

            @Override
            public void onException(Throwable throwable) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        registerTeamUpdateObserver(false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AbsContactItem item = (AbsContactItem) adapter.getItem(position);
        switch (item.getItemType()) {
            case ItemTypes.TEAM:
                SessionHelper.startTeamSession(this, ((ContactItem) item).getContact().getContactId());
                break;
        }
    }

    private void registerTeamUpdateObserver(boolean register) {
        NimUIKit.getTeamChangedObservable().registerTeamDataChangedObserver(teamDataChangedObserver, register);
    }

    TeamDataChangedObserver teamDataChangedObserver = new TeamDataChangedObserver() {
        @Override
        public void onUpdateTeams(List<Team> teams) {
            adapter.load(true);
        }

        @Override
        public void onRemoveTeam(Team team) {
            adapter.load(true);
        }
    };

    private static class GroupStrategy extends ContactGroupStrategy {
        GroupStrategy() {
            add(ContactGroupStrategy.GROUP_NULL, 0, ""); // 默认分组
        }

        @Override
        public String belongs(AbsContactItem item) {
            switch (item.getItemType()) {
                case ItemTypes.TEAM:
                    return GROUP_NULL;
                default:
                    return null;
            }
        }
    }


    class MyExpandableListView extends BaseExpandableListAdapter {
        private List<List<Team>> mLists;
        private Context mContext;

        public MyExpandableListView(Context context, List<List<Team>> lists) {
            mContext = context;
            mLists = lists;
        }

        /*一级列表个数*/
        @Override
        public int getGroupCount() {
            return groups.length;
        }

        /*每个二级列表的个数*/
        @Override
        public int getChildrenCount(int groupPosition) {
            return mLists.get(groupPosition).size();
        }

        /*一级列表中单个item*/
        @Override
        public Object getGroup(int groupPosition) {
            return mLists.get(groupPosition);
        }

        /*二级列表中单个item*/
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return mLists.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        /*每个item的id是否固定，一般为true*/
        @Override
        public boolean hasStableIds() {
            return true;
        }


        /*#TODO 填充一级列表
         * isExpanded 是否已经展开
         * */
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.group_expan_item, null);
            }
            TextView tv_group = (TextView) convertView.findViewById(R.id.grouping);
            ImageView group_switch = convertView.findViewById(R.id.group_switch);
            tv_group.setText(groups[groupPosition]);
            //控制是否展开图标
            if (isExpanded) {
                group_switch.setImageResource(R.drawable.group_3j2);
            } else {
                group_switch.setImageResource(R.drawable.group_3j);
            }
            return convertView;
        }

        /*#TODO 填充二级列表*/
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildViewHolder childViewHolder;
            if (convertView == null) {
                childViewHolder = new ChildViewHolder();
                convertView = View.inflate(mContext, R.layout.group_list_item, null);
                childViewHolder.team_head = (ImageViewPlus) convertView.findViewById(R.id.team_head);
                childViewHolder.team_name = (TextView) convertView.findViewById(R.id.team_name);
                convertView.setTag(childViewHolder);
            } else {
                childViewHolder = (ChildViewHolder) convertView.getTag();
            }

            childViewHolder.team_name.setText(mLists.get(groupPosition).get(childPosition).getName());
            RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.nim_avatar_groups).error(R.drawable.nim_avatar_groups);
            Glide.with(mContext).load(mLists.get(groupPosition).get(childPosition).getIcon()).apply(requestOptions).into(childViewHolder.team_head);

            return convertView;
        }

        /*二级列表中每个能否被选中，如果有点击事件一定要设为true*/
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }


    }

    static class ChildViewHolder {
        ImageViewPlus team_head;
        TextView team_name;

    }


}
