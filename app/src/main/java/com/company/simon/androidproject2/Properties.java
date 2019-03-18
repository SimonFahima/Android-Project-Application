package com.company.simon.androidproject2;

import java.util.HashMap;
import java.util.Map;

public class Properties {

    private static Properties properties;
    public Map<String, Property> propertyMap;

    Properties(){
        propertyMap = new HashMap<>();
    }

    public static Properties getProperties(){
        if(properties == null){
            properties = new Properties();
        }
        return properties;
    }

    public void getProperty(){

    }

    public boolean addPropertyToServerAndClient(Property property){
        if(propertyMap.containsKey(property.getAddress())){
            return false;
        }else {
            propertyMap.put(property.getAddress(), property);
            ServerSidePost serverSidePostProperty = new ServerSidePost(property);
            serverSidePostProperty.postToServer(Variables.PROPERTY);
//            serverSidePostProperty.sendPictureToServer(property.getImgPropertyBitmap());
            return true;
        }
    }

    public void addPropertyToClientFromServer(Property property){
        if(propertyMap.containsKey(property.getAddress())){
            return;
        }else {
            propertyMap.put(property.getAddress(), property);
        }
    }

    public String checkIfKeyExists(String key){
        if(propertyMap.containsKey(key)){
            return propertyMap.get(key).getAddress() + " exists in market";
        }else {
            return "property does not exist";
        }
    }

    public int getPropertyMapSize(){
        return propertyMap.size();
    }


}
