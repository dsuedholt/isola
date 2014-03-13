package com.example.isola.ai;

import android.util.Log;

import com.example.isola.game.Board;

public abstract class Strategy {
	
	protected int WAITINGTIME = 400;
	
	protected Board board;
	protected boolean player1;
	
	public Strategy(Board board, boolean player1) {
		this.board = board;
		this.player1 = player1;
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
	
	protected abstract void doMove();
	protected abstract void doDestroy();
}
