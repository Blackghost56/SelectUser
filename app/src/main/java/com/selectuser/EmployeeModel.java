package com.selectuser;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableLong;

public class EmployeeModel {

    public ObservableLong id = new ObservableLong();
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> surname = new ObservableField<>();
    public ObservableField<String> organization = new ObservableField<>();
    public ObservableField<String> positionO = new ObservableField<>();
    public ObservableBoolean isSelected = new ObservableBoolean(false);

    private final Employee employee;

    public EmployeeModel(Employee employee){
        this.employee = employee;
        id.set(employee.id);
        name.set(employee.name);
        surname.set(employee.surname);
        organization.set(employee.organizationName);
        positionO.set(employee.position);
    }

    public Employee getEmployee() {
        return employee;
    }

    public boolean isSelected() {
        return isSelected.get();
    }

    public void setSelected(boolean selected) {
        isSelected.set(selected);
    }
}
