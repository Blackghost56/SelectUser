package com.selectuser.ui.main;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.selectuser.Adapter;
import com.selectuser.MainActivity;
import com.selectuser.R;
import com.selectuser.databinding.MainFragmentBinding;

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
        mAdapter = new Adapter(getContext(), mViewModel.getEmployeeList());
        mAdapter.registerCallback(employee -> {
                mViewModel.itemSelect(employee);
        });
        recyclerView.setAdapter(mAdapter);

//        mViewModel.getRemoveSelection().observe(getViewLifecycleOwner(), aVoid -> mAdapter.removeSelection());

//        if (savedInstanceState != null) {
//            int position = savedInstanceState.getInt(SAVE_POSITION);
//            Log.d(TAG, "onViewStateRestored SAVE_POSITION: " + position);
//            mAdapter.setSelectedPosition(position);
//        }
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


//    private final String SAVE_POSITION = "save_position";
//
//
//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putInt(SAVE_POSITION, mAdapter.getSelectedPosition());
//
//        Log.d(TAG, "SAVE_POSITION: " + mAdapter.getSelectedPosition());
//    }
}