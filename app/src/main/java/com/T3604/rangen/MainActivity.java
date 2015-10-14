package com.T3604.rangen;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity
{
	public SharedPreferences sp;
	public SharedPreferences.Editor e;
	public LinearLayout list;
	public EditText in;
	public Button add;
	public View.OnLongClickListener l;
	public int count;

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
		l = new View.OnLongClickListener()
		{
			@Override
			public boolean onLongClick(View v)
			{
				list.removeView(v);
				return false;
			}
		};
		
		count = sp.getInt("count", 0);

		if (count == 0) list.removeView(findViewById(R.id.desc));
		else
		{
			for (int i = 0; i < count; i++)
			{
				TextView t = new TextView(this);
				t.setText(sp.getString("_" + i, ""));
				t.setOnLongClickListener(l);
			}
		}
	}
}
