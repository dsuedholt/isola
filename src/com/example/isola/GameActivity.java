package com.example.isola;

import java.util.Observable;
import java.util.Observer;

import com.example.isola.GameEvent.EventType;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class GameActivity extends Activity implements Observer {
	
	protected Board board;
	protected Game game;
	protected BoardView bv;
	protected Player p1, p2;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		board = new Board();
		Intent startIntent = getIntent();
		p1 = (Player) startIntent.getSerializableExtra("player1");
		p2 = (Player) startIntent.getSerializableExtra("player2");
		bv = new BoardView(this, board);
		board.addObserver(this);
		board.addObserver(bv);
		
		setContentView(bv);
		
		game = new Game(board, p1, p2);
	}

	@Override
	public void update(Observable board, Object data) {
		GameEvent ev = (GameEvent) data;
		if (ev.getType() == EventType.GAMEOVER) {
			// TODO: End game / rematch
		}
	}
}
