package com.zomindianew.comman.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import com.zomindianew.helper.MySharedPreferances;
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

public class SignUpActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_register;
    private RelativeLayout customerRelative;
    private RelativeLayout serviceRelative;
    private String userRoleStr = "user";
    private EditText phoneNumBer;
    private EditText firstNameTextView;
    private EditText lastNameTe;
    private TextView providerSignTextView;
    EditText edt_uname, edt_email, edt_pass, edt_mobile, edt_c_pass;
    TextView tv_continue, terms_condition;
    CheckBox checkbox;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String MobilePattern = "[0-9]{10}";
    String get_uname, get_email, get_mobile, get_pass, getres, device_id;
    String devicetype = "android";

    MySharedPreferances sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edt_uname = (EditText) findViewById(R.id.edt_uname);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_mobile = (EditText) findViewById(R.id.edt_mobile);
        edt_pass = (EditText) findViewById(R.id.edt_pass);
        edt_c_pass = (EditText) findViewById(R.id.edt_c_pass);
        tv_continue = (TextView) findViewById(R.id.tv_continue);
        terms_condition = (TextView) findViewById(R.id.terms_condition);
        checkbox = (CheckBox) findViewById(R.id.checkbox);
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("Signup Event");
        //device_id = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
        device_id = FirebaseInstanceId.getInstance().getToken();
        tv_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isError = false;

                if (edt_uname.getText().toString().trim().length() == 0) {
                    edt_uname.setError("Username is not entered");
                    edt_uname.requestFocus();
                    isError = true;
                }
                if (!edt_email.getText().toString().trim().matches(emailPattern)) {
                    edt_email.setError("Email id is not entered");
                    edt_email.requestFocus();
                    isError = true;
                }
                if (!edt_mobile.getText().toString().trim().matches(MobilePattern)) {
                    edt_mobile.setError("Enter 10 Digit Mobile no.");
                    edt_mobile.requestFocus();
                    isError = true;
                }
                if (edt_pass.getText().toString().trim().length() == 0) {
                    edt_pass.setError("Password is not entered");
                    edt_pass.requestFocus();
                    isError = true;
                }
                if (!edt_pass.getText().toString().equals(edt_c_pass.getText().toString())) {
                    edt_c_pass.setError("Password is not matched");
                    edt_c_pass.requestFocus();
                    isError = true;
                }
                if (!checkbox.isChecked()) {
                    checkbox.setError("Accept Terms And Conditions");
                    checkbox.requestFocus();
                    isError = true;
                }
                if (!isError) {

                    get_uname = edt_uname.getText().toString().trim();
                    get_email = edt_email.getText().toString().trim();
                    get_pass = edt_pass.getText().toString().trim();
                    get_mobile = edt_mobile.getText().toString().trim();
                    signUPAPI(get_uname, get_email, get_pass, get_mobile, device_id, devicetype);

                }

            }
        });
//        intView();
//        setClick();

        terms_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "http://service-provider.zomindia.com/terms-condition";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);

                // Toast.makeText(SignUpActivity.this, "terms and conditions", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    private void intView() {
//        providerSignTextView = findViewById(R.id.providerSignTextView);
        firstNameTextView = findViewById(R.id.firstNameTextView);
//        lastNameTe = findViewById(R.id.lastNameTe);
//        customerRelative = findViewById(R.id.customerRelative);
        serviceRelative = findViewById(R.id.serviceRelative);
        tv_register = findViewById(R.id.tv_register);

    }

    private void setClick() {
        providerSignTextView.setOnClickListener(this);
        phoneNumBer = findViewById(R.id.phoneNumBer);
        tv_register.setOnClickListener(this);
        customerRelative.setOnClickListener(this);
        serviceRelative.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.serviceRelative:
                userRoleStr = "provider";
                serviceRelative.setBackgroundResource(R.drawable.blue_bg_circle);
                customerRelative.setBackgroundResource(0);
                break;

            case R.id.tv_register:
                String firstNameStr = firstNameTextView.getText().toString();
                String lastNumberStr = lastNameTe.getText().toString();
                String phoneStr = phoneNumBer.getText().toString();
                if (firstNameStr.equalsIgnoreCase("")) {
                    Constants.showToastAlert(getResources().getString(R.string.enter_first_name), this);
                } else if (lastNumberStr.equals("")) {
                    Constants.showToastAlert(getResources().getString(R.string.enter_last_name), this);
                } else if (phoneStr.length() < 10) {
                    Constants.showToastAlert(getResources().getString(R.string.enter_phone_number), this);
                } else if (phoneStr.length() < 10) {
                    Constants.showToastAlert(getResources().getString(R.string.Please_enter_valid_number), this);
                } else {
                    if (Constants.isInternetOn(this)) {
//                        signUPAPI(phoneStr, firstNameStr, lastNumberStr);
                    } else {
                        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) SignUpActivity.this.findViewById(android.R.id.content)).getChildAt(0);
                        showSnackbar(viewGroup, getResources().getString(R.string.no_internet), "Retry");
                    }
                }
                break;
            default:
                break;
        }
    }


    public void signUPAPI(final String get_uname, String get_email, String get_pass, String get_mobile, String device_id, String devicetype) {
        startSmsUserConsent();
        Api api = ApiFactory.getClient(SignUpActivity.this).create(Api.class);
        Call<ResponseBody> call;
        HashMap<String, String> stringHashMap = new HashMap<>();
        stringHashMap.put("full_name", get_uname);
        stringHashMap.put("email", get_email);
        stringHashMap.put("password", get_pass);
        stringHashMap.put("mobile", get_mobile);
        stringHashMap.put("device_id", device_id);
        stringHashMap.put("device_type", devicetype);
        call = api.signupUser(stringHashMap);
        Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT ------------------->>>>>" + " " + call.request().url());
        Log.e(Constants.LOG_CAT, "HEADERS : " + call.request().headers());
        Constants.showProgressDialog(SignUpActivity.this, Constants.LOADING);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Constants.hideProgressDialog();
                getres = response.toString();
                Log.e(",,,,,jjj", "" + getres);
                if (response.isSuccessful()) {
                    String output = ErrorUtils.getResponseBody(response);
                    Log.e(",,,,,goapla", "" + getres);

                    try {
                        JSONObject jsonObject = new JSONObject(output);
                        Log.e(",,,,,jjj new ", "" + jsonObject);

                        if (jsonObject.optString("success").equals("true")) {
                            JSONObject jsonObject1 = jsonObject.optJSONObject("data");
                            sharedpreferences = MySharedPreferances.getInstance(SignUpActivity.this);
                            SharedPreferences.Editor editor = sharedpreferences.editor();
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

                            appPreference.putString(Constants.ID, jsonObject1.optString("id"));
                            appPreference.putString(Constants.USER_ROLE, jsonObject1.optString("role"));
                            appPreference.putString(Constants.PROFILE_IMAGE, jsonObject1.optString("profile"));
                            appPreference.putString(Constants.FIRST_NAME, jsonObject1.optString("first_name"));
                            appPreference.putString(Constants.LAST_NAME, jsonObject1.optString("last_name"));
                            appPreference.putString(Constants.ACCESS_TOKEN, jsonObject1.optString("access_token"));
                            appPreference.putString(Constants.MOBILE, jsonObject1.optString("mobile"));
                            appPreference.putString(Constants.DOB, jsonObject1.optString("dob"));
                            appPreference.putString(Constants.EMAIL, jsonObject1.optString("email"));
                            appPreference.putString(Constants.ADDRESS, jsonObject1.optString("email"));
                            appPreference.putString(Constants.PROFILE_COMPLETE, jsonObject1.optString("profile_complete"));
                            appPreference.putString(Constants.PROFILE_APPORVED, jsonObject1.optString("is_apporved"));
                            appPreference.putString(Constants.USER_PROFILE_COMPLETE, jsonObject1.optString("user_profile_complete"));
                            Constants.showDialogAlert("Verification Email is sent to you, Please verify your email.", SignUpActivity.this,new Constants.MyDialogInterface(){
                                @Override
                                public void onClose() {
                                    Intent intent = new Intent(SignUpActivity.this,VerifyScreen.class);
                                    startActivity(intent);

                                }
                            });
                            //{"success":true,"data":{"first_name":"zutest","email":"zutest@mailinator.com","mobile":"9828012345","role":"user","otp":123456,"status":"disapprove","updated_at":"2021-03-13 00:48:31","created_at":"2021-03-13 00:48:31","id":315,"categorys":[],"profile_complete":true,"is_apporved":true,"average_rating":0,"user_profile_complete":false},"message":""}



//                            appPreference.putString(Constants.MOBILE, jsonObject.optString("mobile"));
//                            appPreference.putString(Constants.EMAIL, jsonObject.optString("email"));
//                            appPreference.putString(Constants.ID, jsonObject.optString("id"));
//                            appPreference.putString(Constants.FIRST_NAME, jsonObject.optString("first_name"));
//                            appPreference.putString(Constants.ACCESS_TOKEN, jsonObject.optString("access_token"));
//


                        } else if (jsonObject.optString("success").equals("false")) {
                            JSONObject offerObject = jsonObject.optJSONObject("error");
                            Toast.makeText(getApplicationContext(), offerObject.optString("message"), Toast.LENGTH_SHORT).show();


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

//                try {
//                    if (response.isSuccessful()) {
//                        String output = ErrorUtils.getResponseBody(response);
//                        JSONObject object = new JSONObject(output);
//                        Log.e("here. isss",""+output);
//
//
//                        if (object.getString("success").equals("true"))
//                        {
//                            JSONObject offerObject = object.getJSONObject("data");
//                            Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
//                            intent.putExtra("mobile", offerObject.getString("mobile"));
//                            intent.putExtra("uname", offerObject.getString("first_name"));
//                            intent.putExtra("email", offerObject.getString("email"));
//                            startActivity(intent);
//
//
//                        }
//                        else if (object.getString("success").equals("false"))
//
//                        {
//
//                        }
//
//                    }
//                    else
//                    {
//
//                    }
//
//
//                } catch (JSONException e) {
//                    Constants.hideProgressDialog();
//                    e.printStackTrace();
//                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Constants.hideProgressDialog();
            }
        });


    }
}
