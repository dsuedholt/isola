package com.example.isola.android;

import java.util.Observable;
import java.util.Observer;

import com.example.isola.game.Board;
import com.example.isola.game.Game;
import com.example.isola.game.GameEvent;
import com.example.isola.game.Player;
import com.example.isola.game.GameEvent.EventType;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class GameActivity extends Activity implements Observer {
	
	public static final String PLAYER1_KEY = "player1";
	public static final String PLAYER2_KEY = "player2";
	public static final String STARTATONCE_KEY = "startatonce";
	
	protected Board board;
	protected Game game;
	protected BoardView bv;
	protected Player p1, p2;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		board = new Board();
		Intent startIntent = getIntent();
		p1 = (Player) startIntent.getSerializableExtra(PLAYER1_KEY);
		p2 = (Player) startIntent.getSerializableExtra(PLAYER2_KEY);
		boolean startAtOnce = startIntent.getBooleanExtra(STARTATONCE_KEY, true);
		bv = new BoardView(this, board);
		board.addObserver(this);
		board.addObserver(bv);
		
		setContentView(bv);
		
		if(startAtOnce)
			startGame();
	}
	
	protected void startGame() {
		game = new Game(board, p1, p2);
		(new Thread(game)).start();
		bv.setOnTouchListener(new GameListener(game));
	}
	
	@Override
	public void update(Observable board, Object data) {
		GameEvent ev = (GameEvent) data;
		if (ev.getType() == EventType.GAMEOVER) {
			// TODO: End game / rematch
		}
	}
}
