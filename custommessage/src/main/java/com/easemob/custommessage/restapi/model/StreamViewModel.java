package com.easemob.custommessage.restapi.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.easemob.custommessage.bean.LiveRoom;
import com.easemob.custommessage.bean.LiveRoomUrlBean;
import com.easemob.custommessage.reponsitories.AppServerRepository;
import com.easemob.custommessage.reponsitories.Resource;


public class StreamViewModel extends AndroidViewModel {
    private AppServerRepository repository;
    private MediatorLiveData<Resource<LiveRoomUrlBean>> publishUrlObservable;
    private MediatorLiveData<Resource<LiveRoomUrlBean>> playUrlObservable;
    private MediatorLiveData<Resource<LiveRoom>> roomDetailObservable;

    public StreamViewModel(@NonNull Application application) {
        super(application);
        repository = new AppServerRepository();
        publishUrlObservable = new MediatorLiveData<>();
        playUrlObservable = new MediatorLiveData<>();
        roomDetailObservable = new MediatorLiveData<>();
    }

    public LiveData<Resource<LiveRoomUrlBean>> getPublishUrlObservable() {
        return publishUrlObservable;
    }

    public LiveData<Resource<LiveRoomUrlBean>> getPlayUrlObservable() {
        return playUrlObservable;
    }

    public void getPublishUrl(String roomId) {
        publishUrlObservable.addSource(repository.getPublishUrl(roomId), response -> publishUrlObservable.postValue(response));
    }

    public void getPlayUrl(String roomId) {
        playUrlObservable.addSource(repository.getPlayUrl(roomId), response -> playUrlObservable.postValue(response));
    }
    public MediatorLiveData<Resource<LiveRoom>> getRoomDetailObservable() {
        return roomDetailObservable;
    }

    public void getLiveRoomDetails(String roomId) {
        roomDetailObservable.addSource(repository.getLiveRoomDetails(roomId), response -> roomDetailObservable.postValue(response));
    }
}
