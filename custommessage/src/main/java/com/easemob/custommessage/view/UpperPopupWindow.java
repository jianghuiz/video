package com.easemob.custommessage.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.easemob.custommessage.R;
import com.easemob.custommessage.uitls.ViewUtils;


public class UpperPopupWindow extends PopupWindow {

    private Activity context;
    private UpperPopupWindow.CallBackCouponListener mListener;
    private EditText emEditContent;
    private ImageView comment_image;
    RelativeLayout constrain_layout;

    public UpperPopupWindow(final Activity context, UpperPopupWindow.CallBackCouponListener mListener) {
        super(context);
        this.context = context;
        this.mListener = mListener;
        View view = LayoutInflater.from(context).inflate(R.layout.popup_comment, null);
        initDate(view);
        this.setContentView(view);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setAnimationStyle(R.style.popup_window_anim);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                hintKeyboard();
                setData();
            }
        });
    }
    public void setUpdata(String content){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(emEditContent, 0);
//        if(!TextUtils.isEmpty(content)){
//            emEditContent.setText(content);
//        }
    }
public void setData(){
    ViewUtils.backgroundAlpha(context, 1.0f);
//    if (!TextUtils.isEmpty(emEditContent.getText())) {
//        if(mListener!=null){
//            mListener.onItemClick(emEditContent.getText().toString(),0);
//        }
//    }
    emEditContent.setText("");
}
    private void hintKeyboard() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        imm.hideSoftInputFromWindow(emEditContent.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
    private void initDate(View view) {

        comment_image=(ImageView)view.findViewById(R.id.comment_images) ;
        constrain_layout=(RelativeLayout) view.findViewById(R.id.rela_tive);
        emEditContent=(EditText)view.findViewById(R.id.edt_content);
        constrain_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null){
                    mListener.onItemClick("",1);
                }
            }
        });
        comment_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(emEditContent.getText())) {
                    Toast.makeText(context, "文字内容不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                String content=emEditContent.getText().toString();
                if(mListener!=null){
                    mListener.onItemClick(content,1);
                }
            }
        });
//        rela_tive.setAlpha(0.7f);
    }

    /**
     * 页面更多 pop window里面的点击监听
     */
    public interface CallBackCouponListener {
        /**
         * 单个推广方式点击效果
         */
         void onItemClick(String content, int type);
    }

}


