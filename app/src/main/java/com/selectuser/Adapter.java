package com.selectuser;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableLong;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.selectuser.databinding.UserItemBinding;

import java.util.ArrayList;
import java.util.List;


//todo make table header

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private final String TAG = "Adapter";

    List<Employee> mItemsList;
    List<Employee> mFilteredItemsList = new ArrayList<>();
    Context mContext;


    public int UNCHECKED = -1;
    // if checkedPosition = -1, there is no default selection
    // if checkedPosition = 0, 1st item is selected by default
    private int mCheckedPosition = UNCHECKED;

    public Adapter(Context context, LiveData<List<Employee>> itemsList){
        mContext = context;
        itemsList.observe((LifecycleOwner) context, employeeList -> {
            if (employeeList != null) {
                mItemsList = employeeList;
                mFilteredItemsList.clear();
                mFilteredItemsList.addAll(itemsList.getValue());
            }
            notifyDataSetChanged();
        });
//        mItemsList = itemsList.getValue();
//        if (mItemsList != null) {
//            mFilteredItemsList.addAll(mItemsList);
//        }
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
            return mFilteredItemsList.get(mCheckedPosition);
        }
        return null;
    }


    public void filter(String text) {
        mFilteredItemsList.clear();
        if(text.isEmpty()){
            mFilteredItemsList.addAll(mItemsList);
        } else{
            text = text.toLowerCase();
            for(Employee item: mItemsList){
                if(item.name.toLowerCase().contains(text) || item.surname.toLowerCase().contains(text)){
                    mFilteredItemsList.add(item);
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
    protected void callback(int position){
        if (mCallback != null)
            mCallback.checkedCallback(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public UserItemBinding mBinding;

        public ObservableLong id = new ObservableLong();
        public ObservableField<String> name = new ObservableField<>();
        public ObservableField<String> surname = new ObservableField<>();
        public ObservableField<String> organization = new ObservableField<>();
        public ObservableField<String> positionO = new ObservableField<>();
        public ObservableInt access = new ObservableInt();

        public ViewHolder(UserItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(Employee employee){
            mBinding.setModel(this);
            mBinding.executePendingBindings();

            id.set(employee.id);
            name.set(employee.name);
            surname.set(employee.surname);
            organization.set(employee.organizationName);
            positionO.set(employee.position);
            access.set(employee.access);

            itemView.setSelected(mCheckedPosition == getAdapterPosition());

            itemView.setOnClickListener(v -> {
                if (mCheckedPosition != getAdapterPosition()) {
                    mCheckedPosition = getAdapterPosition();
//                    notifyItemChanged(getAdapterPosition());
                } else {
                    mCheckedPosition = UNCHECKED;
//                    notifyItemChanged(getAdapterPosition());
                }
                notifyDataSetChanged();


                callback(getAdapterPosition());
            });
        }
    }

}

