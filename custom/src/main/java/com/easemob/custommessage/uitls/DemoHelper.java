package com.easemob.custommessage.uitls;


import android.text.TextUtils;

import java.text.DecimalFormat;

public class DemoHelper {
    public static void saveLivingId(String liveId) {
        PreferenceManager.getInstance().saveLivingId(liveId);
    }
    /**
     * 获取用户的昵称
     * @param username
     * @return
     */
    public static String getNickName(String username) {
        User user = UserRepository.getInstance().getUserByUsername(username);
        if(user == null) {
            return username;
        }
        return user.getNickname();
    }
    /**
     * 清除用户id
     */
    public static void clearUserId() {
        PreferenceManager.getInstance().saveUserId("");
    }

    public static String getUserId() {
        return PreferenceManager.getInstance().getUserId();
    }
    /**
     * 格式化数字
     * @param num
     * @return
     */
    public static String formatNum(double num) {
        if(num < 10000) {
            return String.valueOf((int) num);
        }
        return new DecimalFormat("#0.0").format(num / 10000) + "万";
    }

    /**
     * 判断房间状态
     * @param status
     * @return
     */
    public static boolean isLiving(String status) {
        return !TextUtils.isEmpty(status) && TextUtils.equals(status, DemoConstants.LIVE_ONGOING);
    }
}
