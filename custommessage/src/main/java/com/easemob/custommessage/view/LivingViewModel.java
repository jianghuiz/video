package com.easemob.custommessage.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.easemob.custommessage.bean.LiveRoom;
import com.easemob.custommessage.reponsitories.AppServerRepository;
import com.easemob.custommessage.reponsitories.Resource;


public class LivingViewModel extends AndroidViewModel {
    private AppServerRepository repository;
    private MediatorLiveData<Resource<LiveRoom>> changeObservable;
    private MediatorLiveData<Resource<LiveRoom>> roomDetailObservable;
    private MediatorLiveData<Resource<LiveRoom>> closeObservable;

    public LivingViewModel(@NonNull Application application) {
        super(application);
        repository = new AppServerRepository();
        changeObservable = new MediatorLiveData<>();
        roomDetailObservable = new MediatorLiveData<>();
        closeObservable = new MediatorLiveData<>();
    }

    public LiveData<Resource<LiveRoom>> getChangeObservable() {
        return changeObservable;
    }

    public void changeLiveStatus(String roomId, String username, String status) {
        changeObservable.addSource(repository.changeLiveStatus(roomId, username, status), response -> changeObservable.postValue(response));
    }

    public MediatorLiveData<Resource<LiveRoom>> getRoomDetailObservable() {
        return roomDetailObservable;
    }

    public void getLiveRoomDetails(String roomId) {
        roomDetailObservable.addSource(repository.getLiveRoomDetails(roomId), response -> roomDetailObservable.postValue(response));
    }

    public MediatorLiveData<Resource<LiveRoom>> getCloseObservable() {
        return closeObservable;
    }

    public void closeLive(String roomId, String username) {
        closeObservable.addSource(repository.changeLiveStatus(roomId, username, "offline"), response -> closeObservable.postValue(response));
    }
}
