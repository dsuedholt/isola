package com.example.isola;

import android.os.Bundle;
import android.app.Activity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class InfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		
		TextView developer_info = (TextView) findViewById(R.id.developer_info);
		developer_info.setMovementMethod(new ScrollingMovementMethod());
		
		Button close_info = (Button) findViewById(R.id.button_close_info);
		close_info.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				finish();
			}
		});
	}


}
