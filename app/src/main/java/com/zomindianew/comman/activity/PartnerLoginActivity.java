package com.zomindianew.comman.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.iid.FirebaseInstanceId;
import com.zomindianew.R;
import com.zomindianew.helper.Constants;
import com.zomindianew.helper.ErrorUtils;
import com.zomindianew.helper.MySharedPreferances;
import com.zomindianew.providernew.activity.HomeActivityProvider;
import com.zomindianew.webservice.Api;
import com.zomindianew.webservice.ApiFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PartnerLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etMobile;
    private EditText etPassword;
    private TextView tvLogin;
    private TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_login);
        intView();
        setClick();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    private void intView() {
        etPassword = findViewById(R.id.etPLoginPassword);
        etMobile = findViewById(R.id.etPLoginMobile);
        tvRegister = findViewById(R.id.tvPLoginRegister);
        tvLogin = findViewById(R.id.tvPLoginLogin);
    }

    private void setClick() {
        tvLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tvPLoginLogin:
                String mobile = etMobile.getText().toString();
                String password = etPassword.getText().toString();

                if (mobile.length() == 0) {
                    Constants.showToastAlert(getResources().getString(R.string.enter_phone_number), this);
                } else if (mobile.length() != 10) {
                    Constants.showToastAlert(getResources().getString(R.string.Please_enter_valid_number), this);
                } else if (password.trim().length() == 0) {
                    Constants.showToastAlert("Password is not entered", this);
                } else {
                    signINAPI(mobile, password);
                }
                break;

            case R.id.tvPLoginRegister:
                Intent intent = new Intent(PartnerLoginActivity.this, PartnerSignupActivity.class);
                startActivity(intent);
                finishAffinity();
                break;
            default:
                break;
        }
    }


    public void signINAPI(String mobile, String password) {
        Api api = ApiFactory.getClient(PartnerLoginActivity.this).create(Api.class);
        Call<ResponseBody> call;
        HashMap<String, String> stringHashMap = new HashMap<>();
        stringHashMap.put("password", password);
        stringHashMap.put("mobile", mobile);
        stringHashMap.put("device_id", FirebaseInstanceId.getInstance().getToken());
        stringHashMap.put("device_type", "android");
        call = api.loginProvider(stringHashMap);
        Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT ------------------->>>>>" + " " + call.request().url());
        Log.e(Constants.LOG_CAT, "HEADERS : " + call.request().headers());
        Constants.showProgressDialog(PartnerLoginActivity.this, Constants.LOADING);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Constants.hideProgressDialog();

                try {
                    if (response.isSuccessful()) {
                        String output = ErrorUtils.getResponseBody(response);
                        JSONObject jsonObject = new JSONObject(output);

                        SharedPreferences sharedpreferences = getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);
                        JSONObject jsonObject1 = jsonObject.optJSONObject("data");
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("mobile", jsonObject1.getString("mobile"));
                        editor.putString("uname", jsonObject1.getString("first_name"));
                        editor.putString("email", jsonObject1.getString("email"));
                        editor.putString("id", jsonObject1.getString("id"));
                        editor.putString("accesstoken", jsonObject1.getString("access_token"));
                        editor.apply();

                        MySharedPreferances appPreference = MySharedPreferances.getInstance(PartnerLoginActivity.this);
                        appPreference.putString(Constants.ID, jsonObject1.optString("id"));
                        appPreference.putString(Constants.USER_ROLE, jsonObject1.optString("role"));
                        appPreference.putString(Constants.PROFILE_IMAGE, jsonObject1.optString("profile"));
                        appPreference.putString(Constants.FIRST_NAME, jsonObject1.optString("first_name"));
                        appPreference.putString(Constants.LAST_NAME, jsonObject1.optString("last_name"));
                        appPreference.putString(Constants.ACCESS_TOKEN, jsonObject1.optString("access_token"));
                        appPreference.putString(Constants.MOBILE, jsonObject1.optString("mobile"));
                        appPreference.putString(Constants.DOB, jsonObject1.optString("dob"));
                        appPreference.putString(Constants.EMAIL, jsonObject1.optString("email"));
//                            appPreference.putString(Constants.ADDRESS, jsonObject1.optString("email"));
                        appPreference.putString(Constants.PROFILE_COMPLETE, jsonObject1.optString("profile_complete"));
                        appPreference.putString(Constants.PROFILE_APPORVED, jsonObject1.optString("is_apporved"));
                        appPreference.putString(Constants.USER_PROFILE_COMPLETE, jsonObject1.optString("user_profile_complete"));
                        appPreference.putString(Constants.STATUS, jsonObject1.optString("status"));
                        if (jsonObject1.optString("status").equalsIgnoreCase("disapprove")) {
                            Intent intent = new Intent(PartnerLoginActivity.this, UnderReviewActivty.class);
                            startActivity(intent);
                            finishAffinity();
                        } else if (jsonObject1.optString("is_mobile_verified").equalsIgnoreCase("0")) {
                            Intent intent = new Intent(PartnerLoginActivity.this, VerifyScreen.class);
                            startActivity(intent);
                            finishAffinity();
                        } else if (jsonObject.optString("success").equals("true")) {
                            Intent intent1 = new Intent(PartnerLoginActivity.this, HomeActivityProvider.class);
                            startActivity(intent1);
                            finishAffinity();
                            //{"success":true,"data":{"first_name":"zutest","email":"zutest@mailinator.com","mobile":"9828012345","role":"user","otp":123456,"status":"disapprove","updated_at":"2021-03-13 00:48:31","created_at":"2021-03-13 00:48:31","id":315,"categorys":[],"profile_complete":true,"is_apporved":true,"average_rating":0,"user_profile_complete":false},"message":""}
                        } else if (jsonObject.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.FALSE)) {
                            Constants.showFalseMessage(jsonObject, PartnerLoginActivity.this);
                        }
                    } else if (response.code() == 400 || response.code() == 500 || response.code() == 403 || response.code() == 404 || response.code() == 401) {
                        if (response.code() == 401) {
                            Constants.showSessionExpireAlert(PartnerLoginActivity.this);
                        } else {
                            Constants.showToastAlert(ErrorUtils.getHtttpCodeError(response.code()), PartnerLoginActivity.this);
                        }

                    } else {
                        String responseStr = ErrorUtils.getResponseBody(response);
                        JSONObject jsonObject = new JSONObject(responseStr);
                        Constants.showToastAlert(ErrorUtils.checkJosnErrorBody(jsonObject), PartnerLoginActivity.this);
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
}