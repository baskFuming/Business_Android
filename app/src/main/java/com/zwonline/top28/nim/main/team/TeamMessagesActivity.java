package com.zwonline.top28.nim.main.team;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.SimpleCallback;
import com.netease.nim.uikit.api.model.contact.ContactChangedObserver;
import com.netease.nim.uikit.api.model.session.SessionCustomization;
import com.netease.nim.uikit.api.model.team.TeamDataChangedObserver;
import com.netease.nim.uikit.api.model.team.TeamMemberDataChangedObserver;
import com.netease.nim.uikit.business.session.activity.TeamMessageActivity;
import com.netease.nim.uikit.business.session.constant.Extras;
import com.netease.nim.uikit.business.session.fragment.MessageFragment;
import com.netease.nim.uikit.business.session.fragment.TeamMessageFragment;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.zwonline.top28.R;

import java.util.List;

/**
 * 群聊天界面
 */
public class TeamMessagesActivity extends TeamMessageActivity {
    // model
    private Team team;

    private View invalidTeamTipView;

    private TextView invalidTeamTipText;

    private TeamMessageFragment fragment;

    private Class<? extends Activity> backToClass;
    private RelativeLayout back;
    private TextView groupName;
    private ImageView groupInfo;

    public static void start(Context context, String tid, SessionCustomization customization,
                             Class<? extends Activity> backToClass, IMMessage anchor) {
        Intent intent = new Intent();
        intent.putExtra(Extras.EXTRA_ACCOUNT, tid);
        intent.putExtra(Extras.EXTRA_CUSTOMIZATION, customization);
        intent.putExtra(Extras.EXTRA_BACK_TO_CLASS, backToClass);
        if (anchor != null) {
            intent.putExtra(Extras.EXTRA_ANCHOR, anchor);
        }
        intent.setClass(context, TeamMessageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    protected void findViews() {
        invalidTeamTipView = findView(com.netease.nim.uikit.R.id.invalid_team_tip);
        invalidTeamTipText = findView(com.netease.nim.uikit.R.id.invalid_team_text);
        back = (RelativeLayout) findViewById(com.netease.nim.uikit.R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        groupName = (TextView) findView(com.netease.nim.uikit.R.id.group_name);
        groupInfo = (ImageView) findView(com.netease.nim.uikit.R.id.group_info);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        backToClass = (Class<? extends Activity>) getIntent().getSerializableExtra(Extras.EXTRA_BACK_TO_CLASS);
        findViews();

        registerTeamUpdateObserver(true);
        requestTeamInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        registerTeamUpdateObserver(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestTeamInfo();
    }

    /**
     * 请求群基本信息
     */
    private void requestTeamInfo() {
        // 请求群基本信息
        Team t = NimUIKit.getTeamProvider().getTeamById(sessionId);
        if (t != null) {
            updateTeamInfo(t);
        } else {
            NimUIKit.getTeamProvider().fetchTeamById(sessionId, new SimpleCallback<Team>() {
                @Override
                public void onResult(boolean success, Team result, int code) {
                    if (success && result != null) {
                        updateTeamInfo(result);
                    } else {
                        onRequestTeamInfoFailed();
                    }
                }
            });
        }
        groupName.setText(t.getName() + "(" + t.getMemberCount() + "人)");
        groupInfo.setImageResource(com.netease.nim.uikit.R.drawable.nim_ic_message_actionbar_team);
        groupInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (team != null && team.isMyTeam()) {
                    NimUIKit.startTeamInfo(getApplicationContext(), sessionId);
                } else {
                    Toast.makeText(getApplicationContext(), com.netease.nim.uikit.R.string.team_invalid_tip, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void onRequestTeamInfoFailed() {
        Toast.makeText(TeamMessagesActivity.this, "获取群组信息失败!", Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * 更新群信息
     *
     * @param d
     */
    private void updateTeamInfo(final Team d) {
        if (d == null) {
            return;
        }

        team = d;
        fragment.setTeam(team);

        setTitle(team == null ? sessionId : team.getName() + "(" + team.getMemberCount() + "人)");


        invalidTeamTipText.setText(team.getType() == TeamTypeEnum.Normal ? com.netease.nim.uikit.R.string.normal_team_invalid_tip : com.netease.nim.uikit.R.string.team_invalid_tip);
        invalidTeamTipView.setVisibility(team.isMyTeam() ? View.GONE : View.VISIBLE);
    }

    /**
     * 注册群信息更新监听
     *
     * @param register
     */
    private void registerTeamUpdateObserver(boolean register) {
        NimUIKit.getTeamChangedObservable().registerTeamDataChangedObserver(teamDataChangedObserver, register);
        NimUIKit.getTeamChangedObservable().registerTeamMemberDataChangedObserver(teamMemberDataChangedObserver, register);
        NimUIKit.getContactChangedObservable().registerObserver(friendDataChangedObserver, register);
    }

    /**
     * 群资料变动通知和移除群的通知（包括自己退群和群被解散）
     */
    TeamDataChangedObserver teamDataChangedObserver = new TeamDataChangedObserver() {
        @Override
        public void onUpdateTeams(List<Team> teams) {
            if (team == null) {
                return;
            }
            for (Team t : teams) {
                if (t.getId().equals(team.getId())) {
                    updateTeamInfo(t);
                    break;
                }
            }
        }

        @Override
        public void onRemoveTeam(Team team) {
            if (team == null) {
                return;
            }
            if (team.getId().equals(TeamMessagesActivity.this.team.getId())) {
                updateTeamInfo(team);
            }
        }
    };

    /**
     * 群成员资料变动通知和移除群成员通知
     */
    TeamMemberDataChangedObserver teamMemberDataChangedObserver = new TeamMemberDataChangedObserver() {

        @Override
        public void onUpdateTeamMember(List<TeamMember> members) {
            fragment.refreshMessageList();
        }

        @Override
        public void onRemoveTeamMember(List<TeamMember> member) {
        }
    };

    ContactChangedObserver friendDataChangedObserver = new ContactChangedObserver() {
        @Override
        public void onAddedOrUpdatedFriends(List<String> accounts) {
            fragment.refreshMessageList();
        }

        @Override
        public void onDeletedFriends(List<String> accounts) {
            fragment.refreshMessageList();
        }

        @Override
        public void onAddUserToBlackList(List<String> account) {
            fragment.refreshMessageList();
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> account) {
            fragment.refreshMessageList();
        }
    };

    @Override
    protected MessageFragment fragment() {
        // 添加fragment
        Bundle arguments = getIntent().getExtras();
        arguments.putSerializable(Extras.EXTRA_TYPE, SessionTypeEnum.Team);
        fragment = new TeamMessageFragment();
        fragment.setArguments(arguments);
        fragment.setContainerId(com.netease.nim.uikit.R.id.message_fragment_container);
        return fragment;
    }

    @Override
    protected int getContentViewId() {
        return com.netease.nim.uikit.R.layout.nim_team_message_activity;
    }

    @Override
    protected void initToolBar() {
//        ToolBarOptions options = new NimToolBarOptions();
//        options.titleString = "群聊";
//        setToolBar(R.id.toolbar, options);
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        if (backToClass != null) {
//            Intent intent = new Intent();
//            intent.setClass(this, backToClass);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            startActivity(intent);
//            finish();
//        }
//    }
}
