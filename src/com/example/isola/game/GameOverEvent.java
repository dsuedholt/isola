package com.example.isola.game;

/**
 * This event represents the game ending.
 */
public class GameOverEvent extends GameEvent {
	
	private boolean player1;
	
	/**
	 * Construct the GameOverEvent and save the winning player.
	 * @param player1 True if player1 won, false if player2 won.
	 */
	public GameOverEvent(boolean player1) {
		super(EventType.GAMEOVER);
		this.player1 = player1;
	}
	
	/**
	 * Return the winning player.
	 * @return True if player1 won, false if player2 won.
	 */
	public boolean isPlayer1() {
		return player1;
	}
}
