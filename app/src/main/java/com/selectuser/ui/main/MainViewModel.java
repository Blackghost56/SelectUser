package com.selectuser.ui.main;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.selectuser.DB.AppDatabase;
import com.selectuser.DB.EmployeeDao;
import com.selectuser.Employee;
import com.selectuser.R;
import com.selectuser.tools.SingleLiveEvent;
import com.selectuser.tools.Tools;

import java.util.List;


public class MainViewModel extends AndroidViewModel {

    private final String TAG = "MainViewModel";

    private AppDatabase mDatabase;
    private EmployeeDao mEmployeeDao;

    public MainViewModel(@NonNull Application application) {
        super(application);

        mDatabase = Room.databaseBuilder(getApplication().getApplicationContext(), AppDatabase.class, "database").build();      // todo move to singleton (DeviceHolder)
        mEmployeeDao = mDatabase.employeeDao();

        mEmployeeList = mEmployeeDao.getAllLive();
    }

    public enum State {MAIN_IDLE, SELECT, ADD, EDIT};


    public State mState = State.MAIN_IDLE;          // todo дублирует  mStateChange


    private final MutableLiveData<State> mStateChange = new MutableLiveData<>();
    public LiveData<State> getStateChange (){
        return mStateChange;
    }


    private final LiveData<List<Employee>> mEmployeeList;
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


    private final SingleLiveEvent<Void> mBackPressed = new SingleLiveEvent<>();
    public LiveData<Void> getBackPressed(){
        return mBackPressed;
    }

//    private final SingleLiveEvent<Void> mRemoveSelection = new SingleLiveEvent<>();
//    public LiveData<Void> getRemoveSelection(){
//        return mRemoveSelection;
//    }

    public ObservableBoolean mSelectEnabled = new ObservableBoolean(false);

    private final SingleLiveEvent<Void> mRequestPin = new SingleLiveEvent<>();
    public LiveData<Void> getRequestPin(){
        return mRequestPin;
    }

    private final SingleLiveEvent<String> mShowSnackBar = new SingleLiveEvent<>();
    public LiveData<String> getShowSnackBar(){
        return mShowSnackBar;
    }


    private Employee mSelectedEmployee;
    public Employee getSelectedEmployee(){
        return mSelectedEmployee;
    }

    public void itemSelect(Employee employee){
        mSelectedEmployee = employee;
        if (employee != null){
            setState(State.SELECT);
        } else {
            setState(State.MAIN_IDLE);
        }
    }

    private void setState(State state){
        mState = state;
        switch (state){
            case MAIN_IDLE:
                mSelectEnabled.set(false);
//                mRemoveSelection.call();
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
        mRequestPin.call();
    }

    public void onPinEntered(String pinStr){
        Log.d(TAG, "onPinEntered, pin: " + pinStr);
        try {
            int pin = Integer.parseInt(pinStr.trim());
            if (mSelectedEmployee != null) {
                if (mSelectedEmployee.pin == pin){
                    Log.d(TAG, "Selected employee: " + mSelectedEmployee.name);
                    // todo send to device employee data
                    mBackPressed.call();
                } else {
                    mShowSnackBar.setValue(getApplication().getResources().getString(R.string.msg_list_manager_invalid_pin));
                }
            }
        } catch (NumberFormatException e){
            e.printStackTrace();
            mShowSnackBar.setValue(getApplication().getResources().getString(R.string.msg_list_manager_invalid_pin));
        }
    }

    public void onAddPressed(){
        Log.d(TAG, "onAddPressed");

        setState(State.ADD);
        mOpenFragment.setValue(AddFragment.newInstance());
    }

    public void onEditPressed(){
        Log.d(TAG, "onEditPressed");
        setState(State.EDIT);
        mOpenFragment.setValue(EditFragment.newInstance());
    }

    public void onDeletePressed(){
        Log.d(TAG, "onDeletePressed");
        if (mSelectedEmployee != null){
            setState(State.MAIN_IDLE);

            new Thread(() -> {
                mEmployeeDao.delete(mSelectedEmployee);
            }).start();
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

    @SuppressLint("CheckResult")
    public void itemAdded(Employee employee){
        if (employee != null){
            new Thread(() -> {
                mEmployeeDao.insert(employee);
            }).start();

            setState(State.MAIN_IDLE);
        }
    }

    public void itemEdited(Employee employee){
        if (employee != null){
            if(mSelectedEmployee.id != employee.id){
                new Thread(() -> {
                    mEmployeeDao.delete(mSelectedEmployee.id);
                    mEmployeeDao.insert(employee);
                }).start();
            } else {
                new Thread(() -> {
                    mEmployeeDao.update(employee);
                }).start();
            }

            setState(State.MAIN_IDLE);
        }
    }


    public boolean calculateId(Employee employee){
        try {
            employee.id = calculateEmployeeId(employee.name, employee.surname);
            employee.organizationId = calculateOrganizationId(employee.organizationName);
        } catch (Exception e) {
            e.printStackTrace();
            return false;                 // todo сообщение об ошибке
        }
        return true;
    }


    // returned unsigned int
    public long calculateEmployeeId(String name, String surname) throws Exception {
        if (name != null && surname != null){
            return Tools.getUnsignedInt((name.toLowerCase().trim() + surname.toLowerCase().trim()).hashCode());
        } else {
            throw new Exception();
        }
    }

    // returned unsigned int
    public long calculateOrganizationId(String organizationName) throws Exception {
        return Tools.getUnsignedInt(organizationName.toLowerCase().trim().hashCode());
    }

}