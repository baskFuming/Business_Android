package com.zwonline.top28.web;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

import com.zwonline.top28.R;

public class WebViewActivity extends ZWBaseActivity {

    private WebView mWebView;
    public String urlToLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Intent intent = getIntent();
        urlToLoad = intent.getStringExtra(WebViewFrag.URL_TO_LOAD);
        getSupportActionBar().setTitle(R.string.data_loading);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF2B2B")));
        boolean showActBar = intent.getBooleanExtra("showActBar",true);
        if(showActBar){
            getSupportActionBar().show();
        }else{
            getSupportActionBar().hide();
        }

        mWebViewFrag = new WebViewFrag();
        mWebViewFrag.urlToLoad = urlToLoad;
//        setupWebView();


        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.replace(R.id.actWebview, mWebViewFrag);
                ft.commit();
            }
        }

    }

    //HACK...
    //to make actionbar back button work as dismiss current task.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
//        return super.onOptionsItemSelected(item);
    }
}
