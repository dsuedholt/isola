package com.example.isola.game;

public abstract class GameEvent {
	public enum EventType {
		MOVE,
		DESTROY,
		GAMEOVER
	}
	
	private EventType type;
	
	public GameEvent(EventType type) {
		this.type = type;
	}
	
	public EventType getType() {
		return type;
	}
}
