package com.selectuser;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.selectuser.databinding.UserHeaderWideScreenBinding;
import com.selectuser.databinding.UserItemBinding;
import com.selectuser.databinding.UserItemWideScreenBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String TAG = "Adapter";

    List<EmployeeModel> mItemsList = new ArrayList<>();
    Map<Long, EmployeeModel> mItemsMap = new HashMap<>();
    List<EmployeeModel> mFilteredItemsList = new ArrayList<>();
    Context mContext;
    boolean mIsWideScreen;
    int mWidthColumn;

    private final int TYPE_HEADER = 0;
    private final int TYPE_ITEM = 1;
    private final int TYPE_ITEM_WIDE = 2;

    private final int WIDE_SCREEN_DP = 700;
    private final int WIDE_COLUMN_NUM = 5;
    private final int WIDE_LIST_OFFSET = 1;

    public static int UNCHECKED = -1;
    private long mCheckedId = UNCHECKED;

    public Adapter(Context context, LiveData<List<Employee>> itemsList, long defaultId){
        mContext = context;

        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        Log.d(TAG, "dpHeight: " + dpHeight + "\tdpWidth: " + dpWidth);
        mIsWideScreen = dpWidth > WIDE_SCREEN_DP;
        mWidthColumn = (int) ((double) displayMetrics.widthPixels / WIDE_COLUMN_NUM);

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
    public int getItemViewType(int position) {
        if (mIsWideScreen) {
            if (position == 0)
                return TYPE_HEADER;
            return TYPE_ITEM_WIDE;
        }

        return TYPE_ITEM;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == TYPE_ITEM) {
            UserItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.user_item, parent, false);
            return new ViewHolder(binding);
        } else if (viewType == TYPE_ITEM_WIDE) {
            UserItemWideScreenBinding binding = DataBindingUtil.inflate(inflater, R.layout.user_item_wide_screen, parent, false);
            return new WideViewHolder(binding);
        } else if (viewType == TYPE_HEADER) {
            UserHeaderWideScreenBinding binding = DataBindingUtil.inflate(inflater, R.layout.user_header_wide_screen, parent, false);
            return new HeaderViewHolder(binding);
        }

        return null;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).bind(mFilteredItemsList.get(position));
        } else if (holder instanceof WideViewHolder) {
            ((WideViewHolder) holder).bind(mFilteredItemsList.get(position - WIDE_LIST_OFFSET));
        } else if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).setWidth((mWidthColumn));
        }
    }

    @Override
    public int getItemCount() {
        if (mIsWideScreen) {
            return mFilteredItemsList.size() + WIDE_LIST_OFFSET;
        }
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



    public class WideViewHolder extends RecyclerView.ViewHolder {
        public UserItemWideScreenBinding mBinding;

        public WideViewHolder(UserItemWideScreenBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(EmployeeModel employeeModel){
            mBinding.setModel(employeeModel);
            mBinding.executePendingBindings();
            mBinding.setWidth(mWidthColumn);

            itemView.setOnClickListener(v -> {
                if (mCheckedId != employeeModel.id.get()) {
                    setSelected(employeeModel.id.get());
                } else {
                    setSelected(UNCHECKED);
                }
            });
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public UserHeaderWideScreenBinding mBinding;

        public HeaderViewHolder(UserHeaderWideScreenBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void setWidth(int width){
            mBinding.setWidth(width);
        }
    }

}

