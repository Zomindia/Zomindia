package com.zomindianew.comman.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class ResetPasswordActivity extends AppCompatActivity {

    EditText etOTP;
    EditText etPassword;
    EditText etCPassword;
    TextView tvForgot;
    String mobile = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        mobile = getIntent().getStringExtra("mobile");
        etOTP = (EditText) findViewById(R.id.etRPOTP);
        etPassword = (EditText) findViewById(R.id.etRPPassword);
        etCPassword = (EditText) findViewById(R.id.etRPCPassword);
        tvForgot = (TextView) findViewById(R.id.tvrpSubmit);
        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isError = false;
                if (etOTP.getText().toString().trim().length() == 0) {
                    etOTP.setError("Please enter OTP");
                    etOTP.requestFocus();
                }  if (etPassword.getText().toString().trim().length() == 0) {
                    etPassword.setError("Password is not entered");
                    etPassword.requestFocus();
                    isError = true;
                }
                if (!etPassword.getText().toString().equals(etCPassword.getText().toString())) {
                    etCPassword.setError("Password is not matched");
                    etCPassword.requestFocus();
                    isError = true;
                }if(!isError){

                    reset(etOTP.getText().toString(),etPassword.getText().toString());
                }

            }
        });
    }


    public void reset(final String otp,String password) {
        Api api = ApiFactory.getClient(ResetPasswordActivity.this).create(Api.class);
        Call<ResponseBody> call;
        HashMap<String, String> stringHashMap = new HashMap<>();
        stringHashMap.put("otp", otp);
        stringHashMap.put("password", password);
        stringHashMap.put("mobile", mobile);
        call = api.resetPassword(stringHashMap);
        Constants.showProgressDialog(ResetPasswordActivity.this, Constants.LOADING);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Constants.hideProgressDialog();
                String getres = response.toString();
                Log.e(",,,,,jjj", "" + getres);
                if (response.isSuccessful()) {
                    String output = ErrorUtils.getResponseBody(response);
                    try {
                        JSONObject jsonObject = new JSONObject(output);
                        Log.e(",,,,,jjj new ", "" + jsonObject);
                        if (jsonObject.getString("success").equals("true")) {
                            Intent intent = new Intent(ResetPasswordActivity.this, WelcomeLogin.class);
                            startActivity(intent);
                        } else if (jsonObject.getString("success").equals("false")) {
                            JSONObject offerObject = jsonObject.getJSONObject("error");
                            Toast.makeText(getApplicationContext(), offerObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Constants.hideProgressDialog();
            }
        });
    }
}