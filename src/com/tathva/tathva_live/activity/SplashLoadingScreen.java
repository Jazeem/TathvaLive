package com.tathva.tathva_live.activity;

import java.io.InputStream;

import com.tathva.tathva_live.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class SplashLoadingScreen extends Activity {

	ImageView loading;
	AnimationDrawable loadingAnimation;
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.splashloadingscreen);
		
		
		
		loading=(ImageView) findViewById(R.id.loading);
		loading.setBackgroundResource(R.drawable.loadinganimation);
		loadingAnimation=(AnimationDrawable) loading.getBackground();
		
		

		int secondsDelayed = 3;
		new Handler().postDelayed(new Runnable() {
			public void run() {

				startActivity(new Intent(SplashLoadingScreen.this, Main.class));
				finish();
			}
		}, 3500);

	

	}



public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    if(hasFocus){

    	loadingAnimation.start();

        }
    }
}
