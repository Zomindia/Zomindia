package com.zomindianew.comman.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.zomindianew.R;
import com.zomindianew.helper.Constants;
import com.zomindianew.helper.ErrorUtils;
import com.zomindianew.webservice.Api;
import com.zomindianew.webservice.ApiFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;


public class PartnerSignupActivity extends BaseActivity implements View.OnClickListener {
    private EditText etName;
    private EditText etEmail;
    private EditText etMobile;
    private EditText etPassword;
    private EditText etCPassword;
    private TextView tvLogin;
    private TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_signup);
        intView();
        setClick();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    private void intView() {
        etPassword = findViewById(R.id.etPSUPassword);
        etCPassword = findViewById(R.id.etPSUCPassword);
        etName = findViewById(R.id.etPSUName);
        etEmail = findViewById(R.id.etPSUEmail);
        etMobile = findViewById(R.id.etPSUMobile);
        tvRegister = findViewById(R.id.tvPSURegister);
        tvLogin = findViewById(R.id.tvPSULogin);
    }

    private void setClick() {
        tvLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tvPSURegister:
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String mobile = etMobile.getText().toString();
                String password = etPassword.getText().toString();
                String cPassword = etCPassword.getText().toString();

                if (name.equalsIgnoreCase("")) {
                    Constants.showToastAlert(getResources().getString(R.string.please_enter_name), this);
                } else if (email.equals("")) {
                    Constants.showToastAlert(getResources().getString(R.string.please_enter_email_address), this);
                } else if (!Constants.isValidEmail(email)) {
                    Constants.showToastAlert(getResources().getString(R.string.enter_valid_email_id), this);
                } else if (mobile.length() == 0) {
                    Constants.showToastAlert(getResources().getString(R.string.enter_phone_number), this);
                } else if (mobile.length() != 10) {
                    Constants.showToastAlert(getResources().getString(R.string.Please_enter_valid_number), this);
                } else if (password.trim().length() == 0) {
                    Constants.showToastAlert("Password is not entered", this);
                } else if (!password.equals(cPassword)) {
                    Constants.showToastAlert("Password is not matched", this);
                } else {
                    signUPAPI(name,email,mobile,password);
                }
                break;

            case R.id.tvPSULogin:
                Intent intent = new Intent(PartnerSignupActivity.this, PartnerLoginActivity.class);
                startActivity(intent);
                finishAffinity();
                break;
            default:
                break;
        }
    }


    public void signUPAPI(final String name, String email, String mobile,String password) {
        startSmsUserConsent();
        Api api = ApiFactory.getClient(PartnerSignupActivity.this).create(Api.class);
        Call<ResponseBody> call;
        HashMap<String, String> stringHashMap = new HashMap<>();
        stringHashMap.put("email", email);
        stringHashMap.put("first_name", name);
        stringHashMap.put("password", password);
        stringHashMap.put("mobile", mobile);
        stringHashMap.put("device_id", FirebaseInstanceId.getInstance().getToken());
        stringHashMap.put("device_type", "android");
        call = api.signupProvider(stringHashMap);
        Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT ------------------->>>>>" + " " + call.request().url());
        Log.e(Constants.LOG_CAT, "HEADERS : " + call.request().headers());
       /* Intent intent = new Intent(PartnerSignupActivity.this,VerifyScreen.class);
        startActivity(intent);
        finish();*/
        Constants.showProgressDialog(PartnerSignupActivity.this, Constants.LOADING);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Constants.hideProgressDialog();

                try {
                    if (response.isSuccessful()) {
                        String output = ErrorUtils.getResponseBody(response);
                        JSONObject jsonObject = new JSONObject(output);


                        if (jsonObject.optString("success").equals("true")) {
                            JSONObject jsonObject1 = jsonObject.optJSONObject("data");
                            SharedPreferences sharedpreferences = getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("mobile", jsonObject1.optString("mobile"));
                            editor.putString("uname", jsonObject1.optString("first_name"));
                            editor.putString("email", jsonObject1.optString("email"));
                            editor.putString("id", jsonObject1.optString("id"));
                            editor.putString("accesstoken", jsonObject1.optString("access_token"));
                            editor.putString("role", jsonObject1.optString("role"));
                            editor.putString("status", jsonObject1.optString("status"));
                            editor.putBoolean("profile_complete", jsonObject1.optBoolean("profile_complete"));
                            editor.putBoolean("user_profile_complete", jsonObject1.optBoolean("user_profile_complete"));
                            editor.putBoolean("is_apporved", jsonObject1.optBoolean("is_apporved"));
                            editor.apply();

                            //{"success":true,"data":{"first_name":"zutest","email":"zutest@mailinator.com","mobile":"9828012345","role":"user","otp":123456,"status":"disapprove","updated_at":"2021-03-13 00:48:31","created_at":"2021-03-13 00:48:31","id":315,"categorys":[],"profile_complete":true,"is_apporved":true,"average_rating":0,"user_profile_complete":false},"message":""}
                            Intent intent = new Intent(PartnerSignupActivity.this,VerifyScreen.class);
                            startActivity(intent);
                            finish();
                        } else if (jsonObject.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.FALSE)) {
                            Constants.showFalseMessage(jsonObject, PartnerSignupActivity.this);
                        }
                    } else if (response.code() == 400 || response.code() == 500 || response.code() == 403 || response.code() == 404 || response.code() == 401) {
                        if (response.code() == 401) {
                            Constants.showSessionExpireAlert(PartnerSignupActivity.this);
                        } else {
                            Constants.showToastAlert(ErrorUtils.getHtttpCodeError(response.code()), PartnerSignupActivity.this);
                        }

                    } else {
                        String responseStr = ErrorUtils.getResponseBody(response);
                        JSONObject jsonObject = new JSONObject(responseStr);
                        Constants.showToastAlert(ErrorUtils.checkJosnErrorBody(jsonObject), PartnerSignupActivity.this);
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
