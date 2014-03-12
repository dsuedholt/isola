package com.example.isola;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class ChooseOpponentActivity extends Activity {
	
	private static Context mContext; //so Enum Player can reference
	
	public static String PLAYER1_KEY = "player1";
	public static String PLAYER2_KEY = "player2";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_opponent);
		mContext = this;
		
		final Spinner choose_player1 = (Spinner) findViewById(R.id.spinner_first_player);
		choose_player1.setAdapter(new ArrayAdapter<Player>(this, android.R.layout.simple_spinner_dropdown_item, Player.values()));
		final Spinner choose_player2 = (Spinner) findViewById(R.id.spinner_second_player);
		choose_player2.setAdapter(new ArrayAdapter<Player>(this, android.R.layout.simple_spinner_dropdown_item, Player.values()));
		
		Button start_game_activity = (Button) findViewById(R.id.button_start_game);
		start_game_activity.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Player chosen_player1 = (Player) choose_player1.getSelectedItem();
				Player chosen_player2 = (Player) choose_player2.getSelectedItem();
				Intent start_game_activity = new Intent(mContext, GameActivity.class);
				start_game_activity.putExtra(PLAYER1_KEY, chosen_player1);
				start_game_activity.putExtra(PLAYER2_KEY, chosen_player2);
				startActivity(start_game_activity);
			}
		});
	}

    public static Context getContext(){
        return mContext;
    }
}
