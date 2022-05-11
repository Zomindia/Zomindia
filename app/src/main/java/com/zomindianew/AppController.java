package com.zomindianew;

import android.app.Application;
import android.content.Context;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import java.util.ArrayList;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;


public class AppController extends MultiDexApplication {

    private static AppController instance;
    public ArrayList<String> arrayList = new ArrayList<>();

    private void init(Application application) {
        instance = this;
    }

    public static synchronized AppController getInstance() {
        return instance;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init(this);
        initCalligraphy();


    }

    private void initCalligraphy() {
        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/ProximaNova-Regular.otf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());

        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/Digit.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());

    }


}
