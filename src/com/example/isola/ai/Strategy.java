package com.example.isola.ai;

import android.util.Log;

import com.example.isola.game.Board;

/**
 * Abstract base class for all computer players.
 */
public abstract class Strategy {
	
	protected int WAITINGTIME = 400;
	
	protected Board board;
	protected boolean player1;
	
	/**
	 * Abstract constructor saving the information all computer players need in instance variables
	 * @param board   The game board
	 * @param player1 True if the computer is player1, false otherwise
	 */
	public Strategy(Board board, boolean player1) {
		this.board = board;
		this.player1 = player1;
	}
	
	/**
	 * This method does exactly one turn consisting of move and destroy. Strategies that are
	 * too simple to need time to calculate their moves will sleep for some time to appear
	 * more natural to the user instead of responding instantaneously.
	 */
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
