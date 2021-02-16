package com.selectuser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableLong;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.selectuser.databinding.UserItemBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//todo make table header

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private final String TAG = "Adapter";

    List<EmployeeModel> mItemsList = new ArrayList<>();
    Map<Long, EmployeeModel> mItemsMap = new HashMap<>();
    List<EmployeeModel> mFilteredItemsList = new ArrayList<>();
    Context mContext;


    public static int UNCHECKED = -1;
    private long mCheckedId = UNCHECKED;

    public Adapter(Context context, LiveData<List<Employee>> itemsList, long defaultId){
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


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        UserItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.user_item, parent, false);
        return new ViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mFilteredItemsList.get(position));
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


    public class ViewHolder extends RecyclerView.ViewHolder {

        public UserItemBinding mBinding;



        public ViewHolder(UserItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(EmployeeModel employeeModel){
            mBinding.setModel(employeeModel);
            mBinding.executePendingBindings();

            itemView.setOnClickListener(v -> {
                if (mCheckedId != employeeModel.id.get()) {
                    setSelected(employeeModel.id.get());
                } else {
                    setSelected(UNCHECKED);
                }
            });
        }
    }

}

