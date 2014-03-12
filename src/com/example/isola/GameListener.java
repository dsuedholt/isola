package com.example.isola;

import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

public class GameListener implements View.OnTouchListener {
	
	private Game game;
	
	public GameListener(Game game) {
		this.game = game;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent ev) {
		if ((game.isP1Turn() && game.getPlayer(true) == Player.HUMAN) ||
			(!game.isP1Turn() && game.getPlayer(false) == Player.HUMAN)) {
			BoardView bv = (BoardView) v;
			Point coords = bv.getTileCoords((int) ev.getX(), (int) ev.getY());
			if (coords != null) {
				game.doTurn(coords.x, coords.y);
				return true;
			}
		}
		return false;
	}

}
