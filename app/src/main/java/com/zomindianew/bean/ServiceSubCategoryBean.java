package com.zomindianew.bean;

public class ServiceSubCategoryBean {

    public String id;
    public String name;
    public String sub_category_id;
    public String amount;
    public String created_at;
    public String updated_at;
    public String quntity;
    public String sid;
    public String sName;
    public String cid;
    public String categoryname;
    public String category_pic;
    public String rate_list;

    public String getRate_list() {
        return rate_list;
    }

    public void setRate_list(String rate_list) {
        this.rate_list = rate_list;
    }

    public String getCategory_pic() {
        return category_pic;
    }

    public void setCategory_pic(String category_pic) {
        this.category_pic = category_pic;
    }


    public String getInfodsc() {
        return infodsc;
    }

    public void setInfodsc(String infodsc) {
        this.infodsc = infodsc;
    }

    public String getRetingpoint() {
        return retingpoint;
    }

    public void setRetingpoint(String retingpoint) {
        this.retingpoint = retingpoint;
    }

    public String infodsc;
    public String retingpoint;


    boolean isSlected;
    boolean setBoolean;

    public ServiceSubCategoryBean(String id, String name, String sub_category_id, String amount, String created_at, String updated_at ,String sid,String sName,String cid,String categoryname) {
        this.id = id;
        this.name = name;
        this.sub_category_id = sub_category_id;
        this.amount = amount;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.sid = sid;
        this.sName = sName;
        this.cid = cid;
        this.categoryname = categoryname;
    }

    public ServiceSubCategoryBean() {

    }

    public boolean isSetBoolean() {
        return setBoolean;
    }

    public void setSetBoolean(boolean setBoolean) {
        this.setBoolean = setBoolean;
    }

    public boolean isSlected() {
        return isSlected;
    }

    public String getQuntity() {
        return quntity;
    }

    public void setQuntity(String quntity) {
        this.quntity = quntity;
    }

    public void setSlected(boolean slected) {
        isSlected = slected;
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


    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }
}
