package com.selectuser.ui.main;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.selectuser.Employee;
import com.selectuser.tools.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private final String TAG = "MainViewModel";

    public MainViewModel(@NonNull Application application) {
        super(application);

        mEmployeeList.setValue(new ArrayList<Employee>());


        // todo dbg
        Employee employee = new Employee();
        employee.id = 0;
        employee.name = "Michael";
        employee.surname = "Tsvetkov";
        employee.organizationName = "NPP CRTS";
        employee.position = "Developer";
        employee.access = 0;
        mEmployeeList.getValue().add(employee);

        employee = new Employee();
        employee.id = 11;
        employee.name = "Петров";
        employee.surname = "Фёдр";
        employee.organizationName = "АО ЦРТС";
        employee.position = "Монтажник";
        employee.access = 1;
        mEmployeeList.getValue().add(employee);
    }

    public enum State {MAIN_IDLE, SELECT, ADD, EDIT};


    public State mState = State.MAIN_IDLE;          // todo дублирует  mStateChange


    private final MutableLiveData<State> mStateChange = new MutableLiveData<>();
    public LiveData<State> getStateChange (){
        return mStateChange;
    }


    static class MutableLiveDataList<T> extends MutableLiveData<List<T>> {
        public void notifyObserver(){
            setValue(getValue());
        }
    }

    private final MutableLiveDataList<Employee> mEmployeeList = new MutableLiveDataList<>();
    public LiveData<List<Employee>> getEmployeeList(){
        return mEmployeeList;
    }


    private final SingleLiveEvent<Fragment> mOpenFragment = new SingleLiveEvent<>();
    public LiveData<Fragment> getOpenFragment(){
        return mOpenFragment;
    }

    private final SingleLiveEvent<Void> mPopBackStack = new SingleLiveEvent<>();
    public LiveData<Void> getPopBackStack(){
        return mPopBackStack;
    }

    public ObservableBoolean mSelectEnabled = new ObservableBoolean(false);


    private Employee mSelectedEmployee;
    public Employee getSelectedEmployee(){
        return mSelectedEmployee;
    }

    public void itemSelect(Employee employee){
        Log.d(TAG, "itemSelect");
        if (employee != null){
            mSelectedEmployee = employee;
            setState(State.SELECT);
            Log.d(TAG, "employee name: " + employee.name);
            Log.d(TAG, "list contains employee: " + mEmployeeList.getValue().contains(employee));
        } else {
            setState(State.MAIN_IDLE);
        }

    }

    private void setState(State state){
        mState = state;
        switch (state){
            case MAIN_IDLE:
                mSelectEnabled.set(false);
                break;
            case SELECT:
                mSelectEnabled.set(true);
                break;
            case EDIT:
            case ADD:
                mSelectEnabled.set(false);
                break;
        }
        mStateChange.setValue(mState);
    }


    public void onSelectPressed(){
        Log.d(TAG, "onSelectPressed");
    }

    public void onAddPressed(){
        Log.d(TAG, "onAddPressed");

        setState(State.ADD);
        mOpenFragment.setValue(AddFragment.newInstance());
    }

    public void onEditPressed(){
        Log.d(TAG, "onEditPressed");
        setState(State.EDIT);
        mOpenFragment.setValue(EditFragment.newInstance(null));
    }

    public void onDeletePressed(){
        Log.d(TAG, "onDeletePressed");
        if (mSelectedEmployee != null){
            mEmployeeList.getValue().remove(mSelectedEmployee);
            mEmployeeList.notifyObserver();
        }
    }

    public void onBackPressed(int backStackEntryCount){
        Log.d(TAG, "onBackPressed, backstack count: " + backStackEntryCount);
        if (backStackEntryCount == 0){
            // way out
        } else if (backStackEntryCount == 1) {
            // main fragment
            setState(State.MAIN_IDLE);
        } else {
            // do something
        }
    }

    public void itemAdded(Employee employee){
        if (employee != null){
            mEmployeeList.getValue().add(employee);
            mEmployeeList.notifyObserver();
            mPopBackStack.call();
        }
    }

}