package com.zwonline.top28.nim.main.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.contact.ContactsCustomization;
import com.netease.nim.uikit.business.contact.ContactsFragment;
import com.netease.nim.uikit.business.contact.core.item.AbsContactItem;
import com.netease.nim.uikit.business.contact.core.item.ItemTypes;
import com.netease.nim.uikit.business.contact.core.model.ContactDataAdapter;
import com.netease.nim.uikit.business.contact.core.viewholder.AbsContactViewHolder;
import com.netease.nim.uikit.business.contact.selector.activity.ContactSelectActivity;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.netease.nim.uikit.common.fragment.TabFragment;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.SystemMessageObserver;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.zwonline.top28.R;
import com.zwonline.top28.activity.AddFriendsActivity;
import com.zwonline.top28.activity.MainActivity;
import com.zwonline.top28.base.BaseMainActivity;
import com.zwonline.top28.nim.DemoCache;
import com.zwonline.top28.nim.contact.activity.BlackListActivity;
import com.zwonline.top28.nim.main.activity.SystemMessageActivity;
import com.zwonline.top28.nim.main.activity.TeamListActivity;
import com.zwonline.top28.nim.main.helper.SystemMessageUnreadManager;
import com.zwonline.top28.nim.main.reminder.ReminderId;
import com.zwonline.top28.nim.main.reminder.ReminderItem;
import com.zwonline.top28.nim.main.reminder.ReminderManager;
import com.zwonline.top28.nim.session.SessionHelper;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.badge.BadgeViews;

import java.util.ArrayList;
import java.util.List;


/**
 * 集成通讯录列表
 * <p/>
 * Created by huangjun on 2015/9/7.
 */
public class ContactListFragment extends TabFragment {
    private static final String PAGE_NAME_KEY = "PAGE_NAME_KEY";
    private ContactsFragment fragment;
    private static final int REQUEST_CODE_ADVANCED = 2;
    public static ContactListFragment getInstance(String pageName) {
        Bundle args = new Bundle();
        args.putString(PAGE_NAME_KEY, pageName);
        ContactListFragment pageFragment = new ContactListFragment();
        pageFragment.setArguments(args);

        return pageFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contacts_list, null);
        addContactFragment();  // 集成通讯录页面

        return view;
    }

    /**
     * ******************************** 功能项定制 ***********************************
     */
    final static class FuncItem extends AbsContactItem {
        static final FuncItem VERIFY = new FuncItem();
        static final FuncItem ROBOT = new FuncItem();
        static final FuncItem NORMAL_TEAM = new FuncItem();
        static final FuncItem ADVANCED_TEAM = new FuncItem();
        static final FuncItem BLACK_LIST = new FuncItem();
        static final FuncItem MY_COMPUTER = new FuncItem();

        @Override
        public int getItemType() {
            return ItemTypes.FUNC;
        }

        @Override
        public String belongsGroup() {
            return null;
        }

        public static final class FuncViewHolder extends AbsContactViewHolder<FuncItem> {
            private ImageView image;
            private TextView funcName;
            private TextView unreadNum;
            private TextView fucView;
            private LinearLayout fucLinear;
            private LinearLayout newFriend;
            private LinearLayout infoscan;
            private LinearLayout addFriend;
            private LinearLayout buildGroupChat;
            private RelativeLayout flockList;
            private SharedPreferencesUtils sp;
            private String accid;
            private BadgeViews badgeView;
            private Context context;
            private ImageView new_friend_img;
            public void create(Context context) {
                this.context = context;
                this.view = inflate(LayoutInflater.from(context));
            }

            @Override
            public View inflate(LayoutInflater inflater) {
                final View view = inflater.inflate(R.layout.func_contacts_item, null);
                this.fucLinear = (LinearLayout) view.findViewById(R.id.fuc_linear);
                this.fucView = (TextView) view.findViewById(R.id.fu_cview);
                this.newFriend = (LinearLayout) view.findViewById(R.id.new_friend);
                this.infoscan = (LinearLayout) view.findViewById(R.id.info_scan);
                this.addFriend = (LinearLayout) view.findViewById(R.id.add_friend);
                this.buildGroupChat = (LinearLayout) view.findViewById(R.id.build_group_chat);
                this.flockList = (RelativeLayout) view.findViewById(R.id.flock_list);
                this.image = (ImageView) view.findViewById(R.id.img_head);
                this.funcName = (TextView) view.findViewById(R.id.tv_func_name);
                this.unreadNum = (TextView) view.findViewById(R.id.tab_new_msg_label);
                this.new_friend_img=(ImageView)view.findViewById(R.id.new_friend_img);
                badgeView = new BadgeViews(context);
                badgeView.setTargetView(new_friend_img);

                sp = SharedPreferencesUtils.getUtil();
                accid = (String) sp.getKey(view.getContext(), "account", "");
                buildGroupChat.setOnClickListener(new View.OnClickListener() {//创建群
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> memberAccounts = new ArrayList<>();
                        memberAccounts.add(accid);
                        ContactSelectActivity.Option option = TeamHelper.getCreateContactSelectOption(memberAccounts, 50);
                        NimUIKit.startContactSelector(context, option, REQUEST_CODE_ADVANCED);// 创建群
                    }
                });
                addFriend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(v.getContext(), AddFriendsActivity.class));
                    }
                });
                infoscan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        (MainActivity)context.startActivityForResult(new Intent(context, CaptureActivity.class), 0);
                        ((MainActivity)context).startActivityForResult(new Intent(context, CaptureActivity.class), 0);
                    }
                });
                return view;

            }

            /**
             * 功能定制
             * @param contactAdapter
             * @param position
             * @param item
             */
            @Override
            public void refresh(ContactDataAdapter contactAdapter, int position, FuncItem item) {
                if (item == VERIFY) {
                    fucLinear.setVisibility(View.GONE);
                    flockList.setVisibility(View.VISIBLE);
                    fucView.setVisibility(View.GONE);
                    funcName.setText("新朋友");
                    image.setImageResource(R.mipmap.message_xinpengyou);
                    image.setScaleType(ScaleType.FIT_XY);
                    int unread = NIMClient.getService(SystemMessageService.class)
                            .querySystemMessageUnreadCountBlock();
                    int unreadCount = SystemMessageUnreadManager.getInstance().getSysMsgUnreadCount();
                    updateUnreadNum(unread);
//                    unreadNum.setText("" + unreadCount);
//                    unreadNum.setText(unread+"");

//                    badgeView.setBadgeCount(item.getUnread());
                    Observer<Integer> sysMsgUnreadCountChangedObserver = new Observer<Integer>() {
                        @Override
                        public void onEvent(Integer unreadCount) {
                            // 更新未读数变化
                            if (unreadCount>0){
                                unreadNum.setVisibility(View.VISIBLE);
                                unreadNum.setText(unreadCount+"");
                            }else {
                                unreadNum.setVisibility(View.GONE);
                            }
                        }
                    };
                    NIMClient.getService(SystemMessageObserver.class)
                            .observeUnreadCountChange(sysMsgUnreadCountChangedObserver, true);

                    Log.d("unreadCount=", unreadCount + "");
                    ReminderManager.getInstance().registerUnreadNumChangedCallback(new ReminderManager.UnreadNumChangedCallback() {
                        @Override
                        public void onUnreadNumChanged(ReminderItem item) {
                            if (item.getId() != ReminderId.CONTACT) {
                                return;
                            }

                            updateUnreadNum(item.getUnread());
                        }
                    });
                }
//              //群组列表
                else if (item == ADVANCED_TEAM) {
                    fucLinear.setVisibility(View.GONE);
                    flockList.setVisibility(View.VISIBLE);
                    fucView.setVisibility(View.GONE);
                    funcName.setText("群组列表");
                    image.setImageResource(R.mipmap.message_group_list);
                }

                if (item != VERIFY) {
                    image.setScaleType(ScaleType.FIT_XY);
                    unreadNum.setVisibility(View.GONE);
                }
            }
            //新朋友通知
            private void updateUnreadNum(int unreadCount) {
                // 2.*版本viewholder复用问题
                if (unreadCount > 0 ) {
                    unreadNum.setVisibility(View.VISIBLE);
                    unreadNum.setText("" + unreadCount);
                    badgeView.setBadgeCount(unreadCount);
                } else {
                    unreadNum.setVisibility(View.GONE);
                    badgeView.setBadgeCount(unreadCount);
                }
            }
        }

        static List<AbsContactItem> provide() {
            List<AbsContactItem> items = new ArrayList<AbsContactItem>();
            items.add(VERIFY);
            items.add(ADVANCED_TEAM);

            return items;
        }

        static void handle(Context context, AbsContactItem item) {
            if (item == VERIFY) {
                SystemMessageActivity.start(context);
            }
//            else if (item == ROBOT) {
//                RobotListActivity.start(context);
//            } else if (item == NORMAL_TEAM) {
//                TeamListActivity.start(context, ItemTypes.TEAMS.NORMAL_TEAM);
//            }
            if (item == ADVANCED_TEAM) {
                TeamListActivity.start(context, ItemTypes.TEAMS.ADVANCED_TEAM);
            }
            else if (item == MY_COMPUTER) {
                SessionHelper.startP2PSession(context, DemoCache.getAccount());
            } else if (item == BLACK_LIST) {
                BlackListActivity.start(context);
            }
        }
    }


    /**
     * ******************************** 生命周期 ***********************************
     */

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onCurrent(); // 触发onInit，提前加载
    }


    // 将通讯录列表fragment动态集成进来。 开发者也可以使用在xml中配置的方式静态集成。
    private void addContactFragment() {
        fragment = new ContactsFragment();
        fragment.setContainerId(R.id.contact_fragment);

        BaseMainActivity activity = (BaseMainActivity) getActivity();

        // 如果是activity从堆栈恢复，FM中已经存在恢复而来的fragment，此时会使用恢复来的，而new出来这个会被丢弃掉
        fragment = (ContactsFragment) activity.addFragment(fragment);

        // 功能项定制
        fragment.setContactsCustomization(new ContactsCustomization() {
            @Override
            public Class<? extends AbsContactViewHolder<? extends AbsContactItem>> onGetFuncViewHolderClass() {
                return FuncItem.FuncViewHolder.class;
            }

            @Override
            public List<AbsContactItem> onGetFuncItems() {
                return FuncItem.provide();
            }

            @Override
            public void onFuncItemClick(AbsContactItem item) {
                FuncItem.handle(getActivity(), item);
            }
        });
    }

    @Override
    public void onCurrentTabClicked() {
        // 点击切换到当前TAB
        if (fragment != null) {
            fragment.scrollToTop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
