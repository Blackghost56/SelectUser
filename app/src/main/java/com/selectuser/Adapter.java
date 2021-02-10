package com.selectuser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.selectuser.databinding.UserItemBinding;

import java.util.ArrayList;
import java.util.List;


//todo make table header

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {


    List<UserModel> mItemsList;
    List<UserModel> mFilteredItemsList = new ArrayList<>();
    Context mContext;


    public int UNCHECKED = -1;
    // if checkedPosition = -1, there is no default selection
    // if checkedPosition = 0, 1st item is selected by default
    private int mCheckedPosition = UNCHECKED;

    public Adapter(Context context, List<UserModel> itemsList){
        mContext = context;
        mItemsList = itemsList;
        mFilteredItemsList.addAll(itemsList);
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

    public UserModel getSelected() {
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
            for(UserModel item: mItemsList){
                if(item.name.get().toLowerCase().contains(text) || item.surname.get().toLowerCase().contains(text)){
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

        public UserModel mItemModel;
        public UserItemBinding mBinding;

        public ViewHolder(UserItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(UserModel itemModel){
            mBinding.setModel(itemModel);
            mItemModel = itemModel;

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

