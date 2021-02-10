package com.selectuser.viewholder;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.selectuser.ItemModel;
import com.selectuser.databinding.UserItemBinding;

public abstract class ViewHolder<VDB extends ViewDataBinding, IM extends ItemModel> extends RecyclerView.ViewHolder {

    public VDB mBinding;


    public ViewHolder(VDB binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    public abstract void bind(IM itemModel);


    public interface Factory<VH, VDB>{
        public VH build(VDB binding);
    }


    public interface IViewHolderCallback {
        public void checkCallback(int position);
    }
    private IViewHolderCallback mCallback;

    public void registerCallback(IViewHolderCallback callback){
        mCallback = callback;
    }

    protected void checkCallback(int position){
        if (mCallback != null)
            mCallback.checkCallback(position);
    }

}