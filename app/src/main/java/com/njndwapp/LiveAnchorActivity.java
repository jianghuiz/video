package com.njndwapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.easemob.custommessage.OnResourceParseCallback;
import com.easemob.custommessage.PermissionChecker;
import com.easemob.custommessage.bean.AllQuestBean;
import com.easemob.custommessage.bean.GiftBean;
import com.easemob.custommessage.bean.LiveRoomUrlBean;
import com.easemob.custommessage.reponsitories.NetworkOnlyResource;
import com.easemob.custommessage.reponsitories.Resource;
import com.easemob.custommessage.reponsitories.ResultCallBack;
import com.easemob.custommessage.restapi.model.StreamViewModel;
import com.easemob.custommessage.uitls.DemoConstants;
import com.easemob.custommessage.uitls.DemoHelper;
import com.easemob.custommessage.uitls.DemoMsgHelper;
import com.easemob.custommessage.uitls.LiveDataBus;
import com.easemob.custommessage.uitls.OnConfirmClickListener;
import com.easemob.custommessage.uitls.RoomMessagesView;
import com.easemob.custommessage.uitls.ThreadManager;
import com.easemob.custommessage.uitls.User;
import com.easemob.custommessage.uitls.UserBalaceInfo;
import com.easemob.custommessage.uitls.UserRepository;
import com.easemob.custommessage.uitls.Utils;
import com.easemob.custommessage.view.EaseImageView;
import com.easemob.custommessage.view.UpperPopupWindow;
import com.easemob.qiniu_sdk.LiveCameraView;
import com.easemob.qiniu_sdk.PushStreamHelper;
import com.easemob.qiniu_sdk.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMChatRoomChangeListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.exceptions.HyphenateException;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 推流--开启直播
 */
public class LiveAnchorActivity extends AppCompatActivity implements UpperPopupWindow.CallBackCouponListener {
    Context mContext;
    private boolean isStartCamera;
    //    public static com.facebook.react.bridge.Callback cb;
    public static final int MSG_UPDATE_COUNTDOWN = 1;
    public static final int COUNTDOWN_DELAY = 1000;
    public static final int COUNTDOWN_START_INDEX = 3;
    public static final int COUNTDOWN_END_INDEX = 1;
    private static final int INVALID_VAL = -1;
    private static final int COLOR_DEFAULT = Color.parseColor("#20000000");
    private String useName = "000001";
    private String passWord = "123456";
    public String publishUrl;
    //    public static final String BaseUrl = "http://39.100.59.30:9988/yikeshi";
    public static final String BaseUrl = "http://111.231.137.225:9988/yikeshi";
    private PushStreamHelper streamHelper;
    //获取直播信息
    private static final String GetRootInfo = BaseUrl + "/wx/Live/getRootInfo/";
    //获取推流地址
    private static final String PushUrl = BaseUrl + "/wx/Live/pushUrl/";
    //查询点赞总数
    private static final String ThumBupCount = BaseUrl + "/wx/thumbup/count/";
    //点赞返回点赞总数
    private static final String ThumBup = BaseUrl + "/wx/thumbup/thumbUp/";
    //用户余额
    private static final String UserBalance = BaseUrl + "/pc/balance/info/";
    //关闭直播
    private static final String CloseLive = BaseUrl + "/wx/Live/closeLive/";
    final String GETROOTINFO = "get_root_info";
    final String GETPUSHURL = "get_push_url";
    final String POSTZAN = "post_zan_url";
    final String GETNUM = "get_zan_count";
    final String CLOSELIVE = "close_living";
    final String GET_USER_BALANCE = "get_user_balance";
    @BindView(R.id.container)
    LiveCameraView cameraView;
    protected AllQuestBean liveRoom = new AllQuestBean();
    private PermissionChecker mPermissionChecker = new PermissionChecker(this);
    String countZan;
    @BindView(R.id.countdown_txtv)
    TextView countdownView;
    @BindView(R.id.tv_like_num)
    TextView tvLikeNum;
    @BindView(R.id.iv_icon_ease)
    EaseImageView ivIcon;
    @BindView(R.id.audience_num)
    TextView audienceNumView;
    @BindView(R.id.like_image)
    ImageView like_image;
    @BindView(R.id.em_edit_content)
    TextView editContent;
    @BindView(R.id.message_view)
    RoomMessagesView messageView;
    @BindView(R.id.live_receive_gift_num)
    TextView mGiftNum;
    @BindView(R.id.cons_bottom)
    ConstraintLayout consBottom;
    String chatroomId = "";
    String uuid;
    String cookie;
    String userNick;
    String headFileId;
    protected boolean isShutDownCountdown = false;
    protected EMChatRoom chatroom;
    UpperPopupWindow mUpperPopupWindow;

    /**
     * 输入内容
     */
    private void showUpperPopupWindow() {
        if (mUpperPopupWindow == null) {
            mUpperPopupWindow = new UpperPopupWindow(this, this);
        }
        mUpperPopupWindow.setUpdata(editContent.getText().toString());
        mUpperPopupWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    /**
     * 得到当前软键盘的高度
     *
     * @return 软键盘的高度
     */
    public int getCurrentSoftInputHeight() {
        final View decorView = this.getWindow().getDecorView();
        Rect rect = new Rect();
        decorView.getWindowVisibleDisplayFrame(rect);
        // 获取屏幕的高度(包括状态栏，导航栏)
        int screenHeight = decorView.getRootView().getHeight();
        int keySoftHeight = screenHeight - rect.bottom;
        return keySoftHeight;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_COUNTDOWN:
                    handleUpdateCountdown(msg.arg1);
                    break;
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        mPermissionChecker.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = LiveAnchorActivity.this;
        setContentView(R.layout.em_activity_live_anchor);
        ButterKnife.bind(this);
        boolean isPermissionOK = Build.VERSION.SDK_INT < Build.VERSION_CODES.M || mPermissionChecker.checkPermission();
        if (!isPermissionOK) {
            Util.showToast(this, "Some permissions is not approved !!!");
            return;
        }
        PushStreamHelper.getInstance().init(this);
        initParams();
        initChatSdk();
        setFitSystemForTheme(false, android.R.color.transparent);
        LoginLiveUse();
        initLiveEnv();
        //监听聊天室
        EMClient.getInstance().chatroomManager().addChatRoomChangeListener(presenter);
        //监听消息
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    void initParams() {
        publishUrl = getIntent().getStringExtra("publishUrl");
        useName = getIntent().getStringExtra("useName");
        passWord = getIntent().getStringExtra("passWord");
        chatroomId = getIntent().getStringExtra("chatId");
        uuid = getIntent().getStringExtra("uuid");
        userNick = getIntent().getStringExtra("userNick");
        cookie = getIntent().getStringExtra("cookie");
        headFileId = getIntent().getStringExtra("headFileId");
        Log.e("TAG", "publishUrl = " + publishUrl);
        liveRoom.setUuid(uuid);
        liveRoom.setChatId(chatroomId);
        liveRoom.setCookie(cookie);
        if (TextUtils.isEmpty(chatroomId) || TextUtils.isEmpty(useName) || TextUtils.isEmpty(publishUrl) || TextUtils.isEmpty(passWord) || TextUtils.isEmpty(uuid) || TextUtils.isEmpty(cookie)) {
            Toast.makeText(this, "参数不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @OnClick({R.id.img_bt_close, R.id.em_edit_content})
    void close(View view) {
        switch (view.getId()) {
            case R.id.img_bt_close:
                showExitDialog();
                break;
            case R.id.em_edit_content:
                consBottom.setVisibility(View.GONE);
                showUpperPopupWindow();
                break;
            default:
                break;
        }

    }

    private void initLiveEnv() {
        streamHelper = PushStreamHelper.getInstance();
        streamHelper.initPublishVideo(cameraView, publishUrl);

        //初始化设置消息帮助相关信息
        DemoMsgHelper.getInstance().init(liveRoom.getChatId());
//        streamHelper.setPublishUrl(publishUrl);
        initData();
        startLive();
        changeLiveStatus();
//        PullLiveData();
    }

    private void initViewModel() {

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //获取花总数
            getRootInfo(liveRoom);
            getUserBalance(liveRoom);
            getZanCount(liveRoom);
        }
    };

    /**
     * 每一秒刷新数据
     */
    private void PullData() {
        if (mHanlder != null) {
            mHanlder.postDelayed(runnable, 3 * 1000);
        }
    }


    private void initData() {
        getRootInfo(liveRoom);
        LiveDataBus.get().with(GETROOTINFO, GetRootInfo.class)
                .observe(this, new Observer<GetRootInfo>() {
                    @Override
                    public void onChanged(@Nullable GetRootInfo rootInfo) {
                        if (rootInfo != null) {
//                            PreferenceManager.init(mContext, rootInfo.getDetail().getUsername());
                            if (rootInfo.getDetail() != null && !TextUtils.isEmpty(rootInfo.getDetail().getUsername())) {
                                User user = new User();
                                user.setId(useName);
                                user.setNick(rootInfo.getDetail().getUsername());
                                UserRepository.getInstance().setRandomUser(user);
                                liveRoom.setUserName(rootInfo.getDetail().getUsername());
                            }
                            liveRoom.setCreateBy(String.valueOf(rootInfo.getDetail().getCreateBy()));
                            liveRoom.setFlowerCount(rootInfo.getDetail().getFlowerCount());
                            liveRoom.setHeadFileId(headFileId);
                            liveRoom.setIsThumbup(rootInfo.getDetail().getIsThumbup());
                            liveRoom.setThumbupCount(rootInfo.getDetail().getThumbupCount());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (rootInfo.getDetail().getThumbupCount() > 0) {
                                        like_image.setBackgroundResource(R.mipmap.in_zan);
                                    } else {
                                        like_image.setBackgroundResource(R.mipmap.em_live_like_bg);
                                    }
                                }
                            });
                            initUpdata();
                        }
                    }
                });
    }

    ;

    @OnClick(R.id.comment_image)
    void onCommentImageClick() {
        showInputEdit();
    }

    @OnClick(R.id.live_receive_gift)
    void onCommentGiftClick() {
//        showGiftDialog();
    }

    private void showGiftDialog() {
        getUserBalance(liveRoom);
        LiveNewGiftDialog dialog = (LiveNewGiftDialog) getSupportFragmentManager().findFragmentByTag("live_gift");
        if (dialog == null) {
            dialog = new LiveNewGiftDialog(this);
        }
        if (dialog.isAdded()) {
            return;
        }
        dialog.show(getSupportFragmentManager(), "live_gift");
        dialog.setOnConfirmClickListener(new OnConfirmClickListener() {
            @Override
            public void onConfirmClick(View view, Object bean) {
                if (bean instanceof GiftBean) {
                    //送花
                    postZan(liveRoom, 2, ((GiftBean) bean).getNum());
//                    presenter.sendGiftMsg((GiftBean) bean, new OnMsgCallBack() {
//                        @Override
//                        public void onSuccess(EMMessage message) {
//                            ThreadManager.getInstance().runOnMainThread(()-> {
//                                barrageLayout.showGift((GiftBean) bean);
//                            });
//                        }showInputEdit
//                    });

                }
            }
        });
    }

    /**
     * 接口点赞
     */
    @OnClick(R.id.like_image)
    void PraiseUI() {
        // postZan(liveRoom, 1, 1);
    }

    private void initUpdata() {
        mHanlder.sendEmptyMessage(0);
        String headPul = BaseUrl + "/" + headFileId;
        Glide.with(this).load(headPul).placeholder(R.color.placeholder).into(ivIcon);
        LiveDataBus.get().with(DemoConstants.POSTZAN, ZanInfo.class)
                .observe(this, new Observer<ZanInfo>() {
                    @Override
                    public void onChanged(@Nullable ZanInfo rootInfo) {
                        mHanlder.sendEmptyMessage(0);
                    }
                });
        LiveDataBus.get().with(DemoConstants.GETNUM, ZanInfo.class)
                .observe(this, new Observer<ZanInfo>() {
                    @Override
                    public void onChanged(@Nullable ZanInfo rootInfo) {
                        if (rootInfo != null) {
                            if (TextUtils.isEmpty(countZan) || !countZan.equals(String.valueOf(rootInfo.getDetail()))) {
                                countZan = String.valueOf(rootInfo.getDetail());
                                tvLikeNum.setText(countZan);
                            }
                        }
                    }
                });

    }

    Handler mHanlder = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            getZanCount(liveRoom);
        }
    };

    /**
     * 开始直播
     */
    private void startLive() {
        //Utils.hideKeyboard(titleEdit);
        new Thread() {
            public void run() {
                int i = COUNTDOWN_START_INDEX;
                do {
                    Message msg = Message.obtain();
                    msg.what = MSG_UPDATE_COUNTDOWN;
                    msg.arg1 = i;
                    handler.sendMessage(msg);
                    i--;
                    try {
                        Thread.sleep(COUNTDOWN_DELAY);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } while (i >= COUNTDOWN_END_INDEX);
            }
        }.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //如果需要页面不可见后停止推流调用此方法
//        streamHelper.pause();
    }

    void handleUpdateCountdown(final int count) {
        if (countdownView != null) {
            countdownView.setVisibility(View.VISIBLE);
            countdownView.setText(String.format("%d", count));
            ScaleAnimation scaleAnimation =
                    new ScaleAnimation(1.0f, 0f, 1.0f, 0f, Animation.RELATIVE_TO_SELF, 0.5f,
                            Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setDuration(COUNTDOWN_DELAY);
            scaleAnimation.setFillAfter(false);
            scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    countdownView.setVisibility(View.GONE);

                    if (count == COUNTDOWN_END_INDEX
                            //&& mEasyStreaming != null
                            && !isShutDownCountdown && mContext != null) {
                        EMClient.getInstance()
                                .chatroomManager()
                                .joinChatRoom(liveRoom.getChatId(), new EMValueCallBack<EMChatRoom>() {
                                    @Override
                                    public void onSuccess(EMChatRoom emChatRoom) {
                                        chatroom = emChatRoom;
                                        getMembers(liveRoom.getChatId());
                                        ThreadManager.getInstance().runOnMainThread(LiveAnchorActivity.this::getLiveRoomDetail);
                                    }

                                    @Override
                                    public void onError(int i, String s) {
                                        Utils.showToast(LiveAnchorActivity.this, "加入聊天室失败");
                                        finish();
                                    }
                                });
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            if (!isShutDownCountdown) {
                countdownView.startAnimation(scaleAnimation);
            } else {
                countdownView.setVisibility(View.GONE);
            }
        }
    }

    private void getLiveRoomDetail() {
        LiveAnchorActivity.this.liveRoom = liveRoom;
        startAnchorLive(liveRoom);
    }

    private void startAnchorLive(AllQuestBean liveRoom) {
        DemoHelper.saveLivingId(liveRoom.getUuid());
//        ivIcon.setImageResource(DemoHelper.getAvatarResource(EMClient.getInstance().getCurrentUser()));
        String headPul = BaseUrl + "/" + headFileId;
        Glide.with(mContext).load(headPul).placeholder(R.color.placeholder).into(ivIcon);
//        ivIcon.setImageResource(R.drawable.em_avatar_1);
//        EMClient.getInstance().chatroomManager().addChatRoomChangeListener(presenter);
        onMessageListInit();
        Utils.showToast(this, "直播开始");
        streamHelper.resume();
        isStartCamera = true;
    }

    @OnClick(R.id.switch_camera_image)
    protected void switchCamera() {
        streamHelper.switchCamera();
    }

    protected void onMessageListInit() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageView.init(chatroomId);
                messageView.setMessageViewListener(new RoomMessagesView.MessageViewListener() {
                    @Override
                    public void onMessageSend(String content, boolean isBarrageMsg) {
                        EMMessage message = EMMessage.createTxtSendMessage(content, liveRoom.getUuid());
                        message.setChatType(EMMessage.ChatType.ChatRoom);
                        EMClient.getInstance().chatManager().sendMessage(message);
                        message.setMessageStatusCallback(new EMCallBack() {
                            @Override
                            public void onSuccess() {
                                messageView.refreshSelectLast();
                            }

                            @Override
                            public void onError(int i, String s) {
                            }

                            @Override
                            public void onProgress(int i, String s) {
                            }
                        });
                    }

                    @Override
                    public void onItemClickListener(final EMMessage message) {
                        //if(message.getFrom().equals(EMClient.getInstance().getCurrentUser())){
                        //    return;
                        //}
                        String clickUsername = message.getFrom();
//                        showUserDetailsDialog(clickUsername);
                    }

                    @Override
                    public void onHiderBottomBar() {
//                        bottomBar.setVisibility(View.VISIBLE);
                    }
                });
                messageView.setVisibility(View.VISIBLE);
//                bottomBar.setVisibility(View.VISIBLE);
                if (!chatroom.getAdminList().contains(EMClient.getInstance().getCurrentUser())
                        && !chatroom.getOwner().equals(EMClient.getInstance().getCurrentUser())) {
//                    userManagerView.setVisibility(View.VISIBLE);
                }
//                isMessageListInited = true;
//                updateUnreadMsgView();
//                showMemberList();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isStartCamera) {
            streamHelper.resume();
        }
        //获取花总数
        getUserBalance(liveRoom);
        LiveDataBus.get().with(DemoConstants.GET_USERNum_BALANCE, UserBalaceInfo.class)
                .observe(this, new Observer<UserBalaceInfo>() {
                    @Override
                    public void onChanged(@Nullable UserBalaceInfo rootInfo) {
                        if (rootInfo.getCode() == 0 && rootInfo.getDetail() != null) {
                            mGiftNum.setText(String.valueOf(rootInfo.getDetail()));
                        } else {
                            mGiftNum.setText("0");
                        }
                    }
                });
        PullData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        streamHelper.destroy();
        if (!TextUtils.isEmpty(liveRoom.getChatId())) {
            EMClient.getInstance().chatroomManager().leaveChatRoom(liveRoom.getChatId());
            EMClient.getInstance().chatroomManager().removeChatRoomChangeListener(presenter);
            EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        }
    }


    public void LoginLiveUse() {
        EMClient.getInstance().login(useName, passWord, new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.e("TAG", "publishUrl->登陆成功");
            }

            @Override
            public void onError(int i, final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LiveAnchorActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onProgress(int i, String s) {
            }
        });
    }

    /**
     * 设置是否是沉浸式，并可设置状态栏颜色
     *
     * @param fitSystemForTheme
     * @param colorId           颜色资源路径
     */
    public void setFitSystemForTheme(boolean fitSystemForTheme, @ColorRes int colorId) {
        setFitSystemForTheme(this, fitSystemForTheme, ContextCompat.getColor(this, colorId));
    }

    public static void setFitSystemForTheme(Activity activity, boolean fitSystemForTheme, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (fitSystemForTheme) {
            ViewGroup contentFrameLayout = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
            View parentView = contentFrameLayout.getChildAt(0);
            if (parentView != null && Build.VERSION.SDK_INT >= 14) {
                parentView.setFitsSystemWindows(true);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
        compat(activity, color);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void compat(Activity activity, int statusColor) {

        //当前手机版本为5.0及以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (statusColor != INVALID_VAL) {
                activity.getWindow().setStatusBarColor(statusColor);
            }
            return;
        }

        //当前手机版本为4.4
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            int color = COLOR_DEFAULT;
            ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
            if (statusColor != INVALID_VAL) {
                color = statusColor;
            }
            int childCount = contentView.getChildCount();
            if (childCount > 1) {
                contentView.removeViewAt(1);
            }
            View statusBarView = new View(activity);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(activity));
            statusBarView.setBackgroundColor(color);
            contentView.addView(statusBarView, lp);
        }

    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void initChatSdk() {
        EMOptions options = new EMOptions();
        options.setAutoLogin(false);
        EmClientInit(options);
        EMClient.getInstance().setDebugMode(BuildConfig.DEBUG);
    }

    private void EmClientInit(EMOptions options) {
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(this, pid);
        if (processAppName == null || !processAppName.equalsIgnoreCase(this.getPackageName())) {
            return;
        }
        if (options == null) {
            EMClient.getInstance().init(this, initChatOptions());
        } else {
            EMClient.getInstance().init(this, options);
        }
    }

    /**
     * check the application process name if process name is not qualified, then we think it is a service process and we will not init SDK
     *
     * @param pID
     * @return
     */
    private String getAppName(Context context, int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = context.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    // Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
                    // info.processName +"  Label: "+c.toString());
                    // processName = c.toString();
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    private EMOptions initChatOptions() {
        EMOptions options = new EMOptions();
        // change to need confirm contact invitation
        options.setAcceptInvitationAlways(false);
        // set if need read ack
        options.setRequireAck(true);
        // set if need delivery ack
        options.setRequireDeliveryAck(false);

        return options;
    }


    /**
     * get异步请求--获取直播信息
     */
    public void getRootInfo(AllQuestBean questBeanb) {
        String Url = GetRootInfo + questBeanb.getUuid() + "/1";
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Url)
                .addHeader("Cookie", questBeanb.getCookie())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    String success = response.body().string();
//                    Log.e("==", success);
                    GetRootInfo list = new Gson().fromJson(success, new TypeToken<GetRootInfo>() {
                    }.getType());
                    LiveDataBus.get().with(GETROOTINFO).postValue(list);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    /**
     * 推流地址
     *
     * @param questBeanb
     */
    public void getPushUrl(AllQuestBean questBeanb) {
        String Url = PushUrl + questBeanb.getOnlineFlag() + "/" + questBeanb.getUuid();
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Url)
                .addHeader("Cookie", questBeanb.getCookie())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    String success = response.body().string();
                    Log.e("==", success);
                    PushInfo list = new Gson().fromJson(success, new TypeToken<PushInfo>() {
                    }.getType());
                    LiveDataBus.get().with(GETPUSHURL).postValue(list);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    /**
     * post请求
     */
    public void changeLiveStatus() {
        String Url = BaseUrl + "/wx/Live/updateLiveStatus/" + uuid + "/1";
        Log.e("ErrorUrl", Url);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //实例
                OkHttpClient okHttpClient = new OkHttpClient();
                //创建post请求数据表单
                RequestBody requestBody = new FormBody.Builder()
                        .build();
                //创建请求
                final Request request = new Request.Builder()
                        .url(Url)
                        .post(requestBody)//添加post请求
                        .addHeader("Cookie", cookie)
                        .build();

                //发送请求得到响应
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("error", e.getMessage());
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) {
                        Log.i("error", response.toString());
                    }
                });
            }
        }).start();
    }

    /**
     * post请求
     */
    public void postZan(AllQuestBean questBeanb, int type, int count) {
        int postion = type == 1 ? 1 : count;
        String Url = ThumBup + questBeanb.getUuid() + "/" + type + "/" + postion;
        new Thread(new Runnable() {
            @Override
            public void run() {
                //实例
                OkHttpClient okHttpClient = new OkHttpClient();
                //创建post请求数据表单
                RequestBody requestBody = new FormBody.Builder()
                        .build();
                //创建请求
                final Request request = new Request.Builder()
                        .url(Url)
                        .post(requestBody)//添加post请求
                        .addHeader("Cookie", questBeanb.getCookie())
                        .build();

                //发送请求得到响应
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) {
                        try {
                            if (type == 1) {
                                //1 点赞 2是送花
                                String success = response.body().string();
                                Log.e("==", success);
                                ZanInfo list = new Gson().fromJson(success, new TypeToken<ZanInfo>() {
                                }.getType());
                                LiveDataBus.get().with(POSTZAN).postValue(list);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        }).start();
    }


    /**
     * 获取点赞总数
     *
     * @param questBeanb
     */

    public void getZanCount(AllQuestBean questBeanb) {
        String Url = ThumBupCount + questBeanb.getUuid() + "/" + questBeanb.getOnlineFlag();
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Url)
                .addHeader("Cookie", questBeanb.getCookie())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    String success = response.body().string();
//                    Log.e("==", success);
                    ZanInfo list = new Gson().fromJson(success, new TypeToken<ZanInfo>() {
                    }.getType());
                    LiveDataBus.get().with(GETNUM).postValue(list);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    /**
     * 用户余额
     *
     * @param questBeanb
     */
    public void getUserBalance(AllQuestBean questBeanb) {
        String Url = UserBalance + questBeanb.getUuid();
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Url)
                .addHeader("Cookie", questBeanb.getCookie())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    String success = response.body().string();
//                    Log.e("==", success);
                    UserBalaceInfo list = new Gson().fromJson(success, new TypeToken<UserBalaceInfo>() {
                    }.getType());
                    LiveDataBus.get().with(DemoConstants.GET_USERNum_BALANCE).postValue(list);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    @Override
    public void onItemClick(String content, int type) {
        consBottom.setVisibility(View.VISIBLE);
        if (mUpperPopupWindow != null) {
            mUpperPopupWindow.setData();
            mUpperPopupWindow.dismiss();
        }
        editContent.setText("");
        if (!TextUtils.isEmpty(content)) {
            sendInpuEdit(content);
        }
    }

    private void sendInpuEdit(String content) {
        EMMessage message = EMMessage.createTxtSendMessage(content, liveRoom.getChatId());
        message.setChatType(EMMessage.ChatType.ChatRoom);
        message.setAttribute("userNick", userNick);
        EMClient.getInstance().chatManager().sendMessage(message);
        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                messageView.refreshSelectLast();
            }

            @Override
            public void onError(int i, String s) {
            }

            @Override
            public void onProgress(int i, String s) {
            }
        });
    }

    public class GetRootInfo {

        /**
         * success : true
         * code : 0
         * msg : null
         * detail : {"flowerCount":10,"createBy":493,"headFileId":"/logic/sysfile/downloadImg/578","thumbupCount":0,"isThumbup":0,"username":"流量在拉萨"}
         */

        private boolean success;
        private int code;
        //    private Object msg;
        private DetailBean detail;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public DetailBean getDetail() {
            return detail;
        }

        public void setDetail(DetailBean detail) {
            this.detail = detail;
        }

        public class DetailBean {
            /**
             * flowerCount : 10
             * createBy : 493
             * headFileId : /logic/sysfile/downloadImg/578
             * thumbupCount : 0
             * isThumbup : 0
             * username : 流量在拉萨
             */

            private int flowerCount;
            private String createBy;
            private String headFileId;
            private int thumbupCount;
            private int isThumbup;
            private String username;

            public int getFlowerCount() {
                return flowerCount;
            }

            public void setFlowerCount(int flowerCount) {
                this.flowerCount = flowerCount;
            }

            public String getCreateBy() {
                return createBy;
            }

            public void setCreateBy(String createBy) {
                this.createBy = createBy;
            }

            public String getHeadFileId() {
                return headFileId;
            }

            public void setHeadFileId(String headFileId) {
                this.headFileId = headFileId;
            }

            public int getThumbupCount() {
                return thumbupCount;
            }

            public void setThumbupCount(int thumbupCount) {
                this.thumbupCount = thumbupCount;
            }

            public int getIsThumbup() {
                return isThumbup;
            }

            public void setIsThumbup(int isThumbup) {
                this.isThumbup = isThumbup;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }
        }
    }

    public class PushInfo {


        /**
         * success : true
         * code : 0
         * msg : null
         * detail : {"pushUrl":"rtmp://pili-publish.yksbj.com/piliyikeshi/course_9WrYeI_1_14805de4618948598be19b170e7d0626?e=1590410627&token=4kJKhYCmzbCJQCdUTIrkzmsiC9uFwCU_K3ac4oeB:yXf1aK7_z-uOO5afo6z-67mhft0=","streamName":"course_9WrYeI_1_14805de4618948598be19b170e7d0626","pullUrl":null}
         */

        private boolean success;
        private int code;
        private String msg;
        private DetailBean detail;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public DetailBean getDetail() {
            return detail;
        }

        public void setDetail(DetailBean detail) {
            this.detail = detail;
        }

        public class DetailBean {
            /**
             * pushUrl : rtmp://pili-publish.yksbj.com/piliyikeshi/course_9WrYeI_1_14805de4618948598be19b170e7d0626?e=1590410627&token=4kJKhYCmzbCJQCdUTIrkzmsiC9uFwCU_K3ac4oeB:yXf1aK7_z-uOO5afo6z-67mhft0=
             * streamName : course_9WrYeI_1_14805de4618948598be19b170e7d0626
             * pullUrl : null
             */

            private String pushUrl;
            private String streamName;
            private Object pullUrl;

            public String getPushUrl() {
                return pushUrl;
            }

            public void setPushUrl(String pushUrl) {
                this.pushUrl = pushUrl;
            }

            public String getStreamName() {
                return streamName;
            }

            public void setStreamName(String streamName) {
                this.streamName = streamName;
            }

            public Object getPullUrl() {
                return pullUrl;
            }

            public void setPullUrl(Object pullUrl) {
                this.pullUrl = pullUrl;
            }
        }
    }

    public class ZanInfo {

        /**
         * success : true
         * code : 0
         * msg : null
         * detail : 0
         */

        private boolean success;
        private int code;
        private Object msg;
        private int detail;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public Object getMsg() {
            return msg;
        }

        public void setMsg(Object msg) {
            this.msg = msg;
        }

        public int getDetail() {
            return detail;
        }

        public void setDetail(int detail) {
            this.detail = detail;
        }
    }

    /**
     * 发送文本消息
     */
    public void showInputEdit() {
        if (TextUtils.isEmpty(editContent.getText())) {
            Toast.makeText(this, "文字内容不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        EMMessage message = EMMessage.createTxtSendMessage(editContent.getText().toString(), liveRoom.getChatId());
        message.setChatType(EMMessage.ChatType.ChatRoom);
        message.setAttribute("userNick", userNick);
        EMClient.getInstance().chatManager().sendMessage(message);
        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                messageView.refreshSelectLast();
            }

            @Override
            public void onError(int i, String s) {
            }

            @Override
            public void onProgress(int i, String s) {
            }
        });
        editContent.setText("");
    }


    /**
     * 关闭直播
     *
     * @param questBeanb
     */
    public static void closeLiving(AllQuestBean questBeanb) {
        String Url = CloseLive + questBeanb.getUuid() + "/" + questBeanb.getOnlineFlag();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //实例
                OkHttpClient okHttpClient = new OkHttpClient();
                //创建post请求数据表单
                RequestBody requestBody = new FormBody.Builder()
                        .build();
                //创建请求
                final Request request = new Request.Builder()
                        .url(Url)
                        .post(requestBody)//添加post请求
                        .addHeader("Cookie", questBeanb.getCookie())
                        .build();

                //发送请求得到响应
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) {
                        LiveDataBus.get().with(DemoConstants.CLOSELIVE).postValue(true);
                    }
                });
            }
        }).start();
    }


    /**
     * 关闭直播显示直播成果
     */
    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle(getResources().getString(R.string.em_live_dialog_quit_title)); // 友情提醒
        builder.setMessage("确定结束直播？");
        builder.setCancelable(false);
        builder.setPositiveButton("结束并退出", new DialogInterface.OnClickListener() { // 确定
            @Override
            public void onClick(DialogInterface dialog, int which) {
                leaveRoom();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * 停止直播
     */
    private void leaveRoom() {
//        cb.invoke("finish");
        closeLiving(liveRoom);
        LiveDataBus.get().with(DemoConstants.CLOSELIVE, Boolean.class)
                .observe(this, response -> {
                    cameraView.onPause();
                    DemoHelper.saveLivingId("");
//                    DemoHelper.getReceiveGiftDao().clearData(DemoMsgHelper.getInstance().getCurrentRoomId());
//                    DemoHelper.saveLikeNum(liveRoom.getUuid(), 0);
                    finish();
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN
                && event.getRepeatCount() == 0) {
            //具体的操作代码
//            finish();
            showExitDialog();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 注册监听聊天
     */
    EMChatRoomChangeListener presenter = new EMChatRoomChangeListener() {
        @Override
        public void onChatRoomDestroyed(String roomId, String roomName) {
            //聊天室被解散。
            if (roomId.equals(chatroomId)) {
                finish();
            }
        }

        @Override
        public void onMemberJoined(String roomId, String participant) {
            //聊天室加入新成员事件
            getMembers(liveRoom.getChatId());
        }

        @Override
        public void onMemberExited(String roomId, String roomName, String participant) {
            //聊天室成员主动退出事件
            getMembers(liveRoom.getChatId());
        }

        @Override
        public void onRemovedFromChatRoom(int reason, String roomId, String roomName, String participant) {
            //聊天室人员被移除
            getMembers(liveRoom.getChatId());
        }

        @Override
        public void onMuteListAdded(String chatRoomId, List<String> mutes, long expireTime) {
            //有成员被禁言
        }

        @Override
        public void onMuteListRemoved(String chatRoomId, List<String> mutes) {

        }

        @Override
        public void onWhiteListAdded(String chatRoomId, List<String> whitelist) {
            //白名单成员增加
        }

        @Override
        public void onWhiteListRemoved(String chatRoomId, List<String> whitelist) {
            //白名单成员减少
        }

        @Override
        public void onAllMemberMuteStateChanged(String chatRoomId, boolean isMuted) {
            //全员禁言状态的改变
        }

        @Override
        public void onAdminAdded(String chatRoomId, String admin) {
            //升为管理员
        }

        @Override
        public void onAdminRemoved(String chatRoomId, String admin) {
            //管理员被解除
        }

        @Override
        public void onOwnerChanged(String chatRoomId, String newOwner, String oldOwner) {
            //转移拥有者
        }

        @Override
        public void onAnnouncementChanged(String chatRoomId, String announcement) {
            //聊天室公告更改事件
        }
    };

    /**
     * 获取成员列表
     *
     * @param roomId
     * @return
     */
    public LiveData<Resource<List<String>>> getMembers(String roomId) {
        return new NetworkOnlyResource<List<String>, List<String>>() {
            @Override
            protected void createCall(@NonNull ResultCallBack<LiveData<List<String>>> callBack) {
                ThreadManager.getInstance().runOnIOThread(() -> {
                    try {
                        EMChatRoom chatRoom = EMClient.getInstance().chatroomManager().fetchChatRoomFromServer(roomId, true);
                        List<String> allMembers = new ArrayList<>();
                        List<String> memberList = chatRoom.getMemberList();
                        allMembers.add(chatRoom.getOwner());
                        if (chatRoom.getAdminList() != null) {
                            allMembers.addAll(chatRoom.getAdminList());
                        }
                        if (memberList != null) {
                            allMembers.addAll(memberList);
                        }
                        if (chatroom != null) {
                            //int size = chatroom.getMemberCount();
                            int size = memberList.size();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    audienceNumView.setText(String.valueOf(size));
                                }
                            });
                        }
                    } catch (HyphenateException e) {
                    }
                });
            }
        }.asLiveData();
    }

    EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            //收到消息
            messageView.refreshSelectLast();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //收到透传消息
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
            //收到已读回执
            messageView.refreshSelectLast();
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
            //收到已送达回执
            messageView.refreshSelectLast();
        }

        @Override
        public void onMessageRecalled(List<EMMessage> messages) {
            //消息被撤回
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
            messageView.refreshSelectLast();
        }
    };
}
