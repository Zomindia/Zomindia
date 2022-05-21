package com.zomindianew.providernew.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zomindianew.R;
import com.zomindianew.comman.activity.BaseActivity;
import com.zomindianew.helper.Constants;
import com.zomindianew.helper.ErrorUtils;
import com.zomindianew.helper.MySharedPreferances;
import com.zomindianew.user.activity.HomeActivityUser;
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

public class BookingDetailActivity extends BaseActivity {


    private TextView userNameText;
    private TextView bookingText;
    private TextView statusText;
    private TextView dateTextView;
    private TextView timeTextView;
    private TextView phoneNumText;
    private TextView addressText;
    private TextView categoryTextView;
    private TextView subCategoryTV;
    private TextView amountTextView;
    private TextView OTPTextView;
    private ImageView imageView;
    private EditText enterOtpText;
    private TextView enterOtp;
    private TextView sumbitOTP;
    private TextView addressText2;
    private String OTPStr;
    private ImageView backIV;
    private String comeFrom;
    private String bookingID;
    private TextView completeTV;
    private TextView rateTV;
    String providerID;
    private String userNameStr;
    private String userImageStr;
    private TextView subCategoryTypeTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);
        comeFrom = getIntent().getStringExtra("come_from");
        bookingID = getIntent().getStringExtra("bookingID");
        initView();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    private void initView() {
        addressText2 = findViewById(R.id.addressText2);
        rateTV = findViewById(R.id.rateTV);
        completeTV = findViewById(R.id.completeTV);
        backIV = findViewById(R.id.backIV);
        sumbitOTP = findViewById(R.id.sumbitOTP);
        imageView = findViewById(R.id.imageView);
        enterOtp = findViewById(R.id.enterOtp);
        userNameText = findViewById(R.id.userNameText);
        bookingText = findViewById(R.id.bookingID);
        statusText = findViewById(R.id.statusText);
        dateTextView = findViewById(R.id.dateTextView);
        timeTextView = findViewById(R.id.timeTextView);
        phoneNumText = findViewById(R.id.phoneNumText);
        addressText = findViewById(R.id.addressText);
        categoryTextView = findViewById(R.id.categoryTextView);
        subCategoryTV = findViewById(R.id.subCategoryTV);
        subCategoryTypeTV = findViewById(R.id.subCategoryTypeTV);
        amountTextView = findViewById(R.id.amountTextView);
        OTPTextView = findViewById(R.id.OTPTextView);
        enterOtpText = findViewById(R.id.enterOtpText);
        sumbitOTP.setVisibility(View.GONE);
        rateTV.setVisibility(View.GONE);
        enterOtpText.setVisibility(View.GONE);
        if (appPreference.getString(Constants.USER_ROLE).equals("provider")) {
            if (comeFrom.equals("upcoming")) {
                enterOtp.setVisibility(View.VISIBLE);
                enterOtpText.setVisibility(View.VISIBLE);
                sumbitOTP.setVisibility(View.VISIBLE);
                rateTV.setVisibility(View.GONE);
                completeTV.setVisibility(View.GONE);
            } else if (comeFrom.equals("on_going")) {
                rateTV.setVisibility(View.GONE);
                completeTV.setVisibility(View.VISIBLE);


            }
        } else {
            sumbitOTP.setVisibility(View.GONE);
            OTPTextView.setVisibility(View.VISIBLE);
            completeTV.setVisibility(View.GONE);
            rateTV.setVisibility(View.GONE);
        }

        deatilAPi();

        sumbitOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //otherInfromationDialog();
                setValidation();
            }
        });

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        completeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                otherInfromationDialog();


            }
        });

        rateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showReviewDialog();
            }
        });
    }

    private void showReviewDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_review, null);
        alert.setView(view);
        final AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView imageView = view.findViewById(R.id.userIV);
        TextView userNameTV = view.findViewById(R.id.userNameTV);
        final RatingBar userRatingBar = view.findViewById(R.id.userRatingBar);
        final TextView commentEditText = view.findViewById(R.id.commentEditText);
        TextView submitBTN = view.findViewById(R.id.submitBTN);
        userNameTV.setText(userNameStr);
        Glide.with(BookingDetailActivity.this)
                .load(userImageStr)
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);
        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String commentStr = commentEditText.getText().toString().trim();
                if (commentStr.equals("")) {
                    Constants.showToastAlert("please enter comment", BookingDetailActivity.this);

                } else {
                    raringAPI(commentStr, (int) userRatingBar.getRating());

                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void deatilAPi() {
        Api api = ApiFactory.getClient(BookingDetailActivity.this).create(Api.class);
        Call<ResponseBody> call;
        call = api.getBookingDetail(MySharedPreferances.getInstance(BookingDetailActivity.this).getString(Constants.ACCESS_TOKEN), bookingID);
        Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT ------------------->>>>>" + " " + call.request().url());
        Log.e(Constants.LOG_CAT, "HEADERS : " + call.request().headers());
        Constants.showProgressDialog(BookingDetailActivity.this, Constants.LOADING);
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


                            userNameStr = jsonObject.optString("user_full_name");
                            userImageStr = jsonObject.optString("user_profile");
                            providerID = jsonObject.optString("provider_id");
                            userNameText.setText(jsonObject.optString("user_full_name"));
                            bookingText.setText("Booking ID: " + jsonObject.optString("reference_id"));
                            dateTextView.setText(jsonObject.optString("date"));
                            timeTextView.setText(jsonObject.optString("time"));
                            if (jsonObject.optString("status").equals("complete")) {
                                statusText.setText("completed");
                            } else {
                                statusText.setText(jsonObject.optString("status"));
                            }

                            addressText.setText(jsonObject.optString("address"));
                            addressText2.setText(jsonObject.optString("address2"));
                            amountTextView.setText(jsonObject.optString("amount"));
                            OTPTextView.setText("OTP: " + jsonObject.optString("confirm_code"));
                            categoryTextView.setText(jsonObject.optString("category"));
                            subCategoryTV.setText(jsonObject.optString("sub_category"));
                            subCategoryTypeTV.setText(jsonObject.optString("sub_category_type"));
                            phoneNumText.setText(jsonObject.optString("mobile"));
                            // phoneNumText.setText(jsonObject.optString("rating_given"));
                            if (jsonObject.optString("rating_given").equals("true")) {
                                rateTV.setVisibility(View.GONE);
                            } else {
                                if (!appPreference.getString(Constants.USER_ROLE).equals("provider")) {
                                    if (jsonObject.optString("status").equals("complete")) {
                                        rateTV.setVisibility(View.VISIBLE);
                                    }
                                }
                            }

                            //
                            Glide.with(BookingDetailActivity.this)
                                    .load(jsonObject.optString("user_profile"))
                                    .apply(RequestOptions.circleCropTransform())
                                    .into(imageView);


                            Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT Response================>>>>>" + object.toString());


                        } else if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.FALSE)) {
                            Constants.showFalseMessage(object, BookingDetailActivity.this);
                        }
                    } else if (response.code() == 400 || response.code() == 500 || response.code() == 403 || response.code() == 404 || response.code() == 401) {
                        if (response.code() == 401) {
                            Constants.showSessionExpireAlert(BookingDetailActivity.this);
                        } else {
                            Constants.showToastAlert(getResources().getString(R.string.failled), BookingDetailActivity.this);
                        }

                    } else {
                        String responseStr = ErrorUtils.getResponseBody(response);
                        JSONObject jsonObject = new JSONObject(responseStr);
                        Constants.showToastAlert(getResources().getString(R.string.failled), BookingDetailActivity.this);
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


    private void setValidation() {
        OTPStr = enterOtpText.getText().toString().trim();
        if (OTPStr.equals("")) {
            Constants.showToastAlert(getResources().getString(R.string.Please_enter_opt), this);
        } else {
            confrimOTPAPI(bookingID, OTPStr);
        }
    }


    public void confrimOTPAPI(final String booking_id, String code) {
        Api api = ApiFactory.getClient(BookingDetailActivity.this).create(Api.class);
        Call<ResponseBody> call;
        HashMap<String, String> stringHashMap = new HashMap<>();
        stringHashMap.put("booking_id", booking_id);
        stringHashMap.put("code", code);

        call = api.confrimAPI(appPreference.getString(Constants.ACCESS_TOKEN), stringHashMap);
        Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT ------------------->>>>>" + " " + call.request().url());
        Log.e(Constants.LOG_CAT, "HEADERS : " + call.request().headers());
        Constants.showProgressDialog(BookingDetailActivity.this, Constants.LOADING);
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
                            JSONObject jsonObject = object.optJSONObject("data");
                            Constants.showToastAlert("OTP Submitted successfully!!", BookingDetailActivity.this);
                            Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT Response================>>>>>" + object.toString());
                            Intent intent = new Intent(BookingDetailActivity.this, HomeActivityProvider.class);
                            intent.putExtra("comeFrom", "booking");
                            startActivity(intent);
                            finishAffinity();

                        } else if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.FALSE)) {
                            Constants.showFalseMessage(object, BookingDetailActivity.this);
                        }
                    } else if (response.code() == 400 || response.code() == 500 || response.code() == 403 || response.code() == 404 || response.code() == 401) {
                        if (response.code() == 401) {
                            Constants.showSessionExpireAlert(BookingDetailActivity.this);
                        } else {
                            Constants.showToastAlert(getResources().getString(R.string.failled), BookingDetailActivity.this);
                        }

                    } else {
                        String responseStr = ErrorUtils.getResponseBody(response);
                        JSONObject jsonObject = new JSONObject(responseStr);
                        Constants.showToastAlert(getResources().getString(R.string.failled), BookingDetailActivity.this);
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

    public void completepi(final String booking_id, String amount, String dec) {
        Api api = ApiFactory.getClient(BookingDetailActivity.this).create(Api.class);
        Call<ResponseBody> call;
        HashMap<String, String> stringHashMap = new HashMap<>();
        stringHashMap.put("booking_id", booking_id);
        stringHashMap.put("status", "complete");
        stringHashMap.put("amount", amount);
        stringHashMap.put("description", dec);


        call = api.completeAPI(appPreference.getString(Constants.ACCESS_TOKEN), stringHashMap);
        Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT ------------------->>>>>" + " " + call.request().url());
        Log.e(Constants.LOG_CAT, "HEADERS : " + call.request().headers());
        Constants.showProgressDialog(BookingDetailActivity.this, Constants.LOADING);
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
                            JSONObject jsonObject = object.optJSONObject("data");
                            Constants.showToastAlert("Booking successful completed", BookingDetailActivity.this);
                            Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT Response================>>>>>" + object.toString());
                            Intent intent = new Intent(BookingDetailActivity.this, HomeActivityProvider.class);
                            intent.putExtra("comeFrom", "booking");
                            startActivity(intent);
                            finishAffinity();

                        } else if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.FALSE)) {
                            Constants.showFalseMessage(object, BookingDetailActivity.this);
                        }
                    } else if (response.code() == 400 || response.code() == 500 || response.code() == 403 || response.code() == 404 || response.code() == 401) {
                        if (response.code() == 401) {
                            Constants.showSessionExpireAlert(BookingDetailActivity.this);
                        } else {
                            Constants.showToastAlert(getResources().getString(R.string.failled), BookingDetailActivity.this);
                        }

                    } else {
                        String responseStr = ErrorUtils.getResponseBody(response);
                        JSONObject jsonObject = new JSONObject(responseStr);
                        Constants.showToastAlert(getResources().getString(R.string.failled), BookingDetailActivity.this);
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

    public void raringAPI(final String reviews, int rating) {
        Api api = ApiFactory.getClient(BookingDetailActivity.this).create(Api.class);
        Call<ResponseBody> call;
        HashMap<String, String> stringHashMap = new HashMap<>();
        stringHashMap.put("from_id", appPreference.getString(Constants.ID));
        stringHashMap.put("to_id", providerID);
        stringHashMap.put("rating", String.valueOf(rating));
        stringHashMap.put("reviews", reviews);
        stringHashMap.put("appointment_id", bookingID);


        call = api.rating(appPreference.getString(Constants.ACCESS_TOKEN), stringHashMap);
        Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT ------------------->>>>>" + " " + call.request().url());
        Log.e(Constants.LOG_CAT, "HEADERS : " + call.request().headers());
        Constants.showProgressDialog(BookingDetailActivity.this, Constants.LOADING);
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
                            JSONObject jsonObject = object.optJSONObject("data");
                            Constants.showToastAlert("Thank you for your feedback.", BookingDetailActivity.this);
                            Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT Response================>>>>>" + object.toString());
                            Intent intent = new Intent(BookingDetailActivity.this, HomeActivityUser.class);
                            startActivity(intent);
                            finishAffinity();

                        } else if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.FALSE)) {
                            Constants.showFalseMessage(object, BookingDetailActivity.this);
                        }
                    } else if (response.code() == 400 || response.code() == 500 || response.code() == 403 || response.code() == 404 || response.code() == 401) {
                        if (response.code() == 401) {
                            Constants.showSessionExpireAlert(BookingDetailActivity.this);
                        } else {
                            Constants.showToastAlert(getResources().getString(R.string.failled), BookingDetailActivity.this);
                        }

                    } else {
                        String responseStr = ErrorUtils.getResponseBody(response);
                        JSONObject jsonObject = new JSONObject(responseStr);
                        Constants.showToastAlert(getResources().getString(R.string.failled), BookingDetailActivity.this);
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

    private void otherInfromationDialog() {
        final Dialog dialog = new Dialog(BookingDetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_one_button_new);
        final EditText amountTextView = (EditText) dialog.findViewById(R.id.amountTextView);
        final EditText discriptionTV = (EditText) dialog.findViewById(R.id.discriptionTV);
        TextView button_ok = (TextView) dialog.findViewById(R.id.button_ok);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (Constants.isInternetOn(BookingDetailActivity.this)) {
                    completepi(bookingID, amountTextView.getText().toString().trim(), discriptionTV.getText().toString().trim());
                } else {
                    final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) BookingDetailActivity.this.findViewById(android.R.id.content)).getChildAt(0);
                    showSnackbar(viewGroup, getResources().getString(R.string.no_internet), "Retry");
                }
            }
        });
        dialog.show();

    }


}
    





