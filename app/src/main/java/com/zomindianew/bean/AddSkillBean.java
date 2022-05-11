package com.zomindianew.bean;

public class AddSkillBean {

    private String subCAtegoryID;
    private String categoryID;
    private String category_name;
    private String sub_category_name;

    public AddSkillBean(String subCAtegoryID, String categoryID, String category_name, String sub_category_name) {
        this.subCAtegoryID = subCAtegoryID;
        this.categoryID = categoryID;
        this.category_name = category_name;
        this.sub_category_name = sub_category_name;
    }

    public AddSkillBean() {

    }

    public String getSubCAtegoryID() {
        return subCAtegoryID;
    }

    public void setSubCAtegoryID(String subCAtegoryID) {
        this.subCAtegoryID = subCAtegoryID;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getSub_category_name() {
        return sub_category_name;
    }

    public void setSub_category_name(String sub_category_name) {
        this.sub_category_name = sub_category_name;
    }
}
