package com.zomindianew.comman.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.iid.FirebaseInstanceId;
import com.zomindianew.R;
import com.zomindianew.helper.Constants;
import com.zomindianew.helper.ErrorUtils;
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

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_register;
    private RelativeLayout customerRelative;
    private RelativeLayout serviceRelative;
    private String userRoleStr = "user";
    private EditText phoneNumBer;
    private TextView providerSignTextView;
    private TextView termsTv;
    private CheckBox termsConditionCheck;
    EditText editTextemail, editTextpass;
    TextView textView_login, textView_registered,partner_signup,loginbymobile,forgetpass;
    String get_mail, get_pass, getres, getstatus  ,device_id;
    String devicetype="android";

    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow();
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }
        setContentView(R.layout.activity_login);
        intView();
        editTextemail = (EditText) findViewById(R.id.edt_email);
        editTextpass = (EditText) findViewById(R.id.edt_pass);
        textView_login = (TextView) findViewById(R.id.btn_wel_login);
        textView_registered = (TextView) findViewById(R.id.tv_rgister);
        forgetpass = (TextView) findViewById(R.id.forget_password);


         //device_id = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
        device_id = FirebaseInstanceId.getInstance().getToken();

        AppEventsLogger.newLogger(this).logEvent("Login Event");


        textView_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editTextemail.getText().toString().trim().length() == 0) {
                    editTextemail.setError("Username is not entered");
                    editTextemail.requestFocus();
                }
                if (editTextpass.getText().toString().trim().length() == 0) {
                    editTextpass.setError("Password is not entered");
                    editTextpass.requestFocus();
                } else {
                    get_mail = editTextemail.getText().toString().trim();
                    get_pass = editTextpass.getText().toString().trim();
                    logInApi(get_mail, get_pass,device_id,devicetype);

                }

            }
        });

        textView_registered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);

            }
        });

        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);

            }
        });

//        setClick();


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }


    private void intView() {
//        termsConditionCheck = findViewById(R.id.termsConditionCheck);
//        termsTv = findViewById(R.id.termsTv);
//        providerSignTextView = findViewById(R.id.providerSignTextView);
//        customerRelative = findViewById(R.id.customerRelative);
        serviceRelative = findViewById(R.id.serviceRelative);
//        tv_register = findViewById(R.id.tv_register);

    }

    private void setClick() {
//        termsTv.setOnClickListener(this);
        providerSignTextView.setOnClickListener(this);
        phoneNumBer = findViewById(R.id.phoneNumBer);
        tv_register.setOnClickListener(this);
        customerRelative.setOnClickListener(this);
        serviceRelative.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

//            case R.id.customerRelative:
//                userRoleStr = "user";
//                customerRelative.setBackgroundResource(R.drawable.blue_bg_circle);
//                serviceRelative.setBackgroundResource(0);
//                break;
            case R.id.serviceRelative:
                userRoleStr = "provider";
                serviceRelative.setBackgroundResource(R.drawable.blue_bg_circle);
                customerRelative.setBackgroundResource(0);
                break;

            case R.id.tv_register:
                String mobileStr = phoneNumBer.getText().toString();
                if (mobileStr.equalsIgnoreCase("")) {
                    Constants.showToastAlert(getResources().getString(R.string.Please_enter_mobile_number), this);
                } else if (mobileStr.length() < 10) {
                    Constants.showToastAlert(getResources().getString(R.string.Please_enter_valid_number), this);
                } else if (!termsConditionCheck.isChecked()) {
                    Constants.showToastAlert(getResources().getString(R.string.please_check_terms_conditions), this);
                } else {
                    if (Constants.isInternetOn(this)) {
//                        logInApi(mobileStr, "");
                    } else {
                        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) LoginActivity.this.findViewById(android.R.id.content)).getChildAt(0);
                        showSnackbar(viewGroup, getResources().getString(R.string.no_internet), "Retry");
                    }
                }
                break;
//            case R.id.providerSignTextView:
//                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.termsTv:
//                String url = "http://service-provider.zomindia.com/terms-condition";
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                startActivity(browserIntent);
//                break;
            default:
                break;
        }
    }


    public void logInApi(final String get_mail, String get_pass,String device_id,String devicetype) {
        Api api = ApiFactory.getClient(LoginActivity.this).create(Api.class);
        Call<ResponseBody> call;
        HashMap<String, String> stringHashMap = new HashMap<>();
        stringHashMap.put("email", get_mail);
        stringHashMap.put("password", get_pass);
        stringHashMap.put("device_id", device_id);
        stringHashMap.put("device_type", devicetype);
        call = api.loginUserWithEmail(stringHashMap);
        Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT ------------------->>>>>" + " " + call.request().url());
        Log.e(Constants.LOG_CAT, "HEADERS : " + call.request().headers());
        Constants.showProgressDialog(LoginActivity.this, Constants.LOADING);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Constants.hideProgressDialog();
                getres = response.toString();

                if (response.isSuccessful()) {
                    String output = ErrorUtils.getResponseBody(response);
                    try {
                        JSONObject jsonObject = new JSONObject(output);
                        if (jsonObject.getString("success").equals("true")) {
                            JSONObject offerObject = jsonObject.getJSONObject("data");
//                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//                            startActivity(intent);

                            sharedpreferences = getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("mobile", offerObject.getString("mobile"));
                            editor.putString("uname", offerObject.getString("first_name"));
                            editor.putString("email", offerObject.getString("email"));
                            editor.putString("id", offerObject.getString("id"));
                            editor.putString("accesstoken", offerObject.getString("access_token"));
                            editor.apply();


                            appPreference.putString(Constants.ID, offerObject.optString("id"));
                            appPreference.putString(Constants.USER_ROLE, offerObject.optString("role"));
                            appPreference.putString(Constants.PROFILE_IMAGE, offerObject.optString("profile"));
                            appPreference.putString(Constants.FIRST_NAME, offerObject.optString("first_name"));
                            appPreference.putString(Constants.LAST_NAME, offerObject.optString("last_name"));
                            appPreference.putString(Constants.ACCESS_TOKEN, offerObject.optString("access_token"));
                            appPreference.putString(Constants.MOBILE, offerObject.optString("mobile"));
                            appPreference.putString(Constants.DOB, offerObject.optString("dob"));
                            appPreference.putString(Constants.EMAIL, offerObject.optString("email"));
//                            appPreference.putString(Constants.ADDRESS, offerObject.optString("email"));
                            appPreference.putString(Constants.PROFILE_COMPLETE, offerObject.optString("profile_complete"));
                            appPreference.putString(Constants.PROFILE_APPORVED, offerObject.optString("is_apporved"));
                            appPreference.putString(Constants.USER_PROFILE_COMPLETE, offerObject.optString("user_profile_complete"));

                            Intent intent1 = new Intent(LoginActivity.this, HomeActivityUser.class);
                            startActivity(intent1);
                            finishAffinity();


//                            if (appPreference.getString(Constants.PROFILE_COMPLETE).equalsIgnoreCase(null)) {
//                                Intent intent1 = new Intent(LoginActivity.this, EditProfileActivity.class);
//                                intent1.putExtra("COME_FROM", "verfiy");
//                                startActivity(intent1);
//                                finishAffinity();
//                            } else if (offerObject.optString("is_apporved").equalsIgnoreCase("false")) {
//                                Intent intent1 = new Intent(LoginActivity.this, UnderReviewActivty.class);
//                                startActivity(intent1);
//                                finishAffinity();
//                            } else {
//                                if (offerObject.optString("role").equalsIgnoreCase("provider")) {
//                                    Intent intent1 = new Intent(LoginActivity.this, HomeActivityProvider.class);
//                                    startActivity(intent1);
//                                    finishAffinity();
//                                } else {
//                                    Intent intent1 = new Intent(LoginActivity.this, HomeActivity.class);
//                                    startActivity(intent1);
//                                    finishAffinity();
//                                }
//                            }


                        }




                         else if (jsonObject.optString("success").equals("false"))
                        {
                            JSONObject offerObject = jsonObject.getJSONObject("error");



                        }

//                        else if (response.code() == 400 || response.code() == 500 || response.code() == 403 || response.code() == 404 || response.code() == 401) {
//                            if (response.code() == 401) {
//                                Constants.showSessionExpireAlert(LoginActivity.this);
//                            } else {
//                                Constants.showToastAlert(ErrorUtils.getHtttpCodeError(response.code()), LoginActivity.this);
//                            }
//
//                        } else {
//                            String responseStr = ErrorUtils.getResponseBody(response);
//                            JSONObject offerObject = new JSONObject(responseStr);
//                            Constants.showToastAlert(ErrorUtils.checkJosnErrorBody(offerObject), LoginActivity.this);
//                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getApplicationContext()," Check Login Detail",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Constants.hideProgressDialog();
                Log.e("+++", " failer   ...no");
            }
        });

    }




//                Log.d("tag", object.toString(1));
//
//                        if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.TRUE)) {
//                            JSONObject jsonObject = object.optJSONObject("data");
//                            String otp = jsonObject.getString("otp");
//
//                            Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT Response================>>>>>" + object.toString());
//                            Intent intent = new Intent(LoginActivity.this, VerifyScreen.class);
//                            intent.putExtra("mobile", mobile);
//                            intent.putExtra("OTP", otp);
//                            startActivity(intent);
//
//                        } else if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.FALSE)) {
//                            Constants.showFalseMessage(object, LoginActivity.this);
//                        }

//                    else if (response.code() == 400 || response.code() == 500 || response.code() == 403 || response.code() == 404 || response.code() == 401) {
//                        if (response.code() == 401) {
//                            Constants.showSessionExpireAlert(LoginActivity.this);
//                        } else {
//                            Constants.showToastAlert(ErrorUtils.getHtttpCodeError(response.code()), LoginActivity.this);
//                        }
//
//                    }

//                    else {
//                        String responseStr = ErrorUtils.getResponseBody(response);
//                        JSONObject jsonObject = null;
//
//                        Constants.showToastAlert(ErrorUtils.checkJosnErrorBody(jsonObject), LoginActivity.this);
//                    }



}
