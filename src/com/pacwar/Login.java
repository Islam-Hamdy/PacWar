package com.pacwar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class Login extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	public void Host(View view) {
		EditText t = (EditText) findViewById(R.id.user);
		String name = t.getText().toString();
		if (name.isEmpty())
			return;
	}

	public void Join(View view) {
		EditText t = (EditText) findViewById(R.id.user);
		String name = t.getText().toString();
		if (name.isEmpty())
			return;
		Intent myIntent = new Intent(this, Lobby.class);
		startActivity(myIntent);
	}
}
