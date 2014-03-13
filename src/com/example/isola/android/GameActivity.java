package com.example.isola.android;

import java.util.Observable;
import java.util.Observer;

import com.example.isola.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.isola.game.Board;
import com.example.isola.game.Game;
import com.example.isola.game.GameEvent;
import com.example.isola.game.GameEvent.EventType;
import com.example.isola.game.GameOverEvent;
import com.example.isola.game.Player;

public class GameActivity extends Activity implements Observer {
	
	public static final String PLAYER1_KEY = "player1";
	public static final String PLAYER2_KEY = "player2";
	public static final String STARTATONCE_KEY = "startatonce";
	
	protected Board board;
	protected Game game;
	protected BoardView bv;
	protected Player p1, p2;
	protected Thread gameThread;
	protected GameListener gl;
	
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
		if (game == null) {
			game = new Game(board, p1, p2);
		}
		if (gameThread != null)
			gameThread.interrupt();
		
		gameThread = new Thread(game);
		gameThread.start();
		
		if (gl == null) {
			gl = new GameListener(game);
			bv.setOnTouchListener(gl);
		}
	}
	
	@Override
	public void update(Observable board, Object data) {
		GameEvent ev = (GameEvent) data;
		if (ev.getType() == EventType.GAMEOVER) {
			boolean player1won = ((GameOverEvent)ev).isPlayer1();
			endGame(player1won);
		}
	}
	
	protected void endGame() {
		gameThread.interrupt();
		Intent backIntent = new Intent(this, MainActivity.class);
		backIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(backIntent);
	}
	
	protected void endGame(boolean player1) {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which) {
		        case DialogInterface.BUTTON_POSITIVE:
		            startGame();
		            break;

		        case DialogInterface.BUTTON_NEGATIVE:
		            endGame();
		            break;
		        }
		    }
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		String message = player1?getString(R.string.player1_win):getString(R.string.player2_win);
		String rematch = getString(R.string.rematch);
		String back = getString(R.string.back);
		
		builder.setMessage(message).setPositiveButton(rematch, dialogClickListener)
		    .setNegativeButton(back, dialogClickListener).show();
	}
}
