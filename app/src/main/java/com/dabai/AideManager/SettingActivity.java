package com.dabai.AideManager;
import android.app.*;
import android.os.*;
import android.preference.*;
import java.io.*;
import android.view.*;
import android.content.*;
import android.net.*;
import android.widget.*;
import android.widget.NumberPicker.*;
import android.widget.PopupWindow.*;
import android.content.pm.*;

public class SettingActivity extends PreferenceActivity
{

	String linka ="https://www.coolapk.com/apk/";
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.setting);
		context = getApplicationContext();



        final Preference ver = getPreferenceManager().findPreference("appVersion");
		//change preference version name;
		ver.setSummary(new DabaiUtils().getVersionName(getApplicationContext()));


	}

	@Override
	protected void onPause()
	{
		// TODO: Implement this method
		super.onPause();

		EditTextPreference etp = (EditTextPreference)findPreference("text");
		SwitchPreference spe = (SwitchPreference)findPreference("addVerName");

		SharedPreferences sp = this.getSharedPreferences("data", 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("ic_name", etp.getText());
		editor.putBoolean("add_ver", spe.isChecked());


		if (editor.commit())
		{
			//Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
		}

	}



	//菜单 
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.setting, menu);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
    public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.finish:
				finish();
				break;
        }
        return super.onOptionsItemSelected(item);
    }



	



	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference)
	{

		switch (preference.getKey())
		{

			case "daoproject":

				new AlertDialog.Builder(this)
					.setTitle("注意")
					.setMessage("导入的项目压缩包，不能含有中文字符，包内所有文件名不能含有中文字符！")
					.setPositiveButton("确认",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {
							
							
							Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
							intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
							intent.addCategory(Intent.CATEGORY_OPENABLE);
							startActivityForResult(intent, 5);
						
							
							}
					}) 
					.setNeutralButton("取消", null)
					.show();
				
				break;

			case "delcache":

				new AlertDialog.Builder(this)
					.setTitle("要删除缓存嘛？")
					.setMessage("确定一下，就会让缓存升天。")
					.setPositiveButton("确认",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i)
						{
							File pardir = new File("/sdcard/AIDE项目管理工具/");
							for (File a : pardir.listFiles())
							{
								new DabaiUtils().deleteDir(a);
						
							}
							new DabaiUtils().
							toast(getApplicationContext(),"删除缓存结束");


						}
					}) 
					.setNeutralButton("取消", null)
					.show();

				break;

			case "appVersion":

				Uri uri=Uri.parse(linka + getPackageName());

				startActivity(new Intent(Intent.ACTION_VIEW, uri));

				break;

			case "about":

				startActivity(new Intent(this, AboutActivity.class));

				break;



		}


		return true;
	}


	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == Activity.RESULT_OK)
		{
            Uri uri = data.getData();
			String path = new DabaiUtils().UritoPath(getApplicationContext(),uri);
			
			if (requestCode == 5)
			{
				
				XZip xz = new XZip();
				try
				{
				
						
					new DabaiUtils().unzip_DirFile(new File(path),"/sdcard/AppProjects/");
					
				
					if(new DabaiUtils().isContainChinese(new File(path).getName())){
						new DabaiUtils().toast(getApplicationContext(),"提示:导入成功,但中文字符会有乱码情况");
					}else{
						new DabaiUtils().toast(getApplicationContext(),"提示:导入成功");

					}
					
					}
				catch (Exception e)
				{}

				
				
			}
		}
	}






}
