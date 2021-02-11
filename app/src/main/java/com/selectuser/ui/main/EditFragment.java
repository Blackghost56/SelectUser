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


public class EditFragment extends Fragment {

    TextView mId;
    TextView mName;
    TextView mSurname;
    TextView mOrganization;
    TextView mPosition;
    CoordinatorLayout mSnackBarView;
    MainViewModel mViewModel;



//    private static final String ARG_PARAM_EMPLOYEE = "employee";
//    private Employee mEmployee;

    public EditFragment() {
        // Required empty public constructor
    }


    public static EditFragment newInstance(Employee employee) {
        EditFragment fragment = new EditFragment();
//        Bundle args = new Bundle();
//        args.putSerializable(ARG_PARAM_EMPLOYEE, employee);
//        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);         // common model

        Employee employee1 = mViewModel.getSelectedEmployee();
        if (employee1 != null) {
            mId.setText(employee1.id);
            mName.setText(employee1.name);
            mSurname.setText(employee1.surname);
            mOrganization.setText(employee1.organizationName);
            mPosition.setText(employee1.position);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mEmployee = getArguments().getString(ARG_PARAM_EMPLOYEE);
//        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit, container, false);

        mSnackBarView = view.findViewById(R.id.edit_snackbar_text);

        mId = view.findViewById(R.id.edit_employee_id);
        mName = view.findViewById(R.id.edit_employee_name);
        mSurname = view.findViewById(R.id.edit_employee_surname);
        mOrganization = view.findViewById(R.id.edit_employee_organization);
        mPosition = view.findViewById(R.id.edit_employee_position);



        Button button = view.findViewById(R.id.button_edit);
        button.setOnClickListener(v -> {
            Employee employee = new Employee();

            if (checkAndParseValue(employee))
                mViewModel.itemAdded(employee);
        });

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();


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