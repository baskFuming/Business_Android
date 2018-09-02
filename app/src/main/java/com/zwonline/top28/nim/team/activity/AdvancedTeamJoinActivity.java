package com.zwonline.top28.nim.team.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.SimpleCallback;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.zwonline.top28.R;
import com.zwonline.top28.activity.MainActivity;

/**
 * 申请加入群组界面
 * Created by hzxuwen on 2015/3/20.
 */
public class AdvancedTeamJoinActivity extends UI implements View.OnClickListener {

    private static final String EXTRA_ID = "EXTRA_ID";

    private String teamId;
    private Team team;

    private TextView teamNameText;
    private TextView memberCountText;
    private TextView teamTypeText;
    private Button applyJoinButton;
    private HeadImageView teamHeadImage;
    private TextView groupNum;
    private TextView groupNotice;

    public static void start(Context context, String teamId) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ID, teamId);
        intent.setClass(context, AdvancedTeamJoinActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.nim_advanced_team_join_activity);

        ToolBarOptions options = new NimToolBarOptions();
        options.titleId = R.string.team_join;
        setToolBar(R.id.toolbar, options);

        findViews();
        parseIntentData();
        requestTeamInfo();
    }

    private void findViews() {
//        Team t = NimUIKit.getTeamProvider().getTeamById(teamId);
        teamNameText = (TextView) findViewById(R.id.team_name);
        memberCountText = (TextView) findViewById(R.id.member_count);//群人数
        applyJoinButton = (Button) findViewById(R.id.apply_join);
        teamTypeText = (TextView) findViewById(R.id.team_type);
        //群号
        groupNum = (TextView) findViewById(R.id.group_num);

        //公告
        groupNotice = (TextView) findViewById(R.id.group_notice);

        applyJoinButton.setOnClickListener(this);
        teamHeadImage = (HeadImageView) findViewById(com.netease.nim.uikit.R.id.team_head_image);
    }

    private void parseIntentData() {
        teamId = getIntent().getStringExtra(EXTRA_ID);
    }

    private void requestTeamInfo() {
        Team t = NimUIKit.getTeamProvider().getTeamById(teamId);
        groupNum.setText(teamId);
        teamHeadImage.loadTeamIconByTeam(t);
        if (t != null) {
            updateTeamInfo(t);
        } else {
            NimUIKit.getTeamProvider().fetchTeamById(teamId, new SimpleCallback<Team>() {
                @Override
                public void onResult(boolean success, Team result, int code) {
                    if (success && result != null) {
                        updateTeamInfo(result);
                    }
                }
            });
        }
    }

    /**
     * 更新群信息
     *
     * @param t 群
     */
    private void updateTeamInfo(final Team t) {
        if (t == null) {
            Toast.makeText(AdvancedTeamJoinActivity.this, R.string.team_not_exist, Toast.LENGTH_LONG).show();
            finish();
        } else {
            team = t;
            teamNameText.setText(team.getName()+"");

            if (team==null||team.getAnnouncement()==null||team.getAnnouncement().toString().equals("")){
                groupNotice.setText("");
            }else {
                groupNotice.setText(t.getAnnouncement().toString()+"");
            }
                memberCountText.setText(team.getMemberCount() + "人");
            if (team.getType() == TeamTypeEnum.Advanced) {
                teamTypeText.setText(R.string.advanced_team);
            } else {
                teamTypeText.setText(R.string.normal_team);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (team != null) {
            NIMClient.getService(TeamService.class).applyJoinTeam(team.getId(), null).setCallback(new RequestCallback<Team>() {
                @Override
                public void onSuccess(Team team) {//申请加入群成功
                    applyJoinButton.setEnabled(false);
                    String toast = getString(R.string.team_join_success, team.getName());
                    Toast.makeText(AdvancedTeamJoinActivity.this, toast, Toast.LENGTH_SHORT).show();
                    NimUIKit.startTeamSession(AdvancedTeamJoinActivity.this, team.getId());
                    finish();
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                }

                @Override
                public void onFailed(int code) {
                    if (code == 808) {
                        applyJoinButton.setEnabled(false);
                        Toast.makeText(AdvancedTeamJoinActivity.this, R.string.team_apply_to_join_send_success,
                                Toast.LENGTH_SHORT).show();
                    } else if (code == 809) {
                        applyJoinButton.setEnabled(false);
                        Toast.makeText(AdvancedTeamJoinActivity.this, R.string.has_exist_in_team,
                                Toast.LENGTH_SHORT).show();
                    } else if (code == 806) {
                        applyJoinButton.setEnabled(false);
                        Toast.makeText(AdvancedTeamJoinActivity.this, R.string.team_num_limit,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AdvancedTeamJoinActivity.this, "failed, error code =" + code,
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onException(Throwable exception) {

                }
            });
        }
    }
}
