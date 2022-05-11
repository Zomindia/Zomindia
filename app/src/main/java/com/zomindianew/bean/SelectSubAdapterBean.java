package com.zomindianew.bean;

public class SelectSubAdapterBean  {
    public String id;
    public String name;
    public String sub_category_id;
    public String amount;
    public String created_at;
    public String updated_at;
    public String quntity;
    boolean isSlected;
    boolean setBoolean;


    public SelectSubAdapterBean(String id, String name, String sub_category_id, String amount, String created_at, String updated_at) {
        this.id = id;
        this.name = name;
        this.sub_category_id = sub_category_id;
        this.amount = amount;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public SelectSubAdapterBean() {

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSub_category_id() {
        return sub_category_id;
    }

    public void setSub_category_id(String sub_category_id) {
        this.sub_category_id = sub_category_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getQuntity() {
        return quntity;
    }

    public void setQuntity(String quntity) {
        this.quntity = quntity;
    }

    public boolean isSlected() {
        return isSlected;
    }

    public void setSlected(boolean slected) {
        isSlected = slected;
    }

    public boolean isSetBoolean() {
        return setBoolean;
    }

    public void setSetBoolean(boolean setBoolean) {
        this.setBoolean = setBoolean;
    }
}
