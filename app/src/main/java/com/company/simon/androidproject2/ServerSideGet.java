package com.company.simon.androidproject2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ServerSideGet{


    private User user;
    private Property property;
    private String parameters;
    private OnResultListener onResultListener;
    private Properties properties = Properties.getProperties();


    public ServerSideGet(User user, OnResultListener onResultListener){
        this.user = user;
        this.onResultListener = onResultListener;
    }

    public ServerSideGet(){
    }

    public void executeRequestToServer(final int action) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    switch (action){
                        case Variables.CHECK_USER:
                            parameters = determineUrl(Variables.CHECK_USER);
                            break;
                        case Variables.LOAD_ALL_PROPERTIES:
                            parameters = determineUrl(Variables.LOAD_ALL_PROPERTIES);
                            break;

                    }
                    connectToServlet(Variables.HOST + parameters);
                }
            }).start();
        }

    private String determineUrl(int action){
        String parameter = "";
        switch (action){
            case Variables.CHECK_USER:
                parameter = checkUserURLBuilder();
                break;
            case Variables.LOAD_ALL_PROPERTIES:
                parameter = loadAllPropertiesURLBuilder();
                break;

        }
        return parameter;
    }

    private String loadAllPropertiesURLBuilder() {
        return Variables.ACTION + String.valueOf(Variables.LOAD_ALL_PROPERTIES);
    }

    private String checkUserURLBuilder(){
        String userAction = Variables.ACTION + String.valueOf(Variables.CHECK_USER);

        return userAction + Variables.AND + Variables.USERNAME + Variables.EQUALS +
                user.getUserName() + Variables.AND +
                Variables.PASSWORD + Variables.EQUALS + user.getPassword();
    }



    private void connectToServlet(String urlString) {
        URL url;
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        try {
            while (true) {
                url = new URL(urlString);
                connection = (HttpURLConnection) url.openConnection();
                if(connection == null){
                    onResultListener.onResult(Variables.NO_SERVER_CONNECTION);
                    break;
                }
                connection.setUseCaches(false);
                connection.setRequestMethod("GET");
                connection.setDoOutput(false);
                connection.connect();

                int responseCode = connection.getResponseCode();
                Log.d(Variables.SIMON, String.valueOf(responseCode));
                if (responseCode != 200) {
                    break;
                }

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String actuallyRead;
                StringBuilder response = new StringBuilder();
                while ((actuallyRead = bufferedReader.readLine()) != null) {
                    response.append(actuallyRead);
                }
                String result = String.valueOf(response);
                String[] propertyParts = result.split(Variables.HASHTAG);
                int answer = 0;
                Log.d(Variables.SIMON, "result is " + result);
                if (!result.isEmpty()) {
                    answer = Integer.parseInt(propertyParts[0]);
                    Log.d(Variables.SIMON, "answer is " + answer + ".");
                } else {
                    Log.d(Variables.SIMON, "did'nt receive answer from server.");
                    onResultListener.onResult(Variables.SERVER_NOT_RESPONDING);
                }
                if (answer > 199 && answer < 206) {
                    if (onResultListener != null) {
                        Log.d(Variables.SIMON, "activating onResult");
                        onResultListener.onResult(Integer.valueOf(result));
                    } else {
                        Log.d(Variables.SIMON, "onResultListener = null");
                    }
                }
                if (answer == Variables.LOAD_ALL_PROPERTIES) {
//                    while ((actuallyRead = bufferedReader.readLine()) != null) {
//                        response.append(actuallyRead);
//                    }
//                    result = String.valueOf(response);
//                    answer = Integer.valueOf(result);
                    Log.d(Variables.SIMON, propertyParts[1] + " properties incoming");

                    for (int i = 2; i < propertyParts.length; i++) {
                        String propertyPartsString = propertyParts[i];
                        String[] propertyInfoParts = propertyPartsString.split(Variables.AND);
                        for (int j = 0; j < propertyInfoParts.length; j++) {
                            property = new Property(
                                    //user
                                    propertyInfoParts[0],
                                    //address
                                    propertyInfoParts[1],
                                    //squareMeters
                                    propertyInfoParts[2],
                                    //bedrooms
                                    propertyInfoParts[3],
                                    //price
                                    propertyInfoParts[4]
//                                  //image
//                                    changeStringToByteArrayToBitmap(propertyInfoParts[5])
                            );
                            Log.d(Variables.SIMON, "created property object.");
//                            Log.d(Variables.SIMON, propertyInfoParts[5] + " - imgBitmap");
                            Log.d(Variables.SIMON, propertyInfoParts[0]
                                    + propertyInfoParts[1] + propertyInfoParts[2]
                                    + propertyInfoParts[3] + propertyInfoParts[4]);
                            properties.addPropertyToClientFromServer(property);
                            Log.d(Variables.SIMON, properties.checkIfKeyExists(propertyInfoParts[1]));
                        }


                    }


                }
                break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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
    }

//    public Bitmap changeStringToByteArrayToBitmap(String imgBytesString){
//        String[] imgBytesStringArray = imgBytesString.split(", ");
//        byte[] imgByteArray = new byte[imgBytesStringArray.length];
//        for (int i = 0; i < imgBytesStringArray.length; i++) {
//            imgByteArray[i] = Byte.parseByte(imgBytesStringArray[i]);
//        }
//        Bitmap imgBitmap = BitmapFactory.decodeByteArray(imgByteArray, 0, imgByteArray.length);
//        return imgBitmap;
//    }

}



