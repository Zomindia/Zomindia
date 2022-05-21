package com.zomindianew.providernew.activity;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;
import com.zomindianew.R;
import com.zomindianew.comman.activity.BaseActivity;
import com.zomindianew.comman.activity.LoginActivity;
import com.zomindianew.comman.fragment.ContactAsFragment;
import com.zomindianew.comman.fragment.FaqFragment;
import com.zomindianew.comman.fragment.NotificationFragment;
import com.zomindianew.helper.Constants;
import com.zomindianew.helper.ErrorUtils;
import com.zomindianew.helper.MySharedPreferances;
import com.zomindianew.providernew.fragment.HomeFragmentProvider;
import com.zomindianew.providernew.fragment.MyAppointmentFragment;
import com.zomindianew.providernew.fragment.ProfileProviderFragment;
import com.zomindianew.webservice.Api;
import com.zomindianew.webservice.ApiFactory;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;


public class HomeActivityProvider extends BaseActivity implements View.OnClickListener {
    private static final long DOUBLE_PRESS_INTERVAL = 250; // in millis
    private long lastPressTime;
    private boolean mHasDoubleClicked = false;
    private String unReadBadgeCount = "";
    public static ResideMenu resideMenu;
    private ResideMenuItem itemHome;
    private ResideMenuItem itemMyProfile;
    private ResideMenuItem itemMyBooking;
    private ResideMenuItem itemNotification;
    private ResideMenuItem itemContact;
    private ResideMenuItem itemFAQ;
    private ResideMenuItem itemShare;
    private ResideMenuItem itemLogout;
    public ImageView img_back_arrow;
    private TextView textView_header;
    public ImageView editProfile;
    public de.hdodenhof.circleimageview.CircleImageView profile_image_circle;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_activty);
        textView_header = findViewById(R.id.textView_header);

        editProfile = findViewById(R.id.editProfile);

        profile_image_circle = findViewById(R.id.profile_image_circle);
        profile_image_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView_header.setText("My Profile");
                editProfile.setVisibility(View.VISIBLE);
                Fragment myFragment = getSupportFragmentManager().findFragmentByTag("myprofile");

                if (myFragment != null && myFragment.isVisible()) {
                    resideMenu.closeMenu();
                } else {
                    changeFragment(new ProfileProviderFragment(), "myprofile");
                    resideMenu.closeMenu();
                }

                setResideMenuItemIconColor(false, true, false);
            }
        });
        setUpMenu();
        textView_header.setText("Job Notification");
        editProfile.setVisibility(View.GONE);
        if (getIntent().hasExtra("comeFrom")) {
            if (getIntent().getStringExtra("comeFrom").equals("booking")) {

                changeFragment(new MyAppointmentFragment(""), "MyAppointmentFragment");

            }
        } else {
            if (getIntent().hasExtra("NAVIGATE_SCREEN")) {
                if (getIntent().getStringExtra("NAVIGATE_SCREEN").equals(Constants.ASSIGN_BOOKING)) {

                    changeFragment(new HomeFragmentProvider(), "HomeFragment");
                    editProfile.setVisibility(View.GONE);

                } else if (getIntent().getStringExtra("NAVIGATE_SCREEN").equals(Constants.BOOKING_COMPLETE_PROVIDER)) {
                    textView_header.setText("My Booking");
                    editProfile.setVisibility(View.GONE);
                    changeFragment(new MyAppointmentFragment("past"), "MyAppointmentFragment");
                } else if (getIntent().getStringExtra("NAVIGATE_SCREEN").equals(Constants.RATING_GIVEN)) {
                    editProfile.setVisibility(View.GONE);
                    textView_header.setText("Notifications");
                    changeFragment(new NotificationFragment(), "NotificationFragment");
                }
            } else {
                changeFragment(new HomeFragmentProvider(), "HomeFragment");
            }


        }

        setResideMenuItemIconColor(true, false, false);
        img_back_arrow = findViewById(R.id.img_back_arrow);
        img_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
    }


    private void setUpMenu() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.color.white);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. left menu'width is 150dip.
        resideMenu.setScaleValue(0.8f);

        itemHome = new ResideMenuItem(this, R.mipmap.menu_home, getResources().getString(R.string.home), width);
        itemMyProfile = new ResideMenuItem(this, R.mipmap.menu_photos, getResources().getString(R.string.my_profile), width);
        itemMyBooking = new ResideMenuItem(this, R.mipmap.menu_photos, getResources().getString(R.string.my_booking), width);
        itemNotification = new ResideMenuItem(this, R.mipmap.menu_home, getResources().getString(R.string.notification), width);
        itemContact = new ResideMenuItem(this, R.mipmap.menu_photos, getResources().getString(R.string.contact_us), width);
        itemFAQ = new ResideMenuItem(this, R.mipmap.menu_photos, getResources().getString(R.string.faq), width);
        itemShare= new ResideMenuItem(this, R.mipmap.menu_photos, getResources().getString(R.string.share), width);
        itemLogout = new ResideMenuItem(this, R.mipmap.menu_photos, getResources().getString(R.string.logout), width);

        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemMyProfile, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemMyBooking, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemNotification, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemContact, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemFAQ, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemShare, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemLogout, ResideMenu.DIRECTION_LEFT);

        itemHome.setOnClickListener(this);
        itemMyProfile.setOnClickListener(this);
        itemMyBooking.setOnClickListener(this);
        itemNotification.setOnClickListener(this);
        itemContact.setOnClickListener(this);
        itemFAQ.setOnClickListener(this);
        itemShare.setOnClickListener(this);
        itemLogout.setOnClickListener(this);


        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);



       /* resideMenu.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new ProfileFragment(), "ProfileFragment");
                resideMenu.closeMenu();
            }
        });*/


    }

    public void setResideMenuItemIconColor(boolean itemHomee, boolean itemChangee, boolean itemLogoutt) {

//        itemHome.setResideMenuItemSelect(itemHomee,"","");
//        itemChange.setResideMenuItemSelect(itemChangee);
//        itemLogout.setResideMenuItemSelect(itemLogoutt);


    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {
        if (view == itemHome) {
            textView_header.setText("Job Notification");
            editProfile.setVisibility(View.GONE);
            changeFragment(new HomeFragmentProvider(), "HomeFragment");
            resideMenu.closeMenu();

            setResideMenuItemIconColor(true, false, false);
        } else if (view == itemMyProfile) {
            textView_header.setText("My Profile");
            editProfile.setVisibility(View.VISIBLE);
            Fragment myFragment = getSupportFragmentManager().findFragmentByTag("myprofile");

            if (myFragment != null && myFragment.isVisible()) {
                resideMenu.closeMenu();
            } else {
                changeFragment(new ProfileProviderFragment(), "myprofile");
                resideMenu.closeMenu();
            }

            setResideMenuItemIconColor(false, true, false);
        } else if (view == itemMyBooking) {
            textView_header.setText("My Booking");
            editProfile.setVisibility(View.GONE);
            Fragment myFragment = getSupportFragmentManager().findFragmentByTag("MyBooking");

            if (myFragment != null && myFragment.isVisible()) {
                changeFragment(new MyAppointmentFragment(""), "MyBooking");
                resideMenu.closeMenu();
            } else {
                changeFragment(new MyAppointmentFragment(""), "MyBooking");
                resideMenu.closeMenu();
            }

            setResideMenuItemIconColor(false, true, false);
        } else if (view == itemNotification) {
            textView_header.setText("Notifications");
            editProfile.setVisibility(View.GONE);
            Fragment myFragment = getSupportFragmentManager().findFragmentByTag("itemNotification");

            if (myFragment != null && myFragment.isVisible()) {
                changeFragment(new NotificationFragment(), "itemNotification");
                resideMenu.closeMenu();
            } else {
                changeFragment(new NotificationFragment(), "itemNotification");
                resideMenu.closeMenu();
            }

            setResideMenuItemIconColor(false, true, false);
        } else if (view == itemContact) {
            textView_header.setText("Contact As");
            editProfile.setVisibility(View.GONE);
            Fragment myFragment = getSupportFragmentManager().findFragmentByTag("Contact");

            if (myFragment != null && myFragment.isVisible()) {
                resideMenu.closeMenu();
            } else {
                changeFragment(new ContactAsFragment(), "Contact");
                resideMenu.closeMenu();
            }

            setResideMenuItemIconColor(false, true, false);
        } else if (view == itemFAQ) {
            textView_header.setText("FAQ");
            editProfile.setVisibility(View.GONE);
            Fragment myFragment = getSupportFragmentManager().findFragmentByTag("PhotoFragment");

            if (myFragment != null && myFragment.isVisible()) {
                resideMenu.closeMenu();
            } else {
                changeFragment(new FaqFragment(), "FaqFragment()");
                resideMenu.closeMenu();
            }

            setResideMenuItemIconColor(false, true, false);
        }else if (view == itemShare) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Hey check out ZOMINDIA app at: https://play.google.com/store/apps/details?id=com.zomindianew&hl=en");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
            setResideMenuItemIconColor(false, true, false);
        } else if (view == itemLogout) {
            showLogoutDialog();
            setResideMenuItemIconColor(false, true, false);
        }
        resideMenu.closeMenu();
    }


    @Override
    protected void onResume() {
        super.onResume();
        resideMenu.updateProfile(MySharedPreferances.getInstance(HomeActivityProvider.this).getString(Constants.PROFILE_IMAGE),
                MySharedPreferances.getInstance(HomeActivityProvider.this).getString(Constants.FIRST_NAME)
                        + " " + MySharedPreferances.getInstance(HomeActivityProvider.this).getString(Constants.LAST_NAME),
                MySharedPreferances.getInstance(HomeActivityProvider.this).getString(Constants.MOBILE));

    }


    private void showLogoutDialog() {
        final Dialog dialog = new Dialog(HomeActivityProvider.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_log_out);
        TextView textViewYes = dialog.findViewById(R.id.button_ok);
        TextView textViewNo = dialog.findViewById(R.id.button_cancel);
        textViewYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (Constants.isInternetOn(HomeActivityProvider.this)) {
                    logOutApi();
                } else {
                    final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) HomeActivityProvider.this.findViewById(android.R.id.content)).getChildAt(0);
                    showSnackbar(viewGroup, getResources().getString(R.string.no_internet), "Retry");
                }


            }
        });
        textViewNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        dialog.show();

    }


    public void logOutApi() {
        Api api = ApiFactory.getClient(HomeActivityProvider.this).create(Api.class);
        Call<ResponseBody> call;
        String accessToken = MySharedPreferances.getInstance(HomeActivityProvider.this).getString(Constants.ACCESS_TOKEN);
        call = api.logOutAPI(accessToken);
        Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT ------------------->>>>>" + " " + call.request().url());
        Log.e(Constants.LOG_CAT, "HEADERS : " + call.request().headers());
        Constants.showProgressDialog(HomeActivityProvider.this, Constants.LOADING);
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
                            Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT Response================>>>>>" + object.toString());
                            MySharedPreferances.getInstance(HomeActivityProvider.this).clearPref();
                            Intent intent = new Intent(HomeActivityProvider.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finish();

                        } else if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.FALSE)) {
                            Constants.showFalseMessage(object, HomeActivityProvider.this);
                        }
                    } else if (response.code() == 400 || response.code() == 500 || response.code() == 403 || response.code() == 404 || response.code() == 401) {
                        if (response.code() == 401) {
                            Constants.showSessionExpireAlert(HomeActivityProvider.this);
                        } else {
                            Constants.showToastAlert(getResources().getString(R.string.failled), HomeActivityProvider.this);
                        }

                    } else {
                        String responseStr = ErrorUtils.getResponseBody(response);
                        JSONObject jsonObject = new JSONObject(responseStr);
                        Constants.showToastAlert(getResources().getString(R.string.failled), HomeActivityProvider.this);
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


    //
//    @Override
//    public void onBackPressed() {
//        long pressTime = System.currentTimeMillis();
//        if (pressTime - lastPressTime <= DOUBLE_PRESS_INTERVAL) {
//            finish();
//            mHasDoubleClicked = true;
//        } else {     // If not double click....
//            mHasDoubleClicked = false;
//            Handler myHandler = new Handler() {
//                public void handleMessage(Message m) {
//                    if (!mHasDoubleClicked) {
//                        Toast.makeText(getApplicationContext(), "Please tap to double for exit!!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            };
//            Message m = new Message();
//            myHandler.sendMessageDelayed(m, DOUBLE_PRESS_INTERVAL);
//            lastPressTime = pressTime;
//        }
//
//    }
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(getApplicationContext(), "Double tap to exit.", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
            Log.e(Constants.LOG_CAT, "openMenu: ");
        }

        @Override
        public void closeMenu() {
            Log.e(Constants.LOG_CAT, "openMenu: ");
        }
    };

    public void changeFragment(Fragment targetFragment, String str) {
        resideMenu.clearIgnoredViewList();
        Log.e("Done...................", "onWorked: resume ");
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, str)
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    @Override
    protected void networkConnnectivityChange() {
        super.networkConnnectivityChange();
        if (!Constants.isInternetOn(HomeActivityProvider.this)) {
            final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                    .findViewById(android.R.id.content)).getChildAt(0);
            showSnackbar(viewGroup, getResources().getString(R.string.no_internet), "Retry");
        } else {

            hideSnackbar();
        }
    }


    // What good method is to access resideMenu？
    public ResideMenu getResideMenu() {
        return resideMenu;
    }

}

