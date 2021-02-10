package com.selectuser.viewholder;

import com.selectuser.UserModel;
import com.selectuser.databinding.UserItemBinding;

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