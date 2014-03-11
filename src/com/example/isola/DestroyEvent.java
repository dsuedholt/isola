package com.example.isola;

public class DestroyEvent extends GameEvent {
	
	private int x, y;
	
	public DestroyEvent(int x, int y) {
		super(EventType.DESTROY);
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}
