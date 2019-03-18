package com.company.simon.androidproject2;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.zip.Inflater;

public class LoginFragment extends DialogFragment {

    private String username;
    private String title = "Login";
    public EditText inpUsername;
    public EditText inpPassword;
    private Button btnLoginFragment;
    private onLoginFragmentListener listener;
    private EditText txtUser, txtPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(title);
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        inpUsername = view.findViewById(R.id.inpUsername);
        inpPassword = view.findViewById(R.id.inpPassword);

        btnLoginFragment = view.findViewById(R.id.btnLoginFragment);
        if(username != null){
            inpUsername.setText(username);
            inpPassword.requestFocus();
        }else {
            inpUsername.requestFocus();
        }

        btnLoginFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment.this.username = inpUsername.getText().toString();
                String userName = inpUsername.getText().toString();
                String passWord = inpPassword.getText().toString();
                
                if(userName.isEmpty() || passWord.isEmpty()){
                    Toast.makeText(getContext(), "Username AND Password are required.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(listener != null){
                    listener.onLogin(userName, passWord);
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

    public void setListener(onLoginFragmentListener listener) {
        this.listener = listener;
    }

    public void setInpUsername(EditText inpUsername) {
        this.inpUsername = inpUsername;
    }

    public interface onLoginFragmentListener{
        void onLogin(String username, String password);
    }
}
