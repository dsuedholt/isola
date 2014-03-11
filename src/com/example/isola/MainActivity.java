package com.example.isola;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		final MainActivity usedInOnClickListener = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button newGame = (Button) findViewById(R.id.button_new_game);
		Button help = (Button) findViewById(R.id.button_help);
		Button newBtGame = (Button) findViewById(R.id.Button_new_btgame);
		
		
		newGame.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent_choose_opponent = new Intent(usedInOnClickListener, ChooseOpponentActivity.class);
				startActivity(intent_choose_opponent);
			}
		});
		
		help.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent_help = new Intent(usedInOnClickListener, HelpActivity.class);
				startActivity(intent_help);
			}
		}); //end help.setOnClickListener
		
		newBtGame.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent_btGame = new Intent(usedInOnClickListener, BtGameActivity.class);
				startActivity(intent_btGame);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
