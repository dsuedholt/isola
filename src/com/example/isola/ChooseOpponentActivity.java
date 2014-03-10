package com.example.isola;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ChooseOpponentActivity extends Activity {
	
	private static Context mContext; //so Enum Player can reference

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_opponent);
		mContext = this;
		
		Spinner choose_player1 = (Spinner) findViewById(R.id.spinner_first_player);
		choose_player1.setAdapter(new ArrayAdapter<Player>(this, android.R.layout.simple_spinner_dropdown_item, Player.values()));
		Spinner choose_player2 = (Spinner) findViewById(R.id.spinner_second_player);
		choose_player2.setAdapter(new ArrayAdapter<Player>(this, android.R.layout.simple_spinner_dropdown_item, Player.values()));
	}

    public static Context getContext(){
        return mContext;
    }
}
