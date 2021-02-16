package com.selectuser.ui.main;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.android.material.snackbar.Snackbar;
import com.selectuser.AdapterUserCard;
import com.selectuser.AdapterUser;
import com.selectuser.AdapterUserTable;
import com.selectuser.R;
import com.selectuser.databinding.MainFragmentBinding;

public class MainFragment extends Fragment {

    private final String TAG = "MainFragment";
    private final String SELECTED_ID = "selected_id";

    private final int WIDE_SCREEN_DP = 700;

    private MainViewModel mViewModel;
    private MainFragmentBinding mBinding;
    private AdapterUser mAdapter;
    CoordinatorLayout mSnackBarView;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false);
        View view = mBinding.getRoot();

        mSnackBarView = view.findViewById(R.id.snackBar_text);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);         // common model
        mBinding.setViewModel(mViewModel);

        long defaultSelected = AdapterUserCard.UNCHECKED;
        if (savedInstanceState != null) {
            long selectedId = savedInstanceState.getLong(SELECTED_ID);
            if (selectedId != 0)
                defaultSelected = selectedId;
        }

        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        boolean isWideScreen = dpWidth > WIDE_SCREEN_DP;

        RecyclerView recyclerView = getActivity().findViewById(R.id.itemsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (isWideScreen){
            mAdapter = new AdapterUserTable(getContext(), mViewModel.getEmployeeList(), defaultSelected);
        } else {
            mAdapter = new AdapterUserCard(getContext(), mViewModel.getEmployeeList(), defaultSelected);
        }

        mAdapter.registerCallback(employee -> mViewModel.itemSelect(employee));
        recyclerView.setAdapter(mAdapter);

        mViewModel.getShowSnackBar().observe(getViewLifecycleOwner(), s -> Snackbar.make(mSnackBarView, s, Snackbar.LENGTH_LONG).show());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);        // enable onCreateOptionsMenu
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(SELECTED_ID, mAdapter.getSelectedId());
    }
}