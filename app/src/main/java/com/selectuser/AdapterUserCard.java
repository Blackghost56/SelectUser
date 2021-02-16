package com.selectuser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.selectuser.databinding.UserItemBinding;

import java.util.List;


public class AdapterUserCard extends AdapterUser {

    private final String TAG = "AdapterUserCard";

    public AdapterUserCard(Context context, LiveData<List<Employee>> itemsList, long defaultId){
        super(context, itemsList, defaultId);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        UserItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.user_item, parent, false);
        return new ViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).bind(mFilteredItemsList.get(position));
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

