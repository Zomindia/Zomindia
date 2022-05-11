package com.zomindianew.comman.activity;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.appevents.AppEventsLogger;
import com.zomindianew.R;
import com.zomindianew.helper.CircleAnimationUtil;
import com.zomindianew.helper.Constants;
import com.zomindianew.helper.MySharedPreferances;
import com.zomindianew.providernew.activity.HomeActivityProvider;
import com.zomindianew.user.activity.HomeActivityUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SplashActivty extends BaseActivity {

    private String access_token = "";
    private String user_role = "";

    private ImageView logo;
    private ImageView destLogo;
    RelativeLayout getStarted;
    private ImageView target_logo;
    private int[] layouts;
    FrameLayout frameLayout;
    private RelativeLayout getStartedRelativeLayout;
    private ImageView tv_heading;
    private TextView dialogHeaderText;

    SharedPreferences sharedpreferences;
    String get_email,get_mobile,get_uname,get_id,get_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        sharedpreferences = getApplicationContext().getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);
        get_email= sharedpreferences.getString("email", "");
        get_mobile= sharedpreferences.getString("mobile", "");
        get_uname= sharedpreferences.getString("uname", "");
        get_id= sharedpreferences.getString("id", "");
        get_token= sharedpreferences.getString("accesstoken", "");

        tv_heading = (ImageView) findViewById(R.id.tv_heading);
        dialogHeaderText = findViewById(R.id.dialogHeaderText);
        target_logo = (ImageView) findViewById(R.id.target_logo);
        logo = (ImageView) findViewById(R.id.logo);
        destLogo = (ImageView) findViewById(R.id.destLogo);
        getStartNEW();

        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("Splash Event");



    }

    private void getStartNEW() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                getStart();
               // makeFlyAnimation(destLogo);
//                }
            }
        }, 200);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }


    private void makeFlyAnimation(ImageView targetView) {


        new CircleAnimationUtil().attachActivity(this).setTargetView(targetView).setMoveDuration(1000).setDestView(target_logo).setAnimationListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                logo.setVisibility(View.GONE);
                tv_heading.setVisibility(View.VISIBLE);
                dialogHeaderText.setVisibility(View.VISIBLE);
                target_logo.setVisibility(View.GONE);
                destLogo.setVisibility(View.GONE);

                //    imageView_fav_icon.setImageResource(R.mipmap.fav_orange_star);
                //   Toast.makeText(MainActivity.this, "Continue Shopping...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).startAnimation();
    }


    private void getStart() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

//                if (get_token.equals(""))
//                {
//                    Intent intent_login = new Intent(SplashActivty.this, WelcomeLogin.class);
//                    startActivity(intent_login);
//                }
//                else{
//                    Intent intent_login = new Intent(SplashActivty.this, HomeActivity.class);
//                    startActivity(intent_login);
//                }
                MySharedPreferances appPreference = MySharedPreferances.getInstance(SplashActivty.this);
                access_token = appPreference.getString(Constants.ACCESS_TOKEN);
                if (access_token.equalsIgnoreCase("")) {
                    Intent intent_login = new Intent(SplashActivty.this, SelectionActivity.class);
                    startActivity(intent_login);
                    finish();
                }
                else {

//                    user_role = appPreference.getString(Constants.USER_ROLE);
//                    if (appPreference.getString(Constants.PROFILE_COMPLETE).equalsIgnoreCase("false")) {
//                        Intent intent = new Intent(SplashActivty.this, EditProfileActivity.class);
//                        startActivity(intent);
//                        finishAffinity();
//                    } else if (appPreference.getString(Constants.PROFILE_APPORVED).equalsIgnoreCase("false")) {
//                        Intent intent = new Intent(SplashActivty.this, UnderReviewActivty.class);
//                        startActivity(intent);
//                        finishAffinity();
//                    } else {
//                        if (user_role.equalsIgnoreCase("provider")) {
//                            Intent intent_login = new Intent(SplashActivty.this, HomeActivityProvider.class);
//                            startActivity(intent_login);
//                            finish();
//                        } else {
//                            Intent intent_login = new Intent(SplashActivty.this, HomeActivity.class);
//                            startActivity(intent_login);
//                            finish();
//                        }
//
//                    }
                    if(appPreference.getString(Constants.USER_ROLE).equals("provider")){
                        if(appPreference.getString(Constants.STATUS).equalsIgnoreCase("disapprove")){
                            Intent intent_login = new Intent(SplashActivty.this, UnderReviewActivty.class);
                            startActivity(intent_login);
                            finish();
                        }else{
                            Intent intent_login = new Intent(SplashActivty.this, HomeActivityProvider.class);
                            startActivity(intent_login);
                            finish();
                        }

                    }else {
                        Intent intent_login = new Intent(SplashActivty.this, HomeActivityUser.class);
                        startActivity(intent_login);
                        finish();
                    }
                }


            }
        }, 1000);
    }
}
