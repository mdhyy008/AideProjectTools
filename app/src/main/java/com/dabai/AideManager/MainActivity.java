package com.dabai.AideManager;
import android.app.*;
import android.widget.*;
import java.util.*;
import android.content.*;
import java.io.*;
import android.view.*;
import android.os.*;
import android.widget.AdapterView.*;
import android.util.*;
import java.text.*;
import org.apache.http.util.*;
import android.net.*;
import android.provider.*;
import android.database.*;
import android.annotation.*;
import android.preference.*;
import android.content.pm.*;


public class MainActivity extends Activity 
{

	DabaiUtils du;

	TextView te1,te2;
	ListView lv1,lv2;

	String root = "/sdcard/AppProjects/";

	File tmpfile;

	Context context;

	private ArrayAdapter adapter;

	private ArrayList<String> listdata1;
	private ArrayList<String> listdata2;

	private String path;

	private String icname;

	private File fie;

	private boolean addver;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		initactionbar();

		setContentView(R.layout.main);

		context = getApplicationContext();


		du = new DabaiUtils();
		te1 = (TextView)findViewById(R.id.mainTextView1);
		te2 = (TextView)findViewById(R.id.mainTextView2);

		lv1 = (ListView)findViewById(R.id.mainListView1);
		lv2 = (ListView)findViewById(R.id.mainListView2);


		listdata1 = new ArrayList<String>();
		listdata2 = new ArrayList<String>();

		f5();

		initval();
		init();

    }

	private void initactionbar()
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}


	public void tosetting(View v)
	{
		startActivity(new Intent(this, SettingActivity.class));
	}


	private void init()
	{
		File thisdir = new File("/sdcard/AIDE项目管理工具/");
		if (!thisdir.exists())
		{
			thisdir.mkdirs();
	}
	}


	String get_ic_name()
	{
		SharedPreferences sp = this.getSharedPreferences("data", 0);
		icname = sp.getString("ic_name", "ic_launcher.png");
		return icname;
	}

	boolean get_add_ver_name()
	{
		SharedPreferences sp = this.getSharedPreferences("data", 0);
		addver = sp.getBoolean("add_ver", false);
		return addver;
	}

	@Override
	protected void onResume()
	{
	
		super.onResume();
		f5();
		
	}


	

	private void initval()
	{
		// TODO: 控件初始化

		//单击事件



		//长按事件

		lv1.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
					// TODO: Implement this method
					String name = listdata1.get(p3);	
					String path = root + name;
					tmpfile = new File(path);


					showJavaPopupMenu(p2);

				}
			});
		lv2.setOnItemClickListener(new OnItemClickListener(){


				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
					// TODO: Implement this method
					String name = listdata2.get(p3);
					String path = root + name;
					tmpfile = new File(path);


					showAndroidPopupMenu(p2);

				}
			});


	}




	void add_data_lv1(ArrayList<String> listdata)
	{

		listdata1 = listdata;
		adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, listdata1);
		lv1.setAdapter(adapter);

	}

	void add_data_lv2(ArrayList<String> listdata)
	{

		listdata2 = listdata;
		adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, listdata2);
		lv2.setAdapter(adapter);

	}


	void f5()
	{


		ArrayList<String> tmplist1,tmplist2;
		tmplist1 = new ArrayList<String>();
		tmplist2 = new ArrayList<String>();


		File dirlist[] = new File(root).listFiles();

		for (File a : dirlist)
		{	
			if (a.isDirectory())
			{

				File b = new File(a.getAbsolutePath() + "/app");

				if (!b.exists())
				{
					tmplist1.add(a.getName());
				}
				else
				{
					tmplist2.add(a.getName());
				}

			}
		}


		add_data_lv1(tmplist1);
		add_data_lv2(tmplist2);


		if (tmplist1.size() <= 0)
		{
			te1.setVisibility(8);
		}
		else
		{
			te1.setVisibility(0);
			te1.setText("Java项目 - " + tmplist1.size());
		}

		if (tmplist2.size() <= 0)
		{
			te2.setVisibility(8);
		}
		else
		{
			te2.setVisibility(0);
			te2.setText("Android项目 - " + tmplist2.size());
		}


	}


	public void toast(Object text)
	{
		Toast.makeText(context, text + "", 1).show();
	}




	private void showJavaPopupMenu(View view)
	{
        // 这里的view代表popupMenu需要依附的view
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);

        // 获取布局文件
        popupMenu.getMenuInflater().inflate(R.menu.main_java_pop, popupMenu.getMenu());
        popupMenu.show();
        // 通过上面这几行代码，就可以把控件显示出来了
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item)
				{
					// 控件每一个item的点击事件
					switch (item.getItemId())
					{	
						case R.id.j_del:
							new AlertDialog.Builder(MainActivity.this)
								.setTitle("提示")
								.setMessage("项目名 : " + tmpfile.getName() + "\n项目路径 : " + tmpfile.getAbsolutePath() + "\n项目大小 : " + getDirSizes(tmpfile) + "B")
								.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialogInterface, int i)
									{
										du.deleteDir(tmpfile);
										f5();
									}
								}) 
								.setNeutralButton("取消", null)
								.show();

							break;
						case R.id.j_clone:
							toast("开始克隆...");
							new Thread() {
								@Override
								public void run()
								{
									super.run();
									//新线程操作
									SimpleDateFormat sdf = new SimpleDateFormat("hhmmss");
									du.copyDirectory(tmpfile, new File(tmpfile.getParent() + "/" + tmpfile.getName() + "_clone" + sdf.format(new Date())));

									runOnUiThread(new Runnable(){
											@Override
											public void run()
											{
												//更新UI操作
												f5();
											}

										});
								}
							}.start();



							break;

						case R.id.j_size:

							SimpleDateFormat sssf = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
							StringBuffer sb = new StringBuffer();

							sb.append("项目名 : " + tmpfile.getName() + "\n项目路径 : " + tmpfile.getAbsolutePath() + "\n项目大小 : " + getDirSizes(tmpfile) + "B");
							sb.append("\n");		
							sb.append("是否文件夹 : " + tmpfile.isDirectory());

							try
							{
								sb.append("\n");
								sb.append("Java文件数 : " + new File(tmpfile, "/src").list().length);
							}
							catch (Exception e)
							{
								toast("异常 : " + e.getMessage());
							}

							sb.append("\n");		
							sb.append("最后修改时间 : " + sssf.format(new Date(tmpfile.lastModified())));

							new AlertDialog.Builder(MainActivity.this)
								.setMessage(sb.toString())
								.show();

							f5();
							break;

						case R.id.j_pack:

							toast("打包开始");

							new Thread() {
								@Override
								public void run()
								{
									super.run();
									//新线程操作
									XZip xzip = new XZip();
									try
									{
										xzip.ZipFolder(tmpfile.getAbsolutePath(), "/sdcard/AIDE项目管理工具/" + tmpfile.getName() + ".zip");
									}
									catch (Exception e)
									{
										toast("异常 : " + e);
									}
									runOnUiThread(new Runnable(){
											@Override
											public void run()
											{
												toast("打包完成 - /sdcard/AIDE项目管理工具/");

											}

										});


								}
							}.start();


							break;


						case R.id.j_share_project:

							toast("打包开始");

							final String tag = "/sdcard/AIDE项目管理工具/" + tmpfile.getName() + ".zip";

							new Thread() {
								@Override
								public void run()
								{
									super.run();
									//新线程操作
									XZip xzip = new XZip();
									try
									{
										xzip.ZipFolder(tmpfile.getAbsolutePath(), tag);
									}
									catch (Exception e)
									{
										toast("异常 : " + e);
									}
									runOnUiThread(new Runnable(){
											@Override
											public void run()
											{
												toast("打包完成 - /sdcard/AIDE项目管理工具/");
												du.shareFile(context, tag);
											}

										});


								}
							}.start();


							break;


					}		

					return true;
				}
			});
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
				@Override
				public void onDismiss(PopupMenu menu)
				{
					// 控件消失时的事件
				}
			});

    }

	public static long getDirSizes(File f)
	{
        long size = 0;
        File flist[] = f.listFiles();//文件夹目录下的所有文件
        if (flist == null)
		{//4.2的模拟器空指针。
            return 0;
        }
        if (flist != null)
		{
            for (int i = 0; i < flist.length; i++)
			{
                if (flist[i].isDirectory())
				{//判断是否父目录下还有子目录
                    size = size + getDirSizes(flist[i]);
                }
				else
				{
                    size = size + getFileSize(flist[i]);
                }
            }
        }
        return size;
    }


	public static long getFileSize(File file)
	{

        long size = 0;
        if (file.exists())
		{
            FileInputStream fis = null;
            try
			{
                fis = new FileInputStream(file);//使用FileInputStream读入file的数据流
                size = fis.available();//文件的大小
            }
			catch (IOException e)
			{
                e.printStackTrace();
            }
			finally
			{
                try
				{
                    fis.close();
                }
				catch (IOException e)
				{
                    e.printStackTrace();
                }
            }

        }
		else
		{
        }
        return size;
    }





	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == Activity.RESULT_OK)
		{
            Uri uri = data.getData();

			if (requestCode == 2)
			{

				if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT)
				{//4.4以后
					path = du.UritoPath(this, uri);
					fie = new File(path);

					new AlertDialog.Builder(this)
						.setTitle("警告")
						.setMessage("点击确定，即将导入 " + fie.getName() + " 这个文件。")
						.setPositiveButton("确认导入",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i)
							{

								if(new DabaiUtils().isContainChinese(fie.getName())){
									toast("提示:复制成功,但assets目录不能包含任何中文字符,");
								}else{
									toast("复制完成");
								}
								File dest = new File(tmpfile.getAbsolutePath() + "/app/src/main/assets/" + fie.getName());
								du.copyFile(path, dest.getPath());
								
							}
						}) 
						.setNeutralButton("取消", null)
						.show();

				}
				else
				{//4.4以下下系统调用方法

					toast("现在不支持4.4以下系统,");
				}

			}	



			if (requestCode == 1)
			{

				if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT)
				{//4.4以后
					path = du.UritoPath(this, uri);


					new AlertDialog.Builder(this)
						.setTitle("警告")
						.setMessage("点击确定，会自动清除原有图标，并更换你选择的图标，并且此操作，只支持有drawable&mipmap唯一一个目录的项目使用，(例如:mipmap-hdpi这种目录不会被操作)，此操作不可撤销！可能你还需要[构建刷新项目]才能生效")
						.setPositiveButton("确认更改",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i)
							{

								//复制操作
								if (new File(tmpfile.getAbsolutePath() + "/app/src/main/res/mipmap").exists())	
								{		

									File dest = new File(tmpfile.getAbsolutePath() + "/app/src/main/res/mipmap/" + get_ic_name());
									du.copyFile(path, dest.getPath());
									toast("复制完成");

								}
								else
								{
									File dest = new File(tmpfile.getAbsolutePath() + "/app/src/main/res/drawable/" + get_ic_name());

									du.copyFile(path, tmpfile.getAbsolutePath() + "/app/src/main/res/drawable/" + get_ic_name());	
									toast("复制完成");
								}




							}
						}) 
						.setNeutralButton("取消", null)
						.show();

				}
				else
				{//4.4以下下系统调用方法

					toast("现在不支持4.4以下系统,");
				}
			}





        }

	}





	//Android

	private void showAndroidPopupMenu(View view)
	{
        // 这里的view代表popupMenu需要依附的view
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
        // 获取布局文件
        popupMenu.getMenuInflater().inflate(R.menu.main_android_pop, popupMenu.getMenu());
        popupMenu.show();
        // 通过上面这几行代码，就可以把控件显示出来了
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item)
				{
					// 控件每一个item的点击事件

					switch (item.getItemId())
					{	

						case R.id.a_send_app:
							boolean is_exp = get_add_ver_name();
							File oldtm = new File(tmpfile, "/app/build/bin/app.apk");

							if (oldtm.exists())
							{


								if (is_exp)
								{
									File newtmp = new File("/sdcard/AIDE项目管理工具/", tmpfile.getName() + "_" + du.getVersionName(context) + ".apk");
									File oldtmp = new File(tmpfile, "/app/build/bin/app.apk");
									newtmp.deleteOnExit();
									du.copyFile(oldtmp, newtmp);
									if (newtmp.exists())
									{
										du.shareFile(context, newtmp.getAbsolutePath());
									}
									else
									{
										toast("提取失败");
									}

								}
								else
								{


									File newtmp = new File("/sdcard/AIDE项目管理工具/", tmpfile.getName() + ".apk");
									File oldtmp = new File(tmpfile, "/app/build/bin/app.apk");
									newtmp.deleteOnExit();
									du.copyFile(oldtmp, newtmp);
									if (newtmp.exists())
									{
										du.shareFile(context, newtmp.getAbsolutePath());
									}
									else
									{
										toast("提取失败");
									}
								}

							}
							else
							{toast("你还没有打包APP，不能分享");}

							break;		
						case R.id.a_create_assets:
							File asse = new File(tmpfile.getAbsolutePath() + "/app/src/main/assets/");
							if (!asse.exists())
							{

								if (asse.mkdir())
								{
									toast("新建成功");
								}
							}else{
								toast("已存在");
							}
							break;	
						case R.id.a_changeicon:

							new DabaiUtils().openFileChoose(MainActivity.this, 1);


							break;

						case R.id.a_del:

							

							new AlertDialog.Builder(MainActivity.this)
								.setTitle("提示")
								.setMessage("项目名 : " + tmpfile.getName() + "\n项目路径 : " + tmpfile.getAbsolutePath() + "\n项目大小 : " + Math.ceil((double)getDirSizes(tmpfile) / 1024) + "KB")
								.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialogInterface, int i)
									{
										du.deleteDir(tmpfile);
										f5();
									}
								}) 
								.setNeutralButton("取消", null)
								.show();





							break;

						case R.id.a_clone:
toast("开始克隆...");
							new Thread() {
								@Override
								public void run()
								{
									super.run();
									//新线程操作
									SimpleDateFormat sdf = new SimpleDateFormat("hhmmss");
									du.copyDirectory(tmpfile, new File(tmpfile.getParent() + "/" + tmpfile.getName() + "_clone" + sdf.format(new Date())));

									runOnUiThread(new Runnable(){
											@Override
											public void run()
											{
												//更新UI操作
												f5();
											}
										});
								}
							}.start();
							break;

						case R.id.a_dao_assets:


							File ass = new File(tmpfile.getAbsolutePath() + "/app/src/main/assets/");
							if (!ass.exists())
							{


								toast("还没有创建assets目录");
							}
							else
							{

								Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
								intent1.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
								intent1.addCategory(Intent.CATEGORY_OPENABLE);
								startActivityForResult(intent1, 2);



							}

							break;				
						case R.id.a_size:


							SimpleDateFormat sssf = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");	

							StringBuffer sb = new StringBuffer();
							sb.append("项目名 : " + tmpfile.getName() + "\n项目路径 : " + tmpfile.getAbsolutePath() + "\n项目大小 : " + Math.ceil((double)getDirSizes(tmpfile) / 1024) + "KB");
							sb.append("\n");		
							sb.append("是否文件夹 : " + tmpfile.isDirectory());
							sb.append("\n");
							sb.append("最后修改时间 : " + sssf.format(new Date(tmpfile.lastModified())));


							new AlertDialog.Builder(MainActivity.this)
								.setMessage(sb.toString())
								.show();

							f5();
							break;

						case R.id.a_pack:
							toast("打包开始");

							new Thread() {
								@Override
								public void run()
								{
									super.run();
									//新线程操作
									XZip xzip = new XZip();
									try
									{
										xzip.ZipFolder(tmpfile.getAbsolutePath(), "/sdcard/AIDE项目管理工具/" + tmpfile.getName() + ".zip");
									}
									catch (Exception e)
									{toast("异常 : " + e);}
									runOnUiThread(new Runnable(){
											@Override
											public void run()
											{
												toast("打包完成 - /sdcard/AIDE项目管理工具/");

											}

										});


								}
							}.start();


							break;
						case R.id.a_share_project:

							toast("打包开始");

							final String tag = "/sdcard/AIDE项目管理工具/" + tmpfile.getName() + ".zip";

							new Thread() {
								@Override
								public void run()
								{
									super.run();
									//新线程操作
									XZip xzip = new XZip();
									try
									{
										xzip.ZipFolder(tmpfile.getAbsolutePath(), tag);
									}
									catch (Exception e)
									{
										toast("异常 : " + e);
									}
									runOnUiThread(new Runnable(){
											@Override
											public void run()
											{
												toast("打包完成 - /sdcard/AIDE项目管理工具/");
												du.shareFile(context, tag);
											}

										});


								}
							}.start();


							break;

					}		





					return true;
				}
			});
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
				@Override
				public void onDismiss(PopupMenu menu)
				{
					// 控件消失时的事件
				}
			});

    }








}
