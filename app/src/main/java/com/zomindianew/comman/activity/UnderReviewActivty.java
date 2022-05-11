package com.zomindianew.comman.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;

import com.zomindianew.R;

import com.zomindianew.helper.Constants;
import com.zomindianew.helper.ErrorUtils;
import com.zomindianew.helper.MySharedPreferances;
import com.zomindianew.providernew.activity.HomeActivityProvider;
import com.zomindianew.user.activity.HomeActivityUser;
import com.zomindianew.webservice.Api;
import com.zomindianew.webservice.ApiFactory;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class UnderReviewActivty extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_under_review);
        profileAPI();

    }


    public void profileAPI() {
        Api api = ApiFactory.getClient(UnderReviewActivty.this).create(Api.class);
        Call<ResponseBody> call;
        call = api.userDetail(MySharedPreferances.getInstance(UnderReviewActivty.this).getString(Constants.ACCESS_TOKEN));
        Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT ------------------->>>>>" + " " + call.request().url());
        Log.e(Constants.LOG_CAT, "HEADERS : " + call.request().headers());
        Constants.showProgressDialog(UnderReviewActivty.this, Constants.LOADING);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Constants.hideProgressDialog();

                try {
                    if (response.isSuccessful()) {
                        String output = ErrorUtils.getResponseBody(response);
                        JSONObject object = new JSONObject(output);
                        Log.d("tag", object.toString(1));

                        if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.TRUE)) {
                            JSONObject jsonObject = object.optJSONObject("data");
                            appPreference.putString(Constants.STATUS, jsonObject.optString("status"));
                            MySharedPreferances.getInstance(UnderReviewActivty.this).putString(Constants.PROFILE_COMPLETE, jsonObject.optString("profile_complete"));
                            MySharedPreferances.getInstance(UnderReviewActivty.this).putString(Constants.PROFILE_APPORVED, jsonObject.optString("is_apporved"));
                            if (jsonObject.optString("is_apporved").equalsIgnoreCase("false")) {
//                                Intent intent = new Intent(UnderReviewActivty.this, UnderReviewActivty.class);
//                                startActivity(intent);
//                                finish();
                            } else {
                                if (jsonObject.optString("role").equalsIgnoreCase("provider")) {
                                    Intent intent = new Intent(UnderReviewActivty.this, HomeActivityProvider.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Intent intent = new Intent(UnderReviewActivty.this, HomeActivityUser.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }


                        } else if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.FALSE)) {
                            Constants.showFalseMessage(object, UnderReviewActivty.this);
                        }
                    } else if (response.code() == 400 || response.code() == 500 || response.code() == 403 || response.code() == 404 || response.code() == 401) {
                        if (response.code() == 401) {
                            Constants.showSessionExpireAlert(UnderReviewActivty.this);
                        } else {
                            Constants.showToastAlert(ErrorUtils.getHtttpCodeError(response.code()), UnderReviewActivty.this);
                        }

                    } else {
                        String responseStr = ErrorUtils.getResponseBody(response);
                        JSONObject jsonObject = new JSONObject(responseStr);
                        Constants.showToastAlert(ErrorUtils.checkJosnErrorBody(jsonObject), UnderReviewActivty.this);
                    }
                } catch (JSONException e) {
                    Constants.hideProgressDialog();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Constants.hideProgressDialog();
            }
        });


    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }


}
