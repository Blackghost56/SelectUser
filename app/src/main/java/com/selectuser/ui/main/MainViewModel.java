package com.selectuser.ui.main;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.selectuser.Employee;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private final String TAG = "MainViewModel";

    public MainViewModel(@NonNull Application application) {
        super(application);

        employeeList.setValue(new ArrayList<Employee>());


        // todo dbg
        Employee employee = new Employee();
        employee.id = 0;
        employee.name = "Michael";
        employee.surname = "Tsvetkov";
        employee.organizationName = "NPP CRTS";
        employee.position = "Developer";
        employee.access = 0;
        employeeList.getValue().add(employee);

        employee = new Employee();
        employee.id = 11;
        employee.name = "Петров";
        employee.surname = "Фёдр";
        employee.organizationName = "АО ЦРТС";
        employee.position = "Монтажник";
        employee.access = 1;
        employeeList.getValue().add(employee);
    }

    public enum State {MAIN_IDLE, SELECT, EDIT};


    public State mState = State.MAIN_IDLE;


    private final MutableLiveData<State> mStateChange = new MutableLiveData<>();
    public LiveData<State> getStateChange (){
        return mStateChange;
    }


    static class MutableLiveDataList<T> extends MutableLiveData<List<T>> {
        public void notifyObserver(){
            setValue(getValue());
        }
    }

    private final MutableLiveDataList<Employee> employeeList = new MutableLiveDataList<>();
    public LiveData<List<Employee>> getEmployeeList(){
        return employeeList;
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