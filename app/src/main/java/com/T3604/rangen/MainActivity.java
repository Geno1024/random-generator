package com.T3604.rangen;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends Activity
{
	public SharedPreferences sp;
	public SharedPreferences.Editor e;
	public LinearLayout list;
	public EditText in;
	public Button add;
	public View.OnLongClickListener l;
	public int count;
	public Button gen;
	public Random r;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		e = sp.edit();
		list = (LinearLayout) findViewById(R.id.mainList);
		in = (EditText) findViewById(R.id.text);
		add = (Button) findViewById(R.id.btn);
		gen = (Button) findViewById(R.id.gen);
		r = new Random();
		l = new View.OnLongClickListener()
		{
			@Override
			public boolean onLongClick(final View v)
			{
				new Handler().post
					(new Runnable()
						{
							public void run()
							{
								list.removeView(v);
							}
						}
					);
				count--;
				e.putInt("count", count);
				e.commit();
				return false;
			}
		};

		count = sp.getInt("count", 0);

		if (count != 0)
		{
			for (int i = 0; i < count; i++)
			{
				TextView t = new TextView(this);
				t.setText(sp.getString("_" + i, ""));
				t.setPadding(10, 20, 10, 20);
				t.setTextSize(15);
				t.setOnLongClickListener(l);
				list.addView(t, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			}
		}

		View.OnClickListener addd = new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				count++;
				e.putInt("count", count);
				e.commit();
				TextView t = new TextView(MainActivity.this);
				String s = in.getText().toString();
				t.setPadding(10, 20, 10, 20);
				t.setTextSize(15);
				t.setText(s);
				t.setOnLongClickListener(l);
				e.putString("_" + (count - 1), s);
				e.commit();
				list.addView(t, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				in.setText("");
			}
		};

		View.OnClickListener gene = new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				int rand = r.nextInt(count);
				Log.v("rand", rand + "");
				AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this)
						.setTitle(R.string.res)
						.setMessage(((TextView) list.getChildAt(rand)).getText().toString())
						.setPositiveButton(android.R.string.ok, null);
				b.show();
			}
		};
		gen.setOnClickListener(gene);
		add.setOnClickListener(addd);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		count = list.getChildCount();
		e.putInt("count", count);
		e.commit();
		for (int i = 0; i < count; i++)
			e.putString("_" + i, ((TextView) list.getChildAt(i)).getText().toString());
		e.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(Menu.NONE, Menu.NONE, 1, R.string.about);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		String inst = getString(R.string.desc);
		PackageManager pm = getPackageManager();
		LinearLayout layout = new LinearLayout(this);
		ImageView appImage = new ImageView(this);
		Drawable appDrawable = null;
		TextView appName = new TextView(this);
		TextView version = new TextView(this);
		String vName = null;
		int vCode = 0;
		int apkSize = 0;
		try
		{
			appDrawable = pm.getApplicationIcon(pm.getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA));
			appName.setText(pm.getApplicationLabel(pm.getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA)));
			vName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
			vCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
			apkSize = (int)new File(getPackageManager().getApplicationInfo(getPackageName(), 0).sourceDir).length();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		appImage.setImageDrawable(appDrawable);
		appName.setTypeface(Typeface.MONOSPACE);
		if(Locale.getDefault().equals(Locale.CHINESE) || Locale.getDefault().equals(Locale.CHINA))
			version.setText("\nBy Geno.\nY. Z. Chen\n电子邮箱：754097987@qq.com\n\n版本号：" + vCode + "\n版本名称：" + vName + "\n安装包大小：" + apkSize + "\n\n" + inst);
		else
			version.setText("\nBy Geno.\nY. Z. Chen\nEmail:754097987@qq.com\n\nVersion Code:" + vCode + "\nVersion Name:" + vName + "\nPackageSize:" + apkSize + "\n\n" + inst);
		version.setTypeface(Typeface.MONOSPACE);
		version.setGravity(Gravity.CENTER);

		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setGravity(Gravity.CENTER);
		layout.addView(appImage, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layout.addView(appName, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layout.addView(version, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

		AlertDialog.Builder ad = new AlertDialog.Builder(this)
				.setTitle(R.string.about)
				.setView(layout)
				.setPositiveButton(android.R.string.ok, null);
		ad.show();
		return super.onOptionsItemSelected(item);
	}
}
