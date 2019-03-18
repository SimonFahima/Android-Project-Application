package com.company.simon.androidproject2;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SignUpFragment extends DialogFragment {

    private String title = "Sign Up!";
    private EditText inpUsername;
    private EditText inpAge;
    private RadioGroup radioGroup;
    private RadioButton radioMale;
    private RadioButton radioFemale;
    private EditText inpEmail;
    private EditText inpPassword;
    private EditText inpConfirmedPassword;
    private CheckBox checkTaS;
    private boolean isMale;
    private onSignUpFragmentListener listener;
    private Button btnSignUpFragment;



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(title);
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        inpUsername = view.findViewById(R.id.inpUsernameSignUp);
        inpAge = view.findViewById(R.id.inpAge);
        radioGroup = view.findViewById(R.id.radioGroup);
        radioMale = view.findViewById(R.id.radioMale);
        radioFemale = view.findViewById(R.id.radioFemale);
        inpEmail = view.findViewById(R.id.inpEmail);
        inpPassword = view.findViewById(R.id.inpPasswordSignUp1);
        inpConfirmedPassword = view.findViewById(R.id.inpPasswordSignUp2);
        checkTaS = view.findViewById(R.id.checkTaS);
        btnSignUpFragment = view.findViewById(R.id.btnSignUpFragment);

        inpUsername.requestFocus();

        btnSignUpFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = inpUsername.getText().toString();
                String age = inpAge.getText().toString();
                String email = inpEmail.getText().toString();
                String password = inpPassword.getText().toString();
                String confirmedPassword = inpConfirmedPassword.getText().toString();
                String is_Male;
                if (username.isEmpty() || age.isEmpty() || email.isEmpty() || password.isEmpty()){
                    Toast.makeText(getContext(), "Must fill in all the blanks.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!radioMale.isChecked() && !radioFemale.isChecked()){
                    Toast.makeText(getContext(), "Must choose gender you snowflake.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(radioMale.isChecked()){
                    isMale = true;
                }
                if(radioFemale.isChecked()){
                    isMale = false;
                }
                if(!password.equals(confirmedPassword)){
                    Toast.makeText(getContext(), "Passwords do not match.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!checkTaS.isChecked()){
                    Toast.makeText(getContext(), "Must accept terms and services", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(isMale){
                    is_Male = "Male";
                }else {
                    is_Male = "Female";
                }
                if(listener != null){
                    listener.onSignUp(username, age, email, is_Male, password);
                }
                dismiss();
            }
        });
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return view;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setListener(onSignUpFragmentListener listener) {
        this.listener = listener;
    }

    public interface onSignUpFragmentListener{
        void onSignUp(String username, String age, String email, String isMale, String password);
    }
}
