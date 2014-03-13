package com.example.isola.game;

/**
 * This event represents any player moving on the board.
 */
public class MoveEvent extends GameEvent {
	
	private int oldx, oldy, newx, newy;
	private boolean player1;
	
	/**
	 * Construct the MoveEvent with information about the moving player, his previous and
	 * current position.
	 * @param player1 True if player1 moved, false if player2 moved.
	 * @param oldx 	  The x-coordinate of the previous position.
	 * @param oldy	  The y-coordinate of the previous position.
	 * @param newx	  The x-coordinate of the new position.
	 * @param newy	  The y-coordinate of the new position.
	 */
	public MoveEvent(boolean player1, int oldx, int oldy, int newx, int newy) {
		super(EventType.MOVE);
		this.player1 = player1;
		this.oldx = oldx;
		this.oldy = oldy;
		this.newx = newx;
		this.newy = newy;
	}
	
	/**
	 * Retrieve the moving player.
	 * @return True if player1 moved, false if player2 moved.
	 */
	public boolean isPlayer1() {
		return player1;
	}
	
	/**
	 * @return The x-coordinate of the previous position.
	 */
	public int getOldX() {
		return oldx;
	}
	
	/**
	 * @return The y-coordinate of the previous position.
	 */
	public int getOldY() {
		return oldy;
	}
	
	/**
	 * @return The x-coordinate of the new position.
	 */
	public int getNewX() {
		return newx;
	}
	
	/**
	 * @return The y-coordinate of the new position.
	 */
	public int getNewY() {
		return newy;
	}
}
