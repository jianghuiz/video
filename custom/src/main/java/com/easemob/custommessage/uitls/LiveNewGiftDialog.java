package com.easemob.custommessage.uitls;


import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.easemob.custommessage.R;
import com.easemob.custommessage.bean.GiftBean;
import com.hyphenate.chat.EMClient;
import java.util.List;


public class LiveNewGiftDialog extends BaseLiveDialogFragment implements OnItemClickListener {
    private OnConfirmClickListener listener;
    private RecyclerView rvList;
    private TextView emSendNum;
    private TextView tvGiftName;
    private GiftListRecyclerAdapter adapter;
    private List<GiftBean> mLists;
    private String emNum = "1";
    private Button sendBtnGift;
    private EditText emEditText;
    private String emNumSave=emNum;
    private int emPostSave = 0;
    private String userBalace;
    public Activity mContext;

    public LiveNewGiftDialog(Activity mContext) {
        super();
        this.mContext=mContext;
    }



    @Override
    public int getLayoutId() {
        return R.layout.em_fragment_dialog_live_gift_change;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        rvList = findViewById(R.id.em_live_recyclerview);
        emSendNum = findViewById(R.id.em_live_num);
        tvGiftName = findViewById(R.id.tv_gift_title);
        sendBtnGift = findViewById(R.id.em_gift_confirm_send);
        emEditText = findViewById(R.id.em_dialog_edit_content);
        PagingScrollHelper snapHelper = new PagingScrollHelper();
        HorizontalPageLayoutManager manager = new HorizontalPageLayoutManager(2, 5);
        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(manager);
        adapter = new GiftListRecyclerAdapter();
        rvList.setAdapter(adapter);

        snapHelper.setUpRecycleView(rvList);
        snapHelper.updateLayoutManger();
        snapHelper.scrollToPosition(0);
        rvList.setHorizontalScrollBarEnabled(true);
        mLists = TestGiftRepository.getDefaultListGifts();
        adapter.setData(mLists);
        tvGiftName.setText("送花给" + EMClient.getInstance().getCurrentUser());
        AddTextWach();
        sendBtnGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(!TextUtils.isEmpty(userBalace)&&!userBalace.equals("0")){
                    GiftBean bean = new GiftBean();
                    bean.setId(userBalace);
                    bean.setNum(Integer.parseInt(emNum));
                    if (listener != null) {
                        listener.onConfirmClick(v, bean);
                    }
                    dismiss();
//                }else{
//                    Utils.showToast(getActivity(),"余额不足请充值");
//                }
            }
        });
        LiveDataBus.get().with(DemoConstants.GET_USER_BALANCE, UserBalaceInfo.class)
                .observe(this, new Observer<UserBalaceInfo>() {
                    @Override
                    public void onChanged(@Nullable UserBalaceInfo rootInfo) {
                     if(rootInfo.getCode()==0&&rootInfo.getDetail()!=null){
                         userBalace=String.valueOf(rootInfo.getDetail());
                         emSendNum.setText(String.valueOf(rootInfo.getDetail()));
                     }else{
                         userBalace="0";
                         emSendNum.setText(userBalace);
                     }
                    }
                });
    }


    /**
     * 监听内容变化
     */
    private void AddTextWach() {
        emEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    emNum = s.toString().trim();
                    adapter.setSelectedPosition(-1);
                } else {
                    emNum = emNumSave;
                    adapter.setSelectedPosition(emPostSave);
                }
//                emSendNum.setText(emNum);
            }
        });
    }

    public void initListener() {
        super.initListener();
        adapter.setOnItemClickListener((OnItemClickListener) this);
    }

    public void setOnConfirmClickListener(OnConfirmClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(View view, int position) {
        emPostSave = position;
        adapter.setSelectedPosition(position);
        int index = mLists.get(position).getName().indexOf("束");
        emNum = mLists.get(position).getName().substring(0, index);
        emNumSave = emNum;
//        emSendNum.setText(emNum);
    }

}
