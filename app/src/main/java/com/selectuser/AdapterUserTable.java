package com.selectuser;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.selectuser.databinding.UserHeaderWideScreenBinding;
import com.selectuser.databinding.UserItemWideScreenBinding;

import java.util.List;



public class AdapterUserTable extends AdapterUser {

    private final String TAG = "AdapterUserTable";

    int mWidthColumn;

    private final int TYPE_HEADER = 0;
    private final int TYPE_ITEM_WIDE = 1;

    private final int ITEMS_LIST_OFFSET = 1;        // Offset due to title

    public AdapterUserTable(Context context, LiveData<List<Employee>> itemsList, long defaultId){
        super(context, itemsList, defaultId);

        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        int COLUMN_NUM = 5;
        mWidthColumn = (int) ((double) displayMetrics.widthPixels / COLUMN_NUM);
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEADER;
        return TYPE_ITEM_WIDE;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == TYPE_ITEM_WIDE) {
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
        if (holder instanceof WideViewHolder) {
            ((WideViewHolder) holder).bind(mFilteredItemsList.get(position - ITEMS_LIST_OFFSET));
        }
    }

    @Override
    public int getItemCount() {
        return mFilteredItemsList.size() + ITEMS_LIST_OFFSET;
    }


    public class WideViewHolder extends RecyclerView.ViewHolder {
        public UserItemWideScreenBinding mBinding;

        public WideViewHolder(UserItemWideScreenBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.setWidth(mWidthColumn);
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

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public UserHeaderWideScreenBinding mBinding;

        public HeaderViewHolder(UserHeaderWideScreenBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.setWidth(mWidthColumn);
        }
    }

}