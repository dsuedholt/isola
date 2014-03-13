package com.example.isola.ai;

import java.util.Random;

import com.example.isola.game.Board;

public class SimpleStrategy extends Strategy {
	
	public SimpleStrategy(Board board, boolean player1) {
		super(board, player1);
	}
	
	@Override
	protected void doMove() {
		
		findPosition(player1);
		
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
				if (board.canMove(player1, x + i, y + j)) {
					board.move(player1, x + i, y + j);
					return;
				}
			}
		}
	}
	
	@Override
	protected void doDestroy() {
		// let x, y be the enemy's coordinates
		findPosition(!player1);

		/*
		 * Destroy the first availabe tile in the following
		 * sequence, where P is the enemy position:
		 *     1 2 3
		 *     4 P 5
		 *     6 7 8
		 */
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (board.canDestroy(x + i, y + j)) {
					board.destroy(x + i, y + j);
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
