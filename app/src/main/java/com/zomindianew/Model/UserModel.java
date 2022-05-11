package com.zomindianew.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserModel {
    @SerializedName("Email")
    @Expose
    private String Email;
    @SerializedName("Pass")
    @Expose
    private String Pass;

}
