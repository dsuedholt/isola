package com.example.isola.game;

import com.example.isola.ai.MinimaxStrategy;
import com.example.isola.ai.SimpleStrategy;
import com.example.isola.ai.Strategy;

/**
 * This class controls the game. It provides the interface for human players
 * and networked players to make their moves and controls the computer players,
 * letting them play when it's their turn. It should always run in its own thread
 * so as to not block the UI thread.
 */
public class Game implements Runnable {
	
	private Board board;
	private Player p1, p2;
	
	private Strategy comp1, comp2;
	
	private GameState gameState;
	
	/**
	 * This enum contains all the possible states the game can currently be in while it's not over yet.
	 */
	public enum GameState {
		P1TOMOVE,
		P1TODESTROY,
		P2TOMOVE,
		P2TODESTROY
	}
	
	/**
	 * Construct the Game. This doesn't start the game yet; this is done by creating
	 * a new Thread for this runnable and starting it.
	 * @param board The board on which the game takes place.
	 * @param p1	The type of player1.
	 * @param p2	The type of player2.
	 * @see Player
	 */
	public Game(Board board, Player p1, Player p2) {

		this.board = board;
		this.p1 = p1;
		this.p2 = p2;
		
		// initialize computer players where applicable
		switch (p1) {
		case EASY:
			comp1 = new SimpleStrategy(board, true); break;
		case MEDIUM:
			comp1 = new MinimaxStrategy(board, true, 3); break;
		case HARD:
			comp1 = new MinimaxStrategy(board, true, 5); break;
		default: comp1 = null;
		}
		
		switch (p2) {
		case EASY:
			comp2 = new SimpleStrategy(board, false); break;
		case MEDIUM:
			comp2 = new MinimaxStrategy(board, false, 3); break;
		case HARD:
			comp2 = new MinimaxStrategy(board, false, 5); break;
		default: comp2 = null;
		}
		
		gameState = GameState.P1TOMOVE;
	}
	
	/**
	 * Makes a move on the board. This will have one of four effects, depending on the current gamestate.
	 * It will either:
	 * <ul>
	 * <li>move player1 if the gamestate is P1TOMOVE</li>
	 * <li>destroy a field if the gamestate if P1TODESTROY or P2TODESTROY</li>
	 * <li>move player2 if the gamestate is P2TOMOVE</li>
	 * </ul>
	 * After that, the GameState will be advanced to the next state. If the move determined
	 * by the GameState and the coordinates is invalid, this method will not take any action;
	 * it will neither manipulate the board nor advance the GameState.
	 * @param x The x-coordinate of the affected field.
	 * @param y The y-coordinate of the affected field.
	 * @see GameState
	 */
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
	
	/**
	 * Return the type of the specified player.
	 * @param player1 True for player1, false for player2.
	 * @return An element of the Player enum indicating the type of the specified player.
	 * @see Player
	 */
	public Player getPlayer(boolean player1) {
		if (player1)
			return p1;
		else
			return p2;
	}
	
	/**
	 * Return whether it is player1's turn or not. Turn in this context means that player1 is either
	 * to move or to destroy a field.
	 * @return A boolean indicating whether it is player1's turn to perform any action.
	 */
	public boolean isP1Turn() {
		return gameState == GameState.P1TOMOVE || gameState == GameState.P1TODESTROY; 
	}
	
	/**
	 * Initializes / resets the game and the board.
	 */
	public void init() {
		board.init();
		gameState = GameState.P1TOMOVE;
	}

	/**
	 * This method is executed when a new Thread is created with an instance of Game.
	 * All action in the game by players other than the computer is dealt with in the doTurn()
	 * method. In this method, everytime the GameState leads to it being a computer player's
	 * turn, the game will call the computer's play method and advance the GameState to the next player's turn,
	 * since the play method will do both moving and destroying.
	 */
	@Override
	public void run() {
		init();
		
		while (!Thread.currentThread().isInterrupted()) {
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
