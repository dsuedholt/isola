package com.example.isola;

import java.util.Observable;

public class Board extends Observable {
	
	public static final int WIDTH = 6;
	public static final int HEIGHT = 8; 
	
	//player's coordinates
	private int x1, y1, x2, y2;
	
	private Tile[][] board;
	
	public Board() {
		board = new Tile[WIDTH][HEIGHT];
		init();
	}
	
	public Board(Board copy) {
		this();
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				this.board[i][j] = copy.board[i][j];
			}
		}
		this.x1 = copy.x1;
		this.y1 = copy.y1;
		this.x2 = copy.x2;
		this.y2 = copy.y2;
	}
	
	/**
	 * Initializes the board to the starting position, with every tile undestroyed
	 * and the players on their starting squares
	 */
	public void init() {
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				board[i][j] = Tile.FREE;
			}
		}
		x1 = WIDTH /2 ;
		y1 = HEIGHT - 1;
		x2 = WIDTH / 2 - 1;
		y2 = 0;
		board[x1][y1] = Tile.PLAYER1;
		board[x2][y2] = Tile.PLAYER2;
	}
	
	/**
	 * Returns whether or not a given player is allowed to move to the given
	 * coordinates.
	 * 
	 * @param player1 Indicates if player 1 (true) or player 2 (false) is to move
	 * @param x       The x-coordinate of the destination field
	 * @param y       The y-coordinate of the destination field
	 * @throws        IllegalArgumentException if x, y are out of bounds.
	 * @return		  A boolean indicating if the planned move is valid
	 */
	public boolean canMove(boolean player1, int x, int y) {
		if (!validIndices(x, y)) {
			return false;
		}
		if (board[x][y] != Tile.FREE)
			return false;
		if (player1) {
			// check for distances bigger than 1 or equality; the player HAS to move to
			// a different square
			if (Math.abs(x1 - x) > 1 || Math.abs(y1 - y) > 1 || (x1 == x && y1 == y))
				return false;
		} else {
			if (Math.abs(x2 - x) > 1 || Math.abs(y2 - y) > 1 || (x2 == x && y2 == y))
				return false;
		}
		return true;
	}
	
	public void move(boolean player1, int x, int y) {
		if (!canMove(player1, x, y)) {
			String error = String.format("Player cannot move to x=%d, y=%d", x, y);
			throw new IllegalArgumentException(error);
		}
		int oldx, oldy;
		if (player1) {
			oldx = x1;
			oldy = y1;
			board[x1][y1] = Tile.FREE;
			board[x][y] = Tile.PLAYER1;
			x1 = x;
			y1 = y;
		} else {
			oldx = x2;
			oldy = y2;
			board[x2][y2] = Tile.FREE;
			board[x][y] = Tile.PLAYER2;
			x2 = x;
			y2 = y;
		}
		setChanged();
		notifyObservers(new MoveEvent(player1, oldx, oldy, x, y));
	}
	
	public boolean isOver() {
		return hasLost(true) || hasLost(false);
	}
	
	public int getPossibleMoveCount(boolean player1) {
		int counter = 0;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (player1 && validIndices(x1 + i, y1 + j)) {
					try {
						if (board[x1 + i][y1 + j] == Tile.FREE) {
							counter++;
						}
					} catch (ArrayIndexOutOfBoundsException e) {
						e.printStackTrace();
					}
				} else if (!player1 && validIndices(x2 + i, y2 + j)) {
					try {
						if (board[x2 + i][y2 + j] == Tile.FREE) {
							counter++;
						}
					} catch (ArrayIndexOutOfBoundsException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return counter;
	}
	
	public boolean hasLost(boolean player1) {
		return getPossibleMoveCount(player1) == 0;
	}
	
	public boolean canDestroy(int x, int y) {
		if (!validIndices(x, y)) {
			return false;
		}
		return board[x][y] == Tile.FREE;
	}
	
	public void destroy(int x, int y) {
		if (!canDestroy(x, y)) {
			String error = String.format("Cannot destroy x=%d, y=%d", x, y);
			throw new IllegalArgumentException(error);
		}
		board[x][y] = Tile.DESTROYED;
		setChanged();
		notifyObservers(new DestroyEvent(x, y));
		if (isOver()) {
			setChanged();
			notifyObservers(new GameOverEvent(hasLost(false)));
		}
	}
	
	public Tile getTile(int x, int y) {
		if (!validIndices(x, y)) {
			String error = String.format("Indices x=%d, y=%d out of range", x, y);
			throw new IllegalArgumentException(error);
		}
		return board[x][y];
	}
	
	private boolean validIndices(int x, int y) {
		return 0 <= x && x < WIDTH && 0 <= y && y < HEIGHT;
	}
	
}
