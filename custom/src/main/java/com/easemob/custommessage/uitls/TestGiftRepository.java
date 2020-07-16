package com.easemob.custommessage.uitls;

import com.easemob.custommessage.bean.GiftBean;
import com.hyphenate.chat.EMClient;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于获取本地礼物信息
 */
public class TestGiftRepository {
    static int SIZE = 7;
    static String[] names = {};
    static String[] nameLists = {"1束","2束","5束","10束","50束","66束","99束"};

    public static List<GiftBean> getDefaultListGifts(){
        List<GiftBean> gifts = new ArrayList<>();
        GiftBean bean;
        User user;
        for (int i=0;i<nameLists.length;i++){
            bean = new GiftBean();
            bean.setName(nameLists[i]);
            bean.setId("gift_"+i);
            user = new User();
            user.setUsername(EMClient.getInstance().getCurrentUser());
            bean.setUser(user);
            gifts.add(bean);
        }
        return gifts;
    }
}
