package com.selectuser.ui.main;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.selectuser.Adapter;
import com.selectuser.Employee;
import com.selectuser.MainActivity;
import com.selectuser.UserModel;
import com.selectuser.R;
import com.selectuser.databinding.MainFragmentBinding;
import com.selectuser.databinding.UserItemBinding;
import com.selectuser.viewholder.UserViewHolder;
import com.selectuser.viewholder.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private final String TAG = "MainFragment";

    private MainViewModel mViewModel;
    private MainFragmentBinding mBinding;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false);
        View view = mBinding.getRoot();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);                // local model
        mViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);         // common model
        mBinding.setViewModel(mViewModel);




        RecyclerView recyclerView = getActivity().findViewById(R.id.itemsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));  //??



        List<UserModel> list = new ArrayList<>();
        Employee employee = new Employee();
        employee.id = 0;
        employee.name = "Michael";
        employee.surname = "Tsvetkov";
        employee.organizationName = "NPP CRTS";
        employee.position = "Developer";
        employee.access = 0;
        list.add(new UserModel(employee));

        employee = new Employee();
        employee.id = 11;
        employee.name = "Петров";
        employee.surname = "Фёдр";
        employee.organizationName = "АО ЦРТС";
        employee.position = "Монтажник";
        employee.access = 1;
        list.add(new UserModel(employee));


        Adapter<UserItemBinding, UserModel, UserViewHolder> adapter = new Adapter<>(getContext(), list, R.layout.user_item, UserViewHolder::new);
        adapter.registerViewHolderCallback(new ViewHolder.IViewHolderCallback() {
            @Override
            public void checkCallback(int position) {
                mViewModel.itemSelect(adapter.getSelected().toEmployee());
            }
        });
        recyclerView.setAdapter(adapter);

    }



}