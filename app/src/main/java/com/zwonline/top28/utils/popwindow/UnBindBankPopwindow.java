package com.zwonline.top28.utils.popwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.zwonline.top28.R;

/** 
 * 自定义popupWindow 
 */
  
public class UnBindBankPopwindow extends PopupWindow {
  private View mView;

  public UnBindBankPopwindow(Activity context, View.OnClickListener itemsOnClick, int position) {
    super(context); 
    initView(context, itemsOnClick,position);
  } 
  
  private void initView(final Activity context, View.OnClickListener itemsOnClick, int position) {
    LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
    mView = mInflater.inflate(R.layout.unbindbank_pop_menu, null);
    setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    Button chat = (Button) mView.findViewById(R.id.chat);
    Button cancelAttention = (Button) mView.findViewById(R.id.unbind);
    Button btnCancel = (Button) mView.findViewById(R.id.cancel);
    btnCancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) { 
  
        //销毁弹出框 
        dismiss(); 
        backgroundAlpha(context, 1f); 
      } 
    }); 
    //设置按钮监听 
    chat.setOnClickListener(itemsOnClick);
    cancelAttention.setOnClickListener(itemsOnClick);
    //设置SelectPicPopupWindow的View
    this.setContentView(mView); 
    //设置SelectPicPopupWindow弹出窗体的宽 
    this.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
    //设置SelectPicPopupWindow弹出窗体的高 
    this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT); 
    //设置SelectPicPopupWindow弹出窗体可点击 
    this.setFocusable(true); 
    //设置PopupWindow可触摸 
    this.setTouchable(true);
    //设置非PopupWindow区域是否可触摸
    this.setOutsideTouchable(true);//设置点击屏幕其它地方弹出框消失
    //设置SelectPicPopupWindow弹出窗体动画效果 
    this.setAnimationStyle(R.style.mypopwindow_anim_style);
    //实例化一个ColorDrawable颜色为半透明 
    ColorDrawable dw = new ColorDrawable(0x00000000); 
    //设置SelectPicPopupWindow弹出窗体的背景 
    this.setBackgroundDrawable(dw); 
    backgroundAlpha(context, 0.5f);//0.0-1.0 
    this.setOnDismissListener(new OnDismissListener() { 
  
      @Override
      public void onDismiss() { 
        // TODO Auto-generated method stub 
        backgroundAlpha(context, 1f); 
      } 
    });

    //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
    mView.setOnTouchListener(new View.OnTouchListener() {
      public boolean onTouch(View v, MotionEvent event) {

        int height = mView.findViewById(R.id.ll).getTop();
        int y=(int) event.getY();
        if(event.getAction()==MotionEvent.ACTION_UP){
          if(y<height){
            dismiss();
          }
        }
        return true;
      }
    });
  } 
  
  
  /** 
   * 设置添加屏幕的背景透明度 
   * 
   * @param bgAlpha 
   */
  public void backgroundAlpha(Activity context, float bgAlpha) { 
    WindowManager.LayoutParams lp = context.getWindow().getAttributes(); 
    lp.alpha = bgAlpha; 
    context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); 
    context.getWindow().setAttributes(lp); 
  } 
  
} 