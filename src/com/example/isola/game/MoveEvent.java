package com.example.isola.game;

public class MoveEvent extends GameEvent {
	
	private int oldx, oldy, newx, newy;
	private boolean player1;
	
	public MoveEvent(boolean player1, int oldx, int oldy, int newx, int newy) {
		super(EventType.MOVE);
		this.player1 = player1;
		this.oldx = oldx;
		this.oldy = oldy;
		this.newx = newx;
		this.newy = newy;
	}
	
	public boolean isPlayer1() {
		return player1;
	}
	
	public int getOldX() {
		return oldx;
	}
	
	public int getOldY() {
		return oldy;
	}
	
	public int getNewX() {
		return newx;
	}
	
	public int getNewY() {
		return newy;
	}
}
