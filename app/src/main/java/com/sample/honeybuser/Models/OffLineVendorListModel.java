package com.sample.honeybuser.Models;

/**
 * Created by Im033 on 10/7/2016.
 */

public class OffLineVendorListModel {
    private String vendor_id;
    private String name;
    private String phone_no;
    private String business_name;
    private String logo;
    private String latitude;
    private String longitude;
    private String distance;
    private String star_rating;
    private String rating_count;
    private String is_available;
    private String icon;
    private String time;
    private String new_vendor;
    private String follow;
    /*
    private String delivery;
    private String minimum_order;
    private String delivery_charge;*/


    private String static_store;

    public String getFollow() {
        return follow;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }


    public String getNew_vendor() {
        return new_vendor;
    }

    public void setNew_vendor(String new_vendor) {
        this.new_vendor = new_vendor;
    }


    public String getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(String vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getStatic_store() {
        return static_store;
    }

    public void setStatic_store(String static_store) {
        this.static_store = static_store;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
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

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getStar_rating() {
        return star_rating;
    }

    public void setStar_rating(String star_rating) {
        this.star_rating = star_rating;
    }

    public String getRating_count() {
        return rating_count;
    }

    public void setRating_count(String rating_count) {
        this.rating_count = rating_count;
    }

    public String getIs_available() {
        return is_available;
    }

    public void setIs_available(String is_available) {
        this.is_available = is_available;
    }

   /* public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getMinimum_order() {
        return minimum_order;
    }

    public void setMinimum_order(String minimum_order) {
        this.minimum_order = minimum_order;
    }

    public String getDelivery_charge() {
        return delivery_charge;
    }

    public void setDelivery_charge(String delivery_charge) {
        this.delivery_charge = delivery_charge;
    }*/

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
