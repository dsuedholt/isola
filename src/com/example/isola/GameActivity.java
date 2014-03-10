package com.example.isola;

import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.os.Bundle;

public class GameActivity extends Activity implements Observer {
	
	
	protected void onCreate(Bundle savedInstanceState) {
		
	}

	@Override
	public void update(Observable board, Object data) {
		GameEvent type = (GameEvent) data;
		
	}
	
	
	
}
