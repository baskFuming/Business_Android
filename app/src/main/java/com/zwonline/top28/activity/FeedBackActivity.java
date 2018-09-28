package com.zwonline.top28.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.zwonline.top28.R;
import com.zwonline.top28.adapter.ImagePickersAdapter;
import com.zwonline.top28.api.Api;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.AddBankBean;
import com.zwonline.top28.bean.AtentionDynamicHeadBean;
import com.zwonline.top28.bean.AttentionBean;
import com.zwonline.top28.bean.BusinessCircleBean;
import com.zwonline.top28.bean.DynamicDetailsBean;
import com.zwonline.top28.bean.DynamicDetailsesBean;
import com.zwonline.top28.bean.DynamicShareBean;
import com.zwonline.top28.bean.LikeListBean;
import com.zwonline.top28.bean.NewContentBean;
import com.zwonline.top28.bean.PictursBean;
import com.zwonline.top28.bean.RefotPasswordBean;
import com.zwonline.top28.bean.SendNewMomentBean;
import com.zwonline.top28.bean.SettingBean;
import com.zwonline.top28.bean.ShieldUserBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.SendFriendCirclePresenter;
import com.zwonline.top28.utils.BitmapUtils;
import com.zwonline.top28.utils.LanguageUitils;
import com.zwonline.top28.utils.SelectDialog;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.view.ISendFriendCircleActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 意见反馈
 */
public class FeedBackActivity extends BaseActivity<ISendFriendCircleActivity, SendFriendCirclePresenter> implements ISendFriendCircleActivity, View.OnClickListener, ImagePickersAdapter.OnRecyclerViewItemClickListener {

    private RelativeLayout back;
    private TextView title;
    private RadioButton system;
    private RadioButton idea;
    private RadioGroup typeGroup;
    private EditText ideaEt;
    private TextView tvNum;
    private RecyclerView ideaRecy;
    private Button btnIdea;
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;

    private ImagePickersAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 4;               //允许选择图片最大数
    private String newPath;
    private int ideaId;
    private String ideaType = BizConstant.TYPE_ONE;

    @Override
    protected void init() {
        initView();
    }

    @Override
    protected SendFriendCirclePresenter getPresenter() {
        return new SendFriendCirclePresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_feed_back;
    }

    private void initView() {
        back = (RelativeLayout) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        title.setText("意见反馈");
        system = (RadioButton) findViewById(R.id.system);
        idea = (RadioButton) findViewById(R.id.idea);
        typeGroup = (RadioGroup) findViewById(R.id.type_group);
        ideaEt = (EditText) findViewById(R.id.idea_et);
        ideaEt.addTextChangedListener(textWatcher);
        tvNum = (TextView) findViewById(R.id.tv_num);
        ideaRecy = (RecyclerView) findViewById(R.id.idea_recy);
        btnIdea = (Button) findViewById(R.id.btn_idea);

        typeGroup.setOnCheckedChangeListener(payMethodRadioListener);
        btnIdea.setOnClickListener(this);
        back.setOnClickListener(this);
        selImageList = new ArrayList<>();
        adapter = new ImagePickersAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);

        ideaRecy.setLayoutManager(new GridLayoutManager(this, 4));
        ideaRecy.setHasFixedSize(true);
        ideaRecy.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        ideaEt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (v.getId()) {
                    case R.id.idea_et:
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        switch (event.getAction() & MotionEvent.ACTION_MASK) {
                            case MotionEvent.ACTION_UP:
                                v.getParent().requestDisallowInterceptTouchEvent(false);
                                break;
                        }
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.btn_idea:
                int payCheckedId = typeGroup.getCheckedRadioButtonId();
                //反馈意见判断

                if (selImageList.size() > 0) {
                    if (StringUtil.isNotEmpty(ideaEt.getText().toString().trim())) {
                        List<File> files = new ArrayList<>();
                        for (int i = 0; i < selImageList.size(); i++) {
                            String newPath = BitmapUtils.compressImageUpload(selImageList.get(i).path);
                            files.add(new File(newPath));
                        }
                        presenter.mPictures(FeedBackActivity.this, files);
                    } else {
                        ToastUtils.showToast(getApplicationContext(), "反馈意见内容不能为空");
                    }

                } else {
                    if (payCheckedId == system.getId()) {
                        //发表意见反馈的网络请求
                        feedBackData(ideaType, "");
                    } else if (payCheckedId == idea.getId()) {
                        //发表意见反馈的网络请求
                        feedBackData(ideaType, "");
                    }

                }
                break;
        }
    }

    /**
     * 支付方式group
     */
    private RadioGroup.OnCheckedChangeListener payMethodRadioListener = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            int id = group.getCheckedRadioButtonId();
            ideaId = typeGroup.getCheckedRadioButtonId();
            switch (id) {
                case R.id.system://系统问题
                    ideaType = BizConstant.TYPE_ONE;
                    break;
                case R.id.idea://优化意见
                    ideaType = BizConstant.TYPE_TWO;
                    break;

            }
        }
    };


    /**
     * 上传多张图片
     *
     * @param pictursBean
     */
    @Override
    public void showPictures(PictursBean pictursBean) {
        if (pictursBean.status == 1) {
            //循环遍历出原图图片地址放到一个新的list集合中，然后转换成一 "|"隔开的字符串
            List<String> arrayStr = new ArrayList<>();
            for (int i = 0; i < pictursBean.data.size(); i++) {
                arrayStr.add(Api.baseUrl() + pictursBean.data.get(i).original_save_url);
            }

            int payCheckedId = typeGroup.getCheckedRadioButtonId();
            //反馈意见判断
            if (payCheckedId == system.getId()) {
                //发表意见反馈的网络请求
                feedBackData(ideaType, listToString(arrayStr));
            } else if (payCheckedId == idea.getId()) {
                //发表意见反馈的网络请求
                feedBackData(ideaType, listToString(arrayStr));
            }

        } else {
            ToastUtils.showToast(getApplicationContext(), pictursBean.msg);
        }
    }

    //发表意见反馈的网络请求
    private void feedBackData(String type, String images) {
        //发表意见反馈的网络请求
        if (StringUtil.isNotEmpty(ideaEt.getText().toString().trim())) {
            presenter.mFeedBack(FeedBackActivity.this, type, ideaEt.getText().toString().trim(), images);
        } else {
            ToastUtils.showToast(getApplicationContext(), "反馈意见内容不能为空");
        }
    }

    /**
     * 意见反馈
     *
     * @param settingBean
     */
    @Override
    public void showFeedBack(SettingBean settingBean) {
        if (settingBean.status == 1) {
            finish();
            overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
        } else {
            ToastUtils.showToast(getApplicationContext(), settingBean.msg);
        }
    }

    /**
     * 商机圈动态详情
     *
     * @param dataBeanList
     */
    @Override
    public void showDynamicComment(List<DynamicDetailsBean.DataBean> dataBeanList) {

    }

    @Override
    public void showDynamicShare(DynamicShareBean dynamicShareBean) {

    }

    /**
     * 动态新评论
     *
     * @param settingBean
     */
    @Override
    public void showNewComment(AddBankBean settingBean) {

    }

    /**
     * 删除动态
     *
     * @param settingBean
     */
    @Override
    public void showDeleteMoment(SettingBean settingBean) {

    }

    /**
     * 关注
     *
     * @param attentionBean
     */
    @Override
    public void showAttention(AttentionBean attentionBean) {

    }

    /**
     * 一键关注
     *
     * @param attentionBean
     */
    @Override
    public void showAttentions(AttentionBean attentionBean) {

    }

    //取消关注
    @Override
    public void showUnAttention(AttentionBean attentionBean) {

    }

    /**
     * 屏蔽
     *
     * @param settingBean
     */
    @Override
    public void showBlockUser(RefotPasswordBean settingBean) {

    }

    /**
     * 动态点赞
     *
     * @param attentionBean
     */
    @Override
    public void showLikeMoment(AttentionBean attentionBean) {

    }


    /**
     * 屏蔽用户列表
     *
     * @param shielduserList
     */
    @Override
    public void showBlockUserList(List<ShieldUserBean.DataBean> shielduserList) {

    }

    /**
     * 屏蔽列表判断有没有数据
     *
     * @param flag
     */
    @Override
    public void showUserList(boolean flag) {

    }

    /**
     * 没有数据
     */
    @Override
    public void noLoadMore() {

    }

    /**
     * 动态评论点赞
     *
     * @param attentionBean
     */
    @Override
    public void showLikeMomentComment(AttentionBean attentionBean) {

    }

    /**
     * 删除动态评论
     *
     * @param attentionBean
     */
    @Override
    public void showDeleteComment(AttentionBean attentionBean) {

    }

    /**
     * 推荐关注
     *
     * @param issueList
     */
    @Override
    public void showBusincDate(List<BusinessCircleBean.DataBean> issueList) {

    }

    /**
     * 顶部推荐关注列表
     *
     * @param issueList
     */
    @Override
    public void showAttentionDynamic(List<AtentionDynamicHeadBean.DataBean.ListBean> issueList) {

    }

    /**
     * 商机圈我的消息提醒
     *
     * @param attentionBean
     */
    @Override
    public void showGetMyNotificationCount(AttentionBean attentionBean) {

    }

    /**
     * 点赞列表
     * @param likeList
     */
    @Override
    public void showGetLikeList(List<LikeListBean.DataBean> likeList) {

    }

    /**
     * 动态详情接口
     *
     * @param mommentList
     */
    @Override
    public void showMomentDetail(DynamicDetailsesBean mommentList) {

    }

    /**
     * 举报
     * @param attentionBean
     */
    @Override
    public void showReport(AttentionBean attentionBean) {

    }

    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(this, R.style.transparentFrameWindowStyle, listener, names);
        if (!this.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }

    /**
     * 点击拍照获取图片
     *
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:
                List<String> names = new ArrayList<>();
                names.add("拍照");
                names.add("相册");
                showDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0: // 直接调起相机
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent = new Intent(FeedBackActivity.this, ImageGridActivity.class);
                                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                                startActivityForResult(intent, REQUEST_CODE_SELECT);
                                break;
                            case 1:
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent1 = new Intent(FeedBackActivity.this, ImageGridActivity.class);
                                startActivityForResult(intent1, REQUEST_CODE_SELECT);
                                break;
                            default:
                                break;
                        }
                    }
                }, names);
                break;
            default:
                //打开预览
                Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
                    selImageList.clear();
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        }
    }


    @Override
    public void showSendNewMoment(SendNewMomentBean sendNewMomentBean) {

    }

    @Override
    public void showConment(List<NewContentBean.DataBean> newList) {

    }


    /**
     * 监听输入框的输入状态
     */
    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            String pointsEditT = ideaEt.getText().toString();

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            String pointsEditT = ideaEt.getText().toString();
            if (StringUtil.isNotEmpty(pointsEditT)) {

                tvNum.setText(pointsEditT.length() + "/500");
            } else {
                tvNum.setText("0/500");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            String pointsEditT = ideaEt.getText().toString();
            if (StringUtil.isNotEmpty(pointsEditT)) {

                tvNum.setText(pointsEditT.length() + "/500");
            } else {
                tvNum.setText("0/500");
            }
        }

    };

    /**
     * 点击输入框其他地方软件盘隐藏
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (LanguageUitils.isShouldHideInput(v, ev)) {
                if (LanguageUitils.hideInputMethod(this, v)) {
                    return true; //隐藏键盘时，其他控件不响应点击事件==》注释则不拦截点击事件
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    //将list集合转换成以“||”分割
    public String listToString(List<String> stringList) {
        if (stringList == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        for (String string : stringList) {
            if (flag) {
                result.append("|"); // 分隔符
            } else {
                flag = true;
            }
            result.append(string);
        }
        return result.toString();
    }

}
