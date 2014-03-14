package com.example.isola.android;

import com.example.isola.game.Game;
import com.example.isola.game.Player;

import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

/**
 * This class provides the listener that reacts to user input
 * on the game screen.
 */
public class GameListener implements View.OnTouchListener {
	
	private Game game;
	
	/**
	 * Constructs the GameListener.
	 * @param game The Game object that controls the game.
	 */
	public GameListener(Game game) {
		this.game = game;
	}
	
	/**
	 * When the user touches while it is a human's turn, convert
	 * the touch coordinates to tile coordinates and send those
	 * to the game.
	 */
	@Override
	public boolean onTouch(View v, MotionEvent ev) {
		if (((game.isP1Turn() && game.getPlayer(true) == Player.HUMAN) ||
			(!game.isP1Turn() && game.getPlayer(false) == Player.HUMAN)) &&
			  ev.getAction() == MotionEvent.ACTION_UP) {
			BoardView bv = (BoardView) v;
			Point coords = bv.getTileCoords((int) ev.getX(), (int) ev.getY());
			if (coords != null) {
				game.doTurn(coords.x, coords.y);
			}
		}
		return true;
	}

}