package com.dabai.AideManager;
import android.app.*;
import android.os.*;
import android.widget.*;
import android.content.pm.*;

public class AboutActivity extends Activity
{
	
	TextView te1,te2,te3;
	ImageView im;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		im = (ImageView)findViewById(R.id.aboutImageView1);
		te1 = (TextView)findViewById(R.id.aboutTextView1);
		te2 = (TextView)findViewById(R.id.aboutTextView2);
		te3 = (TextView)findViewById(R.id.aboutTextView3);
		
		
		im.setImageDrawable(getDrawable(R.drawable.ic_launcher));
		te1.setText(getString(R.string.app_name));
		te2.setText("一款可以帮助你使用AIDE的工具APP");
		te3.setText(new DabaiUtils().getVersionName(getApplicationContext()));
		
	}
	
	
	
}
