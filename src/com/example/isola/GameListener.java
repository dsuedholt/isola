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
		if ((game.isP1Turn() && game.isHuman(true)) ||
			(!game.isP1Turn() && game.isHuman(false))) {
			BoardViewTest bv = (BoardViewTest) v;
			Point coords = bv.getTileCoords((int) ev.getX(), (int) ev.getY());
			game.doTurn(coords.x, coords.y);
			return true;
		}
		return false;
	}

}
