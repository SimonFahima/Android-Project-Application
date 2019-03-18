package com.company.simon.androidproject2;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.InvalidParameterException;

public class User {

    private static final String DELIMETER = "&";
    private String userName;
    private String password;
    private String age;
    private String email;
    private String isMale;
    private String action = "101";
    private Users users;


    public User(String userName, String password, String age, String email, String isMale) {
        this.userName = userName;
        this.password = password;
        this.age = age;
        this.email = email;
        this.isMale = isMale;
    }

    public User(String userName, String password){
        this.userName = userName;
        this.password = password;
    }

    public User(String userAsString){
        if(userAsString == null){
            throw new InvalidParameterException();
        }
        String[] parts = userAsString.split(DELIMETER);
        if(parts.length != 5){
            throw new InvalidParameterException("must have five parts");
        }
        this.userName = parts[0];
        this.password = parts[1];
        this.age = parts[2];
        this.email = parts[3];
        this.isMale = parts[4];
    }




    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIsMale() {
        return isMale;
    }

    public void setIsMale(String isMale) {
        this.isMale = isMale;
    }

    @Override
    public String toString() {
        return action + DELIMETER + userName + DELIMETER + password
                + DELIMETER + age + DELIMETER + email+ DELIMETER + isMale;
    }
}


