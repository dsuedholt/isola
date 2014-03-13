package com.example.isola.ai;

import com.example.isola.game.Board;
import com.example.isola.game.Tile;

import android.util.Log;

public abstract class Strategy {
	
	protected int WAITINGTIME = 400;
	
	protected Board board;
	protected boolean player1;
	//player coordinates
	protected int x, y;
	
	public Strategy(Board board, boolean player1) {
		this.board = board;
		this.player1 = player1;
		
		findPosition(player1);
	}
	
	public void play() {
		try {
			Thread.sleep(WAITINGTIME);
			doMove();
			Thread.sleep(WAITINGTIME);
			doDestroy();
		} catch (InterruptedException e) {
			Log.e("Isola", "Computer was interrupted", e);
		}
	}
	
	protected void findPosition(boolean player1) {
		for (int i = 0; i < Board.WIDTH; i++) {
			for (int j = 0; j < Board.HEIGHT; j++) {
				if ((board.getTile(i, j) == Tile.PLAYER1 && player1) ||
					(board.getTile(i, j) == Tile.PLAYER2 && !player1)) {
					x = i;
					y = j;
					return;
				}
			}
		}
	}
	
	protected abstract void doMove();
	protected abstract void doDestroy();
}
