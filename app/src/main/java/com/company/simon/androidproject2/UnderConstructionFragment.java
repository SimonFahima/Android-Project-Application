package com.company.simon.androidproject2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class UnderConstructionFragment extends DialogFragment {

    private Button btnGotIt;
    private OnClickedGotItListener listener;
    private String txtFragmentMessage;
    private TextView txtConstructionText;


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Under Construction");
        View view = inflater.inflate(R.layout.fragment_under_construction, container, false);
        txtConstructionText = view.findViewById(R.id.txtConstructionText);
        if(txtFragmentMessage.equals(Variables.REVIEW)){
            txtConstructionText.setText("Reviews Activity currently under construction!");
        } else if(txtFragmentMessage.equals(Variables.MY_ACCOUNT)){
            txtConstructionText.setText("My Account Activity currently under construction!");
        }
        btnGotIt = view.findViewById(R.id.btnGotIt);
        btnGotIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.gotItClicked();
                }
                dismiss();
            }
        });
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return view;
    }

    public void setActivity(String activity){
        txtFragmentMessage = activity;
    }

    public void setListener(OnClickedGotItListener listener){
        this.listener = listener;
    }

    public interface OnClickedGotItListener{
        void gotItClicked();
    }
}
