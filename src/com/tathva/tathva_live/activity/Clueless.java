package com.tathva.tathva_live.activity;

import com.tathva.tathva_live.R;

import android.R.bool;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;

import android.os.Bundle;


import android.webkit.WebView;
import android.webkit.WebViewClient;


public class Clueless extends Activity{
	WebView web;
	Boolean onActivity=true;
	private static ProgressDialog pdialog;

	@SuppressLint("NewApi")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.clueless);
		web=(WebView) findViewById(R.id.webviewclueless);

	
		web.setWebViewClient(new myWebClient());          
		web.getSettings().setJavaScriptEnabled(true);
	    web.getSettings().setDomStorageEnabled(true);      
	    web.loadUrl("http://clueless.tathva.org"); 
	    
	    pdialog = new ProgressDialog(this);

		pdialog.setCancelable(true);
		//pdialog.setTitle("Please Wait");
		pdialog.setMessage("Please Wait...");
		pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		

	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		onActivity=true;
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		onActivity=false;
	}
	public class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            if(onActivity)
            pdialog.show();
        }
 
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
        		
            view.loadUrl(url);
            if(onActivity)
            pdialog.show();
            return true;
 
        }
        @Override
        public void onPageFinished(WebView view, String url)
        {
        	super.onPageFinished(view, url);
        	if(onActivity)
        	pdialog.hide();
        }
    }
}