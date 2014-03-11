package com.example.isola;

public class Game {
	
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
			comp1 = new SimpleStrategy(); break;
		case MEDIUM:
			comp1 = new MinimaxStrategy(2); break;
		case HARD:
			comp1 = new MinimaxStrategy(4); break;
		default: comp1 = null;
		}
		
		switch (p2) {
		case EASY:
			comp2 = new SimpleStrategy(); break;
		case MEDIUM:
			comp2 = new MinimaxStrategy(2); break;
		case HARD:
			comp2 = new MinimaxStrategy(4); break;
		default: comp2 = null;
		}
		
		gameState = GameState.P1TOMOVE;
		
		if (Player.isComputer(p1) && Player.isComputer(p2))
			playComps();
		
		if (Player.isComputer(p1))
			doTurn();
	}
	
	
	// helper method to let the computer move
	private void doTurn() {
		doTurn(-1, -1);
	}
	
	private void playComps() {
		while (!board.isOver()) {
			comp1.play(board, isP1Turn());
			if (!board.isOver())
				comp2.play(board, isP1Turn());
		}
	}
	
	public boolean doTurn(int x, int y) {
		switch (gameState) {
		case P1TOMOVE:
			if (Player.isComputer(p1)) {
				comp1.play(board, isP1Turn());
				gameState = GameState.P2TOMOVE;
				return true;
			}		
			else if (board.canMove(true, x, y)) {
				board.move(true, x, y);
				gameState = GameState.P1TODESTROY;
				return true;
			}
			else
				return false;
		case P1TODESTROY:
			if (board.canDestroy(x, y)) {
				board.destroy(x, y);
				gameState = GameState.P2TOMOVE;
				if (Player.isComputer(p2))
					doTurn();
				return true;
			}
			else
				return false;
		case P2TOMOVE:
			if (Player.isComputer(p2)) {
				comp2.play(board, isP1Turn());
				gameState = GameState.P1TOMOVE;
				return true;
			}
			else if (board.canMove(true, x, y)) {
				board.move(true, x, y);
				gameState = GameState.P2TODESTROY;
				return true;
			}
			else
				return false;
		case P2TODESTROY:
			if (board.canDestroy(x, y)) {
				board.destroy(x, y);
				gameState = GameState.P1TOMOVE;
				if (Player.isComputer(p1))
					doTurn();
				return true;
			}
			else
				return false;
			
		// to keep compiler from complaining
		default: return false;
		}
	}
	
	public boolean isHuman(boolean player1) {
		if (player1)
			return p1 == Player.HUMAN;
		else
			return p2 == Player.HUMAN;
	}
	
	public boolean isP1Turn() {
		return gameState == GameState.P1TOMOVE || gameState == GameState.P1TODESTROY; 
	}
	
	public void reset() {
		board.init();
		gameState = GameState.P1TOMOVE;
	}
	
}
