package com.zomindianew.comman.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.appevents.AppEventsLogger;
import com.zomindianew.R;

public class SelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        try
        {
            AppEventsLogger logger = AppEventsLogger.newLogger(this);
            logger.logEvent("Selection Event");
        }
        catch (Exception e)
        {
            Log.e("error",e.toString());
        }


        findViewById(R.id.btnSelectionLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_login = new Intent(SelectionActivity.this, WelcomeLogin.class);
                startActivity(intent_login);
            }
        });
        findViewById(R.id.btnSelectionRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectionActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}