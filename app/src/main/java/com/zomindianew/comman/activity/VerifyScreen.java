package com.zomindianew.comman.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.goodiebag.pinview.Pinview;
import com.google.firebase.iid.FirebaseInstanceId;
import com.zomindianew.R;
import com.zomindianew.SPOTP;
import com.zomindianew.helper.Constants;
import com.zomindianew.helper.ErrorUtils;
import com.zomindianew.helper.MySharedPreferances;
import com.zomindianew.providernew.activity.EditProfileActivity;
import com.zomindianew.providernew.activity.HomeActivityProvider;
import com.zomindianew.user.activity.HomeActivityUser;
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

public class VerifyScreen extends BaseActivity implements View.OnClickListener {

    private TextView tv_register;
    private Pinview pinview;
    private TextView mobileNumberText;
    private TextView resendOTP;
    String mobileStr;
    private TextView textinput_counter;
    private TextView dontTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow(); 
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }
        setContentView(R.layout.activity_verify_screen);
        if (ContextCompat.checkSelfPermission(VerifyScreen.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(VerifyScreen.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
        }

        initView();
        setClicks();
        startSmsUserConsent();
        setOTPListener(new OTPListener() {
            @Override
            public void getOTP(String otp) {
                pinview.setValue(otp);
            }
        });
    }

    private void initView() {
        dontTV = findViewById(R.id.dontTV);
        textinput_counter = findViewById(R.id.textinput_counter);
        resendOTP = findViewById(R.id.resendOTP);
        mobileStr = MySharedPreferances.getInstance(VerifyScreen.this).getString("mobile");//getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE).getString("mobile","");//getIntent().getStringExtra("mobile");
        mobileNumberText = findViewById(R.id.mobileNumberText);
        mobileNumberText.setText("Enter OTP sent to " + mobileStr);
        tv_register = findViewById(R.id.tv_register);
        pinview = findViewById(R.id.pinview);
        if(SPOTP.getTime(this)>System.currentTimeMillis()-60*1000){
            pinview.setValue(SPOTP.getOTP(this));
        }

//        SmsReceiver.bindListener(new SmsListner() {
//            @Override
//            public void messageReceived(String messageText) {
//
//                pinview.setValue(messageText);
//                Log.e("hgjgjgj","get msg her"+messageText);
//                Log.d("hgjgjgj","get msg her"+messageText);
//                Toast.makeText(getApplicationContext(),messageText,Toast.LENGTH_SHORT).show();
//
////                pinview.se(messageText);
//                if (Constants.isInternetOn(VerifyScreen.this)) {
//                    if (!messageText.equals("")) {
//                        verifyAPI(messageText);
//                    }
//
//                } else {
//                    final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) VerifyScreen.this.findViewById(android.R.id.content)).getChildAt(0);
//                    showSnackbar(viewGroup, getResources().getString(R.string.no_internet), "Retry");
//                }
//            }
//        });
        timerMethod();
//           pinview.setValue(getIntent().getStringExtra("OTP"));
    }

    private void timerMethod() {

        new CountDownTimer(90000, 1000) {

            public void onTick(long millisUntilFinished) {
                textinput_counter.setVisibility(View.VISIBLE);
                resendOTP.setVisibility(View.GONE);
                dontTV.setVisibility(View.GONE);
                //  textinput_counter.setText("(0:" + millisUntilFinished / 1000 + ")");
                textinput_counter.setText("(" + millisUntilFinished / 1000 + " sec )");

                //here you can have your logic to set text to edittext
            }

            public void onFinish() {

                textinput_counter.setVisibility(View.INVISIBLE);
                resendOTP.setVisibility(View.VISIBLE);
                dontTV.setVisibility(View.VISIBLE);
                //textinput_counter.setText("done!");
            }

        }.start();
    }

    private void setClicks() {
        tv_register.setOnClickListener(this);
        resendOTP.setOnClickListener(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    public void verifyAPI(String otp) {
        String deviceToken = FirebaseInstanceId.getInstance().getToken();
        if (deviceToken == null) {
            deviceToken = FirebaseInstanceId.getInstance().getToken();
        }
        Api api = ApiFactory.getClient(VerifyScreen.this).create(Api.class);
        Call<ResponseBody> call;
        HashMap<String, String> stringHashMap = new HashMap<>();
        stringHashMap.put("otp", otp);
        stringHashMap.put("device_id", deviceToken);
        stringHashMap.put("device_type", "android");
        stringHashMap.put("mobile", getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE).getString("mobile", ""));


        call = api.verify(stringHashMap);
        Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT ------------------->>>>>" + " " + call.request().url());
        Log.e(Constants.LOG_CAT, "HEADERS : " + call.request().headers());
        Constants.showProgressDialog(VerifyScreen.this, Constants.LOADING);
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Constants.hideProgressDialog();

                try {
                    if (response.isSuccessful()) {
                        String output = ErrorUtils.getResponseBody(response);
                        JSONObject object = new JSONObject(output);
                        Log.d("tag", object.toString(1));

                        if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.TRUE)) {
                            Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT Response verify================>>>>>" + object.toString());
                            JSONObject jsonObject = object.optJSONObject("data");
                            appPreference.putString(Constants.ID, jsonObject.optString("id"));
                            appPreference.putString(Constants.USER_ROLE, jsonObject.optString("role"));
                            appPreference.putString(Constants.PROFILE_IMAGE, jsonObject.optString("profile"));
                            appPreference.putString(Constants.FIRST_NAME, jsonObject.optString("first_name"));
                            appPreference.putString(Constants.LAST_NAME, jsonObject.optString("last_name"));
                            appPreference.putString(Constants.ACCESS_TOKEN, jsonObject.optString("access_token"));
                            appPreference.putString(Constants.MOBILE, jsonObject.optString("mobile"));
                            appPreference.putString(Constants.DOB, jsonObject.optString("dob"));
                            appPreference.putString(Constants.EMAIL, jsonObject.optString("email"));
                            appPreference.putString(Constants.ADDRESS, jsonObject.optString("address"));
                            appPreference.putString(Constants.PROFILE_COMPLETE, jsonObject.optString("profile_complete"));
                            appPreference.putString(Constants.PROFILE_APPORVED, jsonObject.optString("is_apporved"));
                            appPreference.putString(Constants.USER_PROFILE_COMPLETE, jsonObject.optString("user_profile_complete"));
                            appPreference.putString(Constants.STATUS, jsonObject.optString("status"));
                            if (jsonObject.optString("status").equalsIgnoreCase("disapprove")) {
                                Intent intent = new Intent(VerifyScreen.this, UnderReviewActivty.class);
                                startActivity(intent);
                                finishAffinity();
                            } else if (appPreference.getString(Constants.PROFILE_COMPLETE).equalsIgnoreCase("false")) {
                                Intent intent = new Intent(VerifyScreen.this, EditProfileActivity.class);
                                intent.putExtra("COME_FROM", "verfiy");
                                intent.putExtra(Constants.ID, jsonObject.optString("id"));
                                intent.putExtra(Constants.USER_ROLE, jsonObject.optString("role"));
                                intent.putExtra("imageViewProfileStr", jsonObject.optString("profile"));
                                intent.putExtra("firstNameStr", jsonObject.optString("first_name"));
                                intent.putExtra("lastNameStr", jsonObject.optString("last_name"));
                                intent.putExtra("phoneStr", jsonObject.optString("mobile"));
                                intent.putExtra("dobStr", jsonObject.optString("dob"));
                                intent.putExtra("emailStr", jsonObject.optString("email"));
                                intent.putExtra("addressStr", jsonObject.optString("address"));
                                startActivity(intent);
                                finishAffinity();
                            } else {
                                if (jsonObject.optString("role").equalsIgnoreCase("provider")) {
                                    Intent intent = new Intent(VerifyScreen.this, HomeActivityProvider.class);
                                    startActivity(intent);
                                    finishAffinity();
                                } else {
                                    Intent intent = new Intent(VerifyScreen.this, HomeActivityUser.class);
                                    startActivity(intent);
                                    finishAffinity();
                                }
                            }


                        } else if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.FALSE)) {
                            Constants.showFalseMessage(object, VerifyScreen.this);
                        }
                    } else if (response.code() == 400 || response.code() == 500 || response.code() == 403 || response.code() == 404 || response.code() == 401) {
                        if (response.code() == 401) {
                            Constants.showSessionExpireAlert(VerifyScreen.this);
                        } else {
                            Constants.showToastAlert(ErrorUtils.getHtttpCodeError(response.code()), VerifyScreen.this);
                        }

                    } else {
                        String responseStr = ErrorUtils.getResponseBody(response);
                        JSONObject jsonObject = new JSONObject(responseStr);
                        Constants.showToastAlert(ErrorUtils.checkJosnErrorBody(jsonObject), VerifyScreen.this);
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


    public void resendOTP(String mobile) {

        Api api = ApiFactory.getClient(VerifyScreen.this).create(Api.class);
        Call<ResponseBody> call;
        HashMap<String, String> stringHashMap = new HashMap<>();
        stringHashMap.put("mobile", mobile);
        call = api.resendOTP(stringHashMap);
        Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT ------------------->>>>>" + " " + call.request().url());
        Log.e(Constants.LOG_CAT, "HEADERS : " + call.request().headers());
        Constants.showProgressDialog(VerifyScreen.this, Constants.LOADING);
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Constants.hideProgressDialog();

                try {
                    if (response.isSuccessful()) {
                        String output = ErrorUtils.getResponseBody(response);
                        JSONObject object = new JSONObject(output);
                        Log.d("tag", object.toString(1));

                        if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.TRUE)) {
                            Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT Response================>>>>>" + object.toString());
                            timerMethod();
                            Toast.makeText(VerifyScreen.this, "Resend OTP successfully!!", Toast.LENGTH_SHORT).show();

                        } else if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.FALSE)) {
                            Constants.showFalseMessage(object, VerifyScreen.this);
                        }
                    } else if (response.code() == 400 || response.code() == 500 || response.code() == 403 || response.code() == 404 || response.code() == 401) {
                        if (response.code() == 401) {
                            Constants.showSessionExpireAlert(VerifyScreen.this);
                        } else {
                            Constants.showToastAlert(ErrorUtils.getHtttpCodeError(response.code()), VerifyScreen.this);
                        }

                    } else {
                        String responseStr = ErrorUtils.getResponseBody(response);
                        JSONObject jsonObject = new JSONObject(responseStr);
                        Constants.showToastAlert(ErrorUtils.checkJosnErrorBody(jsonObject), VerifyScreen.this);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_register:
                String otp = pinview.getValue();
                if (otp.equalsIgnoreCase("")) {
                    Constants.showToastAlert(getResources().getString(R.string.Please_enter_opt), this);
                } else if (otp.length() < 6) {
                    Constants.showToastAlert(getResources().getString(R.string.Please_enter_opt_digits), this);
                } else {
                    if (Constants.isInternetOn(this)) {
                        verifyAPI(otp);
                    } else {
                        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) VerifyScreen.this.findViewById(android.R.id.content)).getChildAt(0);
                        showSnackbar(viewGroup, getResources().getString(R.string.no_internet), "Retry");
                    }
                }

                break;
            case R.id.resendOTP:
                if (Constants.isInternetOn(this)) {
                    resendOTP(mobileStr);
                } else {
                    final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) VerifyScreen.this.findViewById(android.R.id.content)).getChildAt(0);
                    showSnackbar(viewGroup, getResources().getString(R.string.no_internet), "Retry");
                }
                break;
            default:
                break;
        }
    }

}
