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

public class ForgetPasswordActivity extends AppCompatActivity {
    EditText etMobile;
    TextView tvForgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        etMobile = (EditText) findViewById(R.id.etFPphoneNumBer);
        tvForgot = (TextView) findViewById(R.id.tvFPFogot);
        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etMobile.getText().toString().trim().length() != 10) {
                    etMobile.setError("Please enter valid mobile number");
                    etMobile.requestFocus();
                } else {
                    forget(etMobile.getText().toString());
                }

            }
        });
    }


    public void forget(final String mobile) {
        Api api = ApiFactory.getClient(ForgetPasswordActivity.this).create(Api.class);
        Call<ResponseBody> call;
        HashMap<String, String> stringHashMap = new HashMap<>();
        stringHashMap.put("mobile", mobile);
        call = api.forget(stringHashMap);

        Constants.showProgressDialog(ForgetPasswordActivity.this, Constants.LOADING);
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
                        if (jsonObject.optString("success").equals("true")) {
                            String getmsg = jsonObject.optString("message");
                            Toast.makeText(getApplicationContext(), getmsg, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ForgetPasswordActivity.this, ResetPasswordActivity.class);
                            intent.putExtra("mobile",mobile);
                            startActivity(intent);
                        } else if (jsonObject.optString("success").equals("false")) {
                            JSONObject offerObject = jsonObject.getJSONObject("error");
                            Toast.makeText(getApplicationContext(), offerObject.optString("message"), Toast.LENGTH_SHORT).show();
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