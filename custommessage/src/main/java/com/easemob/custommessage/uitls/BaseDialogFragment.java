package com.easemob.custommessage.uitls;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * 作为dialog fragment的基类
 */
public abstract class BaseDialogFragment extends DialogFragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initArgument();
        View view = inflater.inflate(getLayoutId(), container, false);
        setChildView(view);
        setDialogAttrs();
        setAnimation();
        return view;
    }

    /**
     * 设置转场动画
     */
    protected void setAnimation() {

    }

    public void setChildView(View view) {}

    public abstract int getLayoutId();

    private void setDialogAttrs() {
        try {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(savedInstanceState);
        initViewModel();
        initListener();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    public void initArgument() {}

    public void initView(Bundle savedInstanceState) {}

    public void initViewModel() {}

    public void initListener() {}

    public void initData() {}

    /**
     * 通过id获取当前view控件，需要在onViewCreated()之后的生命周期调用
     * @param id
     * @param <T>
     * @return
     */
    protected <T extends View> T findViewById(@IdRes int id) {
        return getView().findViewById(id);
    }


    /**
     * 解析Resource<T>
     * @param response
     * @param callback
     * @param <T>
     */
//    public <T> void parseResource(Resource<T> response, @NonNull OnResourceParseCallback<T> callback) {
//        if(mContext != null) {
//            mContext.parseResource(response, callback);
//        }
//    }
}
