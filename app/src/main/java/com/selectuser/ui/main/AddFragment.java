package com.selectuser.ui.main;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.selectuser.Employee;
import com.selectuser.R;


public class AddFragment extends Fragment {

    TextView mId;
    TextView mName;
    TextView mSurname;
    TextView mOrganization;
    TextView mPosition;
    CoordinatorLayout mSnackBarView;
    MainViewModel mViewModel;

    public AddFragment() {
        // Required empty public constructor
    }

    public static AddFragment newInstance() {
        return new AddFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);         // common model
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        mSnackBarView = view.findViewById(R.id.add_snackbar_text);

        mId = view.findViewById(R.id.add_employee_id);
        mName = view.findViewById(R.id.add_employee_name);
        mSurname = view.findViewById(R.id.add_employee_surname);
        mOrganization = view.findViewById(R.id.add_employee_organization);
        mPosition = view.findViewById(R.id.add_employee_position);

        Button button = view.findViewById(R.id.button_add);
        button.setOnClickListener(v -> {
            Employee employee = new Employee();

            if (checkAndParseValue(employee))
                mViewModel.itemAdded(employee);
        });

        return view;
    }


    private boolean checkAndParseValue(Employee employee){
        if (mId.getText().toString().isEmpty()) {
            Snackbar.make(mSnackBarView,
                    getContext().getString(R.string.msg_list_manager_empty_filed)
                            + "\t" +
                            getContext().getString(R.string.msg_list_manager_id)
                    , Snackbar.LENGTH_LONG).show();
            return false;
        }
        try {
            employee.id = Integer.parseInt(mId.getText().toString());
        } catch (NumberFormatException e){
            e.printStackTrace();

            return false;
        }

        employee.name = mName.getText().toString();
        if (employee.name.isEmpty()){
            Snackbar.make(mSnackBarView,
                    getContext().getString(R.string.msg_list_manager_empty_filed)
                            + "\t" +
                            getContext().getString(R.string.msg_list_manager_name)
                    , Snackbar.LENGTH_LONG).show();
            return false;
        }

        employee.surname = mSurname.getText().toString();
        if (employee.surname.isEmpty()){
            Snackbar.make(mSnackBarView,
                    getContext().getString(R.string.msg_list_manager_empty_filed)
                            + "\t" +
                            getContext().getString(R.string.msg_list_manager_surname)
                    , Snackbar.LENGTH_LONG).show();
            return false;
        }

        employee.organizationName = mOrganization.getText().toString();
        if (employee.organizationName.isEmpty()){
            Snackbar.make(mSnackBarView,
                    getContext().getString(R.string.msg_list_manager_empty_filed)
                            + "\t" +
                            getContext().getString(R.string.msg_list_manager_organization)
                    , Snackbar.LENGTH_LONG).show();
            return false;
        }


        employee.position = mPosition.getText().toString();
        if (employee.position.isEmpty()){
            Snackbar.make(mSnackBarView,
                    getContext().getString(R.string.msg_list_manager_empty_filed)
                            + "\t" +
                            getContext().getString(R.string.msg_list_manager_organization)
                    , Snackbar.LENGTH_LONG).show();
            return false;
        }

        return true;
    }



}