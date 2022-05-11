package com.zomindianew.bean;

public class NotificationBean {


    String id;
    String to_id;
    String from_id;
    String booking_id;
    String type;
    String message;
    String is_read;
    String created_at;
    String fromImage;
    String fromName;
    String category;
    String subcategory;


    public NotificationBean(String id, String to_id, String from_id, String booking_id, String type, String message, String is_read, String created_at, String fromImage, String fromName, String category, String subcategory) {
        this.id = id;
        this.to_id = to_id;
        this.from_id = from_id;
        this.type = type;
        this.booking_id = booking_id;

        this.message = message;
        this.is_read = is_read;
        this.created_at = created_at;
        this.fromImage = fromImage;
        this.fromName = fromName;
        this.category = category;
        this.subcategory = subcategory;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromImage() {
        return fromImage;
    }

    public void setFromImage(String fromImage) {
        this.fromImage = fromImage;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getTo_id() {
        return to_id;
    }

    public void setTo_id(String to_id) {
        this.to_id = to_id;
    }

    public String getFrom_id() {
        return from_id;
    }

    public void setFrom_id(String from_id) {
        this.from_id = from_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIs_read() {
        return is_read;
    }

    public void setIs_read(String is_read) {
        this.is_read = is_read;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }


}

