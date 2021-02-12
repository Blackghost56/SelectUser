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
import com.selectuser.tools.SingleLiveEvent;
import com.selectuser.tools.Tools;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;


public class MainViewModel extends AndroidViewModel {

    private final String TAG = "MainViewModel";

    private AppDatabase mDatabase;
    private EmployeeDao mEmployeeDao;

    public MainViewModel(@NonNull Application application) {
        super(application);

        mDatabase = Room.databaseBuilder(getApplication().getApplicationContext(), AppDatabase.class, "database").build();      // todo move to singleton (DeviceHolder)
        mEmployeeDao = mDatabase.employeeDao();

//        mEmployeeDao.getAllRx()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<List<Employee>>() {
//                    @Override
//                    public void accept(List<Employee> employees) throws Exception {
//                        // ...
//                    }
//                });

//        mEmployeeDao.getByIdRx(1)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new DisposableSingleObserver<Employee>() {
//                    @Override
//                    public void onSuccess(Employee employee) {
//                        Log.d(TAG, "onSuccess");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.d(TAG, "onError");
//                    }
//                });


        mEmployeeDao.getAllRxSingle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<List<Employee>>() {
                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull List<Employee> employees) {
                        Log.d(TAG, "onSuccess, employess size: " + employees.size());
                        mEmployeeList.setValue(employees);
                        mEmployeeList.notifyObserver();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError");
                    }
                });


//        mEmployeeList.setValue(new ArrayList<Employee>());
//        // todo dbg
//        Employee employee = new Employee();
//        employee.id = 0;
//        employee.name = "Michael";
//        employee.surname = "Tsvetkov";
//        employee.organizationName = "NPP CRTS";
//        employee.position = "Developer";
//        employee.access = 0;
//        mEmployeeList.getValue().add(employee);
//
//        employee = new Employee();
//        employee.id = 11;
//        employee.name = "Петров";
//        employee.surname = "Фёдр";
//        employee.organizationName = "АО ЦРТС";
//        employee.position = "Монтажник";
//        employee.access = 1;
//        mEmployeeList.getValue().add(employee);
//        Completable.fromAction(() -> mEmployeeDao.insert(mEmployeeList.getValue())).subscribeOn(Schedulers.io()).subscribe();
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
            setState(State.MAIN_IDLE);

            Completable.fromAction(() -> mEmployeeDao.delete(mSelectedEmployee)).subscribeOn(Schedulers.io()).subscribe();
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
            Log.d(TAG, "Id: " + employee.id);
            Log.d(TAG, "organizationId: " + employee.organizationId);

            mEmployeeList.getValue().add(employee);
            mEmployeeList.notifyObserver();
            //mPopBackStack.call();
            setState(State.MAIN_IDLE);

            Completable.fromAction(() -> mEmployeeDao.insert(employee)).subscribeOn(Schedulers.io()).subscribe();
        }
    }

    public void itemEdited(Employee employee){
        if (employee != null){
            Log.d(TAG, "Id: " + employee.id);
            Log.d(TAG, "organizationId: " + employee.organizationId);


            if(mSelectedEmployee.id != employee.id){
                Completable.fromAction(() -> mEmployeeDao.delete(mSelectedEmployee.id)).subscribeOn(Schedulers.io()).subscribe();
                Completable.fromAction(() -> mEmployeeDao.insert(employee)).subscribeOn(Schedulers.io()).subscribe();
            } else {
                Completable.fromAction(() -> mEmployeeDao.update(employee)).subscribeOn(Schedulers.io()).subscribe();
            }

            mEmployeeList.getValue().remove(mSelectedEmployee);
            mSelectedEmployee = null;
            mEmployeeList.getValue().add(employee);
            mEmployeeList.notifyObserver();
            //mPopBackStack.call();
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
            return Tools.getUnsignedInt((name.toLowerCase() + surname.toLowerCase()).hashCode());
        } else {
            throw new Exception();
        }
    }

    // returned unsigned int
    public long calculateOrganizationId(String organizationName) throws Exception {
        return Tools.getUnsignedInt(organizationName.toLowerCase().hashCode());
    }

}