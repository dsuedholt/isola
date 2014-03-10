package com.example.isola;

public class Board {
	
	public static final int WIDTH = 6;
	public static final int HEIGHT = 8; 
	
	private static Board instance;
	
	//player's coordinates
	private int x1, y1, x2, y2;
	
	static {
		instance = new Board();
	}
	
	private Tile[][] board;
	
	private Board() {
		board = new Tile[WIDTH][HEIGHT];
		init();
	}
	
	public void init() {
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				board[i][j] = Tile.FREE;
			}
		}
		x1 = WIDTH / 2 - 1;
		y1 = 0;
		x2 = WIDTH /2 ;
		y2 = HEIGHT - 1;
		board[x1][y1] = Tile.PLAYER1;
		board[x2][y2] = Tile.PLAYER2;
	}
	
	
	public boolean isValidMove(boolean player1, int x, int y) {
		if (!validIndices(x, y)) {
			String error = String.format("Indices x=%d, y=%d out of range", x, y);
			throw new IllegalArgumentException(error);
		}
		if (board[x][y] != Tile.FREE)
			return false;
		if (player1) {
			if (Math.abs(x1 - x) > 1 || Math.abs(y1 - y) > 1 || (x1 == x && y1 == y))
				return false;
		} else {
			if (Math.abs(x2 - x) > 1 || Math.abs(y2 - y) > 1 || (x2 == x && y2 == y))
				return false;
		}
		return true;
	}
	
	public void move(boolean player1, int x, int y) {
		if (!isValidMove(player1, x, y)) {
			String error = String.format("Player cannot move to x=%d, y=%d", x, y);
			throw new IllegalArgumentException(error);
		}
		if (player1) {
			board[x1][y1] = Tile.FREE;
			board[x][y] = Tile.PLAYER1;
			x1 = x;
			y1 = y;
		} else {
			board[x2][y2] = Tile.FREE;
			board[x][y] = Tile.PLAYER2;
			x2 = x;
			y2 = y;
		}
		
	}
	
	public boolean isOver() {
		return hasLost(true) || hasLost(false);
	}
	
	public boolean hasLost(boolean player1) {
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (player1 && validIndices(x1 + i, y1 + j)) {
					if (board[x1 + i][y1 + j] == Tile.FREE)
						return false;
				} else if (!player1 && validIndices(x2 + i, y2 + j)) {
					if (board[x2 + i][y2 + j] == Tile.FREE)
						return false;
				}
			}
		}
		return true;
	}
	
	public boolean canDestroy(int x, int y) {
		if (!validIndices(x, y)) {
			String error = String.format("Indices x=%d, y=%d out of range", x, y);
			throw new IllegalArgumentException(error);
		}
		return board[x][y] == Tile.FREE;
	}
	
	public void destroy(int x, int y) {
		if (!canDestroy(x, y)) {
			String error = String.format("Cannot destroy x=%d, y=%d", x, y);
			throw new IllegalArgumentException(error);
		}
		board[x][y] = Tile.DESTROYED;
	}
	
	public Tile getTile(int x, int y) {
		if (!validIndices(x, y)) {
			String error = String.format("Indices x=%d, y=%d out of range", x, y);
			throw new IllegalArgumentException(error);
		}
		return board[x][y];
	}
	
	public void setTile(Tile tile, int x, int y) {
		if (!validIndices(x, y)) {
			String error = String.format("Indices x=%d, y=%d out of range", x, y);
			throw new IllegalArgumentException(error);
		}
		board[x][y] = tile;
	}
	
	private boolean validIndices(int x, int y) {
		return 0 < x && x < WIDTH && 0 < y && y < HEIGHT;
	}
	
	public static Board getBoard() {
		return instance;
	}
	
}
