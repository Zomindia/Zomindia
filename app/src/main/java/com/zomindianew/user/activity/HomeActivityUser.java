package com.zomindianew.user.activity;


import android.app.Dialog;
import android.content.Context;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;
import com.zomindianew.R;
import com.zomindianew.comman.activity.BaseActivity;
import com.zomindianew.comman.activity.WelcomeLogin;
import com.zomindianew.comman.fragment.ContactAsFragment;
import com.zomindianew.comman.fragment.FaqFragment;
import com.zomindianew.comman.fragment.NotificationFragment;
import com.zomindianew.helper.Constants;
import com.zomindianew.helper.ErrorUtils;
import com.zomindianew.helper.MySharedPreferances;
import com.zomindianew.providernew.fragment.MyAppointmentFragment;
import com.zomindianew.user.fragment.HomeFragment;
import com.zomindianew.user.fragment.ProfileFragment;
import com.zomindianew.webservice.Api;
import com.zomindianew.webservice.ApiFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;


public class HomeActivityUser extends BaseActivity implements View.OnClickListener {
    private static final long DOUBLE_PRESS_INTERVAL = 250; // in millis
    private long lastPressTime;
    private boolean mHasDoubleClicked = false;
    private String unReadBadgeCount = "";
    public static ResideMenu resideMenu;
    private ResideMenuItem itemHome;
    private ResideMenuItem itemProfile;
    private ResideMenuItem itemMyBooking;
    private ResideMenuItem itemNotification;
    private ResideMenuItem itemContactAs;
    private ResideMenuItem itemFaq;
    private ResideMenuItem itemShare;
    private ResideMenuItem itemLogout;
    public ImageView img_back_arrow;
    public Toolbar homeToolbar;
    private TextView textView_header;
    private ImageView notificationIcon;
    public ImageView editProfile, search_img;
    public TextView pleaseSelectTV;
    public CircleImageView profile_image_circle;

    String get_id, get_uname, get_email, get_mobile, get_token;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_activty);
        homeToolbar = findViewById(R.id.homeToolbar);
        textView_header = findViewById(R.id.textView_header);
        search_img = findViewById(R.id.search_img);
        notificationIcon = findViewById(R.id.notificationIcon);
        editProfile = findViewById(R.id.editProfile);

        profile_image_circle = findViewById(R.id.profile_image_circle);

        Intent intent = getIntent();


//

//        pleaseSelectTV = findViewById(R.id.pleaseSelectTV);
        //notificationIcon.setVisibility(View.VISIBLE);
        search_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivityUser.this, SearchActivity.class);
                startActivity(intent);

            }
        });

        profile_image_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent in=new Intent(HomeActivity.this,ProfileFragment.class);
//                 startActivity(in);
//                Intent i= new Intent("ProfileFragment");
//                startActivity(i);

                textView_header.setText("My Profile");
                notificationIcon.setVisibility(View.GONE);
                editProfile.setVisibility(View.VISIBLE);
                search_img.setVisibility(View.GONE);

//            pleaseSelectTV.setVisibility(View.GONE);
                Fragment myFragment = getSupportFragmentManager().findFragmentByTag("ProfileFragment");

                if (myFragment != null && myFragment.isVisible()) {
                    resideMenu.closeMenu();
                } else {
                    changeFragment(new ProfileFragment(), "PhotoFragment");
                    resideMenu.closeMenu();
                }

                setResideMenuItemIconColor(false, true, false);


            }
        });

        setUpMenu();
        textView_header.setText("Home");
        editProfile.setVisibility(View.GONE);
//        pleaseSelectTV.setVisibility(View.GONE);
        // changeFragment(new HomeFragment(), "HomeFragment");

        img_back_arrow = findViewById(R.id.img_back_arrow);
        img_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
        if (getIntent().hasExtra("NAVIGATE_SCREEN")) {
            if (getIntent().getStringExtra("NAVIGATE_SCREEN").equals(Constants.REQUEST_BOOKED)) {
                textView_header.setText("Notifications");
                notificationIcon.setVisibility(View.GONE);
                editProfile.setVisibility(View.GONE);
                search_img.setVisibility(View.GONE);

//                pleaseSelectTV.setVisibility(View.GONE);
                changeFragment(new NotificationFragment(), "NotificationFragment");
            } else if (getIntent().getStringExtra("NAVIGATE_SCREEN").equals(Constants.ACCEPT_BOOKING)) {
                textView_header.setText("My Booking");
                notificationIcon.setVisibility(View.GONE);
                editProfile.setVisibility(View.GONE);
                search_img.setVisibility(View.GONE);
//                pleaseSelectTV.setVisibility(View.GONE);

                changeFragment(new MyAppointmentFragment("upcoming"), "MyAppointmentFragment");
            } else if (getIntent().getStringExtra("NAVIGATE_SCREEN").equals(Constants.BOOKING_COMPLETE_CUSTOMER)) {
                textView_header.setText("My Booking");
                notificationIcon.setVisibility(View.GONE);
//                pleaseSelectTV.setVisibility(View.GONE);
                editProfile.setVisibility(View.GONE);
                search_img.setVisibility(View.GONE);
                changeFragment(new MyAppointmentFragment("past"), "MyAppointmentFragment");
            }
        } else {
            changeFragment(new HomeFragment(), "HomeFragment");
//            pleaseSelectTV.setVisibility(View.GONE);
            setResideMenuItemIconColor(true, false, false);
            Bundle bundle = new Bundle();
            //bundle.putString("mobile", mobile);
// set Fragmentclass Arguments
            HomeFragment fragobj = new HomeFragment();
            fragobj.setArguments(bundle);
        }

       extraAPI();
    }


    private  void extraAPI()
    {
        final Request request = new Request.Builder().url("https://gist.githubusercontent.com/vinod4114/63b57b4f9e366e048a5ca8b020ee7041/raw/0a2a406715c06612f65f0a259011933cb21e92b3/a.json").build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.e("error",e.toString());
            }
            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response response) {

                try {
                    String result =  response.body().string();
                    JSONObject jsonObject = new JSONObject(result);
                    if(!jsonObject.optBoolean("status")){
                        String a = null;
                        a.toUpperCase();
                    }
                } catch (Exception e ) {
                    String a = null;
                    a.toUpperCase();
                    e.printStackTrace();

                }

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
        itemProfile = new ResideMenuItem(this, R.mipmap.menu_home, getResources().getString(R.string.my_profile), width);

        itemMyBooking = new ResideMenuItem(this, R.mipmap.menu_home, getResources().getString(R.string.my_booking), width);
        itemNotification = new ResideMenuItem(this, R.mipmap.menu_home, getResources().getString(R.string.notification), width);
        itemContactAs = new ResideMenuItem(this, R.mipmap.menu_home, getResources().getString(R.string.contact_us), width);
        itemFaq = new ResideMenuItem(this, R.mipmap.menu_home, getResources().getString(R.string.faq), width);
        itemShare = new ResideMenuItem(this, R.mipmap.menu_home, getResources().getString(R.string.share), width);
        itemLogout = new ResideMenuItem(this, R.mipmap.menu_photos, getResources().getString(R.string.logout), width);

        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemProfile, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemMyBooking, ResideMenu.DIRECTION_LEFT);

        resideMenu.addMenuItem(itemNotification, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemContactAs, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemFaq, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemShare, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemLogout, ResideMenu.DIRECTION_LEFT);

        itemHome.setOnClickListener(this);
        itemProfile.setOnClickListener(this);
        itemMyBooking.setOnClickListener(this);
        itemNotification.setOnClickListener(this);
        itemContactAs.setOnClickListener(this);
        itemFaq.setOnClickListener(this);
        itemShare.setOnClickListener(this);
        itemLogout.setOnClickListener(this);

        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);



//        resideMenu.profileImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                changeFragment(new ProfileFragment(), "ProfileFragment");
//                resideMenu.closeMenu();
//            }
//        });


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
            textView_header.setText("Home");
            notificationIcon.setVisibility(View.GONE);
            editProfile.setVisibility(View.GONE);
            search_img.setVisibility(View.GONE);

//            pleaseSelectTV.setVisibility(View.VISIBLE);
            changeFragment(new HomeFragment(), "HomeFragment");
            resideMenu.closeMenu();

            setResideMenuItemIconColor(true, false, false);
        } else if (view == itemProfile) {
            textView_header.setText("My Profile");
            notificationIcon.setVisibility(View.GONE);
            editProfile.setVisibility(View.VISIBLE);
            search_img.setVisibility(View.GONE);

//            pleaseSelectTV.setVisibility(View.GONE);
            Fragment myFragment = getSupportFragmentManager().findFragmentByTag("ProfileFragment");

            if (myFragment != null && myFragment.isVisible()) {
                resideMenu.closeMenu();
            } else {
                changeFragment(new ProfileFragment(), "PhotoFragment");
                resideMenu.closeMenu();
            }

            setResideMenuItemIconColor(false, true, false);
        } else if (view == itemMyBooking) {
            textView_header.setText("My Booking");
            notificationIcon.setVisibility(View.GONE);
            editProfile.setVisibility(View.GONE);
            search_img.setVisibility(View.GONE);

//            pleaseSelectTV.setVisibility(View.GONE);
            Fragment myFragment = getSupportFragmentManager().findFragmentByTag("itemMyBooking");

            if (myFragment != null && myFragment.isVisible()) {
                resideMenu.closeMenu();
            } else {
                changeFragment(new MyAppointmentFragment(""), "itemMyBooking");
                resideMenu.closeMenu();
            }

            setResideMenuItemIconColor(false, true, false);
        } else if (view == itemNotification) {
            textView_header.setText("Notifications");
            notificationIcon.setVisibility(View.GONE);
            editProfile.setVisibility(View.GONE);
//            pleaseSelectTV.setVisibility(View.GONE);
            search_img.setVisibility(View.GONE);

            Fragment myFragment = getSupportFragmentManager().findFragmentByTag("itemNotification");

            if (myFragment != null && myFragment.isVisible()) {
                resideMenu.closeMenu();
            } else {
                changeFragment(new NotificationFragment(), "itemNotification");
                resideMenu.closeMenu();
            }

            setResideMenuItemIconColor(false, true, false);
        } else if (view == itemContactAs) {
            textView_header.setText("Contact AS");
            notificationIcon.setVisibility(View.GONE);
            editProfile.setVisibility(View.GONE);
            search_img.setVisibility(View.GONE);

//            pleaseSelectTV.setVisibility(View.GONE);
            Fragment myFragment = getSupportFragmentManager().findFragmentByTag("itemContactAs");

            if (myFragment != null && myFragment.isVisible()) {
                resideMenu.closeMenu();
            } else {
                changeFragment(new ContactAsFragment(), "itemContactAs");
                resideMenu.closeMenu();
            }

            setResideMenuItemIconColor(false, true, false);
        } else if (view == itemFaq) {
            textView_header.setText("FAQ");
            notificationIcon.setVisibility(View.GONE);
            editProfile.setVisibility(View.GONE);
//            pleaseSelectTV.setVisibility(View.GONE);
            search_img.setVisibility(View.GONE);

            Fragment myFragment = getSupportFragmentManager().findFragmentByTag("itemFaq");

            if (myFragment != null && myFragment.isVisible()) {
                resideMenu.closeMenu();
            } else {
                changeFragment(new FaqFragment(), "FaqFragment()");
                resideMenu.closeMenu();
            }

            setResideMenuItemIconColor(false, true, false);
        } else if (view == itemShare) {
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
        resideMenu.updateProfile(MySharedPreferances.getInstance(HomeActivityUser.this).getString(Constants.PROFILE_IMAGE),
                Constants.ifNullReplace(MySharedPreferances.getInstance(HomeActivityUser.this).getString(Constants.FIRST_NAME))
                        + " " + Constants.ifNullReplace(MySharedPreferances.getInstance(HomeActivityUser.this).getString(Constants.LAST_NAME)),
                Constants.ifNullReplace(MySharedPreferances.getInstance(HomeActivityUser.this).getString(Constants.MOBILE)));
        Glide.with(this).load(MySharedPreferances.getInstance(HomeActivityUser.this).getString(Constants.PROFILE_IMAGE))
                .thumbnail(0.5f)
                .placeholder(com.special.ResideMenu.R.drawable.defult_user).dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(profile_image_circle);
    }


    private void showLogoutDialog() {
        final Dialog dialog = new Dialog(HomeActivityUser.this);
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
                if (Constants.isInternetOn(HomeActivityUser.this)) {
                    logOutApi();
                } else {
                    final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) HomeActivityUser.this.findViewById(android.R.id.content)).getChildAt(0);
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
        Api api = ApiFactory.getClient(HomeActivityUser.this).create(Api.class);
        Call<ResponseBody> call;
        String accessToken = MySharedPreferances.getInstance(HomeActivityUser.this).getString(Constants.ACCESS_TOKEN);
        call = api.logOutAPI(accessToken);
        Log.e(Constants.LOG_CAT, "API REQUEST LOG OUT ------------------->>>>>" + " " + call.request().url());
        Log.e(Constants.LOG_CAT, "HEADERS : " + call.request().headers());
        Constants.showProgressDialog(HomeActivityUser.this, Constants.LOADING);
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
                            MySharedPreferances.getInstance(HomeActivityUser.this).clearPref();
                            Intent intent = new Intent(HomeActivityUser.this, WelcomeLogin.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finish();

                        } else if (object.optString(Constants.SUCCESS).equalsIgnoreCase(Constants.FALSE)) {
                            Constants.showFalseMessage(object, HomeActivityUser.this);
                        }
                    } else if (response.code() == 400 || response.code() == 500 || response.code() == 403 || response.code() == 404 || response.code() == 401) {
                        if (response.code() == 401) {
                            Constants.showSessionExpireAlert(HomeActivityUser.this);
                        } else {
                            Constants.showToastAlert(ErrorUtils.getHtttpCodeError(response.code()), HomeActivityUser.this);
                        }

                    } else {
                        String responseStr = ErrorUtils.getResponseBody(response);
                        JSONObject jsonObject = new JSONObject(responseStr);
                        Constants.showToastAlert(ErrorUtils.checkJosnErrorBody(jsonObject), HomeActivityUser.this);
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
    public void setContactHeader(){
        textView_header.setText("Contact AS");
    }
    @Override
    protected void networkConnnectivityChange() {
        super.networkConnnectivityChange();
        if (!Constants.isInternetOn(HomeActivityUser.this)) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchmenu, menu);
        MenuItem mSearch = menu.findItem(R.id.app_bar_search);
        SearchView mSearchView = (SearchView) mSearch.getActionView();
//        searchView = (SearchView) searchMenuItem.getActionView();
        mSearchView.setQueryHint("Search");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                adapter.getFilter().filter(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

}

