package com.easemob.custommessage.reponsitories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.easemob.custommessage.bean.LiveRoom;
import com.easemob.custommessage.bean.LiveRoomUrlBean;
import com.easemob.custommessage.restapi.ApiService;
import com.easemob.custommessage.restapi.LiveManager;
import com.easemob.custommessage.restapi.model.ResponseModule;
import java.util.List;

import okhttp3.RequestBody;

/**
 * 用于从app server获取数据LoggerInterceptor
 */
public class AppServerRepository {
    private ApiService apiService;

    public AppServerRepository() {
        apiService = LiveManager.getInstance().getApiService();
    }

    public LiveData<Resource<LiveRoom>> createLiveRoom(final LiveRoom module) {
        return new NetworkOnlyResource<LiveRoom, LiveRoom>() {

            @Override
            protected void createCall(@NonNull ResultCallBack<LiveData<LiveRoom>> callBack) {
                callBack.onSuccess(apiService.createLiveRoom(module));
            }

        }.asLiveData();
    }

    public LiveData<Resource<ResponseModule<List<LiveRoom>>>> getLiveRoomList(final int limit, final String cursor) {
        return new NetworkOnlyResource<ResponseModule<List<LiveRoom>>, ResponseModule<List<LiveRoom>>>() {
            @Override
            protected void createCall(@NonNull ResultCallBack<LiveData<ResponseModule<List<LiveRoom>>>> callBack) {
                callBack.onSuccess(apiService.getLiveRoomList(limit, cursor));
            }
        }.asLiveData();
    }

    public LiveData<Resource<ResponseModule<List<LiveRoom>>>> getLivingRoomLists(final int limit, final String cursor) {
        return new NetworkOnlyResource<ResponseModule<List<LiveRoom>>, ResponseModule<List<LiveRoom>>>() {
            @Override
            protected void createCall(@NonNull ResultCallBack<LiveData<ResponseModule<List<LiveRoom>>>> callBack) {
                callBack.onSuccess(apiService.getLivingRoomList(limit, cursor));
            }
        }.asLiveData();
    }

    public LiveData<Resource<LiveRoom>> changeLiveStatus(final String roomId, final String username, final String status) {
        return new NetworkOnlyResource<LiveRoom, LiveRoom>() {
            @Override
            protected void createCall(@NonNull ResultCallBack<LiveData<LiveRoom>> callBack) {
                callBack.onSuccess(apiService.changeLiveStatus(roomId, username, status));
            }
        }.asLiveData();
    }

    public LiveData<Resource<LiveRoom>> getLiveRoomDetails(final String roomId) {
        return new NetworkOnlyResource<LiveRoom, LiveRoom>() {
            @Override
            protected void createCall(@NonNull ResultCallBack<LiveData<LiveRoom>> callBack) {
                callBack.onSuccess(apiService.getLiveRoomDetail(roomId));
            }
        }.asLiveData();
    }

    public LiveData<Resource<LiveRoom>> updateLiveRoom(final String roomId, final RequestBody body) {
        return new NetworkOnlyResource<LiveRoom, LiveRoom>() {
            @Override
            protected void createCall(@NonNull ResultCallBack<LiveData<LiveRoom>> callBack) {
                callBack.onSuccess(apiService.updateLiveRoom(roomId, body));
            }
        }.asLiveData();
    }

    public LiveData<Resource<LiveRoomUrlBean>> getPublishUrl(final String roomId) {
        return new NetworkOnlyResource<LiveRoomUrlBean, LiveRoomUrlBean>() {
            @Override
            protected void createCall(@NonNull ResultCallBack<LiveData<LiveRoomUrlBean>> callBack) {
                callBack.onSuccess(apiService.getLiveRoomPublishUrl(roomId));
            }
        }.asLiveData();
    }

    public LiveData<Resource<LiveRoomUrlBean>> getPlayUrl(final String roomId) {
        return new NetworkOnlyResource<LiveRoomUrlBean, LiveRoomUrlBean>() {
            @Override
            protected void createCall(@NonNull ResultCallBack<LiveData<LiveRoomUrlBean>> callBack) {
                callBack.onSuccess(apiService.getLiveRoomPublishUrl(roomId));
            }
        }.asLiveData();
    }
}
