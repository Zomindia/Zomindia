package com.zomindianew.user.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.zomindianew.R;
import com.zomindianew.comman.activity.BaseActivity;
import com.zomindianew.helper.Constants;
import com.zomindianew.helper.ErrorUtils;
import com.zomindianew.helper.MySharedPreferances;
import com.zomindianew.webservice.Api;
import com.zomindianew.webservice.ApiFactory;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class ConfirmBookingScreen extends BaseActivity implements View.OnClickListener,DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, PaytmPaymentTransactionCallback {
    private String categoryId, formattedDate;
    private String categoryName;
    private String subCategoryID;
    private String serviceID;
    private String subCategoryName;
    private String count;
    private String amount;
    private String nameStr;
    private String addressStr;
    private String addressStr2;
    private String emailStr;
    private int amountNew;
    private String dateStr = "";
    private String timeFrom = "";
    private String startTime = "";
    private TextView categoryTextView;
    TextView dateTextView;
    private TextView timeTextView;
    private TextView totalPriceText;
    private EditText emailTextView;
    private EditText nameTextView;
    private EditText addressTextEdit;
    private EditText addressTextEdit2;
    private TextView paymentButton;
    private RelativeLayout emailRelative;
    private RelativeLayout nameRelative;
    private String order_id;
    private String id;
    String checkSum;
    RadioButton radioButton, radioButton9, radioButton10, radioButton11, radioButton12, radioButton01, radioButton02, radioButton03, radioButton04, radioButton05, radioButton06;
    RadioGroup paymentRadioGroup;
    RadioGroup f1group, s2group, t3group, f4group, f5group;
    int selectedIdTime;
    String selectedText;
    int number, number1;

    TextView textView_header;
    private ImageView img_back_arrow;

    EditText edtnew, edtname, edtmobile, edtcomment;
    private boolean isChecking = true;
    private int mCheckedId = R.id.red_9;

    SharedPreferences sharedpreferences;
    String get_email, get_mobile, get_uname;
    private long timestamp = 0;
    private RadioButton red9;
    private RadioButton red10;
    private RadioButton red11;
    private RadioButton red12;
    private RadioButton red13;
    private RadioButton red14;
    private RadioButton red15;
    private RadioButton red16;
    private RadioButton red17;
    private RadioButton red18;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking_screen);
        GetTime();
        if (ContextCompat.checkSelfPermission(ConfirmBookingScreen.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ConfirmBookingScreen.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
        }
        categoryId = getIntent().getStringExtra("categoryID");
        categoryName = getIntent().getStringExtra("categoryName");
        subCategoryID = getIntent().getStringExtra("subCategoryID");
        serviceID = getIntent().getStringExtra("serviceID");
        subCategoryName = getIntent().getStringExtra("subCategoryName");
        count = getIntent().getStringExtra("count");
        amount = getIntent().getStringExtra("amount");
        nameStr = getIntent().getStringExtra("subSubCategory");

        edtnew = (EditText) findViewById(R.id.dateTextViewdate);
        edtname = (EditText) findViewById(R.id.edt_name);
        edtmobile = (EditText) findViewById(R.id.edt_mobile_no);
        edtcomment = (EditText) findViewById(R.id.sendcomment);
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh. aa");
        formattedDate = dateFormat.format(new Date()).toString();
        System.out.println(formattedDate);
        Toast.makeText(ConfirmBookingScreen.this, formattedDate,
                Toast.LENGTH_LONG).show();
        sharedpreferences = getApplicationContext().getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);
        get_email = sharedpreferences.getString("email", "");
        get_uname = sharedpreferences.getString("uname", "");


//        Calendar calendar = Calendar.getInstance();
//        int hour24hrs = calendar.get(Calendar.HOUR_OF_DAY);
//        int hour12hrs = calendar.get(Calendar.HOUR);
//        int minutes = calendar.get(Calendar.MINUTE);
//        int seconds = calendar.get(Calendar.SECOND);
//        System.out.println("Current hour 24hrs format:  " + hour24hrs + ":" + minutes +":"+ seconds);
//        System.out.println("Current hour 12hrs format:  " + hour12hrs + ":" + minutes +":"+ seconds);
//        Toast.makeText(ConfirmBookingScreen.this,hour12hrs,
//                Toast.LENGTH_LONG).show();

        edtnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar now = Calendar.getInstance();
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = DatePickerDialog.newInstance(
                        (DatePickerDialog.OnDateSetListener) ConfirmBookingScreen.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setMinDate(Calendar.getInstance());
                Calendar noCalendar = Calendar.getInstance();
                int mYear = now.get(Calendar.YEAR);
                int mMonth = now.get(Calendar.MONTH);
                int mDay = now.get(Calendar.DAY_OF_MONTH);
                noCalendar.set(mYear, mMonth, mDay + 2);

                dpd.setMinDate(Calendar.getInstance());
                dpd.setMaxDate(noCalendar);
                dpd.show(getFragmentManager(), "Datepickerdialog");

            }
        });

        red9 = findViewById(R.id.red_9);
        red10 = findViewById(R.id.red_10);
        red11 = findViewById(R.id.red_11);
        red12 = findViewById(R.id.red_12);
        red13 = findViewById(R.id.red_01);
        red14 = findViewById(R.id.red_02);
        red15 = findViewById(R.id.red_03);
        red16 = findViewById(R.id.red_04);
        red17 = findViewById(R.id.red_05);
        red18 = findViewById(R.id.red_06);

        f1group = (RadioGroup) findViewById(R.id.firstgroup);
        s2group = (RadioGroup) findViewById(R.id.secondgroup);
        t3group = (RadioGroup) findViewById(R.id.thirdgroup);
        f4group = (RadioGroup) findViewById(R.id.fourgroup);
        f5group = (RadioGroup) findViewById(R.id.fivegroup);

        f1group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1 && isChecking) {
                    isChecking = false;
                    s2group.clearCheck();
                    t3group.clearCheck();
                    f4group.clearCheck();
                    f5group.clearCheck();
                    mCheckedId = checkedId;
                    isChecking = true;
                }
            }
        });
        s2group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1 && isChecking) {
                    isChecking = false;
                    f1group.clearCheck();
                    t3group.clearCheck();
                    f4group.clearCheck();
                    f5group.clearCheck();
                    mCheckedId = checkedId;
                    isChecking = true;
                }
            }
        });
        t3group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1 && isChecking) {
                    isChecking = false;
                    f1group.clearCheck();
                    s2group.clearCheck();
                    f4group.clearCheck();
                    f5group.clearCheck();
                    mCheckedId = checkedId;
                    isChecking = true;
                }
            }
        });
        f4group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1 && isChecking) {
                    isChecking = false;
                    f1group.clearCheck();
                    s2group.clearCheck();
                    t3group.clearCheck();
                    f5group.clearCheck();
                    mCheckedId = checkedId;
                    isChecking = true;
                }
            }
        });
        f5group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1 && isChecking) {
                    isChecking = false;
                    f1group.clearCheck();
                    s2group.clearCheck();
                    t3group.clearCheck();
                    f4group.clearCheck();
                    mCheckedId = checkedId;
                    isChecking = true;
                }
            }
        });


        initView();
        setCliks();


        textView_header = findViewById(R.id.text_header);
        textView_header.setText(nameStr);

        img_back_arrow = findViewById(R.id.img_back_arrow);
        img_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private void initView() {

        paymentButton = findViewById(R.id.paymentButton);
        categoryTextView = findViewById(R.id.categoryTextView);
        // timeTextView = findViewById(R.id.timeTextView);
        totalPriceText = findViewById(R.id.totalPriceText);
//        emailTextView = findViewById(R.id.emailTextView);
//        nameTextView = findViewById(R.id.nameTextView);
        addressTextEdit = findViewById(R.id.addressTextEdit);
        addressTextEdit2 = findViewById(R.id.addressTextEdit2);
//        emailRelative = findViewById(R.id.emailRelative);
//        nameRelative = findViewById(R.id.nameRelative);
//        if (SharedPreferances.getInstance(ConfirmBookingScreen.this).getString(Constants.USER_PROFILE_COMPLETE).equals("true")) {
//            emailRelative.setVisibility(View.GONE);
//            nameRelative.setVisibility(View.GONE);
//        } else {
//            emailRelative.setVisibility(View.VISIBLE);
//            nameRelative.setVisibility(View.VISIBLE);
//        }

        categoryTextView.setText(categoryName + ", " + subCategoryName + ", " + nameStr );
        if (!amount.equals("")) {
            amountNew = Integer.parseInt(amount) * Integer.parseInt(count);
            totalPriceText.setText("" + amountNew + " ₹");
        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    private void setCliks() {
        paymentButton.setOnClickListener(this);
//        dateTextView.setOnClickListener(this);
        // timeTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.dateTextView:
//                Calendar now = Calendar.getInstance();
//                DatePickerDialog dpd = DatePickerDialog.newInstance(
//                        ConfirmBookingScreen.this,
//                        now.get(Calendar.YEAR),
//                        now.get(Calendar.MONTH),
//                        now.get(Calendar.DAY_OF_MONTH)
//                );
//                dpd.setMinDate(Calendar.getInstance());
//                Calendar noCalendar = Calendar.getInstance();
//                int mYear = now.get(Calendar.YEAR);
//                int mMonth = now.get(Calendar.MONTH);
//                int mDay = now.get(Calendar.DAY_OF_MONTH);
//                noCalendar.set(mYear, mMonth, mDay + 2);
//
//                dpd.setMinDate(Calendar.getInstance());
//                dpd.setMaxDate(noCalendar);
//                dpd.show(getFragmentManager(), "Datepickerdialog");
//                break;

            /*case R.id.timeTextView:

                Calendar now1 = Calendar.getInstance();
                TimePickerDialog timePickerDialog =
                        TimePickerDialog.newInstance(ConfirmBookingScreen.this,
                                now1.get(Calendar.HOUR_OF_DAY),
                                now1.get(Calendar.MINUTE),
                                false
                        );

                timePickerDialog.enableSeconds(false);
                timePickerDialog.enableMinutes(false);


               *//* Calendar now1 = Calendar.getInstance();
                TimePickerDialog timePickerDialog =
                        TimePickerDialog.newInstance(
                                NewRequestActivity.this,
                                now1.get(Calendar.HOUR_OF_DAY),
                                now1.get(Calendar.MINUTE),
                                false
                        );*//*

                Timepoint timepoint = new Timepoint(9);
                timepoint.setAM();
                Timepoint timepoint18 = new Timepoint(6);
                timepoint18.setPM();

                timePickerDialog.setMinTime(timepoint);
                timePickerDialog.setMaxTime(timepoint18);
                *//*timePickerDialog.setTimeInterval(1, 15);*//*
                timePickerDialog.setStartTime(9, 0);
                timePickerDialog.show(getFragmentManager(), "Timepickerdialog");

                break;*/
            case R.id.paymentButton:
                setValidation();

                //  paymentDialog();
                break;
        }

    }

    private void setValidation() {
//        nameStr = nameTextView.getText().toString().trim();
//        emailStr = emailTextView.getText().toString().trim();
        addressStr = addressTextEdit.getText().toString().trim();
        addressStr2 = addressTextEdit2.getText().toString().trim();
        selectedIdTime = f1group.getCheckedRadioButtonId();
        if(selectedIdTime == -1){
            selectedIdTime = s2group.getCheckedRadioButtonId();
        }
        if(selectedIdTime == -1){
            selectedIdTime = t3group.getCheckedRadioButtonId();
        }
        if(selectedIdTime == -1){
            selectedIdTime = f4group.getCheckedRadioButtonId();
        }
        if(selectedIdTime == -1){
            selectedIdTime = f5group.getCheckedRadioButtonId();
        }
        if (MySharedPreferances.getInstance(ConfirmBookingScreen.this).getString(Constants.USER_PROFILE_COMPLETE).equals("true")) {
            if (edtnew.getText().toString().trim().equals("")) {
                Constants.showToastAlert(getResources().getString(R.string.please_select_date), ConfirmBookingScreen.this);
            } else if (selectedIdTime == -1) {
                Constants.showToastAlert(getResources().getString(R.string.please_select_time), ConfirmBookingScreen.this);
            } else if (addressStr.equals("")) {
                Constants.showToastAlert(getResources().getString(R.string.please_enter_address), ConfirmBookingScreen.this);
            } else if (addressStr2.equals("")) {
                Constants.showToastAlert(getResources().getString(R.string.please_enter_landmark), ConfirmBookingScreen.this);
            } else {
                if (Constants.isInternetOn(this)) {

                    bookingAPI();

//                    number = Integer.parseInt(String.valueOf(selectedIdTime));
//                     number = Integer.parseInt(String.valueOf(formattedDate));
//
//
//                    if (number>=number1)
//                    {
//                        Toast.makeText(ConfirmBookingScreen.this,"yes",
//                                Toast.LENGTH_LONG).show();
//                    }
//                    else
//                    {
//                        Toast.makeText(ConfirmBookingScreen.this,"no",
//                                Toast.LENGTH_LONG).show();
//                    }

                } else {
                    final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) ConfirmBookingScreen.this.findViewById(android.R.id.content)).getChildAt(0);
                    showSnackbar(viewGroup, getResources().getString(R.string.no_internet), "Retry");
                }


            }


        } else {
            if (edtnew.getText().toString().trim().equals("")) {
                Constants.showToastAlert(getResources().getString(R.string.please_select_date), ConfirmBookingScreen.this);
            } else if (selectedIdTime == -1) {
                Constants.showToastAlert(getResources().getString(R.string.please_select_time), ConfirmBookingScreen.this);
            }

//            else if (nameStr.equals("")) {
//                Constants.showToastAlert(getResources().getString(R.string.please_enter_name), ConfirmBookingScreen.this);
//            }
//            else if (emailStr.equals("")) {
//                Constants.showToastAlert(getResources().getString(R.string.please_enter_email_address), ConfirmBookingScreen.this);
//            }

//            else if (!Constants.isValidEmail(emailStr)) {
//                Constants.showToastAlert(getResources().getString(R.string.enter_valid_email_id), ConfirmBookingScreen.this);
//            }
            else if (addressStr.equals("")) {
                Constants.showToastAlert(getResources().getString(R.string.please_enter_address), ConfirmBookingScreen.this);
            } else if (addressStr2.equals("")) {
                Constants.showToastAlert(getResources().getString(R.string.please_enter_landmark), ConfirmBookingScreen.this);
            } else {
                if (Constants.isInternetOn(this)) {
                    appPreference.putString(Constants.EMAIL, emailStr);
                    bookingAPI();

                } else {
                    final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) ConfirmBookingScreen.this.findViewById(android.R.id.content)).getChildAt(0);
                    showSnackbar(viewGroup, getResources().getString(R.string.no_internet), "Retry");
                }


            }
        }


    }


    public void paymentDialog(final String amountRe) {
        final Dialog slidDialog = new Dialog(ConfirmBookingScreen.this);
        slidDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        slidDialog.setCancelable(false);
        slidDialog.setCanceledOnTouchOutside(false);
        slidDialog.setContentView(R.layout.dialog_paytem);
        slidDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        slidDialog.getWindow().getAttributes().windowAnimations = R.style.SmileWindow;
        paymentRadioGroup = slidDialog.findViewById(R.id.paymentRadioGroup);
        TextView totalPriceText = slidDialog.findViewById(R.id.totalPriceText);
        totalPriceText.setText("Total Price : " + amountRe + "" + " ₹");
        TextView paymentButton = slidDialog.findViewById(R.id.paymentButton);
        ImageView closeImageView = slidDialog.findViewById(R.id.closeImageView);
        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int selectedId = paymentRadioGroup.getCheckedRadioButtonId();
                    radioButton = (RadioButton) slidDialog.findViewById(selectedId);
                    Log.e(Constants.LOG_CAT, "onClick: radioButton" + radioButton.getText().toString());
                    if (paymentRadioGroup.getCheckedRadioButtonId() == -1) {
                        Constants.showToastAlert("Please select payment method!!", ConfirmBookingScreen.this);
                    } else {
                        if (radioButton.getText().equals("CASH")) {
                            transationAPI(id, "cod", amountRe);
                            slidDialog.dismiss();
                        } else {
                            checkSumAPI(id);
                            slidDialog.dismiss();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidDialog.dismiss();
            }
        });
        /**/
        slidDialog.show();
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {


       /* if (dayOfMonth < 10 || monthOfYear < 10) {
            String dayof = String.valueOf(dayOfMonth);
            String moth = String.valueOf(monthOfYear);
            dayof = ("0" + dayof);
            moth = ("0" + moth);
            dateStr = year + "-" + moth + "-" + dayof;
        } else {

        }*/
        dateStr = Constants.getDateInFormat("yyyy-MM-dd", "yyyy-MM-dd", year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
        edtnew.setText(Constants.getDateInFormat("yyyy-MM-dd", "MMM dd,yyyy", String.valueOf(dateStr)));

        Calendar calendar = Calendar.getInstance();
        calendar.set(year,monthOfYear,dayOfMonth,0,0,0);
        red9.setChecked(false);
        red10.setChecked(false);
        red11.setChecked(false);
        red12.setChecked(false);
        red13.setChecked(false);
        red14.setChecked(false);
        red15.setChecked(false);
        red16.setChecked(false);
        red17.setChecked(false);
        red18.setChecked(false);

        calendar.set(Calendar.HOUR_OF_DAY,9);
        if(calendar.getTimeInMillis()>timestamp)
        {
            red9.setVisibility(View.VISIBLE);
        }else{
            red9.setVisibility(View.INVISIBLE);
        }

        calendar.set(Calendar.HOUR_OF_DAY,10);
        if(calendar.getTimeInMillis()>timestamp)
        {
            red10.setVisibility(View.VISIBLE);
        }else{
            red10.setVisibility(View.INVISIBLE);
        }

        calendar.set(Calendar.HOUR_OF_DAY,11);
        if(calendar.getTimeInMillis()>timestamp)
        {
            red11.setVisibility(View.VISIBLE);
        }else{
            red11.setVisibility(View.INVISIBLE);
        }

        calendar.set(Calendar.HOUR_OF_DAY,12);
        if(calendar.getTimeInMillis()>timestamp)
        {
            red12.setVisibility(View.VISIBLE);
        }else{
            red12.setVisibility(View.INVISIBLE);
        }

        calendar.set(Calendar.HOUR_OF_DAY,13);
        if(calendar.getTimeInMillis()>timestamp)
        {
            red13.setVisibility(View.VISIBLE);
        }else{
            red13.setVisibility(View.INVISIBLE);
        }

        calendar.set(Calendar.HOUR_OF_DAY,14);
        if(calendar.getTimeInMillis()>timestamp)
        {
            red14.setVisibility(View.VISIBLE);
        }else{
            red14.setVisibility(View.INVISIBLE);
        }

        calendar.set(Calendar.HOUR_OF_DAY,15);
        if(calendar.getTimeInMillis()>timestamp)
        {
            red15.setVisibility(View.VISIBLE);
        }else{
            red15.setVisibility(View.INVISIBLE);
        }

        calendar.set(Calendar.HOUR_OF_DAY,16);
        if(calendar.getTimeInMillis()>timestamp)
        {
            red16.setVisibility(View.VISIBLE);
        }else{
            red16.setVisibility(View.INVISIBLE);
        }

        calendar.set(Calendar.HOUR_OF_DAY,17);
        if(calendar.getTimeInMillis()>timestamp)
        {
            red17.setVisibility(View.VISIBLE);
        }else{
            red17.setVisibility(View.INVISIBLE);
        }

        calendar.set(Calendar.HOUR_OF_DAY,18);
        if(calendar.getTimeInMillis()>timestamp)
        {
            red18.setVisibility(View.VISIBLE);
        }else{
            red18.setVisibility(View.INVISIBLE);
        }
        ++monthOfYear;
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

        final String time = hourOfDay + ":" + minute + ":" + second;
        //  timeFrom = Constants.getDateInFormat("HH:mm:ss", "HH:mm:ss", time);
        //    timeTextView.setText(Constants.getDateInFormat("HH:mm:ss", "hh:mm aa", timeFrom));

    }
    public void bookingAPI() {
//        radioButtonTime = (RadioButton) findViewById(selectedIdTime);
//
//        if (radioButtonTime.getText().equals("09AM - 12PM")) {
//            timeFrom = "09:00-12:00";
//        } else if (radioButtonTime.getText().equals("12PM - 03PM")) {
//            timeFrom = "12:00-15:00";
//        } else if (radioButtonTime.getText().equals("03PM - 06PM")) {
//            timeFrom = "15:00-18:00";
//        }


        if (mCheckedId == R.id.red_9) {
            timeFrom = "09:00-10:00";
            startTime = "09:00:00";
        } else if (mCheckedId == R.id.red_10) {
            timeFrom = "10:00-11:00";
            startTime = "10:00:00";
        } else if (mCheckedId == R.id.red_11) {
            timeFrom = "11:00-12:00";
            startTime = "11:00:00";
        } else if (mCheckedId == R.id.red_12) {
            timeFrom = "12:00-13:00";
            startTime = "12:00:00";
        } else if (mCheckedId == R.id.red_01) {
            timeFrom = "13:00-14:00";
            startTime = "13:00:00";
        } else if (mCheckedId == R.id.red_02) {
            timeFrom = "14:00-15:00";
            startTime = "14:00:00";
        } else if (mCheckedId == R.id.red_03) {
            timeFrom = "15:00-16:00";
            startTime = "15:00:00";
        } else if (mCheckedId == R.id.red_04) {
            timeFrom = "16:00-17:00";
            startTime = "16:00:00";
        } else if (mCheckedId == R.id.red_05) {
            timeFrom = "17:00-18:00";
            startTime = "17:00:00";
        } else if (mCheckedId == R.id.red_06) {
            timeFrom = "06:00-07:00";
            startTime = "18:00:00";
        }

        Api api = ApiFactory.getClient(ConfirmBookingScreen.this).create(Api.class);
        Call<ResponseBody> call;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("category_id", categoryId);
        map.put("sub_category_id", subCategoryID);
        map.put("service_id", serviceID);
        map.put("name", appPreference.getString(Constants.FIRST_NAME)+ appPreference.getString(Constants.LAST_NAME));
        map.put("mobile", appPreference.getString(Constants.MOBILE));
        map.put("phone", appPreference.getString(Constants.MOBILE));
        map.put("address", addressStr);
        map.put("address2", addressStr2);
        map.put("email", appPreference.getString(Constants.EMAIL));
        map.put("date", edtnew.getText().toString().trim());
        map.put("time", timeFrom);
        map.put("quantity", "" + count);
        map.put("alternate_name", edtname.getText().toString().trim());
        map.put("alternate_number", edtmobile.getText().toString().trim());
        map.put("comment",edtcomment.getText().toString().trim());
        call = api.bookingApi(MySharedPreferances.getInstance(ConfirmBookingScreen.this).getString(Constants.ACCESS_TOKEN), map);
        Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT ------------------->>>>>" + " " + call.request().url());
        Log.e(Constants.LOG_CAT, "HEADERS : " + call.request().headers());
        Constants.showProgressDialog(ConfirmBookingScreen.this, Constants.LOADING);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Constants.hideProgressDialog();

                try {
                    if (response.isSuccessful()) {
                        String output = ErrorUtils.getResponseBody(response);
                        JSONObject object = new JSONObject(output);
                        Log.e("h....", "one" + output);
                        Log.d("tag", object.toString(1));
                        if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.TRUE)) {
                            JSONObject jsonObject = object.optJSONObject("data");
                            order_id = jsonObject.optString("order_id");
                            id = jsonObject.optString("id");
                            Log.e("h.2...", "two");


                            paymentDialog(String.valueOf(amountNew));
                        } else if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.FALSE)) {
                            Constants.showFalseMessage(object, ConfirmBookingScreen.this);
                            Log.e("h.3..", "three");

                        }
                    } else if (response.code() == 400 || response.code() == 500 || response.code() == 403 || response.code() == 404 || response.code() == 401) {
                        if (response.code() == 401) {
                            Constants.showSessionExpireAlert(ConfirmBookingScreen.this);
                        } else {
                            Constants.showToastAlert(ErrorUtils.getHtttpCodeError(response.code()), ConfirmBookingScreen.this);
                            Log.e("h.4..", "four");

                        }

                    } else {
                        String responseStr = ErrorUtils.getResponseBody(response);
                        JSONObject jsonObject = new JSONObject(responseStr);
                        Constants.showToastAlert(ErrorUtils.checkJosnErrorBody(jsonObject), ConfirmBookingScreen.this);
                        Log.e("h.5..", "five");

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

    public void checkSumPayment(String amount) {
        String emailStr = appPreference.getString(Constants.EMAIL);
        String mobileStr = appPreference.getString(Constants.MOBILE);
        PaytmPGService Service = PaytmPGService.getStagingService();
        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("MID", "HZaOxj61837511147328");
// Key in your staging and production MID available in your dashboard
        paramMap.put("ORDER_ID", order_id);
        paramMap.put("CUST_ID", appPreference.getString(Constants.ID));
        paramMap.put("MOBILE_NO", mobileStr);
        paramMap.put("EMAIL", emailStr);
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("TXN_AMOUNT", amount);
        paramMap.put("WEBSITE", "DEFAULT");
// This is the staging value. Production value is available in your dashboard
        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
// This is the staging value. Production value is available in your dashboard
        //"https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=$orderId"
        //  paramMap.put("CALLBACK_URL", "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID="+order_id);
        paramMap.put("CALLBACK_URL", "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp");
        paramMap.put("CHECKSUMHASH", checkSum);
        PaytmOrder Order = new PaytmOrder(paramMap);
        Service.initialize(Order, null);
        // start payment service call here
        Service.startPaymentTransaction(ConfirmBookingScreen.this, true, true,
                ConfirmBookingScreen.this);
    }


    public void checkSumAPI(String id) {
        Api api = ApiFactory.getClient(ConfirmBookingScreen.this).create(Api.class);
        Call<ResponseBody> call;


        call = api.checkSumAPI(MySharedPreferances.getInstance(ConfirmBookingScreen.this).getString(Constants.ACCESS_TOKEN), id);
        Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT ------------------->>>>>" + " " + call.request().url());
        Log.e(Constants.LOG_CAT, "HEADERS : " + call.request().headers());
        Constants.showProgressDialog(ConfirmBookingScreen.this, Constants.LOADING);
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
                            JSONObject data = object.optJSONObject("data");
                            checkSum = data.optString("checksum");
                            checkSumPayment(String.valueOf(amountNew));
                        } else if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.FALSE)) {
                            Constants.showFalseMessage(object, ConfirmBookingScreen.this);
                        }
                    } else if (response.code() == 400 || response.code() == 500 || response.code() == 403 || response.code() == 404 || response.code() == 401) {
                        if (response.code() == 401) {
                            Constants.showSessionExpireAlert(ConfirmBookingScreen.this);
                        } else {
                            Constants.showToastAlert(ErrorUtils.getHtttpCodeError(response.code()), ConfirmBookingScreen.this);
                        }

                    } else {
                        String responseStr = ErrorUtils.getResponseBody(response);
                        JSONObject jsonObject = new JSONObject(responseStr);
                        Constants.showToastAlert(ErrorUtils.checkJosnErrorBody(jsonObject), ConfirmBookingScreen.this);
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
    public void onTransactionResponse(Bundle inResponse) {
        Log.e("checksum ", " ui fail respon onTransactionResponse " + inResponse);
        String STATUS = inResponse.getString("STATUS");
        if (STATUS.equals("TXN_SUCCESS")) {
            String TXNAMOUNT = inResponse.getString("TXNAMOUNT");
            String TXNID = inResponse.getString("TXNID");
            String CHECKSUMHASH = inResponse.getString("CHECKSUMHASH");
            transationAPI(TXNID, "paytm", TXNAMOUNT);
        }


    }

    @Override
    public void networkNotAvailable() {
        Log.e("checksum ", " ui fail respon networkNotAvailable ");
        Constants.showToastAlert("Network not available, Please try again later!!", ConfirmBookingScreen.this);
    }

    @Override
    public void clientAuthenticationFailed(String inErrorMessage) {
        Constants.showToastAlert(inErrorMessage, ConfirmBookingScreen.this);
        Log.e("checksum ", inErrorMessage + inErrorMessage);
    }

    @Override
    public void someUIErrorOccurred(String s) {
        Constants.showToastAlert(s, ConfirmBookingScreen.this);
        Log.e("checksum ", " ui fail respon  " + s);
    }

    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        Constants.showToastAlert("Please try again later!!", ConfirmBookingScreen.this);
        Log.e("checksum ", " error loading pagerespon true " + s + "  s1 " + s1);
    }

    @Override
    public void onBackPressedCancelTransaction() {
        //Constants.showToastAlert("", ConfirmBookingScreen.this);
        Log.e("checksum ", " cancel call back respon  ");
    }

    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        Constants.showToastAlert(s, ConfirmBookingScreen.this);
        Log.e("checksum ", "  transaction cancel ");
    }


    public void transationAPI(String transaction, final String type, String amount) {
        Api api = ApiFactory.getClient(ConfirmBookingScreen.this).create(Api.class);
        Call<ResponseBody> call;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("transaction_id", transaction);
        map.put("booking_id", id);
        map.put("amount", amount);
        map.put("type", type);


        call = api.transactionAPI(MySharedPreferances.getInstance(ConfirmBookingScreen.this).getString(Constants.ACCESS_TOKEN), map);
        Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT ------------------->>>>>" + " " + call.request().url());
        Log.e(Constants.LOG_CAT, "HEADERS : " + call.request().headers());
        Constants.showProgressDialog(ConfirmBookingScreen.this, Constants.LOADING);
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
                            Intent intent = new Intent(ConfirmBookingScreen.this, HomeActivityUser.class);
                            startActivity(intent);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                finishAffinity();
                            }
                            if (type.equals("cod")) {
                                Constants.showToastAlert("Your booking has been received.", ConfirmBookingScreen.this);
                            } else {
                                Constants.showToastAlert("Your booking has been received and payment has been received through paytm!!", ConfirmBookingScreen.this);
                            }

                        } else if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.FALSE)) {
                            Constants.showFalseMessage(object, ConfirmBookingScreen.this);
                        }
                    } else if (response.code() == 400 || response.code() == 500 || response.code() == 403 || response.code() == 404 || response.code() == 401) {
                        if (response.code() == 401) {
                            Constants.showSessionExpireAlert(ConfirmBookingScreen.this);
                        } else {
                            Constants.showToastAlert(ErrorUtils.getHtttpCodeError(response.code()), ConfirmBookingScreen.this);
                        }

                    } else {
                        String responseStr = ErrorUtils.getResponseBody(response);
                        JSONObject jsonObject = new JSONObject(responseStr);
                        Constants.showToastAlert(ErrorUtils.checkJosnErrorBody(jsonObject), ConfirmBookingScreen.this);
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
    public void GetTime() {
        Api api = ApiFactory.getClient(ConfirmBookingScreen.this).create(Api.class);
        Call<ResponseBody> call;

        call = api.serverDateTime();
        Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT...secect sub.. ------------------->>>>>" + " " + call.request().url());
        Log.e(Constants.LOG_CAT, "HEADERS ...secect sub...: " + call.request().headers());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Constants.hideProgressDialog();

                try {
                    if (response.isSuccessful()) {
                        String output = ErrorUtils.getResponseBody(response);
                        JSONObject object = new JSONObject(output);
                        if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.TRUE)) {
                            Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT Response== secect ==============>>>>>" + object.toString());
                            JSONObject jsonObject = object.optJSONObject("data");
                            String datetime = jsonObject.optString("datetime");
                            timestamp = jsonObject.optLong("timestamp")*1000;
                            String after= jsonObject.optString("after");
                            String date= jsonObject.optString("date");
                            String time=jsonObject.optString("time");
                            int afterTime = 0;
                            try{
                                afterTime=Integer.parseInt(after);
                            }catch (Exception e){
                                afterTime = 2;
                            }
                            timestamp = timestamp+(afterTime*60*60*1000);
                        } else if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.FALSE)) {
                            Constants.showFalseMessage(object, ConfirmBookingScreen.this);
                        }
                    } else if (response.code() == 400 || response.code() == 500 || response.code() == 403 || response.code() == 404 || response.code() == 401) {
                        if (response.code() == 401) {
                            Constants.showSessionExpireAlert(ConfirmBookingScreen.this);
                        } else {
                            Constants.showToastAlert(ErrorUtils.getHtttpCodeError(response.code()), ConfirmBookingScreen.this);
                        }

                    } else {
                        String responseStr = ErrorUtils.getResponseBody(response);
                        JSONObject jsonObject = new JSONObject(responseStr);
                        Constants.showToastAlert(ErrorUtils.checkJosnErrorBody(jsonObject), ConfirmBookingScreen.this);
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
