package com.selectuser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.selectuser.databinding.UserItemBinding;
import com.selectuser.viewholder.ViewHolder;

import java.util.List;


//todo make table header

public class Adapter<VDB extends ViewDataBinding, IM extends ItemModel, VH extends ViewHolder<VDB, IM>> extends RecyclerView.Adapter<VH> {

    private final ViewHolder.Factory<VH, VDB> mFactory;

    List<IM> mItemsList;
    Context mContext;
    int mItemLayout;


    public int UNCHECKED = -1;
    // if checkedPosition = -1, there is no default selection
    // if checkedPosition = 0, 1st item is selected by default
    private int mCheckedPosition = UNCHECKED;

    public Adapter(Context context, List<IM> itemsList, int itemLayout, ViewHolder.Factory<VH, VDB> factory){
        mContext = context;
        mItemsList = itemsList;
        mItemLayout = itemLayout;
        mFactory = factory;
    }


    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        VDB binding = DataBindingUtil.inflate(inflater, mItemLayout, parent, false);
        return mFactory.build(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.bind(mItemsList.get(position));


//        holder.itemView.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.selector_item));
        holder.itemView.setSelected(mCheckedPosition == position);

        holder.registerCallback(mCallback);

//        holder.itemView.setOnClickListener(v -> {
//            mCheckedPosition = position;
//            notifyDataSetChanged();
//            checkCallback(position);      // todo !!!
//        });


    }

    @Override
    public int getItemCount() {
        return mItemsList.size();
    }

    public IM getSelected() {
        //Log.d(TAG, "getSelected, m_checkedPosition: " + m_checkedPosition);
        if (mCheckedPosition != UNCHECKED) {
            return mItemsList.get(mCheckedPosition);
        }
        return null;
    }


    private ViewHolder.IViewHolderCallback mCallback;
    public void registerViewHolderCallback(ViewHolder.IViewHolderCallback callback){
        mCallback = callback;
    }
    protected void checkCallback(int position){
        if (mCallback != null)
            mCallback.checkCallback(position);
    }


    public class UserViewHolder extends ViewHolder<UserItemBinding, UserModel> {

        public UserModel mItemModel;

        public UserViewHolder(UserItemBinding binding) {
            super(binding);
        }

        public void bind(UserModel itemModel){
            mBinding.setModel(itemModel);
            mItemModel = itemModel;


            holder.itemView.setOnClickListener(v -> {
                mCheckedPosition = position;
                notifyDataSetChanged();
                checkCallback(position);      // todo !!!
            });
        }
    }

}

