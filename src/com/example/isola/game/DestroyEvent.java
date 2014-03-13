package com.example.isola.game;

/**
 * This event represents any field on the board being destroyed.
 */
public class DestroyEvent extends GameEvent {
	
	private int x, y;
	
	/**
	 * Constructs the DestroyEvent with the position of the destroyed field.
	 * @param x The x-coordinate of the destroyed field.
	 * @param y The y-coordinate of the destroyed field.
	 */
	public DestroyEvent(int x, int y) {
		super(EventType.DESTROY);
		this.x = x;
		this.y = y;
	}
	
	/**
	 * @return The x-coordinate of the destroyed field.
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * @return The y-coordinate of the destroyed field.
	 */
	public int getY() {
		return y;
	}
}
