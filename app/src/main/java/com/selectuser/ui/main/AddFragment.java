package com.selectuser.ui.main;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.selectuser.Employee;
import com.selectuser.R;


public class AddFragment extends Fragment {

    TextView mPin;
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


        mName = view.findViewById(R.id.add_employee_name);
        mSurname = view.findViewById(R.id.add_employee_surname);
        mOrganization = view.findViewById(R.id.add_employee_organization);
        mPosition = view.findViewById(R.id.add_employee_position);
        mPin = view.findViewById(R.id.add_employee_pin);

        Button button = view.findViewById(R.id.button_add);
        button.setOnClickListener(v -> {
            Employee employee = new Employee();

            if (checkAndParseValue(employee)) {
                mViewModel.itemAdded(employee);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });


        ImageView imageView = view.findViewById(R.id.add_employee_pin_show);

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


    private boolean checkAndParseValue(Employee employee){

        employee.name = mName.getText().toString().trim();
        if (employee.name.isEmpty()){
            Snackbar.make(mSnackBarView,
                    getContext().getString(R.string.msg_list_manager_empty_filed)
                            + "\t" +
                            getContext().getString(R.string.msg_list_manager_name)
                    , Snackbar.LENGTH_LONG).show();
            return false;
        }

        employee.surname = mSurname.getText().toString().trim();
        if (employee.surname.isEmpty()){
            Snackbar.make(mSnackBarView,
                    getContext().getString(R.string.msg_list_manager_empty_filed)
                            + "\t" +
                            getContext().getString(R.string.msg_list_manager_surname)
                    , Snackbar.LENGTH_LONG).show();
            return false;
        }

        employee.organizationName = mOrganization.getText().toString().trim();
        if (employee.organizationName.isEmpty()){
            Snackbar.make(mSnackBarView,
                    getContext().getString(R.string.msg_list_manager_empty_filed)
                            + "\t" +
                            getContext().getString(R.string.msg_list_manager_organization)
                    , Snackbar.LENGTH_LONG).show();
            return false;
        }


        employee.position = mPosition.getText().toString().trim();
        if (employee.position.isEmpty()){
            Snackbar.make(mSnackBarView,
                    getContext().getString(R.string.msg_list_manager_empty_filed)
                            + "\t" +
                            getContext().getString(R.string.msg_list_manager_organization)
                    , Snackbar.LENGTH_LONG).show();
            return false;
        }

        if (mPin.getText().toString().isEmpty()) {
            Snackbar.make(mSnackBarView,
                    getContext().getString(R.string.msg_list_manager_empty_filed)
                            + "\t" +
                            getContext().getString(R.string.msg_list_manager_pin)
                    , Snackbar.LENGTH_LONG).show();
            return false;
        }
        try {
            employee.pin = Integer.parseInt(mPin.getText().toString());
        } catch (NumberFormatException e){
            e.printStackTrace();
            return false;
        }

        return mViewModel.calculateId(employee);
    }



}