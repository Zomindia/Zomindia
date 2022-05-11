package com.zomindianew.bean;

public class CategoryBean {

    private String id;
    private String category_name;
    private String status;
    private String category_icon;
    private String created_at;
    boolean setSlected;


    public CategoryBean(String id, String category_name, String status, String category_icon, String created_at) {
        this.id = id;
        this.category_name = category_name;
        this.status = status;
        this.category_icon = category_icon;
        this.created_at = created_at;
    }

    public CategoryBean() {

    }

    public boolean isSetSlected() {
        return setSlected;
    }

    public void setSetSlected(boolean setSlected) {
        this.setSlected = setSlected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory_icon() {
        return category_icon;
    }

    public void setCategory_icon(String category_icon) {
        this.category_icon = category_icon;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
