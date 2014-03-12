package com.example.isola;

public class Game implements Runnable {
	
	private Board board;
	private Player p1, p2;
	
	private Strategy comp1, comp2;
	
	private GameState gameState;
	
	public enum GameState {
		P1TOMOVE,
		P1TODESTROY,
		P2TOMOVE,
		P2TODESTROY
	}
	
	public Game(Board board, Player p1, Player p2) {
		this.board = board;
		this.p1 = p1;
		this.p2 = p2;
		
		// initialize computer players where applicable
		switch (p1) {
		case EASY:
			comp1 = new SimpleStrategy(board, true); break;
		case MEDIUM:
			comp1 = new MinimaxStrategy(board, true, 2); break;
		case HARD:
			comp1 = new MinimaxStrategy(board, true, 4); break;
		default: comp1 = null;
		}
		
		switch (p2) {
		case EASY:
			comp2 = new SimpleStrategy(board, false); break;
		case MEDIUM:
			comp2 = new MinimaxStrategy(board, false, 2); break;
		case HARD:
			comp2 = new MinimaxStrategy(board, false, 4); break;
		default: comp2 = null;
		}
		
		gameState = GameState.P1TOMOVE;
	}
	
	public void doTurn(int x, int y) {
		switch (gameState) {
		case P1TOMOVE:
			if (board.canMove(true, x, y)) {
				board.move(true, x, y);
				gameState = GameState.P1TODESTROY;
			}
			break;
		case P2TOMOVE:
			if (board.canMove(false, x, y)) {
				board.move(false, x, y);
				gameState = GameState.P2TODESTROY;
			}
			break;
		case P1TODESTROY:
			if (board.canDestroy(x, y)) {
				board.destroy(x, y);
				gameState = GameState.P2TOMOVE;
			}
			break;
		case P2TODESTROY:
			if (board.canDestroy(x, y)) {
				board.destroy(x, y);
				gameState = GameState.P1TOMOVE;
			}
			break;
		}
	}
	
	public Player getPlayer(boolean player1) {
		if (player1)
			return p1;
		else
			return p2;
	}
	
	public boolean isP1Turn() {
		return gameState == GameState.P1TOMOVE || gameState == GameState.P1TODESTROY; 
	}
	
	public void init() {
		board.init();
		gameState = GameState.P1TOMOVE;
	}


	@Override
	public void run() {
		init();
		
		while (true) {
			if (gameState == GameState.P1TOMOVE && Player.isComputer(p1)) {
				comp1.play();
				gameState = GameState.P2TOMOVE;
			}
			if (board.isOver()) break;
			if (gameState == GameState.P2TOMOVE && Player.isComputer(p2)) {
				comp2.play();
				gameState = GameState.P1TOMOVE;
			}
			if (board.isOver()) break;
		}
	}
	
}
