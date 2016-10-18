package com.sample.honeybuser.Models;

/**
 * Created by IM028 on 7/4/16.
 */
public class ChangeAddress {
    private String address_id;
    private String user_id;
    private String title;
    private String address;
    private String latitude;
    private String longitude;
    //private String is_default;

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

//    public String getIs_default() {
//        return is_default;
//    }

//    public void setIs_default(String is_default) {
//        this.is_default = is_default;
//    }
}
