package com.company.simon.androidproject2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import java.util.Set;

public class HomeActivity extends AppCompatActivity implements OnResultListener {
    //private ProgressBar loadingAnimation;
    private TextView txtAppName;
    private TextView txtNoServerConnection1;
    private TextView txtNoServerConnection2;
    private TextView txtNoServerConnection3;
    private LoginFragment loginFragment;
    private OnResultListener onResultListener;
    private Users users;
    private User user;
    private int taskResult;
    private boolean haveResult = false;
    private boolean automaticLogin = false;
    private boolean serverConnection;
    private Button btnLogin;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        txtAppName = findViewById(R.id.txtAppName);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);
//        txtNoServerConnection1 = findViewById(R.id.txtNoServerConnection1);
//        txtNoServerConnection2 = findViewById(R.id.txtNoServerConnection2);
//        txtNoServerConnection3 = findViewById(R.id.txtNoServerConnection3);
        users = Users.getUsers();
        try{
            ServerSideGet serverSideGetProperties = new ServerSideGet();
            serverSideGetProperties.executeRequestToServer(Variables.LOAD_ALL_PROPERTIES);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(getIntent().hasExtra("from")){
            String from = getIntent().getStringExtra("from");
            if(from.equals("activity")){
                users = Users.getUsers();
                serverConnection = true;
                setContentView(R.layout.activity_home);
            }
        }else {
//        loadingAnimation = findViewById(R.id.loadingAnimation);
            setWelcomePage();
            //make method that will be true once loading from server is complete

        }

    }


    public void sharedPrefsUsers(){
        SharedPreferences prefs = getSharedPreferences(Variables.PREFS,MODE_PRIVATE);

        if(prefs.contains(Variables.USERS)){
            Set<String> userAsString = prefs.getStringSet(Variables.USERS, null);
            users.loadUsers(userAsString);
        }

        if(prefs.contains(Variables.USERNAME)){
            String userName = prefs.getString(Variables.USERNAME,null);
            String password = prefs.getString(Variables.PASSWORD,null);
            user = new User(userName,password);
            onResultListener = this;
            ServerSideGet serverSideGetUser = new ServerSideGet(user, onResultListener);
            serverSideGetUser.executeRequestToServer(Variables.CHECK_USER);
            while (!haveResult){
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(taskResult == Variables.CORRECT_USER){
                automaticLogin = true;
            }
            if(taskResult == Variables.NO_SERVER_CONNECTION){
                Toast.makeText(this, "No server connection", Toast.LENGTH_SHORT).show();
                serverConnection = false;
            }
            serverConnection = true;
        }
    }


    public void setWelcomePage(){
        setContentView(R.layout.app_icon_loader);
        sharedPrefsUsers();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(automaticLogin){
                    goToSecondActivity();
                }else {
                    if(!serverConnection){
//                        txtNoServerConnection1.setVisibility(View.VISIBLE);
//                        txtNoServerConnection2.setVisibility(View.VISIBLE);
//                        txtNoServerConnection3.setVisibility(View.VISIBLE);
                    }
                    setContentView(R.layout.activity_home);
                }
            }
        },4000);
    }

    public void animateAppName(){
        YoYo.with(Techniques.RubberBand).duration(2000);
        YoYo.with(Techniques.RubberBand).repeat(1);
        YoYo.with(Techniques.RubberBand).playOn(findViewById(R.id.txtAppName));
    }


    @Override
    public void onResult(int result) {
        switch (result){
            case Variables.SIGN_UP_SUCCESSFUL:
                Log.d(Variables.SIMON, "have result SIGN_UP_SUCCESSFUL");
                taskResult = result;
                haveResult = true;
                break;
            case Variables.SIGN_UP_FAILED:
                Log.d(Variables.SIMON, "have result SIGN_UP_FAILED");
                taskResult  = result;
                haveResult = true;
                break;
            case Variables.USERNAME_TAKEN:
                Log.d(Variables.SIMON, "have result USERNAME_TAKEN");
                taskResult = result;
                haveResult = true;
                break;
            case Variables.CORRECT_USER:
                Log.d(Variables.SIMON, "have result CORRECT_USER");
                taskResult = result;
                haveResult = true;
                break;
            case Variables.WRONG_PASSWORD:
                Log.d(Variables.SIMON, "have result WRONG_PASSWORD");
                taskResult = result;
                haveResult = true;
                break;
            case Variables.NO_SUCH_USER:
                Log.d(Variables.SIMON, "have result NO_SUCH_USER");
                taskResult = result;
                haveResult = true;
                break;
            case Variables.SERVER_NOT_RESPONDING:
                Log.d(Variables.SIMON, "have result SERVER_NOT_RESPONDING");
                break;
            case Variables.NO_SERVER_CONNECTION:
                Log.d(Variables.SIMON, "have result NO_SERVER_CONNECTION");
                taskResult = result;
                haveResult = true;
                break;
        }
    }

    public void btnClickedLoginOrSignup(View view) {
        String tag = (String) view.getTag();
        switch(tag){
            case Variables.LOGIN:
                loginClicked();
                break;
            case Variables.SIGN_UP:
                signUpClicked();
                break;
        }
    }

    public void loginClicked(){
//        if(!serverConnection){
//            Toast.makeText(this, "Must have connection to server to continue.", Toast.LENGTH_SHORT).show();
//        }else {
            loginFragment = new LoginFragment();
            loginFragment.setTitle("Login");
            loginFragment.setListener(new LoginFragment.onLoginFragmentListener() {
                @Override
                public void onLogin(String username, String password) {
                    User user = new User(username, password);
                    HomeActivity.this.user = user;
                    onResultListener = HomeActivity.this;
                    ServerSideGet serverSideGetUser = new ServerSideGet(user, onResultListener);
                    serverSideGetUser.executeRequestToServer(Variables.CHECK_USER);
                    while (!haveResult){
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    switch (taskResult){
                        case Variables.CORRECT_USER:
                            haveResult = false;
                            Log.d(Variables.SIMON, "initializing login");
                            loginOrSignup(true);
                            break;
                        case Variables.WRONG_PASSWORD:
                            haveResult = false;
                            Toast.makeText(HomeActivity.this, "Wrong password.", Toast.LENGTH_SHORT).show();
                            break;
                        case Variables.NO_SUCH_USER:
                            haveResult = false;
                            Toast.makeText(HomeActivity.this, "Incorrect user", Toast.LENGTH_SHORT).show();
                    }

                }
            });
            loginFragment.show(getFragmentManager(), "");
        }

   // }

    public void signUpClicked(){
        if(!serverConnection){
            Toast.makeText(this, "Must have connection to server to continue.", Toast.LENGTH_SHORT).show();
        }else {
            SignUpFragment signUpFragment = new SignUpFragment();
            signUpFragment.setTitle("Sign up!");
            signUpFragment.setListener(new SignUpFragment.onSignUpFragmentListener() {
                @Override
                public void onSignUp(String username, String age, String email, String isMale, String password) {
                    User user = new User(username,password,age,email,isMale);
                    HomeActivity.this.user = user;
                    onResultListener = HomeActivity.this;
                    ServerSidePost serverSidePostUser = new ServerSidePost(user, onResultListener);
                    serverSidePostUser.postToServer(Variables.USER);
                    while(!haveResult){
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    switch (taskResult){
                        case Variables.SIGN_UP_SUCCESSFUL:
                            haveResult = false;
                            loginOrSignup(false);
                            break;
                        case Variables.SIGN_UP_FAILED:
                            haveResult = false;
                            Toast.makeText(HomeActivity.this, "Sign up failed.", Toast.LENGTH_SHORT).show();
                            break;
                        case Variables.USERNAME_TAKEN:
                            haveResult = false;
                            Toast.makeText(HomeActivity.this, "Username taken.", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }
            });
            signUpFragment.show(getFragmentManager(), "");
        }
    }



    @SuppressLint("ApplySharedPref")
    private void loginOrSignup(boolean isLogin){

        if(user == null){
            return;
        }
        if(isLogin){
            user = getUserFromUi();
        }
        SharedPreferences prefs = getSharedPreferences(Variables.PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Variables.USERNAME, user.getUserName())
              .putString(Variables.PASSWORD,user.getPassword());
        if(!isLogin){
            Set<String> usersAsString = users.getUsersAsString();
            editor.putStringSet(Variables.USERS, usersAsString);
        }
        editor.commit();

        goToSecondActivity();
    }

    private void goToSecondActivity(){
        Intent intent = new Intent(this,PropertiesActivity.class);
        intent.putExtra(Variables.USERNAME, user.getUserName());
        startActivity(intent);
        finish();
    }

    private User getUserFromUi(){
        String userName = loginFragment.inpUsername.getText().toString();
        String password = loginFragment.inpPassword.getText().toString();
        if(userName.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Both username and password\nare mandatory", Toast.LENGTH_SHORT).show();
            //TastyToast.makeText(this,"Error",TastyToast.LENGTH_LONG,TastyToast.ERROR).show();
            return null;
        }
        return new User(userName,password);
    }


}
