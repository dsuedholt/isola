package com.example.isola.game;

/**
 * This abstract class is the base class for any specific Game events that can occur,
 * such as a player moving, a field being destroyed or the game ending.
 */
public abstract class GameEvent {
	
	/**
	 * This enum details all possible types of events that can occur.
	 */
	public enum EventType {
		MOVE,
		DESTROY,
		GAMEOVER,
		GAMESTART
	}
	
	private EventType type;
	
	/**
	 * The abstract constructor saves the type of the event in an instance variable for
	 * future reference by classes notified by the event.
	 * @param type The type of the event.
	 */
	public GameEvent(EventType type) {
		this.type = type;
	}
	
	/**
	 * Retrieve the type of this event.
	 * @return An element of the EventType enum.
	 */
	public EventType getType() {
		return type;
	}
}
