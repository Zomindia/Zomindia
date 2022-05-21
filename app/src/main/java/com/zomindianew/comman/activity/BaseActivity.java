package com.zomindianew.comman.activity;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.theartofdev.edmodo.cropper.CropImage;
import com.zomindianew.R;
import com.zomindianew.SPOTP;
import com.zomindianew.SmsBroadcastReceiver;
import com.zomindianew.helper.MySharedPreferances;
import com.zomindianew.providernew.activity.EditProfileActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;


/**
 * Created by Ghanshyam on 02/01/2017.
 */
public abstract class BaseActivity extends AppCompatActivity {

    String[] permission;
    Snackbar snackbar;
    public MySharedPreferances appPreference;
    private SmsBroadcastReceiver smsBroadcastReceiver;
    private OTPListener otpListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permission = null;
        setFont(String.valueOf(R.string.font_josefinsans_medium));
        setFont(String.valueOf(R.string.font_quicksand_medium));
        setFont(String.valueOf(R.string.font_quicksand_regular));
        appPreference = MySharedPreferances.getInstance(BaseActivity.this);
        smsBroadcastReceiver = new SmsBroadcastReceiver();
    }

    public void checkRequiredPermission(String[] permission) {
        this.permission = permission;
        checkPermissions(permission);
    }

    public void setFont(String font) {
        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath(font)
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());
    }

    /**
     * called when required permission is granted to notify in child class need to override this
     */
    protected void invokedWhenPermissionGranted() {

    }

    /**
     * called when required permission is not or allready granted to notify in child class need to override this
     */
    protected void invokedWhenNoOrAllreadyPermissionGranted() {

    }

    /**
     * check the permission
     *
     * @param permission
     */
    private void checkPermissions(String... permission) {

        if (Build.VERSION.SDK_INT >= 23 && permission != null) {

            int result;
            List<String> listPermissionsNeeded = new ArrayList<>();
            for (String p : permission) {
                result = ContextCompat.checkSelfPermission(this, p);
                if (result != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(p);
                }
            }
            if (!listPermissionsNeeded.isEmpty()) {

                ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 111);

            } else {

                invokedWhenNoOrAllreadyPermissionGranted();

            }

        } else {

            invokedWhenNoOrAllreadyPermissionGranted();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 111 && hasAllPermissionsGranted(grantResults)) {
            allPermissionsGranted();
        } else if (requestCode == 111) {
            invokedWhenDeniedWithResult(grantResults);
        }
    }

    /**
     * called when all required permission is checked and granted
     */
    private void allPermissionsGranted() {
        invokedWhenPermissionGranted();
    }

    /**
     * check and show denied permission to notify in child class need to Override this
     *
     * @param grantResults
     */
    protected void invokedWhenDeniedWithResult(int[] grantResults) {

    }

    /**
     * check all permission granted
     *
     * @param grantResults
     * @return
     */
    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    boolean mIsReceiverRegistered;

    private void registerReceiver() {
        if (!mIsReceiverRegistered) {
            registerReceiver(networkReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
            registerReceiver(gpsReceiver, new IntentFilter("android.location.PROVIDERS_CHANGED"));
            mIsReceiverRegistered = true;
        }
    }

    private void unRegisterReceiver() {
        if (mIsReceiverRegistered) {
            unregisterReceiver(networkReceiver);
            unregisterReceiver(gpsReceiver);
            mIsReceiverRegistered = false;
        }
    }

    /**
     * override this method when you need to check connectivity in child class
     */
    protected void networkConnnectivityChange() {


    }

    private BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction().matches("android.net.conn.CONNECTIVITY_CHANGE")) {
                networkConnnectivityChange();
            }
        }
    };


    /**
     * override this method when you need to check location enabelity in child class
     */
    public void checkGpsConnectivity() {


    }


    private BroadcastReceiver gpsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
                checkGpsConnectivity();
            }
        }
    };

    protected void onSnackbarAction() {
    }

    public void showSnackbar(View view, String text1, String text2) {

        try {
            if (snackbar == null) {
                snackbar = Snackbar
                        .make(view, text1, Snackbar.LENGTH_INDEFINITE)
                        .setAction(text2, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                onSnackbarAction();
                            }
                        });

                // Changing message text color
                snackbar.setActionTextColor(getResources().getColor(R.color.yellow));

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

    public void hideSnackbar() {
        try {
            if (snackbar != null && snackbar.isShown()) {
                snackbar.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    protected void startSmsUserConsent() {
        SmsRetrieverClient client = SmsRetriever.getClient(this);
        //We can add sender phone number or leave it blank
        // I'm adding null here
        client.startSmsUserConsent(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        // Inside OnCreate Method
        registerReceiver(broadcastReceiver, new IntentFilter("broadCastName"));


// Add this inside your class


    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle extras = intent.getExtras();
            Status smsRetrieverStatus = (Status) extras.get(SmsRetriever.EXTRA_STATUS);
            switch (smsRetrieverStatus.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:

                    /*Intent messageIntent = extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT);
                    startActivityForResult(messageIntent, 2906);*/
                    //smsBroadcastReceiverListener.onSuccess(messageIntent);
                    break;
                case CommonStatusCodes.TIMEOUT:
                    //smsBroadcastReceiverListener.onFailure();
                    break;
            }
        }
    };

    private void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcastReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2906) {
            if ((resultCode == RESULT_OK) && (data != null)) {
                //That gives all message to us.
                // We need to get the code from inside with regex
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);

                getOtpFromMessage(message);
            }
        }
    }

    private void getOtpFromMessage(String message) {
        // This will match any 6 digit number in the message
        Pattern pattern = Pattern.compile("(|^)\\d{6}");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            String otp = matcher.group(0);

            try {
                SPOTP.setOTP(this,otp);
                SPOTP.setTime(this,System.currentTimeMillis());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (otpListener != null) {
                otpListener.getOTP(otp);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(smsBroadcastReceiver);
    }

    public interface OTPListener {
        public void getOTP(String otp);
    }

    public void setOTPListener(OTPListener otpListener) {
        this.otpListener = otpListener;
    }



}
