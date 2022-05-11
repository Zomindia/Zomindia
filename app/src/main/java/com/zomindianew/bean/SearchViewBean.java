package com.zomindianew.bean;

public class SearchViewBean {
    private String id;
    private String svcategory_name;
    private String svstatus;
    private String svcategory_icon;
    private String svcreated_at;
    boolean svsetSlected;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSvcategory_name() {
        return svcategory_name;
    }

    public void setSvcategory_name(String svcategory_name) {
        this.svcategory_name = svcategory_name;
    }

    public String getSvstatus() {
        return svstatus;
    }

    public void setSvstatus(String svstatus) {
        this.svstatus = svstatus;
    }

    public String getSvcategory_icon() {
        return svcategory_icon;
    }

    public void setSvcategory_icon(String svcategory_icon) {
        this.svcategory_icon = svcategory_icon;
    }

    public String getSvcreated_at() {
        return svcreated_at;
    }

    public void setSvcreated_at(String svcreated_at) {
        this.svcreated_at = svcreated_at;
    }

    public boolean isSvsetSlected() {
        return svsetSlected;
    }

    public void setSvsetSlected(boolean svsetSlected) {
        this.svsetSlected = svsetSlected;
    }
}
