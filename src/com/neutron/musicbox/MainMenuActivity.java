package com.neutron.musicbox;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class MainMenuActivity extends Activity {

	
	Button newVideo;
	Button videoList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		
		newVideo =(Button)findViewById(R.id.newvideo);
		videoList =(Button)findViewById(R.id.videolist);
		
		Display display = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();
//		
//
//
		LinearLayout.LayoutParams params = (LayoutParams) newVideo.getLayoutParams();
		//Then: change the width of the button
		params.width = width/10; //or whatever relative size you want.
		params.height = width/3;
		//Then set the modefied LayoutParams to your button.
		newVideo.setLayoutParams(params);
		
		LinearLayout.LayoutParams params2 = (LayoutParams) videoList.getLayoutParams();
		//Then: change the width of the button
		params2.width = width/10; //or whatever relative size you want.
		params2.height = width/3;
		//Then set the modefied LayoutParams to your button.
		videoList.setLayoutParams(params2);
	}
}
