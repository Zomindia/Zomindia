package com.zomindianew.comman.activity;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.facebook.appevents.AppEventsLogger;
import com.zomindianew.R;

public class WelcomeLogin extends AppCompatActivity {
    private TextView textView_login_otp,textView_login_email,partner_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_login);
        textView_login_otp = (TextView) findViewById(R.id.login_by_otp);
        textView_login_email = (TextView) findViewById(R.id.login_by_email);
        partner_login = (TextView) findViewById(R.id.partner_login);
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("Welcome Event");

        partner_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(WelcomeLogin.this, PartnerSignupActivity.class);
                startActivity(intent);

            }
        });

        textView_login_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(WelcomeLogin.this, LoginByOTPActivity.class);
                startActivity(intent);

            }
        });
        textView_login_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(WelcomeLogin.this, LoginActivity.class);
                startActivity(intent);

            }
        });


    }


}
