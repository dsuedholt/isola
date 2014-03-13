package com.example.isola.android;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.isola.R;

public class HelpActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		
		TextView how_to_play = (TextView) findViewById(R.id.text_view_how_to_play);
		how_to_play.setMovementMethod(new ScrollingMovementMethod());
		
		Button close_activity = (Button) findViewById(R.id.button_close_help);
		
		close_activity.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
	}

}