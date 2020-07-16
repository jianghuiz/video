package com.easemob.custommessage.uitls;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

/**
 * @author jianghuizhong
 * @describe com.easywed.marry.utils
 * @date 2018/6/1.
 */

public class ViewUtils {
    /**
     * 背景透明度设置
     *
     * @param bgAlpha
     */
    public static void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        context.getWindow().setAttributes(lp);
    }

    /**
     * 计算屏幕的宽度
     */
    public static int DisplayWidth(Activity context) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    /**
     * 将dp转化为px
     */
    public static int dip2px(Activity context, float dip) {
        float v = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources().getDisplayMetrics());
        return (int) (v + 0.5f);
    }
}
