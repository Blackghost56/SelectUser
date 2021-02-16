package com.selectuser.dialog;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.selectuser.R;
import com.selectuser.ui.main.MainViewModel;

import java.util.Objects;


public class PinDialog extends DialogFragment {

    private MainViewModel mViewModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pin_dialog, null);

        EditText editText = view.findViewById(R.id.request_pin);

        ImageView imageView = view.findViewById(R.id.request_pin_show);

        imageView.setOnClickListener(v -> {
            if(editText.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                //Show Password
                imageView.setImageResource(R.drawable.ic_visibility_24);
                editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                //Hide Password
                imageView.setImageResource(R.drawable.ic_visibility_off_24);
                editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        Button enter = view.findViewById(R.id.request_pin_enter);
        enter.setOnClickListener(v -> {
            mViewModel.onPinEntered(editText.getText().toString());
            dismiss();
        });

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity())).get(MainViewModel.class);         // common model
    }
}
