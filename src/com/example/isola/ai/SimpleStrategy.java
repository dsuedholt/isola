package com.example.isola.ai;

import java.util.Random;

import android.graphics.Point;

import com.example.isola.game.Board;

/**
 * This class represents a very naive computer player that will almost always lose.
 */
public class SimpleStrategy extends Strategy {
	
	/**
	 * Construct the computer player.
	 * @param board   The game board
	 * @param player1 True if the computer is player1, false otherwise.
	 */
	public SimpleStrategy(Board board, boolean player1) {
		super(board, player1);
	}
	
	@Override
	protected void doMove() {
		Point pos = board.getPlayerPosition(player1);
		
		/*
		 * Move to the first availabe tile in the following
		 * sequence, where P is our position:
		 *     1 2 3
		 *     4 P 5
		 *     6 7 8
		 * 
		 * This method is guaranteed to be called with the player
		 * having at least one square left to go
		 */
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (board.canMove(player1, pos.x + i, pos.y + j)) {
					board.move(player1, pos.x + i, pos.y + j);
					return;
				}
			}
		}
	}
	
	@Override
	protected void doDestroy() {
		Point pos = board.getPlayerPosition(!player1);

		/*
		 * Destroy the first availabe tile in the following
		 * sequence, where P is the enemy position:
		 *     1 2 3
		 *     4 P 5
		 *     6 7 8
		 */
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (board.canDestroy(pos.x + i, pos.y + j)) {
					board.destroy(pos.x + i, pos.y + j);
					return;
				}
			}
		}
		
		// if we weren't able to destroy a field right next to the player,
		// destroy a random one.
		Random rand = new Random();
		int i, j;
		do {
			i = rand.nextInt(Board.WIDTH);
			j = rand.nextInt(Board.HEIGHT);
		} while (!board.canDestroy(i, j));
		board.destroy(i, j);
	}
}
