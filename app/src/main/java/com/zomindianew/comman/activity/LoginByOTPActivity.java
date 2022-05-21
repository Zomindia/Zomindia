package com.zomindianew.comman.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

public class LoginByOTPActivity extends BaseActivity implements View.OnClickListener {
    private TextView textView_login;
    private RelativeLayout customerRelative;
    private RelativeLayout serviceRelative;
    private String userRoleStr = "user";
    private EditText phoneNumBer;
    private TextView providerSignTextView;
    private TextView termsTv;
    private TextView textView_registered;

    private CheckBox termsConditionCheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login_by_o_t_p);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_login_by_o_t_p);
        intView();
        setClick();

        textView_registered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginByOTPActivity.this, SignUpActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    private void intView() {
        termsConditionCheck = findViewById(R.id.cbTermsCondition);
        termsTv = findViewById(R.id.termsTv);
        providerSignTextView = findViewById(R.id.providerSignTextView);
        customerRelative = findViewById(R.id.customerRelative);
        serviceRelative = findViewById(R.id.serviceRelative);
        textView_login = findViewById(R.id.textView_login);

        textView_registered = (TextView) findViewById(R.id.tv_rgister);
    }

    private void setClick() {
        termsTv.setOnClickListener(this);
        findViewById(R.id.tvTerms).setOnClickListener(this);
        providerSignTextView.setOnClickListener(this);
        phoneNumBer = findViewById(R.id.phoneNumBer);
        textView_login.setOnClickListener(this);
        customerRelative.setOnClickListener(this);
        serviceRelative.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.customerRelative:
                userRoleStr = "user";
                customerRelative.setBackgroundResource(R.drawable.blue_bg_circle);
                serviceRelative.setBackgroundResource(0);
                break;
            case R.id.serviceRelative:
                userRoleStr = "provider";
                serviceRelative.setBackgroundResource(R.drawable.blue_bg_circle);
                customerRelative.setBackgroundResource(0);
                break;

            case R.id.textView_login:
                String mobileStr = phoneNumBer.getText().toString();
                if (mobileStr.equalsIgnoreCase("")) {
                    Constants.showToastAlert(getResources().getString(R.string.Please_enter_mobile_number), this);
                } else if (mobileStr.length() < 10) {
                    Constants.showToastAlert(getResources().getString(R.string.Please_enter_valid_number), this);
                } else if (!termsConditionCheck.isChecked()) {
                    Constants.showToastAlert(getResources().getString(R.string.please_check_terms_conditions), this);
                } else {
                    if (Constants.isInternetOn(this)) {
                        logInApi(mobileStr, "");
                    } else {
                        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) LoginByOTPActivity.this.findViewById(android.R.id.content)).getChildAt(0);
                        showSnackbar(viewGroup, getResources().getString(R.string.no_internet), "Retry");
                    }
                }
                break;
            case R.id.providerSignTextView:
                Intent intent = new Intent(LoginByOTPActivity.this, SignUpActivity.class);
                startActivity(intent);
                break;
            case R.id.termsTv:
                String url = "http://service-provider.zomindia.com/terms-condition";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
                break;
            case R.id.tvTerms:
                String url1 = "http://service-provider.zomindia.com/terms-condition";
                Intent browserIntent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(url1));
                startActivity(browserIntent1);
                break;

            default:
                break;
        }
    }


    public void logInApi(final String mobile, String role) {
        startSmsUserConsent();
        Api api = ApiFactory.getClient(LoginByOTPActivity.this).create(Api.class);
        Call<ResponseBody> call;
        HashMap<String, String> stringHashMap = new HashMap<>();
        stringHashMap.put("mobile", mobile);
/*
        stringHashMap.put("role", role);
*/
        call = api.loginUser(stringHashMap);
        Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT ------------------->>>>>" + " " + call.request().url());
        Log.e(Constants.LOG_CAT, "HEADERS : " + call.request().headers());
        Constants.showProgressDialog(LoginByOTPActivity.this, Constants.LOADING);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Constants.hideProgressDialog();
Log.e("","get otp respoce "+response);
                try {
                    if (response.isSuccessful()) {
                        String output = ErrorUtils.getResponseBody(response);
                        JSONObject object = new JSONObject(output);
                        Log.d("tag", object.toString(1));

                        if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.TRUE)) {

                            Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT Response================>>>>>" + object.toString());
                            MySharedPreferances sharedpreferences = MySharedPreferances.getInstance(LoginByOTPActivity.this);
                            SharedPreferences.Editor editor = sharedpreferences.editor();
                            editor.putString("mobile", mobile);
                            editor.apply();

                            Intent intent = new Intent(LoginByOTPActivity.this, VerifyScreen.class);
                            startActivity(intent);
                            finish();

                        } else if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.FALSE)) {
                            Constants.showFalseMessage(object, LoginByOTPActivity.this);
                        }
                    } else if (response.code() == 400 || response.code() == 500 || response.code() == 403 || response.code() == 404 || response.code() == 401) {
                        if (response.code() == 401) {
                            Constants.showSessionExpireAlert(LoginByOTPActivity.this);
                        } else {
                            Constants.showToastAlert(getResources().getString(R.string.failled), LoginByOTPActivity.this);
                        }

                    } else {
                        String responseStr = ErrorUtils.getResponseBody(response);
                        JSONObject jsonObject = new JSONObject(responseStr);
                        Constants.showToastAlert(getResources().getString(R.string.failled), LoginByOTPActivity.this);
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
