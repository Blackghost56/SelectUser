package com.selectuser;


import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AdapterUser extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final String TAG = "AdapterUser";

    protected List<EmployeeModel> mItemsList = new ArrayList<>();
    protected Map<Long, EmployeeModel> mItemsMap = new HashMap<>();
    protected List<EmployeeModel> mFilteredItemsList = new ArrayList<>();
    protected Context mContext;

    public static int UNCHECKED = -1;
    protected long mCheckedId = UNCHECKED;

    AdapterUser(Context context, LiveData<List<Employee>> itemsList, long defaultId){
        mContext = context;

        itemsList.observe((LifecycleOwner) context, employeeList -> {
            if (employeeList != null) {
                mItemsMap.clear();
                mItemsList.clear();
                for (Employee employee : employeeList) {
                    EmployeeModel employeeModel = new EmployeeModel(employee);
                    mItemsMap.put(employee.id, employeeModel);
                    mItemsList.add(employeeModel);
                }
                mFilteredItemsList.clear();
                mFilteredItemsList.addAll(mItemsList);

                setSelected(defaultId);
            }
            notifyDataSetChanged();
        });
    }


    @Override
    public int getItemCount() {
        return mFilteredItemsList.size();
    }

    public Employee getSelected() {
        if (mCheckedId != UNCHECKED) {
            EmployeeModel employeeModel = mItemsMap.get(mCheckedId);
            if (employeeModel != null)
                return employeeModel.getEmployee();
        }
        return null;
    }

    public long getSelectedId() {
        return mCheckedId;
    }

    public void removeSelection(){
        setSelected(UNCHECKED);
    }

    public void setSelected(long id) {
        if (mCheckedId != UNCHECKED) {
            EmployeeModel employeeModelOld = mItemsMap.get(mCheckedId);
            if (employeeModelOld != null)
                employeeModelOld.setSelected(false);
        }

        Employee employee = null;
        if (id != UNCHECKED){
            EmployeeModel employeeModel = mItemsMap.get(id);
            if (employeeModel != null) {
                employeeModel.setSelected(true);
                employee = employeeModel.getEmployee();
            }
        }

        mCheckedId = id;
        notifyDataSetChanged();
        checkedCallback(employee);
    }


    public void filter(String text) {
        mFilteredItemsList.clear();
        if(text.isEmpty()){
            mFilteredItemsList.addAll(mItemsList);
        } else{
            text = text.toLowerCase();
            for(EmployeeModel item: mItemsList){
                if(item.getEmployee().name.toLowerCase().contains(text) || item.getEmployee().surname.toLowerCase().contains(text)){
                    mFilteredItemsList.add(item);
                }
            }
        }

        notifyDataSetChanged();
    }

    public interface ICallback {
        void checkedCallback(Employee employee);
    }

    private ICallback mCallback;
    public void registerCallback(ICallback callback){
        mCallback = callback;
    }
    protected void checkedCallback(Employee employee){
        if (mCallback != null)
            mCallback.checkedCallback(employee);
    }

}
