package com.selectuser.ui.main;

import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.selectuser.Employee;

public class MainViewModel extends ViewModel {

    private final String TAG = "MainViewModel";

    public enum State {MAIN_IDLE, SELECT, EDIT};


    public State mState = State.MAIN_IDLE;


    private final MutableLiveData<State> mStateChange = new MutableLiveData<>();
    public LiveData<State> getStateChange (){
        return mStateChange;
    }


    public ObservableBoolean mSelectEnabled = new ObservableBoolean(false);

    public void itemSelect(Employee employee){
        Log.d(TAG, "itemSelect");
        if (employee != null){
            mState = State.SELECT;
            mSelectEnabled.set(true);
            Log.d(TAG, "employee name: " + employee.name);
        } else {
            mState = State.MAIN_IDLE;
            mSelectEnabled.set(false);
        }
        mStateChange.setValue(mState);
    }



    public void onSelectPressed(){
        Log.d(TAG, "onSelectPressed");
    }

    public void onAddPressed(){
        Log.d(TAG, "onAddPressed");
    }

    public void onEditPressed(){
        Log.d(TAG, "onEditPressed");
    }

    public void onDeletePressed(){
        Log.d(TAG, "onDeletePressed");
    }

}