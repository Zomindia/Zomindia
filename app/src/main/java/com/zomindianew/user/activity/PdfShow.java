package com.zomindianew.user.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.zomindianew.R;

public class PdfShow extends AppCompatActivity {

    TextView textView_header;
    ImageView img_back_arrow;
    String title,Url;
    WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_show);

        String url = "https://drive.google.com/viewerng/viewer?embedded=true&url="+getIntent().getStringExtra("url");

        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new MyWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);

        textView_header = findViewById(R.id.textView_header);
        textView_header.setText(getIntent().getStringExtra("title"));

        title = getIntent().getStringExtra("subCategoryName");
        Url = getIntent().getStringExtra("url");


        img_back_arrow = findViewById(R.id.img_back_arrow);
        img_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            onBackPressed();

            }
        });
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}