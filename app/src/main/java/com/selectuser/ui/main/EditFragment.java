package com.selectuser.ui.main;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.selectuser.Employee;
import com.selectuser.R;


public class EditFragment extends Fragment {

    private final String TAG = "EditFragment";

    TextView mId;
    TextView mOrganizationId;
    TextView mName;
    TextView mSurname;
    TextView mOrganization;
    TextView mPosition;
    TextView mPin;
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
            mId.setText(String.valueOf(employee1.id));
            mOrganizationId.setText(String.valueOf(employee1.organizationId));
            mName.setText(employee1.name);
            mSurname.setText(employee1.surname);
            mOrganization.setText(employee1.organizationName);
            mPosition.setText(employee1.position);
            mPin.setText(String.valueOf(employee1.pin));
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
        mOrganizationId = view.findViewById(R.id.edit_employee_organization_id);
        mName = view.findViewById(R.id.edit_employee_name);
        mSurname = view.findViewById(R.id.edit_employee_surname);
        mOrganization = view.findViewById(R.id.edit_employee_organization);
        mPosition = view.findViewById(R.id.edit_employee_position);
        mPin = view.findViewById(R.id.edit_employee_pin);

        Button button = view.findViewById(R.id.button_edit);
        button.setOnClickListener(v -> {
            Employee employee = new Employee();

            if (checkAndParseValue(employee)) {
                mViewModel.itemEdited(employee);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });


        ImageView imageView = view.findViewById(R.id.edit_employee_pin_show);

        imageView.setOnClickListener(v -> {
            if(mPin.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                //Show Password
                imageView.setImageResource(R.drawable.ic_visibility_24);
                mPin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                //Hide Password
                imageView.setImageResource(R.drawable.ic_visibility_off_24);
                mPin.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    private boolean checkAndParseValue(Employee employee){


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

        try {
            employee.pin = Integer.parseInt(mPin.getText().toString());
        } catch (NumberFormatException e){
            e.printStackTrace();
            return false;
        }

        if (!mViewModel.calculateId(employee)){
            return false;
        }

        return true;
    }
}