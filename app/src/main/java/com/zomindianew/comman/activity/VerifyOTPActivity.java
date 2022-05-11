package com.zomindianew.comman.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zomindianew.R;

public class VerifyOTPActivity extends AppCompatActivity {
    String MobilePattern = "[0-9]{6}";
    EditText editTextotp;
    TextView textViewlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_o_t_p);
        editTextotp=(EditText)findViewById(R.id.edt_verifyotp);
        textViewlogin=(TextView)findViewById(R.id.btn_verifydone);

        textViewlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!editTextotp.getText().toString().trim().matches(MobilePattern)) {
                    editTextotp.setError("Enter 6Digit OTP.");
                    editTextotp.requestFocus();
                }
                else

                {
                    Intent intent = new Intent(VerifyOTPActivity.this, VerifyOTPActivity.class);
                    startActivity(intent);
                }

            }
        });


    }
}