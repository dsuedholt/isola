package com.example.isola.game;

/**
 * This event represents the game being (re)started.
 */
public class GameStartEvent extends GameEvent {
	
	/**
	 * Construct the GameStartEvent
	 */
	public GameStartEvent() {
		super(EventType.GAMESTART);
	}
	
}
