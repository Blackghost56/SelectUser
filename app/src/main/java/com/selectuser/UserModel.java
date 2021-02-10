package com.selectuser;

import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

public class UserModel {

    private final String TAG = "UserModel";

    public ObservableInt id = new ObservableInt();
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> surname = new ObservableField<>();
    public ObservableField<String> organization = new ObservableField<>();
    public ObservableField<String> position = new ObservableField<>();
    public ObservableInt access = new ObservableInt();


    public UserModel(Employee employee){
        updateModel(employee);
    }

    public void updateModel(Employee employee){
        id.set(employee.id);
        name.set(employee.name);
        surname.set(employee.surname);
        organization.set(employee.organizationName);
        position.set(employee.position);
        access.set(employee.access);
    }


    public Employee toEmployee(){
        Employee employee = new Employee();
        employee.id = id.get();
        employee.name = name.get();
        employee.surname = surname.get();
        employee.organizationName = organization.get();
        employee.position = position.get();
        employee.access = access.get();

        return employee;
    }


}
