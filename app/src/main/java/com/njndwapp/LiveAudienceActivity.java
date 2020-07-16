package com.njndwapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.easemob.custommessage.bean.AllQuestBean;
import com.easemob.custommessage.bean.GiftBean;
import com.easemob.custommessage.bean.LiveRoom;
import com.easemob.custommessage.reponsitories.NetworkOnlyResource;
import com.easemob.custommessage.reponsitories.Resource;
import com.easemob.custommessage.reponsitories.ResultCallBack;
import com.easemob.custommessage.restapi.model.StreamViewModel;
import com.easemob.custommessage.uitls.DemoConstants;
import com.easemob.custommessage.uitls.DemoMsgHelper;
import com.easemob.custommessage.uitls.LiveDataBus;
import com.easemob.custommessage.uitls.OnConfirmClickListener;
import com.easemob.custommessage.uitls.PreferenceManager;
import com.easemob.custommessage.uitls.RoomMessagesView;
import com.easemob.custommessage.uitls.ThreadManager;
import com.easemob.custommessage.uitls.User;
import com.easemob.custommessage.uitls.UserBalaceInfo;
import com.easemob.custommessage.uitls.UserRepository;
import com.easemob.custommessage.uitls.Utils;
import com.easemob.custommessage.view.EaseImageView;
import com.easemob.custommessage.view.PayPopupWindow;
import com.easemob.custommessage.view.UpperPopupWindow;
import com.easemob.qiniu_sdk.LiveVideoView;
import com.easemob.qiniu_sdk.PushStreamHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMChatRoomChangeListener;
import com.hyphenate.EMError;
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
 * 播流--选择直播
 */
public class LiveAudienceActivity extends AppCompatActivity implements PayPopupWindow.CallBackCouponListener, LiveVideoView.OnVideoListener, UpperPopupWindow.CallBackCouponListener {
    public static final int MSG_UPDATE_COUNTDOWN = 1;
    public static final int COUNTDOWN_DELAY = 1000;
    public static final int COUNTDOWN_START_INDEX = 3;
    public static final int COUNTDOWN_END_INDEX = 1;
    //    public static com.facebook.react.bridge.Callback cb;
    private static final int INVALID_VAL = -1;
    private static final int COLOR_DEFAULT = Color.parseColor("#20000000");
    //    public static final String BaseUrl = "http://39.100.59.30:9988/yikeshi";
    public static final String BaseUrl = "http://111.231.137.225:9988/yikeshi";
    //获取播流地址
    private static final String PullUrl = BaseUrl + "/wx/Live/pullUrl/";
    //查询点赞总数
    private static final String ThumBupCount = BaseUrl + "/wx/thumbup/count/";
    //点赞返回点赞总数
    private static final String ThumBup = BaseUrl + "/wx/thumbup/thumbUp/";
    //用户余额
    private static final String UserBalance = BaseUrl + "/wx/balance/info/";
    //获取直播信息
    private static final String GetRootInfo = BaseUrl + "/wx/Live/getRootInfo/";
    private String useName = "";
    private String passWord = "";
    private String pullUrl;
    String nickName;
    String chatroomId = "";
    String uuid;
    String cookie;
    String headFileId;
    Context mContext;
    protected AllQuestBean liveRoom = new AllQuestBean();
    @BindView(R.id.videoview)
    LiveVideoView videoview;
    @BindView(R.id.ll_stream_loading)
    View llStreamLoading;
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

    private boolean isPrepared;
    private StreamViewModel viewModel;
    @BindView(R.id.loading_layout)
    RelativeLayout loadingLayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.loading_text)
    TextView loadingText;
    @BindView(R.id.cover_image)
    ImageView coverImage;
    protected EMChatRoom chatroom;
    @BindView(R.id.live_receive_gift_num)
    TextView mGiftNum;
    Timer mTimer = null;
    String userNick;
    String countZan;
    LiveRoom mliveRoom;
    @BindView(R.id.bottom_baddr)
    ConstraintLayout consBottom;
    UpperPopupWindow mUpperPopupWindow;
    private String userBalace;

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

    PayPopupWindow mPayPopupWindow;

    /**
     * 选择支付
     */
    private void showPayPopupWindow(GiftBean mGiftBean) {
        if (mPayPopupWindow == null) {
            mPayPopupWindow = new PayPopupWindow(this, this);
        }
        mPayPopupWindow.setData(mGiftBean);
        mPayPopupWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = LiveAudienceActivity.this;
        setContentView(R.layout.em_activity_live_audiences);
        ButterKnife.bind(this);
        setFitSystemForTheme(false, android.R.color.transparent);
        PushStreamHelper.getInstance().init(this);
        initParams();
        initChatSdk();
        LoginLiveUse();
        init();
        inData();
        initUpdata();
        //监听聊天室
        EMClient.getInstance().chatroomManager().addChatRoomChangeListener(presenter);
        //监听消息
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    void initParams() {
        pullUrl = getIntent().getStringExtra("pullUrl");
        nickName = getIntent().getStringExtra("nickName");
        useName = getIntent().getStringExtra("useName");
        passWord = getIntent().getStringExtra("passWord");
        chatroomId = getIntent().getStringExtra("chatId");
        uuid = getIntent().getStringExtra("uuid");
        cookie = getIntent().getStringExtra("cookie");
        userNick = getIntent().getStringExtra("userNick");
        headFileId = getIntent().getStringExtra("headFileId");
        liveRoom.setUuid(uuid);
        liveRoom.setChatId(chatroomId);
        liveRoom.setCookie(cookie);
        if (TextUtils.isEmpty(chatroomId) || TextUtils.isEmpty(useName) || TextUtils.isEmpty(pullUrl) || TextUtils.isEmpty(passWord) || TextUtils.isEmpty(uuid) || TextUtils.isEmpty(cookie)) {
            Toast.makeText(this, "参数不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.e("liveroom", liveRoom.toString());
    }

    private void initChatSdk() {
        EMOptions options = new EMOptions();
        options.setAutoLogin(false);
        EmClientInit(options);
        EMClient.getInstance().setDebugMode(BuildConfig.DEBUG);
        //初始化设置消息帮助相关信息
        DemoMsgHelper.getInstance().init(liveRoom.getChatId());
    }

    private void init() {
        Log.e("TAG", "pullUrl = " + pullUrl);
        videoview.attachView();
        videoview.setOnVideoListener(this);
        Glide.with(mContext)
                .load(liveRoom.getCover())
                .error(R.drawable.em_live_default_bg)
                .into(coverImage);
    }

    @OnClick({R.id.img_bt_close, R.id.em_edit_content})
    void close(View view) {
        switch (view.getId()) {
            case R.id.img_bt_close:
                finish();
                break;
            case R.id.em_edit_content:
                consBottom.setVisibility(View.GONE);
                showUpperPopupWindow();
                break;
            default:
                break;
        }

    }

    private void inData() {
        videoview.setAvOptions();
//        //提供了设置加载动画的接口，在播放器进入缓冲状态时，自动显示加载界面，缓冲结束后，自动隐藏加载界面
        videoview.setLoadingView(llStreamLoading);
//        videoview.setVideoPath(pullUrl);
        getRootInfo(liveRoom);
        LiveDataBus.get().with(DemoConstants.GETROOTINFO, LiveAnchorActivity.GetRootInfo.class)
                .observe(this, new Observer<LiveAnchorActivity.GetRootInfo>() {
                    @Override
                    public void onChanged(@Nullable LiveAnchorActivity.GetRootInfo rootInfo) {
                        if (rootInfo != null) {
//                            PreferenceManager.init(mContext, rootInfo.getDetail().getUsername());
                            if (rootInfo.getDetail() != null && !TextUtils.isEmpty(rootInfo.getDetail().getUsername())) {
                                liveRoom.setUserName(rootInfo.getDetail().getUsername());
                                User user = new User();
                                user.setId(useName);
                                user.setNick(rootInfo.getDetail().getUsername());
                                UserRepository.getInstance().setRandomUser(user);
                            }
                            liveRoom.setCreateBy(String.valueOf(rootInfo.getDetail().getCreateBy()));
                            liveRoom.setFlowerCount(rootInfo.getDetail().getFlowerCount());
                            liveRoom.setHeadFileId(headFileId);
                            liveRoom.setIsThumbup(rootInfo.getDetail().getIsThumbup());
                            liveRoom.setThumbupCount(rootInfo.getDetail().getThumbupCount());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (liveRoom.getIsThumbup() == 1) {
                                        like_image.setBackgroundResource(R.mipmap.in_zan);
                                    } else {
                                        like_image.setBackgroundResource(R.mipmap.em_live_like_bg);
                                    }
                                }
                            });
                        }
                    }
                });

        initViewModel();
    }

    @OnClick(R.id.comment_image)
    void onCommentImageClick() {
        showInputEdit();
    }

    @OnClick(R.id.live_receive_gift)
    void onCommentGiftClick() {
        showGiftDialog();
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
                    userBalace = ((GiftBean) bean).getId();
                    showPayPopupWindow((GiftBean) bean);
//                    presenter.sendGiftMsg((GiftBean) bean, new OnMsgCallBack() {
//                        @Override
//                        public void onSuccess(EMMessage message) {
//                            ThreadManager.getInstance().runOnMainThread(()-> {
//                                barrageLayout.showGift((GiftBean) bean);
//                            });
//                        }
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
        postZan(liveRoom, 1, 1);
    }

    private void initUpdata() {
        mHanlder.sendEmptyMessage(0);
        String headPul = BaseUrl + "/" + headFileId;
        Glide.with(this).load(headPul).placeholder(R.color.placeholder).into(ivIcon);
        LiveDataBus.get().with(DemoConstants.POSTZAN, LiveAnchorActivity.ZanInfo.class)
                .observe(this, new Observer<LiveAnchorActivity.ZanInfo>() {
                    @Override
                    public void onChanged(@Nullable LiveAnchorActivity.ZanInfo rootInfo) {
                        mHanlder.sendEmptyMessage(0);
                    }
                });
        LiveDataBus.get().with(DemoConstants.GETNUM, LiveAnchorActivity.ZanInfo.class)
                .observe(this, new Observer<LiveAnchorActivity.ZanInfo>() {
                    @Override
                    public void onChanged(@Nullable LiveAnchorActivity.ZanInfo rootInfo) {
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

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(StreamViewModel.class);
        viewModel.getPlayUrl(liveRoom.getChatId());
        videoview.setVideoPath(pullUrl);
        LiveDataBus.get().with(DemoConstants.EVENT_ANCHOR_FINISH_LIVE, Boolean.class).observe(LiveAudienceActivity.this, event -> {
            stopVideo();
        });
        LiveDataBus.get().with(DemoConstants.EVENT_ANCHOR_JOIN, Boolean.class).observe(LiveAudienceActivity.this, event -> {
            videoview.attachView();
            videoview.setOnVideoListener(this);
            videoview.setAvOptions();
            videoview.setLoadingView(llStreamLoading);
            viewModel.getPlayUrl(liveRoom.getChatId());
        });
    }

    private void getLiveRoomDetail() {
        this.liveRoom = liveRoom;
//                    if(DemoHelper.isLiving(liveRoom.getStatus())) {
        //直播正在进行
//        if (liveListener != null) {
//            liveListener.onLiveOngoing();
//        }
        messageView.getInputView().requestFocus();
        messageView.getInputView().requestFocusFromTouch();
        joinChatRoom();
    }

    private void joinChatRoom() {
        loadingLayout.setVisibility(View.INVISIBLE);
        EMClient.getInstance()
                .chatroomManager()
                .joinChatRoom(liveRoom.getChatId(), new EMValueCallBack<EMChatRoom>() {
                    @Override
                    public void onSuccess(EMChatRoom emChatRoom) {
                        chatroom = emChatRoom;
//                        addChatRoomChangeListener();
                        onMessageListInit();
                        getMembers(liveRoom.getChatId());
                        //postUserChangeEvent(StatisticsType.JOIN, EMClient.getInstance().getCurrentUser());
                    }

                    @Override
                    public void onError(int i, String s) {
                        if (i == EMError.GROUP_PERMISSION_DENIED || i == EMError.CHATROOM_PERMISSION_DENIED) {
                            Utils.showToast(LiveAudienceActivity.this, "你没有权限加入此房间");
                            finish();
                        } else if (i == EMError.CHATROOM_MEMBERS_FULL) {
                            Utils.showToast(LiveAudienceActivity.this, "房间成员已满");
                            finish();
                        } else {
                            Utils.showToast(LiveAudienceActivity.this, "加入聊天室失败");
                        }
                    }
                });
    }

    protected void onMessageListInit() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageView.init(liveRoom.getChatId());
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

    private void stopVideo() {
        Log.e("TAG", "stopVideo");
        isPrepared = false;
        videoview.stopPlayback();
        videoview.setVisibility(View.GONE);
        llStreamLoading.setVisibility(View.GONE);
    }

    Handler mlist = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            getLiveRoomDetail();
        }
    };

    public void LoginLiveUse() {
        EMClient.getInstance().login(useName, passWord, new EMCallBack() {
            @Override
            public void onSuccess() {
                PreferenceManager.init(mContext, EMClient.getInstance().getCurrentUser());
                mlist.sendEmptyMessage(0);
                Log.e("TAG", "publishUrl->登陆成功");
            }

            @Override
            public void onError(int i, final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LiveAudienceActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onProgress(int i, String s) {
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopVideo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPrepared) {
            videoview.start();
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
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (!TextUtils.isEmpty(liveRoom.getChatId())) {
            EMClient.getInstance().chatroomManager().leaveChatRoom(liveRoom.getChatId());
            EMClient.getInstance().chatroomManager().removeChatRoomChangeListener(presenter);
            EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        }
    }

    /**
     * get异步请求--获取直播信息
     */
    public void getRootInfo(AllQuestBean questBeanb) {
        String Url = GetRootInfo + uuid + "/1";
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Url)
                .addHeader("Cookie", cookie)
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
                    LiveAnchorActivity.GetRootInfo list = new Gson().fromJson(success, new TypeToken<LiveAnchorActivity.GetRootInfo>() {
                    }.getType());
                    LiveDataBus.get().with(DemoConstants.GETROOTINFO).postValue(list);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
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

    @Override
    public void onPrepared(int i) {
        videoview.setVisibility(View.VISIBLE);
        isPrepared = true;
        videoview.start();
    }

    @Override
    public void onCompletion() {
        Log.e("==", "");
    }

    @Override
    public boolean onError(int i) {
        isClose(this);
        return false;
    }

    @Override
    public void onVideoSizeChanged(int i, int i1) {
        Log.e("==", "" + i);
    }

    @Override
    public void onStopVideo() {
        runOnUiThread(this::stopVideo);
    }


    /**
     * 获取播放流地址
     *
     * @param questBeanb
     */

    public static void getPullUrl(AllQuestBean questBeanb) {
        String Url = PullUrl + questBeanb.getUuid() + "/" + questBeanb.getCreateBy() + "/" + questBeanb.getOnlineFlag();
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
                    PullInfo list = new Gson().fromJson(success, new TypeToken<PullInfo>() {
                    }.getType());
                    LiveDataBus.get().with(DemoConstants.GETPUllURL).postValue(list);
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

    @Override
    public void onItemClick(int type, int money) {
        //支付 type=0,余额支付，1微信支付 2支付宝支付
        switch (type) {
            case 0:
                if (!TextUtils.isEmpty(userBalace) && !userBalace.equals("0")) {
                    //送花
                    postZan(liveRoom, 2, money);
                } else {
                    Utils.showToast(this, "余额不足请充值");
                }
                break;
            default:
                break;
        }
    }

    public class PullInfo {


        /**
         * success : true
         * code : 0
         * msg : null
         * detail : {"pushUrl":null,"streamName":null,"pullUrl":{"rtmpUrl":"rtmp://pili-live-rtmp.yksbj.com/piliyikeshi/course_4m7AhS_1_14805de4618948598be19b170e7d0626"}}
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
             * pushUrl : null
             * streamName : null
             * pullUrl : {"rtmpUrl":"rtmp://pili-live-rtmp.yksbj.com/piliyikeshi/course_4m7AhS_1_14805de4618948598be19b170e7d0626"}
             */

            private Object pushUrl;
            private Object streamName;
            private PullUrlBean pullUrl;

            public Object getPushUrl() {
                return pushUrl;
            }

            public void setPushUrl(Object pushUrl) {
                this.pushUrl = pushUrl;
            }

            public Object getStreamName() {
                return streamName;
            }

            public void setStreamName(Object streamName) {
                this.streamName = streamName;
            }

            public PullUrlBean getPullUrl() {
                return pullUrl;
            }

            public void setPullUrl(PullUrlBean pullUrl) {
                this.pullUrl = pullUrl;
            }

            public class PullUrlBean {
                /**
                 * rtmpUrl : rtmp://pili-live-rtmp.yksbj.com/piliyikeshi/course_4m7AhS_1_14805de4618948598be19b170e7d0626
                 */

                private String rtmpUrl;

                public String getRtmpUrl() {
                    return rtmpUrl;
                }

                public void setRtmpUrl(String rtmpUrl) {
                    this.rtmpUrl = rtmpUrl;
                }
            }
        }
    }

    /**
     * 获取点赞总数
     *
     * @param questBeanb
     */

    public void getZanCount(AllQuestBean questBeanb) {
        String Url = ThumBupCount + questBeanb.getUuid() + "/1";
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
                    LiveAnchorActivity.ZanInfo list = new Gson().fromJson(success, new TypeToken<LiveAnchorActivity.ZanInfo>() {
                    }.getType());
                    LiveDataBus.get().with(DemoConstants.GETNUM).postValue(list);
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
                    Log.e("==", success);
                    UserBalaceInfo list = new Gson().fromJson(success, new TypeToken<UserBalaceInfo>() {
                    }.getType());
                    LiveDataBus.get().with(DemoConstants.GET_USER_BALANCE).postValue(list);
                    LiveDataBus.get().with(DemoConstants.GET_USERNum_BALANCE).postValue(list);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * post请求
     */
    public void isClose(LiveAudienceActivity that) {
        String Url = BaseUrl + "/wx/Live/liveStatus/" + uuid + "/1";
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
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) {
                        try {
                            //1 点赞 2是送花
                            String success = response.body().string();
                            Log.e("==", success);
                            // 发请求判断
                            LiveAnchorActivity.ZanInfo zanObj = new Gson().fromJson(success, new TypeToken<LiveAnchorActivity.ZanInfo>() {
                            }.getType());
                            if (zanObj != null && zanObj.isSuccess() && zanObj.getDetail() == 3) {
//                            cb.invoke("finish");
                                Utils.showToast(that, "直播已结束");
                                finish();
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
                                LiveAnchorActivity.ZanInfo list = new Gson().fromJson(success, new TypeToken<LiveAnchorActivity.ZanInfo>() {
                                }.getType());
                                LiveDataBus.get().with(DemoConstants.POSTZAN).postValue(list);
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
     * 发送文本消息
     */
    public void showInputEdit() {
        if (TextUtils.isEmpty(editContent.getText())) {
            Toast.makeText(this, "文字内容不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        EMMessage message = EMMessage.createTxtSendMessage(editContent.getText().toString(), liveRoom.getChatId());
        message.setChatType(EMMessage.ChatType.ChatRoom);
        message.setAttribute("nikename", userNick);
        EMClient.getInstance().chatManager().sendMessage(message);
        messageView.refreshSelectLast();
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

    /**
     * 每一秒刷新数据
     */
    private void PullData() {
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                //获取花总数
                getRootInfo(liveRoom);
                getUserBalance(liveRoom);
                getZanCount(liveRoom);
            }
        }, 1, 1000);
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
