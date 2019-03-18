package com.company.simon.androidproject2;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class UserTaskToServletPost extends AsyncTask<String, String, String> {

    public static final String SIGN_UP_USER = "user";
    public static final String PROPERTY = "property";
    public static final String REVIEW = "review";
    public static final String SIMON = "Simon";
    private final String EQUALS = "=";
    private final String AND = "&";
    private static final String HOST = "http://10.0.2.2:8082/AndroidProjectServer_4__war_exploded/HouseHunterServlet";

    private static final int SIGN_UP_SUCCESSFUL = 200;
    public static final int SIGN_UP_FAILED = 202;
    private static final int USERNAME_TAKEN = 701;

    private User user;
    private String parameters;

    public UserTaskToServletPost(User user){
        this.user = user;
    }

    @Override
    protected void onPreExecute() {
        try {
            parameters = determineUrl(SIGN_UP_USER);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String... doInBackgroundStrings) {
        publishProgress("Started in doInBackground");
        URL url = null;
        HttpURLConnection connection = null;
        InputStreamReader inputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        try {
            while (true) {
                url = new URL(HOST);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Accept-Language", "UTF-8");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
                outputStreamWriter.write(parameters);
                outputStreamWriter.flush();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String actuallyRead;
                StringBuilder response = new StringBuilder();
                while ((actuallyRead = bufferedReader.readLine()) != null) {
                    response.append(actuallyRead);
                }
                String status = String.valueOf(response);
                if (status.equals("200")) {
                    Log.d(SIMON, "Server received " + doInBackgroundStrings[1]);
                    Log.d(SIMON, "Sign up successful");
                    return "200";
                }
                if (status.equals("701")) {
                    Log.d(SIMON, "Username taken");
                    return "701";
                }

                int responseCode = connection.getResponseCode();
                Log.d("Simon", String.valueOf(responseCode));
                break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }

        return null;
    }


    @Override
    protected void onProgressUpdate(String... progress) {
        Log.d(SIMON, progress[0]);
    }

    @Override
    protected void onPostExecute(String result) {
//        Log.d(SIMON, result);
//        if(result != null){
//            int taskResult = Integer.valueOf(result);
//            switch (taskResult){
//                case SIGN_UP_SUCCESSFUL:
//                    loginOrSignup(false);
//                    break;
//                case USERNAME_TAKEN:
//                    Toast.makeText(HomeActivity.this, "Username taken.", Toast.LENGTH_SHORT).show();
//                    break;
//                case SIGN_UP_FAILED:
//                    Toast.makeText(HomeActivity.this, "Sign up failed.", Toast.LENGTH_SHORT).show();
//                    break;
//            }

       // }
    }

    public String determineUrl(String action) throws UnsupportedEncodingException {
        String parameter = "";
        switch (action){
            case SIGN_UP_USER:
                parameter = userURLBuilder();
                break;
            case PROPERTY:

                break;
            case REVIEW:

                break;
        }
        return parameter;
    }

    public String userURLBuilder() throws UnsupportedEncodingException {
        StringBuilder parameters = new StringBuilder("?action=");
        parameters.append(URLEncoder.encode("101", "UTF-8"));
        parameters.append("&username=");
        parameters.append(URLEncoder.encode(user.getUserName(), "UTF-8"));
        parameters.append("&password=");
        parameters.append(URLEncoder.encode(user.getPassword(), "UTF-8"));
        parameters.append("&age=");
        parameters.append(URLEncoder.encode(user.getAge(), "UTF-8"));
        parameters.append("&isMale=");
        parameters.append(URLEncoder.encode(user.getIsMale(), "UTF-8"));

        return String.valueOf(parameters);
    }


}

