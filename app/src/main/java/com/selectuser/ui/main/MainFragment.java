package com.selectuser.ui.main;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.selectuser.Adapter;
import com.selectuser.Adapter.ViewHolder;
import com.selectuser.Employee;
import com.selectuser.UserModel;
import com.selectuser.R;
import com.selectuser.databinding.MainFragmentBinding;
import com.selectuser.databinding.UserItemBinding;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private final String TAG = "MainFragment";

    private MainViewModel mViewModel;
    private MainFragmentBinding mBinding;
    private Adapter mAdapter;

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



        List<Employee> list = new ArrayList<>();
        Employee employee = new Employee();
        employee.id = 0;
        employee.name = "Michael";
        employee.surname = "Tsvetkov";
        employee.organizationName = "NPP CRTS";
        employee.position = "Developer";
        employee.access = 0;
        list.add(employee);

        employee = new Employee();
        employee.id = 11;
        employee.name = "Петров";
        employee.surname = "Фёдр";
        employee.organizationName = "АО ЦРТС";
        employee.position = "Монтажник";
        employee.access = 1;
        list.add(employee);


        mAdapter = new Adapter(getContext(), list);
        mAdapter.registerCallback(position -> {
                mViewModel.itemSelect(mAdapter.getSelected());
        });
        recyclerView.setAdapter(mAdapter);

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);        // enable onCreateOptionsMenu
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Log.d(TAG, "onCreateOptionsMenu");

        MenuItem item = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.filter(newText);
                return true;
            }
        });
    }
}