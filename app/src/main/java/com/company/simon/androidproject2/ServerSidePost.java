package com.company.simon.androidproject2;


import android.graphics.Bitmap;
import android.util.Log;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;

class ServerSidePost {

    private OnResultListener onResultListener;
    private String result;
    private User user;
    private Property property;



    ServerSidePost(User user, OnResultListener onResultListener){
        this.user = user;
        this.onResultListener = onResultListener;
    }

    ServerSidePost(Property property){
        this.property = property;
    }

    void postToServer(final String type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                switch (type){
                    case Variables.USER:
                        try {
                            connectToServlet(determineUrl(Variables.USER),Variables.USER);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Variables.PROPERTY:
                        try {
                            connectToServlet(determineUrl(Variables.PROPERTY), Variables.PROPERTY);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Variables.REVIEW:

                        break;
                    default:
                        Log.d(Variables.SIMON, "did'nt receive viable type");
                        break;
                }
            }
        }).start();
    }


    private void connectToServlet(String urlString, String type){
        URL url = null;
        HttpURLConnection connection = null;
        InputStreamReader inputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        OutputStream outputStream = null;
        try {
            while (true) {
                url = new URL(Variables.HOST);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Accept-Language", "UTF-8");
                connection.setDoOutput(true);
                outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
                outputStreamWriter.write(urlString);
                outputStreamWriter.flush();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String actuallyRead = null;
                StringBuilder response = new StringBuilder();
                while ((actuallyRead = bufferedReader.readLine()) != null) {
                    response.append(actuallyRead);
                }
                String status = String.valueOf(response);
                int responseCode = connection.getResponseCode();
                if(status.isEmpty()){
                    Log.d(Variables.SIMON, "server did'nt respond");
                    activateOnResult(String.valueOf(Variables.SERVER_NOT_RESPONDING));
                }else {
                    result = status;
                    if (responseCode == 200){
                        activateOnResult(result);
                        break;
                    }else {
                        Log.d("Simon", String.valueOf(responseCode));
                    }
                }

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
    }


//    public void sendPictureToServer(final Bitmap imgBitmap){
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        imgBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//        final byte[] imgBytes = stream.toByteArray();
//        imgBitmap.recycle();
//        Log.d(Variables.SIMON, Arrays.toString(imgBytes) + "");
//        String urlString = null;
//
//        try {
//            urlString = determineUrl(Variables.IMAGE);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        final String finalUrlString = urlString;
//
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                URL url = null;
//                HttpURLConnection connection = null;
//                InputStreamReader inputStream = null;
//                OutputStreamWriter outputStreamWriter = null;
//                OutputStream outputStream = null;
//                try {
//                    while (true) {
//                        url = new URL(Variables.HOST);
//                        connection = (HttpURLConnection) url.openConnection();
//                        connection.setRequestMethod("POST");
//                        connection.setRequestProperty("Accept-Language", "UTF-8");
//                        connection.setDoOutput(true);
//                        outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
//                        outputStreamWriter.write(finalUrlString);
//                        outputStreamWriter.flush();
//
//                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                        String actuallyRead = null;
//                        StringBuilder response = new StringBuilder();
//                        while ((actuallyRead = bufferedReader.readLine()) != null) {
//                            response.append(actuallyRead);
//                        }
//                        String status = String.valueOf(response);
//                        int responseCode = connection.getResponseCode();
//                        if(status.isEmpty()){
//                            Log.d(Variables.SIMON, "server did'nt respond");
//                            activateOnResult(String.valueOf(Variables.SERVER_NOT_RESPONDING));
//                        }else {
//                            result = status;
//                            if (status.equals(String.valueOf(Variables.ADD_IMAGE_FAILED))){
//                                activateOnResult(result);
//                                if(status.equals(String.valueOf(Variables.IMAGE_ADDED))){
//                                    activateOnResult(result);
//                                    Log.d(Variables.SIMON, "image added");
//                                }
//                            }else {
//                                Log.d("Simon", String.valueOf(responseCode));
//                            }
//                        }
//                        break;
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }finally {
//                    if (inputStream != null) {
//                        try {
//                            inputStream.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    if (connection != null) {
//                        connection.disconnect();
//                    }
//                }
//            }
//        }).start();
//
//    }


    private void activateOnResult(String result){
            Log.d(Variables.SIMON, result);
            int taskResult = Integer.valueOf(result);
            switch (taskResult){
                case Variables.SIGN_UP_SUCCESSFUL:
                    onResultListener.onResult(Variables.SIGN_UP_SUCCESSFUL);
                    break;
                case Variables.USERNAME_TAKEN:
                    onResultListener.onResult(Variables.USERNAME_TAKEN);
                    break;
                case Variables.SIGN_UP_FAILED:
                    onResultListener.onResult(Variables.SIGN_UP_FAILED);
                    break;
                case Variables.PROPERTY_ADDED:
                    Log.d(Variables.SIMON, "result is " + Variables.PROPERTY_ADDED);
                    break;
                case Variables.PROPERTY_EXISTS:
                    Log.d(Variables.SIMON, "result is " + Variables.PROPERTY_EXISTS);
                    break;
                case Variables.PROPERTY_MISSING_INFO:
                    Log.d(Variables.SIMON, "result is " + Variables.PROPERTY_MISSING_INFO);
                    break;
                case Variables.PROPERTY_DOES_NOT_EXIST:
                    Log.d(Variables.SIMON, "result is " + Variables.PROPERTY_DOES_NOT_EXIST);
                    break;
                case Variables.SERVER_NOT_RESPONDING:
                    Log.d(Variables.SIMON, "result is " + Variables.SERVER_NOT_RESPONDING);
                    break;
                case Variables.UNRECOGNIZABLE_REQUEST:
                    Log.d(Variables.SIMON, "result is " + Variables.UNRECOGNIZABLE_REQUEST);
                    break;
//                case Variables.IMAGE_ADDED:
//                    Log.d(Variables.SIMON, "result is " + Variables.IMAGE_ADDED);
//                    break;
//                case Variables.ADD_IMAGE_FAILED:
//                    Log.d(Variables.SIMON, "result is " + Variables.ADD_IMAGE_FAILED);
//                    break;
            }

    }

    public String determineUrl(String action) throws UnsupportedEncodingException {
        String parameter = "";
        switch (action){
            case Variables.USER:
                parameter = userURLBuilder();
                break;
            case Variables.PROPERTY:
                parameter = propertyURLBuilder();
                break;
            case Variables.REVIEW:
                parameter = reviewURLBuilder();
                break;
//            case Variables.IMAGE:
//                parameter = imageURLBuilder();
//                break;
        }
        return parameter;
    }

    public String userURLBuilder() throws UnsupportedEncodingException {
        StringBuilder parameters = new StringBuilder("action=");
        parameters.append(URLEncoder.encode(String.valueOf(Variables.ADD_USER), "UTF-8"));
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

    public String propertyURLBuilder() throws UnsupportedEncodingException {
        StringBuilder parameters = new StringBuilder("action=");
        parameters.append(URLEncoder.encode(String.valueOf(Variables.ADD_PROPERTY), "UTF-8"));
        parameters.append("&username=");
        parameters.append(URLEncoder.encode(property.getUser(), "UTF-8"));
        parameters.append("&address=");
        parameters.append(URLEncoder.encode(property.getAddress(), "UTF-8"));
        parameters.append("&squaremeters=");
        parameters.append(URLEncoder.encode(property.getSquareMeters(), "UTF-8"));
        parameters.append("&bedrooms=");
        parameters.append(URLEncoder.encode(property.getBedrooms(), "UTF-8"));
        parameters.append("&price=");
        parameters.append(URLEncoder.encode(property.getPrice(), "UTF-8"));

        return String.valueOf(parameters);
    }

    public String reviewURLBuilder(){


        return null;
    }

//    private String imageURLBuilder() throws UnsupportedEncodingException {
//        StringBuilder parameters = new StringBuilder("action=");
//        parameters.append(URLEncoder.encode(String.valueOf(Variables.ADD_IMAGE), "UTF-8"));
//        parameters.append("&address=");
//        parameters.append(URLEncoder.encode(property.getAddress(), "UTF-8"));
//        parameters.append("&image=");
//        parameters.append(URLEncoder.encode(String.valueOf(property.getImgPropertyBitmap()), "UTF-8"));
//
//        return String.valueOf(parameters);
//    }


}
