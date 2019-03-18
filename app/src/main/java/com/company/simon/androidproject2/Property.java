package com.company.simon.androidproject2;


import android.graphics.Bitmap;
import android.support.annotation.NonNull;

public class Property {

    private String user;
    private String address;
    private String price;
    private String bedrooms;
    private String squareMeters;
    private String timeStamp;
    private int flag;
    private Bitmap imgPropertyBitmap;



    public Property(String user, String address, String squareMeters, String bedrooms, String price, Bitmap imgPropertyBitmap) {
        this.user = user;
        this.address = address;
        this.bedrooms = bedrooms;
        this.squareMeters = squareMeters;
        this.price = price;
        this.imgPropertyBitmap = imgPropertyBitmap;
    }

    public Property(String user, String address, String squareMeters, String bedrooms, String price ) {
        this.user = user;
        this.address = address;
        this.bedrooms = bedrooms;
        this.squareMeters = squareMeters;
        this.price = price;
    }

    public Property(String user, String address, String squareMeters, String bedrooms, String price, int flag){
        this.user = user;
        this.address = address;
        this.price = price;
        this.bedrooms = bedrooms;
        this.squareMeters = squareMeters;
        this.flag = flag;
    }

    public Bitmap getImgPropertyBitmap() {
        return imgPropertyBitmap;
    }

    public void setImgPropertyBitmap(Bitmap imgPropertyBitmap) {
        this.imgPropertyBitmap = imgPropertyBitmap;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrice() {
        return price;
    }

    public String setPrice(String priceNum) {
        StringBuilder j = new StringBuilder();
        j.append("$");
        int x = Integer.valueOf(priceNum);
        String number = String.valueOf(x);
        for (int i = 0; i < number.length(); i++) {
            if(number.length() > 3 || number.length() < 7){
                if(number.length() == 4 && i == 1){
                    j.append(",");
                }
                if(number.length() == 5 && i == 2){
                    j.append(",");
                }
                if(number.length() == 6 && i == 3){
                    j.append(",");
                }
            }
            if(number.length() > 6 && number.length() < 10){
                if(number.length() == 7 && (i == 4 || i == 1)){
                    j.append(",");
                }
                if(number.length() == 8 && (i == 5 || i == 2)){
                    j.append(",");
                }
                if(number.length() == 9 && (i == 6 || i == 3)){
                    j.append(",");
                }
            }
            x = Character.digit(number.charAt(i), 10);
            j.append(String.valueOf(x));
        }
        return String.valueOf(j);
    }

    private String setAmountOfBedrooms(int bedrooms) {
        String bedroomString = String.valueOf(bedrooms);
        bedroomString += "bd";
        return bedroomString;
    }

    public void setBedrooms(String bedrooms) {
        this.bedrooms = bedrooms;
    }

    public String getBedrooms() {
        return bedrooms;
    }

    public String getSquareMeters() {
        return squareMeters;
    }

    public String setSquareMeters(int squareMeters) {
        String squareMetersString = String.valueOf(squareMeters);
        squareMetersString += "sqmt.";
        return squareMetersString;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @NonNull
    @Override
    public String toString() {
        return this.user + Variables.AND + this.address + Variables.AND +
                this.bedrooms + Variables.AND + this.squareMeters +
                Variables.AND + this.price + Variables.AND + this.flag;
    }
}
