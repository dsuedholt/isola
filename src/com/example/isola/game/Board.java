package com.example.isola.game;

import java.util.Observable;

import android.graphics.Point;

/**
 * This class represents the game board consisting of Tiles.
 * On every change of the board (move or destroy) the registered 
 * observers are notified with the corresponding GameEvent.
 */
public class Board extends Observable {
	
	public static final int WIDTH = 6;
	public static final int HEIGHT = 8; 
	
	//player's coordinates
	private int x1, y1, x2, y2;
	
	private Tile[][] board;
	
	/**
	 * Constructs the board and initializes the player positions.
	 */
	public Board() {
		board = new Tile[WIDTH][HEIGHT];
		init();
	}
	
	/**
	 * Copy-Constructor.
	 * @param copy The board instance to be copied.
	 */
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
	
	/**
	 * If possible, move the player to the field specified by x and y.
	 * 
	 * @param player1 Boolean indicating if player1 (true) or player2 (false) moves
	 * @param x		  Integer x-coordinate of the destination field
	 * @param y		  Integer y-coordinate of the destination field
	 * @throws IllegalArgumentException if the player cannot move to this field. Call canMove first!
	 */
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
	
	/**
	 * Indicates whether the game is over, meaning that any of the players cannot move anymore.
	 * @return True if the game is over, false if it is not.
	 */
	public boolean isOver() {
		return hasLost(true) || hasLost(false);
	}
	
	/**
	 * Counts and returns the number of the maximal 8 surrounding fields of the player
	 * that the player can move to.
	 * @param player1 Boolean that is true if player1's moves should be counted, false for player2.
	 * @return An Integer between 0 and 8.
	 */
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
	
	/**
	 * Helper method to query if a certain player has no possible moves left.
	 * @param player1 True for player1, false for player2
	 * @return True when the player has zero moves left, false otherwise
	 */
	public boolean hasLost(boolean player1) {
		return getPossibleMoveCount(player1) == 0;
	}
	
	/**
	 * Checks whether or not the specified field can be destroyed. Only fields of the type
	 * Tile.FREE can be destroyed.
	 * @param x The x-coordinate of the candidate field.
	 * @param y The y-coordinate of the candidate field.
	 * @return True if the field can be destroyed, false otherwise.
	 */
	public boolean canDestroy(int x, int y) {
		if (!validIndices(x, y)) {
			return false;
		}
		return board[x][y] == Tile.FREE;
	}
	
	/**
	 * If possible, destroy the specified field.
	 * @param x The x-coordinate of the destination field.
	 * @param y The y-coordinate of the destination field.
	 * @throws IllegalArgumentException if the field cannot be destroyed. Call canDestroy first!
	 */
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
	
	/**
	 * Get the coordinates of the field on which the specified player currently stands.
	 * @param player1 True for player1, false for player2.
	 * @return A Point object that has the coordinate's of the player's position.
	 */
	public Point getPlayerPosition(boolean player1) {
		if (player1)
			return new Point(x1, y1);
		else
			return new Point(x2, y2);
	}
	
	/**
	 * Get the type of the tile at the specified Position. A tile is either free, destroyed,
	 * or occupied by one of the players.
	 * @param x The x-coordinate of the tile.
	 * @param y The y-coordinate of the tile.
	 * @return The tile at x, y.
	 * @throws IllegalArgumentException if x and y are out of range.
	 * @see Tile
	 */
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
