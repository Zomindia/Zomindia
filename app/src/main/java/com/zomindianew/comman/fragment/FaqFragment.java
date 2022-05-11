package com.zomindianew.comman.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.zomindianew.R;


public class FaqFragment extends Fragment implements View.OnClickListener {

    private View view;
    ProgressBar progressBar1 = null;
    private WebView webView;
    ViewGroup viewGroup;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_faq, container, false);
        viewGroup = container;

        intView();
        progressBar1.setMax(100);
        webView.setWebChromeClient(new MyWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://zomindia.com/zomindia-website-data/faq.html");
        //webView.loadUrl("http://service-provider.zomindia.com/faq");
        progressBar1.setProgress(0);


        return view;
    }

    private void intView() {
        progressBar1 = (ProgressBar) view.findViewById(R.id.progressBar1);
        webView = (WebView) view.findViewById(R.id.webView);
    }


    private class MyWebViewClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress >= 100) {
                progressBar1.setVisibility(View.GONE);
            }
            setValue(newProgress);
            super.onProgressChanged(view, newProgress);
        }
    }

    public void setValue(int progress) {
        progressBar1.setProgress(progress);
    }

    @Override
    public void onClick(View view) {

    }


}

