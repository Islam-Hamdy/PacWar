package com.pacwar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class Launcher extends Activity {
	public Intent intent;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		intent = new Intent(this, MainActivity.class);
		this.requestFullScreen();
		this.setContentView(R.layout.main);
		startActivity(intent);
	}

	private void requestFullScreen() {
		Window window = this.getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		window.requestFeature(Window.FEATURE_NO_TITLE);
	}
}
