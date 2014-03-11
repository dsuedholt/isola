package com.example.isola;

public class SimpleStrategy implements Strategy {
	
	//player coordinates
	private int x, y;
	private Board board;
	
	@Override
	public void play(Board board, boolean player1) {
		this.board = board;
		findPosition(player1);
		try {
			Thread.sleep(500);
			doDestroy(player1);
			Thread.sleep(500);
			doMove(player1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	private void doMove(boolean player1) {
		/*
		 * Move to the first availabe tile in the following
		 * sequence, where P is our position:
		 *     1 2 3
		 *     4 P 5
		 *     6 7 8
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
	
	private void doDestroy(boolean player1) {
		/*
		 * Destroy the first availabe tile in the following
		 * sequence, where P is the enemy position:
		 *     1 2 3
		 *     4 P 5
		 *     6 7 8
		 */
		
		// let x, y be the enemy's coordinates
		findPosition(!player1);
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (board.canDestroy(x + i, y + j)) {
					board.destroy(x + i, y + j);
					return;
				}
			}
		}
	}
	
	private void findPosition(boolean player1) {
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

}
