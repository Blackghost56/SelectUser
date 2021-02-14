package com.selectuser;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableLong;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.selectuser.databinding.UserItemBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


//todo make table header

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private final String TAG = "Adapter";




    public class EmployeeModel {

        public ObservableLong id = new ObservableLong();                    //todo move to EmployeeModel
        public ObservableField<String> name = new ObservableField<>();
        public ObservableField<String> surname = new ObservableField<>();
        public ObservableField<String> organization = new ObservableField<>();
        public ObservableField<String> positionO = new ObservableField<>();
        public ObservableBoolean isSelected = new ObservableBoolean(false);

        private Employee employee;
//        private boolean isSelected = false;

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

    List<EmployeeModel> mItemsList = new ArrayList<>();
    List<EmployeeModel> mFilteredItemsList = new ArrayList<>();
    Context mContext;


    public int UNCHECKED = -1;
    // if checkedPosition = -1, there is no default selection
    // if checkedPosition = 0, 1st item is selected by default
    private int mCheckedPosition = UNCHECKED;

    public Adapter(Context context, LiveData<List<Employee>> itemsList){
        mContext = context;
        itemsList.observe((LifecycleOwner) context, employeeList -> {
            if (employeeList != null) {
//                mItemsList = employeeList;
//                mFilteredItemsList.clear();
//                mFilteredItemsList.addAll(itemsList.getValue());

                mItemsList.clear();
                for (Employee employee : employeeList) {
                    mItemsList.add(new EmployeeModel(employee));
                }
                mFilteredItemsList.clear();
                mFilteredItemsList.addAll(mItemsList);
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
        if (mCheckedPosition != UNCHECKED) {
            return mFilteredItemsList.get(mCheckedPosition).getEmployee();
        }
        return null;
    }

    public void removeSelection(){
        mCheckedPosition = UNCHECKED;
        notifyDataSetChanged();
//        callback(UNCHECKED);
    }

    public void setSelectedPosition(int position) {
        if (position != UNCHECKED){
            for (int i = 0; i < mFilteredItemsList.size(); i++) {
                mFilteredItemsList.get(i).setSelected(i == position);
            }
        } else {
            for (EmployeeModel employeeModel : mFilteredItemsList) {
                employeeModel.setSelected(false);
            }
        }

        notifyDataSetChanged();
    }

    public void clearSelection() {
        for (EmployeeModel employeeModel : mFilteredItemsList) {
            employeeModel.setSelected(false);
        }
        notifyDataSetChanged();
    }

    public void filter(String text) {
        mFilteredItemsList.clear();
        if(text.isEmpty()){
            mFilteredItemsList.addAll(mItemsList);
        } else{
            text = text.toLowerCase();
            Log.d(TAG, "filter, text: " + text);
            for(EmployeeModel item: mItemsList){
                if(item.getEmployee().name.toLowerCase().contains(text) || item.getEmployee().surname.toLowerCase().contains(text)){
                    mFilteredItemsList.add(item);
                    Log.d(TAG, "filter, contains");
                }
            }
        }

        notifyDataSetChanged();
    }


    public interface ICallback {
        public void checkedCallback(int position);
    }

    private ICallback mCallback;
    public void registerCallback(ICallback callback){
        mCallback = callback;
    }
    protected void checkedCallback(int position){
        if (mCallback != null)
            mCallback.checkedCallback(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public UserItemBinding mBinding;



        public ViewHolder(UserItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(EmployeeModel employeeModel){
//            mBinding.setModel(this);
            mBinding.setModel(employeeModel);
            mBinding.executePendingBindings();


//            id.set(employeeModel.getEmployee().id);
//            name.set(employeeModel.getEmployee().name);
//            surname.set(employeeModel.getEmployee().surname);
//            organization.set(employeeModel.getEmployee().organizationName);
//            positionO.set(employeeModel.getEmployee().position);

//            itemView.setSelected(mCheckedPosition == getAdapterPosition());

            itemView.setOnClickListener(v -> {
                if (mCheckedPosition != getAdapterPosition()) {
                    mCheckedPosition = getAdapterPosition();
                } else {
                    mCheckedPosition = UNCHECKED;
                }
                setSelectedPosition(mCheckedPosition);          // todo оптимизировать, убрать перебор по всей коллекции
//                notifyDataSetChanged();


                checkedCallback(mCheckedPosition);
            });
        }
    }

}

