package com.example.isola;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class GameActivity extends Activity {
	
	private IsolaController ic;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		BoardView board = (BoardView) findViewById(R.id.isolaboard);
		ic = new IsolaController();
		board.setOnClickListener(new GameListener());
		
		Intent intent = getIntent();
		Player p1 = (Player) intent.getSerializableExtra("player1");
		Player p2 = (Player) intent.getSerializableExtra("player2");
		
		switch (p1) {
		case HUMAN: ic.setHumanP1(); break;
		//TODO: Computer strategies
		case EASY: break;
		case MEDIUM: break;
		case HARD: break;
		}
		
		switch (p2) {
		case HUMAN: ic.setHumanP2(); break;
		//TODO: Computer strategies
		case EASY: break;
		case MEDIUM: break;
		case HARD: break;
		}
		
		ic.run();
		
	}
	
	private class GameListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			//TODO: Do something with the clicked square
			ic.notify();
		}
	}
	
}
