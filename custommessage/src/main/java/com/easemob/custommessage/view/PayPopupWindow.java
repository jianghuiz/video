package com.easemob.custommessage.view;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.custommessage.R;
import com.easemob.custommessage.bean.GiftBean;
import com.easemob.custommessage.uitls.ViewUtils;
import com.hyphenate.chat.EMClient;

/**
 * 支付弹框
 */
public class PayPopupWindow extends PopupWindow implements View.OnClickListener {

    private Activity context;
    private PayPopupWindow.CallBackCouponListener mListener;
    private TextView text_name;
    private ImageView image_cancel;
    private TextView text_money;
    private RelativeLayout confirm_pay;
    private ImageView image_click_change_w,image_click_change_chat,image_click_change;
    private RelativeLayout popop_pay_yue,popop_pay_weix,popop_pay_api;
    private  int  mtype=0;
    private RelativeLayout rela_tive;
    private int money;

    public PayPopupWindow(final Activity context, PayPopupWindow.CallBackCouponListener mListener) {
        super(context);
        this.context = context;
        this.mListener = mListener;
        View view = LayoutInflater.from(context).inflate(R.layout.popup_pay, null);
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
                ViewUtils.backgroundAlpha(context, 1.0f);
            }
        });
    }
    private void initDate(View view) {
        rela_tive=(RelativeLayout)view.findViewById(R.id.rela_tive);
        text_name=(TextView) view.findViewById(R.id.text_name) ;
        image_cancel=(ImageView) view.findViewById(R.id.image_cancel);
        text_money=(TextView)view.findViewById(R.id.text_money);
        confirm_pay=(RelativeLayout)view.findViewById(R.id.confirm_pay);
        image_click_change_w=(ImageView)view.findViewById(R.id.image_click_change_w);
        image_click_change_chat=(ImageView)view.findViewById(R.id.image_click_change_chat);
        image_click_change=(ImageView)view.findViewById(R.id.image_click_change);

        popop_pay_yue=(RelativeLayout)view.findViewById(R.id.popop_pay_yue);
        popop_pay_weix=(RelativeLayout)view.findViewById(R.id.popop_pay_weix);
        popop_pay_api=(RelativeLayout)view.findViewById(R.id.popop_pay_api);

        confirm_pay=(RelativeLayout)view.findViewById(R.id.confirm_pay);
        confirm_pay.setOnClickListener(this);
        popop_pay_yue.setOnClickListener(this);
        popop_pay_weix.setOnClickListener(this);
        popop_pay_api.setOnClickListener(this);
        image_cancel.setOnClickListener(this);
        confirm_pay.setOnClickListener(this);
        rela_tive.setOnClickListener(this);
        rela_tive.setAlpha(0.7f);
    }

    public void setData(GiftBean object){
        money=object.getNum();
        text_name.setText(EMClient.getInstance().getCurrentUser());
        text_money.setText("￥"+object.getNum());
    }

private void initPop(int type){
    mtype=type;
        switch (type){
            case 0:
                image_click_change_w.setBackgroundResource(R.mipmap.pop_cick_icon);
                image_click_change_chat.setBackgroundResource(R.mipmap.pop_money_x);
                image_click_change.setBackgroundResource(R.mipmap.pop_money_x);
                break;
            case 1: {
                image_click_change_w.setBackgroundResource(R.mipmap.pop_money_x);
                image_click_change_chat.setBackgroundResource(R.mipmap.pop_cick_icon);
                image_click_change.setBackgroundResource(R.mipmap.pop_money_x);
               }
                break;
            case 2:{
                image_click_change_w.setBackgroundResource(R.mipmap.pop_money_x);
                image_click_change_chat.setBackgroundResource(R.mipmap.pop_money_x);
                image_click_change.setBackgroundResource(R.mipmap.pop_cick_icon);
               }
                break;
                default:break;
        }
}
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.popop_pay_yue) {
            initPop(0);
        } else if (id == R.id.popop_pay_weix) {
            initPop(1);
        } else if (id == R.id.popop_pay_api) {
            initPop(2);
        } else if (id == R.id.rela_tive || id == R.id.image_cancel) {
            dismiss();
        } else if (id == R.id.confirm_pay) {
            dismiss();
            if (mListener != null) {
                mListener.onItemClick(mtype, money);
            }
        }
    }

    /**
     * 页面更多 pop window里面的点击监听
     */
    public interface CallBackCouponListener {
        /**
         * 单个推广方式点击效果
         */
        void onItemClick(int type, int content);
    }

}



