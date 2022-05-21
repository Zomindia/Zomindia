package com.zomindianew.helper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

import com.google.android.material.snackbar.Snackbar;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;


import com.zomindianew.R;
import com.zomindianew.comman.activity.LoginActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Constants {

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String BASE_URL = "http://service-provider.zomindia.com/api/";

    public static final String LOG_CAT = "Trackter";
    public static final String APP_NAME = "Trackter";
    public static final String APP_PREFERENCE_NAME = "TrackerApp";
    public static final String ACCESS_TOKEN = "access_token";

    public static final String VENDOR_MOBILE = "vendor_mobile";
    public static final String OTP = "otp";
    //   private static Snackbar snackbar;
    public static final int CAMERA_REQUEST = 2;
    public static final int PICK_IMAGE_REQUEST = 1;
    public static final String LOADING = "Loading...";
    /*Login*/

    public static final String ID = "id";
    public static final String USER_ROLE = "user_type";
    public static final String EMAIL = "email";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String PROFILE_APPORVED = "is_apporved";
    public static final String PROFILE_COMPLETE = "profile_complete";
    public static final String USER_PROFILE_COMPLETE = "user_profile_complete";

    public static final String PROFILE_IMAGE = "profile_image";
    public static final String ADDRESS = "contact_address";
    public static final String CITY = "contact_city";
    public static final String STATE = "contact_state";
    public static final String MOBILE = "mobile";
    public static final String DOB = "dob";

    public static final String REQUEST_BOOKED = "request_booked";//payment success//
    public static final String ASSIGN_BOOKING = "assign_booking";//provder by admin//
    public static final String ACCEPT_BOOKING = "accept_booking";//customer by provider//
    public static final String BOOKING_COMPLETE_PROVIDER = "booking_complete_provider"; //complte//
    public static final String BOOKING_COMPLETE_CUSTOMER = "booking_complete_customer";//complte//
    public static final String RATING_GIVEN = "rating_given";
    public static final String ACCOUNT_APPROVED = "account_approved";


    public static final String VIDEO_END_MESSAGE = "Video has been saved successfully.";
    public static final String ERROR = "error";
    public static final String MESSAGE = "message";
    public static final String SUCCESS = "success";
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String HEADDER = "Headder";
    public static final String CANCEL = "cancel";
    public static final String ACTIVE = "active";
    public static final String END = "end";
    public static String STATUS = "status";
    private static Snackbar snackbar;

    public static CustomProgressDialog pDialog;

    public static final String[] MEDIA_PERMISSION = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.CAMERA"};

    public static void showProgressDialog(Context context, String loading_text) {
        if (pDialog != null && pDialog.isShowing()) {
            return;
        }

        pDialog = CustomProgressDialog.show(context, false, loading_text);
        if (pDialog != null) {
            if (!pDialog.isShowing())
                pDialog.show();
        }
    }
//    public static void showProgressDialog1(Context context, String loading_text) {
//        if (pDialog != null && pDialog.isShowing()) {
//            return;
//        }
//
//        pDialog = CustomProgressDialog.show(context, false, loading_text);
//        if (pDialog != null) {
//            if (!pDialog.isShowing())
//                pDialog.show();
//        }
//    }

    public static void hideProgressDialog() {
        if (pDialog != null) {
            if (pDialog.isShowing()) {
                pDialog.hide();
            }

            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }
    }

    public static boolean isValidEmail(String target) {
        return target != null && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static void showSnackbar(final Context context, View view, String text1, String text2) {

        try {
            if (snackbar == null) {
                snackbar = Snackbar
                        .make(view, text1, Snackbar.LENGTH_INDEFINITE)
                        .setAction(text2, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.e(Constants.LOG_CAT, "onClick: " + view);
                            }
                        });

                // Changing message text color
                snackbar.setActionTextColor(context.getResources().getColor(R.color.yellow));
                // Changing action button text color
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(com.google.android.material.R.id.snackbar_text);

                textView.setTextColor(Color.WHITE);
            }
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();


        }

    }

    public static boolean isStringNullOrBlank(String str) {
        try {
            if (str != null) {
                if (str.equals("null") || str.equals("") || str.isEmpty() || str.length() <= 0 || str.equalsIgnoreCase("null")) {
                    return true;


                }
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return false;
    }

    public static String timeAgo(String createTimeStr, Context context) {
        try {
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            TimeZone tz = TimeZone.getDefault();
            simpleDateFormat.setTimeZone(tz);
            //  simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date d = simpleDateFormat.parse(createTimeStr);

//		java.util.Date d = f.parse(createTimeStr);
            String currentDateandTime = f.format(new Date());
            java.util.Date d1 = f.parse(currentDateandTime);
            long milliseconds = d.getTime();
            long millisecondsCurrent = d1.getTime();
            long diff_Milli = millisecondsCurrent - milliseconds;
            long minutes = Math.abs((millisecondsCurrent - milliseconds) / 60000);
            long seconds = Math.abs((diff_Milli) / 1000);
            long hours = Math.abs((minutes) / 60);
            long days = Math.abs((hours) / 24);
            long weeks = Math.abs((days) / 7);

            /* long month = days * 30;
             long year = month * 12;*/
            long months = Math.abs((days) / 30);
            long years = Math.abs((months) / 12);

            if (days > 7) {
                createTimeStr = String.valueOf(weeks);
                if (weeks == 1) {
                    createTimeStr = createTimeStr + " " + context.getResources().getString(R.string.week_ago);
                } else {
                    createTimeStr = createTimeStr + " " + context.getResources().getString(R.string.weeks_ago);
                }
            } else if (hours > 24) {
                createTimeStr = String.valueOf(days);
                if (days == 1) {
                    createTimeStr = createTimeStr + " " + context.getResources().getString(R.string.day_ago);
                } else {
                    createTimeStr = createTimeStr + " " + context.getResources().getString(R.string.days_ago);
                }

            } else if (minutes > 60) {
                createTimeStr = String.valueOf(hours);

                if (hours == 1) {
                    createTimeStr = createTimeStr + " " + context.getResources().getString(R.string.hour_ago);
                } else {
                    createTimeStr = createTimeStr + " " + context.getResources().getString(R.string.hours_ago);
                }
            } else if (seconds > 60) {
                createTimeStr = String.valueOf(minutes);

                if (minutes == 1) {
                    createTimeStr = createTimeStr + " " + context.getResources().getString(R.string.minute_ago);
                } else {
                    createTimeStr = createTimeStr + " " + context.getResources().getString(R.string.minutes_ago);
                }
            } else {
                createTimeStr = context.getResources().getString(R.string.just_now);
               /* createTimeStr = String.valueOf(minutes);
                createTimeStr = createTimeStr + " Minutes Ago ";*/
            }


        } catch (Exception e) {
            e.printStackTrace();
            createTimeStr = "";
        }

        return createTimeStr;

    }


    public static void showFalseMessage(JSONObject object, Context mcontext) {
        try {
            JSONArray jsonArray = object.optJSONArray(Constants.ERROR);
            if (jsonArray == null) {
                Constants.showToastAlert(object.getJSONObject(Constants.ERROR).getString(Constants.MESSAGE), mcontext);
            } else {
                Constants.showToastAlert(jsonArray.getJSONObject(0).getString(Constants.MESSAGE), mcontext);
            }
        } catch (Exception e) {
            Log.e(Constants.LOG_CAT, "onResponse:", e);
        }
    }

    public static boolean isInternetOn(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Network[] networks = connectivityManager.getAllNetworks();
                NetworkInfo networkInfo;
                for (Network mNetwork : networks) {
                    networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                    if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                        return true;
                    }
                }
            } else {
                if (connectivityManager != null) {
                    // noinspection deprecation
                    NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                    if (info != null) {
                        for (NetworkInfo anInfo : info) {
                            if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                                return true;
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
        }
        return false;
    }


    public static void showToastAlert(String message, Context context) {
        try {
            Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            View toastView = toast.getView();
            if (toastView==null)
            {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
            else
            {
                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextColor(Color.WHITE);
                toastMessage.setGravity(Gravity.CENTER);

                // Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/RALEWAY-REGULAR_0.OTF");
                // toastMessage.setTypeface(font);

                toast.getView().setBackgroundResource(R.drawable.custom_toast);
                toast.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showSessionExpireAlert(final Context context) {
        final SharedPreferences preferences = context.getSharedPreferences(Constants.APP_PREFERENCE_NAME, context.MODE_PRIVATE);
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_one_button);
        TextView tv_header = (TextView) dialog.findViewById(R.id.tv_header);
        TextView tv_messages = (TextView) dialog.findViewById(R.id.tv_messages);
        TextView button_ok = (TextView) dialog.findViewById(R.id.button_ok);

        tv_messages.setText(context.getResources().getString(R.string.session_expired_msg));

        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(context, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
            }
        });
        dialog.show();

    }
    public interface MyDialogInterface {
        void onClose();
    }

    public static void showDialogAlert(String message, Context context, final MyDialogInterface myDialogInterface) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_one_button);

        TextView tv_header = (TextView) dialog.findViewById(R.id.tv_header);
        TextView tv_messages = (TextView) dialog.findViewById(R.id.tv_messages);
        TextView button_ok = (TextView) dialog.findViewById(R.id.button_ok);

        tv_messages.setText(message);

        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myDialogInterface!=null){
                    myDialogInterface.onClose();
                }
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public static boolean validateEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public static String getDateInFormat(String dateInput, String dateOutput, String dateString) {
//         INPUT   : "yyyy-MM-dd HH:mm:ss"
//         Output  : "hh:mm a dd MMMM"
        String result = "";
        DateFormat formatComingFromServer = new SimpleDateFormat(dateInput);
        DateFormat formatRequired = new SimpleDateFormat(dateOutput);

        try {
            Log.v(Constants.LOG_CAT, "COMING DATE : " + dateString);
            Date dateN = formatComingFromServer.parse(dateString);
            result = formatRequired.format(dateN);
            if (result.contains("a.m.")) {
                result = result.replace("a.m.", "AM");
            } else if (result.contains("p.m.")) {
                result = result.replace("p.m.", "PM");
            } else if (result.contains("am")) {
                result = result.replace("am", "AM");
            } else if (result.contains("pm")) {
                result = result.replace("pm", "PM");
            }

            Log.v(Constants.LOG_CAT, "! RETURNING PARSED DATE : " + result);
        } catch (Exception e) {
            Log.v(Constants.LOG_CAT, "Some Exception while parsing the date : " + e.toString());
        }
        return result;
    }


    public static String wordFirstCap(String str) {
        String capStr = "";
        try {
            String[] words = str.trim().split(" ");
            StringBuilder ret = new StringBuilder();
            for (int i = 0; i < words.length; i++) {
                if (words[i].trim().length() > 0) {
                    Log.e("words[i].trim", "" + words[i].trim().charAt(0));
                    ret.append(Character.toUpperCase(words[i].trim().charAt(0)));
                    ret.append(words[i].trim().substring(1));
                    if (i < words.length - 1) {
                        ret.append(' ');
                    }
                }
            }
            capStr = ret.toString();

        } catch (Exception e) {
            e.printStackTrace();
            capStr = "";
        }
        return capStr;
    }

    public static String ifNullReplace(String str) {
        if (null == str || str.equalsIgnoreCase("null")) {
            return "--";
        } else {
            return str;
        }
    }

    public static String convertDateTime(String dateString) {
        SimpleDateFormat sdf =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdfNew =
                new SimpleDateFormat("dd MMM yy hh:mm a");
        try {
            // string to date
            Date date = sdf.parse(dateString);
            return sdfNew.format(date).toUpperCase();
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
